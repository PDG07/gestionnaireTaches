import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './SignUp.css';
import {signUpUser} from "../../services/apiUserService";

const SignUp = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!username || !password) {
            setMessage('Both fields are required');
            return;
        }

        try {
            await signUpUser({ username, password });
            setMessage('User created successfully');
            navigate('/dashboard');
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
        <div className="signup-container">
            <form className="signup-form" onSubmit={handleSubmit} noValidate>
                <h2 className="signup-title">Sign Up</h2>
                <div className="form-group">
                    <label className="signup-label">Username:</label>
                    <input
                        type="text"
                        className="signup-input"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label className="signup-label">Password:</label>
                    <input
                        type="password"
                        className="signup-input"
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
