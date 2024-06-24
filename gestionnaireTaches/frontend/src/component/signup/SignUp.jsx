import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './SignUp.css';

const SignUp = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleUserCreation = async (createdUserDto) => {
        const userId = createdUserDto.id;
        const user = {
            userId: userId,
            username: username,
            groups: []
        };
        localStorage.setItem('accountInfos', JSON.stringify(user));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const userData = {
            username,
            password,
            tasks: []
        };

        if (!username || !password) {
            setMessage('Both fields are required');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData),
            });
            if (response.status === 201) {
                const createdUserDto = await response.json();
                setMessage('User created successfully');
                await handleUserCreation(createdUserDto);
                navigate('/dashboard'); // Rediriger vers le tableau de bord
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            setMessage('Error creating user');
        }
    };

    return (
        <div className="container">
            <form className="form" onSubmit={handleSubmit} noValidate>
                <h2 className="title">Sign Up</h2>
                <div>
                    <label className="label">Username:</label>
                    <input
                        type="text"
                        className="input"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label className="label">Password:</label>
                    <input
                        type="password"
                        className="input"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="button">Sign Up</button>
                {message && <p className={`message ${message.startsWith('Error') ? 'error' : 'success'}`}>{message}</p>}
            </form>
        </div>
    );
};

export default SignUp;
