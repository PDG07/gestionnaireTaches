import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './ShowTasksFromGroup.css';
import {assignTask, fetchTasks, fetchGroups, fetchUsers, filterTasksByCategory, completeTask} from "../services/apiGroupService";

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
        const loadGroups = async () => {
            try {
                const groups = await fetchGroups(userId);
                setGroups(groups);
            } catch (error) {
                setMessage('Error fetching groups');
            }
        };

        loadGroups().then(r => r);
    }, [userId]);

    const handleGroupChange = async (e) => {
        const groupId = e.target.value;
        setSelectedGroup(groupId);
        try {
            const tasks = await fetchTasks(groupId);
            const users = await fetchUsers(groupId);
            setTasks(tasks);
            setFilteredTasks(tasks);
            setUsers(users);
        } catch (error) {
            setMessage('Error fetching tasks or users');
        }
    };

    const handleCompleteTask = async (taskId) => {
        try {
            await completeTask(taskId, selectedGroup);
            setMessage('Task completed successfully');
            setTasks(tasks.filter(task => task.id !== taskId));
            setFilteredTasks(filteredTasks.filter(task => task.id !== taskId));
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

        try {
            if (selectedCategory) {
                const filteredTasks = await filterTasksByCategory(selectedGroup, selectedCategory);
                setFilteredTasks(filteredTasks);
            } else {
                setFilteredTasks(tasks);
            }
        } catch (error) {
            setMessage('Error filtering tasks');
        }
    };

    const handleAssignTask = async (taskId, userId) => {
        try {
            await assignTask(taskId, userId, selectedGroup);
            setMessage('Task assigned successfully');
            const tasks = await fetchTasks(selectedGroup);
            setTasks(tasks);
            setFilteredTasks(tasks);
        } catch (error) {
            setMessage('Error assigning task');
        }
    };

    return (
        <div className="container">
            <h2>Manage Tasks From Group</h2>
            <div className="form-group">
                <label className="label">Group:</label>
                <select value={selectedGroup} onChange={handleGroupChange} required className="select">
                    <option value="">Select a group</option>
                    {groups.map(group => (
                        <option key={group.id} value={group.id}>{group.title}</option>
                    ))}
                </select>
            </div>
            {selectedGroup && (
                <div className="tasks-container">
                    <div className="form-group">
                        <label htmlFor="category" className="label">Filter by category:</label>
                        <select id="category" value={category} onChange={handleCategoryChange} className="select">
                            <option value="">All</option>
                            <option value="WORK">Work</option>
                            <option value="PERSONAL">Personal</option>
                            <option value="SHOPPING">Shopping</option>
                            <option value="SPORTS">Sports</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </div>
                    <h3>Tasks</h3>
                    <ul className="task-list">
                        {filteredTasks.map(task => (
                            <li key={task.id} className="task-item">
                                <div className="task-details">
                                    <h2>{task.title}</h2>
                                    <p><strong>Description:</strong> {task.description}</p>
                                    <p><strong>Due date:</strong> {task.deadline}</p>
                                </div>
                                <button className="button update" onClick={() => handleUpdateTask(task)}>✎</button>
                                <button className="button complete" onClick={() => handleCompleteTask(task.id)}>✔
                                </button>
                                <select onChange={(e) => handleAssignTask(task.id, e.target.value)}
                                        className="select assign">
                                    <option value="">Assign to:</option>
                                    {users.map(user => (
                                        <option key={user.id} value={user.id}>{user.username}</option>
                                    ))}
                                </select>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
            {message && <p className={`message ${message.startsWith('Error') ? 'error' : 'success'}`}>{message}</p>}
        </div>
    );
};
export default ShowTasksFromGroup;
