import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faList, faCheck, faUsers, faTasks, faUserPlus, faEye } from '@fortawesome/free-solid-svg-icons';
import './Dashboard.css';

const Dashboard = () => {
    return (
        <div className="dashboard-container">
            <h1 className="dashboard-title">Welcome to Your Dashboard</h1>
            <nav className="dashboard-nav">
                <ul className="dashboard-menu">
                    <li>
                        <Link to="/create-task" className="dashboard-link">
                            <FontAwesomeIcon icon={faPlus} className="dashboard-icon"/>
                            <span>Create Task</span>
                        </Link>
                    </li>
                    <li>
                        <Link to="/taskList" className="dashboard-link">
                            <FontAwesomeIcon icon={faList} className="dashboard-icon"/>
                            <span>Task List</span>
                        </Link>
                    </li>
                    <li>
                        <Link to="/completed-tasks" className="dashboard-link">
                            <FontAwesomeIcon icon={faCheck} className="dashboard-icon"/>
                            <span>Completed Tasks</span>
                        </Link>
                    </li>
                    <li>
                        <Link to="/create-task-group" className="dashboard-link">
                            <FontAwesomeIcon icon={faUsers} className="dashboard-icon"/>
                            <span>Create Task Group</span>
                        </Link>
                    </li>
                    <li>
                        <Link to="/create-task-for-group" className="dashboard-link">
                            <FontAwesomeIcon icon={faTasks} className="dashboard-icon"/>
                            <span>Create Task for Group</span>
                        </Link>
                    </li>
                    <li>
                        <Link to="/add-user-to-group" className="dashboard-link">
                            <FontAwesomeIcon icon={faUserPlus} className="dashboard-icon"/>
                            <span>Add User to Group</span>
                        </Link>
                    </li>
                    <li>
                        <Link to="/show-tasks-from-group" className="dashboard-link">
                            <FontAwesomeIcon icon={faEye} className="dashboard-icon"/>
                            <span>Show Tasks From Group</span>
                        </Link>
                    </li>
                </ul>
            </nav>
        </div>
    );
};

export default Dashboard;
