import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const ShowTasksFromGroup = () => {
    const [groups, setGroups] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState('');
    const [tasks, setTasks] = useState([]);
    const [filteredTasks, setFilteredTasks] = useState([]);
    const [category, setCategory] = useState('');
    const [users, setUsers] = useState([]);
    const [message, setMessage] = useState('');

    const storedUserInfo = JSON.parse(localStorage.getItem('accountInfos'));
    const userId = storedUserInfo.userId;
    const navigate = useNavigate();

    useEffect(() => {
        const fetchGroups = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/group/getGroupsFromUserId?userId=${userId}`);
                if (response.ok) {
                    const data = await response.json();
                    setGroups(data);
                } else {
                    setMessage('Error fetching groups');
                }
            } catch (error) {
                setMessage('Error fetching groups');
            }
        };

        fetchGroups();
    }, [userId]);

    const fetchTasks = async (groupId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/group/getTasksOfGroup?groupId=${groupId}`);
            if (response.ok) {
                const data = await response.json();
                const incompleteTasks = data.filter(task => task.status !== 'COMPLETED');
                setTasks(incompleteTasks);
                setFilteredTasks(incompleteTasks); // Initialize filteredTasks
            } else {
                setMessage('Error fetching tasks');
            }
        } catch (error) {
            setMessage('Error fetching tasks');
        }
    };

    const fetchUsers = async (groupId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/user/findAllUserFromGroup?groupId=${groupId}`);
            if (response.ok) {
                const data = await response.json();
                setUsers(data);
            } else {
                setMessage('Error fetching users');
            }
        } catch (error) {
            setMessage('Error fetching users');
        }
    };

    const handleGroupChange = (e) => {
        const groupId = e.target.value;
        setSelectedGroup(groupId);
        fetchTasks(groupId);
        fetchUsers(groupId); // Add this line
    };

    const handleCompleteTask = async (taskId) => {
        const taskData = {
            groupId: selectedGroup,
            id: taskId,
        };

        try {
            const response = await fetch('http://localhost:8080/api/group/completeTaskFromGroup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskData),
            });

            if (response.ok) {
                setMessage('Task completed successfully');
                setTasks(tasks.filter(task => task.id !== taskId));
                setFilteredTasks(filteredTasks.filter(task => task.id !== taskId));
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            setMessage('Error completing task');
        }
    };

    const handleUpdateTask = (task) => {
        navigate('/update-task', { state: { task } });
    };

    const handleCategoryChange = async (event) => {
        const selectedCategory = event.target.value;
        setCategory(selectedCategory);

        if (selectedCategory) {
            try {
                const response = await fetch('http://localhost:8080/api/group/filterByCategoryGroup', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        groupId: selectedGroup,
                        category: selectedCategory
                    }),
                });

                if (response.ok) {
                    const filteredTasks = await response.json();
                    setFilteredTasks(filteredTasks);
                } else {
                    const errorData = await response.json();
                    setMessage(`Error: ${errorData.message}`);
                }
            } catch (error) {
                setMessage('Error filtering tasks');
            }
        } else {
            setFilteredTasks(tasks);
        }
    };

    const handleAssignTask = async (taskId, userId) => {
        const taskData = {
            groupId: selectedGroup,
            id: taskId,
            userId: userId
        };

        try {
            const response = await fetch('http://localhost:8080/api/group/assignTaskForGrTo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskData),
            });

            if (response.ok) {
                setMessage('Task assigned successfully');
                fetchTasks(selectedGroup); // Refresh tasks
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            setMessage('Error assigning task');
        }
    };

    return (
        <div>
            <h2>Manage Tasks From Group</h2>
            <div>
                <label>Group:</label>
                <select value={selectedGroup} onChange={handleGroupChange} required>
                    <option value="">Select a group</option>
                    {groups.map(group => (
                        <option key={group.id} value={group.id}>{group.title}</option>
                    ))}
                </select>
            </div>
            {selectedGroup && (
                <div>
                    <div>
                        <label htmlFor="category">Filter by category: </label>
                        <select id="category" value={category} onChange={handleCategoryChange}>
                            <option value="">All</option>
                            <option value="WORK">Work</option>
                            <option value="PERSONAL">Personal</option>
                            <option value="SHOPPING">Shopping</option>
                            <option value="SPORTS">Sports</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </div>
                    <h3>Tasks</h3>
                    <ul>
                        {filteredTasks.map(task => (
                            <li key={task.id}>
                                {task.title}
                                <button onClick={() => handleCompleteTask(task.id)}>Complete</button>
                                <button onClick={() => handleUpdateTask(task)}>Update</button>
                                <select onChange={(e) => handleAssignTask(task.id, e.target.value)}>
                                    <option value="">Assign to:</option>
                                    {users.map(user => (
                                        <option key={user.id} value={user.id}>Assign to: {user.username}</option>
                                    ))}
                                </select>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
            {message && <p>{message}</p>}
        </div>
    );
};
export default ShowTasksFromGroup;
