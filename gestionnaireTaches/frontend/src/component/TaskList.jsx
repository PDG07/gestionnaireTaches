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

    return (
        <div>
            <h1>Task List</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <ul>
                {tasks.map(task => (
                    <li key={task.id}>
                        <h2>{task.title}</h2>
                        <p>{task.description}</p>
                        <button onClick={() => handleUpdate(task)}>Update Task</button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TaskList;
