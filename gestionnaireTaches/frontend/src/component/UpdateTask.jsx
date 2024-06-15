import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';

const UpdateTask = () => {
    const location = useLocation();
    const { task } = location.state || {};
    const [taskData, setTaskData] = useState(task || {});
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const userId = JSON.parse(localStorage.getItem('accountInfos')).userId;

    if (!task) {
        return <p>No task selected</p>;
    }

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTaskData({
            ...taskData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const updatedTaskData = {
                id: taskData.id,
                title: taskData.title,
                description: taskData.description,
                priority: taskData.priority,
                deadline: taskData.deadline,
                category: taskData.category,
                userId: userId
            };

            const response = await fetch('http://localhost:8080/api/updatetask', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedTaskData),
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const updatedTask = await response.json();
            setSuccess('Task updated successfully!');
            setError(null);
        } catch (error) {
            setError('Error updating task');
            setSuccess(null);
        }
    };

    return (
        <div>
            <h1>Update Task</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Titre:</label>
                    <input
                        type="text"
                        name="title"
                        value={taskData.title || ''}
                        onChange={handleChange}
                        placeholder="Title"
                    />
                </div>
                <div>
                    <label>Description:</label>
                    <input
                        type="text"
                        name="description"
                        value={taskData.description || ''}
                        onChange={handleChange}
                        placeholder="Description"
                    />
                </div>
                <div>
                        <label>Priority:</label>
                        <select value={taskData.priority} onChange={handleChange} name="priority">
                            <option value="">Select priority</option>
                            <option value="HIGH">High</option>
                            <option value="AVERAGE">Average</option>
                            <option value="LOW">Low</option>
                        </select>
                    </div>
                    <div>
                        <label>Category:</label>
                        <select value={taskData.category} onChange={handleChange} name="category">
                            <option value="">Select category</option>
                            <option value="WORK">Work</option>
                            <option value="PERSONAL">Personal</option>
                            <option value="SHOPPING">Shopping</option>
                            <option value="SPORTS">Sports</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </div>
                <div>
                    <label>DeadLine:</label>
                    <input
                        type="date"
                        name="deadline"
                        value={taskData.deadline || ''}
                        onChange={handleChange}
                        placeholder="Deadline"
                    />
                </div>
                <button type="submit">Update Task</button>
            </form>
        </div>
    );
};

export default UpdateTask;
