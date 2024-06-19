import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const TaskList = () => {
    const [tasks, setTasks] = useState([]);
    const [error, setError] = useState(null);
    const [category, setCategory] = useState('');
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
                setTasks(tasks.filter(task => !task.completed));
            } catch (error) {
                setError(error.message);
            }
        };

        fetchTasks();
    }, [userId]);

    const handleCategoryChange = async (event) => {
        const selectedCategory = event.target.value;
        setCategory(selectedCategory);
        if (selectedCategory) {
            try {
                const response = await fetch(`http://localhost:8080/api/tasks/filter?userId=${userId}&category=${selectedCategory}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch tasks by category');
                }
                const filteredTasks = await response.json();
                setTasks(filteredTasks.filter(task => !task.completed));
            } catch (error) {
                setError(error.message);
            }
        } else {
            // Fetch all tasks if no category is selected
            try {
                const response = await fetch(`http://localhost:8080/api/tasks?userId=${userId}`);
                if (!response.ok) {
                    throw new Error('Failed to fetch tasks');
                }
                const tasks = await response.json();
                setTasks(tasks.filter(task => !task.completed));
            } catch (error) {
                setError(error.message);
            }
        }
    };

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
            setTasks(tasks.filter(task => task.id !== taskId));
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <div>
            <h1>Your tasks</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
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
