import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './TaskList.css';
import {completeTask, fetchTasks, fetchTasksByCategory} from "../services/apiTaskService";

const TaskList = () => {
    const [tasks, setTasks] = useState([]);
    const [error, setError] = useState(null);
    const [category, setCategory] = useState('');
    const navigate = useNavigate();
    const userId = JSON.parse(localStorage.getItem('accountInfos')).userId;

    useEffect(() => {
        const loadTasks = async () => {
            try {
                const allTasks = await fetchTasks(userId);
                setTasks(allTasks.filter(task => !task.completed));
            } catch (error) {
                setError(error.message);
            }
        };

        loadTasks().then(r => r);
    }, [userId]);

    const handleCategoryChange = async (event) => {
        const selectedCategory = event.target.value;
        setCategory(selectedCategory);

        try {
            const filteredTasks = selectedCategory
                ? await fetchTasksByCategory(userId, selectedCategory)
                : await fetchTasks(userId);
            setTasks(filteredTasks.filter(task => !task.completed));
        } catch (error) {
            setError(error.message);
        }
    };

    const handleUpdate = (task) => {
        navigate('/update-task', { state: { task } });
    };

    const handleComplete = async (taskId) => {
        try {
            const success = await completeTask(taskId, userId);
            if (success) {
                setTasks(tasks.filter(task => task.id !== taskId));
            }
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
                            <button className="button update" onClick={() => handleUpdate(task)}>✎ Update</button>
                            <button className="button complete" onClick={() => handleComplete(task.id)}>✔ Complete
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default TaskList;
