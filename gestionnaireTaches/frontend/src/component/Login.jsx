import {useState} from "react";
import {useNavigate} from "react-router-dom";

const Login = ({ setIsAuthenticated }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            const responseBody = await response.text();

            if (response.ok) {
                const userLogged = JSON.parse(responseBody);
                const userToStore = {
                    userId: userLogged.id,
                    username: userLogged.username,
                    tasks: userLogged.tasks || []
                };
                localStorage.setItem('accountInfos', JSON.stringify(userToStore));
                setIsAuthenticated(true);
                navigate('/dashboard');
            } else {
                const errorData = await response.json();
                setError(`Error: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error logging in:', error);
            setError('Error logging in');
        }
    };

    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Login</button>
                {error && <p>{error}</p>}
            </form>
        </div>
    );
};

export default Login;
