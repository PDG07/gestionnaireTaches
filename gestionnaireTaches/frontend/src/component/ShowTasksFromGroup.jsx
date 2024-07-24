import React, { useState, useEffect } from 'react';

const ShowTasksFromGroup = () => {
    const [groups, setGroups] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState('');
    const [tasks, setTasks] = useState([]);
    const [message, setMessage] = useState('');

    const storedUserInfo = JSON.parse(localStorage.getItem('accountInfos'));
    const userId = storedUserInfo.userId;

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
            } else {
                setMessage('Error fetching tasks');
            }
        } catch (error) {
            setMessage('Error fetching tasks');
        }
    };

    const handleGroupChange = (e) => {
        const groupId = e.target.value;
        setSelectedGroup(groupId);
        fetchTasks(groupId);
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
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            setMessage('Error completing task');
        }
    };

    return (
        <div>
            <h2>Complete Task From Group</h2>
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
                    <h3>Tasks</h3>
                    <ul>
                        {tasks.map(task => (
                            <li key={task.id}>
                                {task.title}
                                <button onClick={() => handleCompleteTask(task.id)}>Compléter</button>
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
