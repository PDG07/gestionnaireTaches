import React, { useState, useEffect } from 'react';
import './CreateTasksForGroup.css';

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
            fetchTaskGroups(storedUserInfo.groups);
        } else {
            console.error('User info or groups not found in localStorage');
        }
    }, []);

    const fetchTaskGroups = async (groupIds) => {
        try {
            const encodedGroupIds = encodeURIComponent(JSON.stringify(groupIds));
            const response = await fetch(`http://localhost:8080/api/group/findGroupById?groupIds=${encodedGroupIds}`);
            if (response.ok) {
                const data = await response.json();
                setGroups(data);
                setSelectedGroup(data.length > 0 ? data[0].id : '');
                console.log('Task groups:', data);
            } else {
                console.error('Failed to fetch task groups');
            }
        } catch (error) {
            console.error('Fetch error:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const taskData = {
            userId: userId,
            groupId: selectedGroup,
            title: title,
            description: description,
            status: status,
            priority: priority,
            deadline: deadline,
            category: category
        };

        try {
            const response = await fetch('http://localhost:8080/api/group/addTask', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskData),
            });

            if (response.ok) {
                setMessage('Task added to group successfully');
                setTitle('');
                setDescription('');
                setPriority('');
                setDeadline('');
                setCategory('');
                setSelectedGroup('');
            } else if (response.status === 403) {
                setMessage('Access denied: You do not have permission to create a task.');
            } else {
                const errorData = await response.json().catch(() => null);
                setMessage(`Error: ${errorData ? errorData.message : 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Fetch error:', error);
            setMessage('Error adding task to group');
        }
    };

    const isSubmitDisabled = !title || !description || !priority || !deadline || !category || !selectedGroup;

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
