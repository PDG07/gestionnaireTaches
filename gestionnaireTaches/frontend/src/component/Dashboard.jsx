import React from 'react';
import { Link } from 'react-router-dom';

const Dashboard = () => {
    return (
        <div>
            <h1>Welcome to your dashboard</h1>
            <nav>
                <ul>
                    <li>
                        <Link to="/create-task">Create Task</Link>
                    </li>
                    <li>
                        <Link to="/taskList">Task List</Link>
                    </li>
                </ul>
            </nav>
        </div>
    );
};

export default Dashboard;
