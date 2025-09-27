import { useState } from 'preact/hooks';
import { route } from 'preact-router';
import { loginService } from './Context';
import { useContext } from 'preact/hooks';
import { AuthContext } from './AuthContext';


const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { setToken } = useContext(AuthContext);

  const handleSubmit = async (e: Event) => {
    e.preventDefault();
    setError('');

    try {

      const res = await loginService.log_in(username, password)

      if (!res.token) {
        throw new Error("Login failed: token missing in response");
      }

      setToken(res.token);
      console.log('Login success!');


      if (res.token) localStorage.setItem('token', res.token);

      const params = new URLSearchParams(window.location.search);
      route(params.get("route") ?? "/");
    } catch (err) {
      console.error(err);
      setError('Wrong username or password!');
    }
  };

  return (
    <div className="container d-flex justify-content-center align-items-center min-vh-100">
      <form
        className="p-4 bg-white rounded shadow-sm"
        style={{ maxWidth: '400px', width: '100%' }}
        onSubmit={handleSubmit}
      >
        <h2 className="mb-3">Login</h2>

        {error && <div className="alert alert-danger">{error}</div>}

        <div className="mb-3">
          <label className="form-label">Username</label>
          <input
            type="text"
            className="form-control"
            value={username}
            onInput={(e: any) => setUsername(e.target.value)}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Password</label>
          <input
            type="password"
            className="form-control"
            value={password}
            onInput={(e: any) => setPassword(e.target.value)}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary w-100">
          Log In
        </button>
      </form>
    </div>
  );
};

export default Login;
