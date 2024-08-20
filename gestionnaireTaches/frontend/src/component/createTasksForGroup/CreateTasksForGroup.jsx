import React, { useState, useEffect } from 'react';
import './CreateTasksForGroup.css';
import {addTaskToGroup, findGroupById} from "../services/apiGroupService";

const CreateGroupTask = () => {
    const [userId, setUserId] = useState('');
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [status] = useState('TODO');
    const [priority, setPriority] = useState('');
    const [deadline, setDeadline] = useState('');
    const [category, setCategory] = useState('');
    const [message, setMessage] = useState('');
    const [groups, setGroups] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState('');

    useEffect(() => {
        const storedUserInfo = JSON.parse(localStorage.getItem('accountInfos'));
        if (storedUserInfo && storedUserInfo.userId && storedUserInfo.groups) {
            setUserId(storedUserInfo.userId);
            fetchTaskGroups(storedUserInfo.groups).then(r => r).catch(e => e);
        } else {
            console.error('User info or groups not found in localStorage');
        }
    }, []);

    const fetchTaskGroups = async (groupIds) => {
        try {
            const groups = await findGroupById(groupIds);
            setGroups(groups);
            setSelectedGroup(groups.length > 0 ? groups[0].id : '');
        } catch (error) {
            console.error('Failed to fetch task groups:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const taskData = {
            userId,
            groupId: selectedGroup,
            title,
            description,
            status,
            priority,
            deadline,
            category
        };


        try {
            const response = await addTaskToGroup(taskData);

            if (response.ok) {
                setMessage('Task added to group successfully');
                resetForm();
            } else if (response.status === 403) {
                setMessage('Access denied: You do not have permission to create a task.');
            } else {
                const errorData = await response.json().catch(() => null);
                setMessage(`Error: ${errorData ? errorData.message : 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Error adding task to group:', error);
            setMessage('Error adding task to group');
        }
    };

    const resetForm = () => {
        setTitle('');
        setDescription('');
        setPriority('');
        setDeadline('');
        setCategory('');
        setSelectedGroup('');
    };

    const isSubmitDisabled = !title || !description || !priority || !deadline || !category || !selectedGroup;

    const today = new Date().toISOString().split('T')[0];

    return (
        <div className="container">
            <h2>Create your task for your group</h2>
            <form onSubmit={handleSubmit} noValidate className="form">
                <div className="form-group">
                    <label className="label">Select Group:</label>
                    <select
                        value={selectedGroup}
                        onChange={(e) => setSelectedGroup(e.target.value)}
                        required
                        className="select"
                    >
                        <option value="">Select a group</option>
                        {groups.map(group => (
                            <option key={group.id} value={group.id}>{group.title}</option>
                        ))}
                    </select>
                </div>
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
                        min={today}
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
                    Add Task to Group
                </button>
            </form>
            {message && <p className={`message ${message.includes('Error') ? 'error' : ''}`}>{message}</p>}
        </div>
    );
};

export default CreateGroupTask;
