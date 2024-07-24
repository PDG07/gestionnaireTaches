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
                    <li>
                        <Link to="/completed-tasks">Completed Tasks</Link>
                    </li>
                    <li>
                        <Link to="/create-task-group">Create Task Group</Link>
                    </li>
                    <li>
                        <Link to="/create-task-for-group">Create Task for Group</Link>
                    </li>
                    <li>
                        <Link to="/add-user-to-group">Add User to Group</Link>
                    </li>
                </ul>
            </nav>
        </div>
    );
};

export default Dashboard;
