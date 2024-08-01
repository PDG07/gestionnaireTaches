import React from 'react';
import { Link } from 'react-router-dom';

const LandingPage = () => {
    return (
        <div className="landing-container">
            <h2>Welcome</h2>
            <p>Please choose an option:</p>
            <div className="button-group">
                <Link to="/login">
                    <button className="button">Login</button>
                </Link>
                <Link to="/signup">
                    <button className="button">Sign Up</button>
                </Link>
            </div>
        </div>
    );
};

export default LandingPage;
