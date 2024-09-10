import React, { useState, useEffect } from 'react';
import './CompletedTasksFromGroup.css';
import { fetchGroups, fetchCompletedTasks } from "../services/apiGroupService";

const CompletedTasksFromGroup = () => {
    const [groups, setGroups] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState('');
    const [completedTasks, setCompletedTasks] = useState([]);
    const [message, setMessage] = useState('');

    const storedUserInfo = JSON.parse(localStorage.getItem('accountInfos'));
    const userId = storedUserInfo ? storedUserInfo.userId : null;

    useEffect(() => {
        const loadGroups = async () => {
            try {
                const groups = await fetchGroups(userId);
                setGroups(groups);
            } catch (error) {
                setMessage('Error fetching groups');
            }
        };
        if (userId) {
            loadGroups().then(r => r);
        }
    }, [userId]);

    const handleGroupChange = async (e) => {
        const groupId = e.target.value;
        setSelectedGroup(groupId);
        try {
            const tasks = await fetchCompletedTasks(groupId);
            setCompletedTasks(tasks);
        } catch (error) {
            setMessage('Error fetching completed tasks');
        }
    };

    return (
        <div className="completed-tasks-container">
            <h2>Completed Tasks From Group</h2>
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
                    <h3>Completed Tasks</h3>
                    <ul className="task-list">
                        {completedTasks.length > 0 ? (
                            completedTasks.map(task => (
                                <li key={task.id} className="task-item">
                                    <div className="task-details">
                                        <h2>{task.title}</h2>
                                        <p><strong>Description:</strong> {task.description}</p>
                                        <p><strong>Completed on:</strong> {task.completionDate}</p>
                                    </div>
                                </li>
                            ))
                        ) : (
                            <p>No completed tasks found for this group.</p>
                        )}
                    </ul>
                </div>
            )}
            {message && <p className={`message ${message.startsWith('Error') ? 'error' : 'success'}`}>{message}</p>}
        </div>
    );
};

export default CompletedTasksFromGroup;
