import './App.css';
import SignUp from "./component/SignUp";
import CreateTask from "./component/CreateTask";

function App() {
  return (
    <div className="App">
        <h1>Task Manager</h1>
        <SignUp/>
        <CreateTask/>
    </div>
  );
}

export default App;
