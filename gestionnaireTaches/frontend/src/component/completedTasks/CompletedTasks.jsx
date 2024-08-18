import React, { useState, useEffect } from 'react';
import './CompletedTasks.css';
import {fetchCompletedTasks} from "../services/apiTaskService";


const CompletedTasks = () => {
    const [completedTasks, setCompletedTasks] = useState([]);
    const [error, setError] = useState(null);
    const userId = JSON.parse(localStorage.getItem('accountInfos')).userId;

    useEffect(() => {
        const loadCompletedTasks = async () => {
            try {
                const tasks = await fetchCompletedTasks(userId);
                setCompletedTasks(tasks);
            } catch (error) {
                setError(error.message);
            }
        };

        loadCompletedTasks();
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
