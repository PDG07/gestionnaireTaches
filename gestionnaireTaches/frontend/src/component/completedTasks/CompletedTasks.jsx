import React, { useState, useEffect } from 'react';
import './CompletedTasks.css';

const CompletedTasks = () => {
    const [completedTasks, setCompletedTasks] = useState([]);
    const [error, setError] = useState(null);
    const userId = JSON.parse(localStorage.getItem('accountInfos')).userId;

    useEffect(() => {
        const fetchCompletedTasks = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/completedtasks?userId=${userId}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch completed tasks');
                }
                const tasks = await response.json();
                setCompletedTasks(tasks);
            } catch (error) {
                setError(error.message);
            }
        };

        fetchCompletedTasks();
    }, [userId]);

    return (
        <div className="completed-tasks-container">
            <h1>Completed Tasks</h1>
            {error && <p className="text-danger">{error}</p>}
            <ul className="list-group">
                {completedTasks.map(task => (
                    <li key={task.id} className="list-group-item">
                        <h2 className="task-title">{task.title}</h2>
                        <p><strong>Description:</strong> {task.description}</p>
                        <p><strong>Due date:</strong> {task.completionDate}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default CompletedTasks;
