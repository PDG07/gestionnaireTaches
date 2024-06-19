import React, { useState, useEffect } from 'react';

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
        <div>
            <h1>Completed Tasks</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <ul>
                {completedTasks.map(task => (
                    <li key={task.id}>
                        <h2>{task.title}</h2>
                        <p>{task.description}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default CompletedTasks;
