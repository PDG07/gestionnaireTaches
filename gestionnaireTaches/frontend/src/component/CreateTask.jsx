import React, { useState, useEffect } from 'react';

const CreateTask = () => {
    const [userId, setUserId] = useState('');
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [status, setStatus] = useState('TODO'); // Valeur par défaut "TODO"
    const [priority, setPriority] = useState('');
    const [deadline, setDeadline] = useState('');
    const [category, setCategory] = useState('');
    const [message, setMessage] = useState('');

    // Effet pour récupérer l'ID utilisateur depuis le localStorage
    useEffect(() => {
        const storedUserInfo = localStorage.getItem('accountInfos');
        if (storedUserInfo) {
            const { userId } = JSON.parse(storedUserInfo);
            setUserId(userId);
        }
    }, []);

    // Méthode de soumission du formulaire
    const handleSubmit = async (e) => {
        e.preventDefault();

        const taskData = {
            userId: userId,
            title: title,
            description: description,
            status: status,
            priority: priority,
            deadline: deadline,
            category: category
        };

        try {
            const response = await fetch('http://localhost:8080/api/createtask', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskData),
            });

            console.log('Response status:', response.status);

            if (response.status === 201) {
                setMessage('Task created successfully');
            } else if (response.status === 403) {
                setMessage('Access denied: You do not have permission to create a task.');
            } else {
                const errorData = await response.json().catch(() => null);
                setMessage(`Error: ${errorData ? errorData.message : 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Fetch error:', error);
            setMessage('Error creating task');
        }
    };

    // Vérification si le bouton de soumission doit être désactivé
    const isSubmitDisabled = !title || !description || !priority || !deadline || !category;

    return (
        <div>
            <h2>Create Task</h2>
            <form onSubmit={handleSubmit} noValidate>
                <div>
                    <label>Title:</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Description:</label>
                    <input
                        type="text"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Priority:</label>
                    <select value={priority} onChange={(e) => setPriority(e.target.value)} required>
                        <option value="">Select priority</option>
                        <option value="HIGH">High</option>
                        <option value="AVERAGE">Average</option>
                        <option value="LOW">Low</option>
                    </select>
                </div>
                <div>
                    <label>Deadline:</label>
                    <input
                        type="date"
                        value={deadline}
                        onChange={(e) => setDeadline(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Category:</label>
                    <select value={category} onChange={(e) => setCategory(e.target.value)} required>
                        <option value="">Select category</option>
                        <option value="WORK">Work</option>
                        <option value="PERSONAL">Personal</option>
                        <option value="SHOPPING">Shopping</option>
                        <option value="SPORTS">Sports</option>
                        <option value="OTHER">Other</option>
                    </select>
                </div>
                {/* Vous pouvez choisir de cacher complètement le champ Status ou le laisser visible */}
                {/* <div>
                    <label>Status:</label>
                    <select value={status} onChange={(e) => setStatus(e.target.value)} disabled>
                        <option value="TODO">Todo</option>
                    </select>
                </div> */}
                <button type="submit" disabled={isSubmitDisabled}>Create Task</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default CreateTask;
