import React, { useState, useEffect } from 'react';
import './CreateTask.css';
import {createTask} from "../services/apiTaskService";


const CreateTask = () => {
    const [userId, setUserId] = useState('');
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [status] = useState('TODO');
    const [priority, setPriority] = useState('');
    const [deadline, setDeadline] = useState('');
    const [category, setCategory] = useState('');
    const [message, setMessage] = useState('');

    useEffect(() => {
        const storedUserInfo = localStorage.getItem('accountInfos');
        if (storedUserInfo) {
            const { userId } = JSON.parse(storedUserInfo);
            setUserId(userId);
        }
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const taskData = {
            userId,
            title,
            description,
            status,
            priority,
            deadline,
            category,
        };

        try {
            const resultMessage = await createTask(taskData);
            setMessage(resultMessage);
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    const isSubmitDisabled = !title || !description || !priority || !deadline || !category;

    return (
        <div className="container">
            <h2>Create Task</h2>
            <form onSubmit={handleSubmit} noValidate className="form">
                <div className="form-group">
                    <label className="label">Title:</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                <div className="form-group">
                    <label className="label">Description:</label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                <div className="form-group">
                    <label className="label">Priority:</label>
                    <select
                        value={priority}
                        onChange={(e) => setPriority(e.target.value)}
                        required
                        className="select"
                    >
                        <option value="">Select priority</option>
                        <option value="HIGH">High</option>
                        <option value="AVERAGE">Average</option>
                        <option value="LOW">Low</option>
                    </select>
                </div>
                <div className="form-group">
                    <label className="label">Deadline:</label>
                    <input
                        type="date"
                        value={deadline}
                        onChange={(e) => setDeadline(e.target.value)}
                        required
                        className="input"
                    />
                </div>
                <div className="form-group">
                    <label className="label">Category:</label>
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        required
                        className="select"
                    >
                        <option value="">Select category</option>
                        <option value="WORK">Work</option>
                        <option value="PERSONAL">Personal</option>
                        <option value="SHOPPING">Shopping</option>
                        <option value="SPORTS">Sports</option>
                        <option value="OTHER">Other</option>
                    </select>
                </div>
                <button
                    type="submit"
                    disabled={isSubmitDisabled}
                    className="button"
                >
                    Create Task
                </button>
            </form>
            {message && <p className={`message ${message.startsWith('Error') ? 'error' : 'success'}`}>{message}</p>}
        </div>
    );
};

export default CreateTask;
