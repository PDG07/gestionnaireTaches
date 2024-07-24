import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import './App.css';
import TaskList from "./component/TaskList";
import UpdateTask from "./component/UpdateTask";
import CreateTask from "./component/createtask/CreateTask";
import SignUp from "./component/signup/SignUp";
import Dashboard from "./component/Dashboard";
import CompletedTasks from "./component/CompletedTasks";
import CreateTaskGroup from "./component/CreateTaskGroup";
import CreateGroupTask from "./component/CreateTasksForGroup";
import CreateTasksForGroup from "./component/CreateTasksForGroup";
import AddUserToGroup from "./component/AddUserToGroup";

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const accountInfos = localStorage.getItem('accountInfos');
        if (accountInfos) {
            setIsAuthenticated(true);
        }
    }, []);

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/signup" />} />
                <Route path="/signup" element={<SignUp />} />
                {isAuthenticated && (
                    <>
                        <Route path="/dashboard" element={<Dashboard />} />
                        <Route path="/taskList" element={<TaskList />} />
                        <Route path="/update-task" element={<UpdateTask />} />
                        <Route path="/create-task" element={<CreateTask />} />
                        <Route path={"/completed-tasks"} element={<CompletedTasks completed />} />
                        <Route path="/create-task-group" element={<CreateTaskGroup />} />
                        <Route path="/create-task-for-group" element={<CreateTasksForGroup />} />
                        <Route path="/add-user-to-group" element={<AddUserToGroup />} />
                    </>
                )}
                <Route path="*" element={<Navigate to={isAuthenticated ? "/dashboard" : "/signup"} />} />
            </Routes>
        </Router>
    );
}

export default App;
