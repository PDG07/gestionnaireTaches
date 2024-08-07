import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import './App.css';
import TaskList from "./component/showTasks/TaskList";
import UpdateTask from "./component/UpdateTask";
import CreateTask from "./component/createtask/CreateTask";
import SignUp from "./component/landingPage/signup/SignUp";
import Dashboard from "./component/dashboard/Dashboard";
import CompletedTasks from "./component/completedTasks/CompletedTasks";
import CreateTaskGroup from "./component/createTaskGroup/CreateTaskGroup";
import CreateTasksForGroup from "./component/createTasksForGroup/CreateTasksForGroup";
import AddUserToGroup from "./component/addUserToGroup/AddUserToGroup";
import ShowTasksFromGroup from "./component/showTasksFromGroup/ShowTasksFromGroup";
import Login from "./component/landingPage/login/Login";
import LandingPage from "./component/landingPage/LandingPage";

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
                <Route path="/" element={<LandingPage />} />
                <Route path="/signup" element={<SignUp />} />
                <Route path="/login" element={<Login setIsAuthenticated={setIsAuthenticated} />} />
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
                        <Route path="/show-tasks-from-group" element={<ShowTasksFromGroup/> } />
                    </>
                )}
                <Route path="*" element={<Navigate to={isAuthenticated ? "/dashboard" : "/"} />} />
            </Routes>
        </Router>
    );
}

export default App;
