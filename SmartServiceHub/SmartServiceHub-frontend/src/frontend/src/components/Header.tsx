
import { useState, useEffect, useContext } from 'preact/hooks';
import { route } from 'preact-router';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { AuthContext } from '../pages/AuthContext';

const Header = () => {
  const [search, setSearch] = useState('');
  // reactive variable
  const {token, setToken} = useContext(AuthContext);

  // Listen for token changes
  useEffect(() => {
    const handleStorage = () => setToken(localStorage.getItem('token'));
    window.addEventListener('storage', handleStorage);
    return () => window.removeEventListener('storage', handleStorage);
  }, []);

  const handleAuthClick = (e: Event) => {
    e.preventDefault();
    if (token) {
      localStorage.removeItem('token');
      setToken(null);
      route('/login');
    } else {
      route('/login');
    }
  };

  const createPostClick = (e: Event) => {
    if (token){
        route("/create_post");
    } else {
      route("/login?route=create_post");
    }
  }

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light fixed-top shadow-sm w-100 mb-4">
      <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css"
      />

      <div className="container justify-content-between">
        {/* Brand */}
        

        {/* Links */}
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <a href="#" className="navbar-brand" onClick={(e) => { e.preventDefault(); route('/'); }}>
              MyApp
            </a>
            <li className="nav-item">
              <a className="nav-link" href="#">Account</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="#">Users</a>
            </li>
            <li className="nav-item">
              <a className="nav-link" href="#" onClick={handleAuthClick}>
                {token ? 'Logout' : 'Login'}
              </a>
            </li>
            <li className="nav-item">
              <a
                className="nav-link d-flex align-items-center"
                href="#"
                onClick={createPostClick}
                title="Create Post"
              >
                <i className="bi bi-plus-circle me-1 fs-4"></i>
                <span className="d-none d-lg-inline">Create</span> {/* optional text on large screens */}
              </a>
            </li>

          </ul>

          {/* Search */}
          <form className="d-flex ms-auto">
            <input
              type="search"
              className="form-control"
              placeholder="Search"
              value={search}
              onInput={(e: any) => setSearch(e.target.value)}
              style={{ height: '38px', minWidth: '150px' }}
            />
          </form>
        </div>
    </nav>
  );
};

export default Header;