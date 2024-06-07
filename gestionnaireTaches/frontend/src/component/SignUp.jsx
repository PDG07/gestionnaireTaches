import React, { useState } from 'react';

const SignUp = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');

    const handleUserCreation = async (createdUserDto) => {
        const userId = createdUserDto.id;
        const user = {
            userId: userId,
            username: username
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
            } else {
                const errorData = await response.json();
                setMessage(`Error: ${errorData.message}`);
            }
        } catch (error) {
            setMessage('Error creating user');
        }


    };

    return (
        <div>
            <h2>Signup</h2>
            <form onSubmit={handleSubmit} noValidate>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Signup</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default SignUp;
