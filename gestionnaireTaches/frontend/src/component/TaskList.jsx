// TaskList.js

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const TaskList = () => {
    const [tasks, setTasks] = useState([]);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const userId = JSON.parse(localStorage.getItem('accountInfos')).userId;

    useEffect(() => {
        const fetchTasks = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/tasks?userId=${userId}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch tasks');
                }
                const tasks = await response.json();
                setTasks(tasks);
            } catch (error) {
                setError(error.message);
            }
        };

        fetchTasks();
    }, [userId]);

    const handleUpdate = (task) => {
        navigate('/update-task', { state: { task } });
    };

    const handleComplete = async (taskId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/completetask/${taskId}?userId=${userId}`, {
                method: 'PUT',
            });
            if (!response.ok) {
                throw new Error('Failed to complete task');
            }
            const completedTask = await response.json();
            // Update tasks state to reflect completed task
            setTasks(tasks.map(t => t.id === completedTask.id ? completedTask : t));
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <div>
            <h1>Your tasks</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <ul>
                {tasks.map(task => (
                    <li key={task.id}>
                        <h2>{task.title}</h2>
                        <p>{task.description}</p>
                        <button onClick={() => handleUpdate(task)}>Update Task</button>
                        <button onClick={() => handleComplete(task.id)}>Complete Task</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TaskList;
