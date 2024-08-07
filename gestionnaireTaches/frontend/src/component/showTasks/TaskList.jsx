import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './TaskList.css';

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
        <div className="task-list-container">
            <h1>Your Tasks</h1>
            {error && <p className="error">{error}</p>}
            <div className="filter-container">
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
            <ul className="task-list">
                {tasks.map(task => (
                    <li key={task.id} className="task-item">
                        <div className="task-details">
                            <h2>{task.title}</h2>
                            <p><strong>Description:</strong> {task.description}</p>
                            <p><strong>Due date:</strong> {task.deadline}</p>
                        </div>
                        <div className="task-actions">
                            <button className="button update" onClick={() => handleUpdate(task)}>✎ Update Task</button>
                            <button className="button complete" onClick={() => handleComplete(task.id)}>✔ Complete
                                Task
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TaskList;
