import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import './App.css';
import TaskList from "./component/TaskList";
import UpdateTask from "./component/UpdateTask";
import CreateTask from "./component/CreateTask";
import SignUp from "./component/SignUp";

function App() {
    return (
        <Router>
            <div>
                <nav>
                    <ul>
                        <li>
                            <Link to="/signup">Sign Up</Link>
                        </li>
                        <li>
                            <Link to="/create-task">Create Task</Link>
                        </li>
                        <li>
                            <Link to="/taskList">Task List</Link>
                        </li>
                    </ul>
                </nav>
                <Routes>
                    <Route path="/taskList" element={<TaskList />} />
                    <Route path="/update-task" element={<UpdateTask />} />
                    <Route path="/create-task" element={<CreateTask />} />
                    <Route path="/signup" element={<SignUp />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
