import React from 'react';
import { Link } from 'react-router-dom';
import './LandingPage.css';

const LandingPage = () => {
    return (
        <div className="landing-container">
            <h1 className="mb-4">Welcome</h1>
            <h3 className="mb-4">Please choose an option:</h3>
            <div className="button-group">
                <Link to="/login">
                    <button className="button mb-4">Login</button>
                </Link>
                <Link to="/signup">
                    <button className="button mb-4">Sign Up</button>
                </Link>
            </div>
        </div>
    );
};

export default LandingPage;
