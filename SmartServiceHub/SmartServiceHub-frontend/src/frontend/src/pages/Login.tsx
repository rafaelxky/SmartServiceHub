import { useState } from 'preact/hooks';
import { route } from 'preact-router';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e: Event) => {
    e.preventDefault(); // prevent page reload
    setError('');

    try {
      const res = await fetch('http://localhost:8081/api/log-in', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) {
        const data = await res.json();
        setError(data.message || 'Login failed');
        return;
      }

      const data = await res.json();
      console.log('Login success:', data);

      if (data.token) localStorage.setItem('token', data.token);

      // Navigate to home page
      route('/');
    } catch (err) {
      console.error(err);
      setError('Network error');
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
