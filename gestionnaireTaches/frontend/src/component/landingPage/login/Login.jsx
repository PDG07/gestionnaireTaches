import {useState} from "react";
import {useNavigate} from "react-router-dom";
import './Login.css';
import {loginUser} from "../../services/apiUserService";

const Login = ({ setIsAuthenticated }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const userLogged = await loginUser(username, password);
            const userToStore = {
                userId: userLogged.id,
                username: userLogged.username,
                tasks: userLogged.tasks || []
            };
            localStorage.setItem('accountInfos', JSON.stringify(userToStore));
            setIsAuthenticated(true);
            navigate('/dashboard');
        } catch (error) {
            console.error('Error logging in:', error);
            setError(`Error: ${error.message}`);
        }
    };

    return (
        <div className="login-container">
            <h2 className="login-title">Login</h2>
            <form onSubmit={handleSubmit} className="login-form">
                <div className="form-group">
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" className="button">Login</button>
                {error && <p className="error-message">{error}</p>}
            </form>
        </div>
    );
};

export default Login;
