import Router, { Route } from 'preact-router';
import Header from './components/Header';
import './styles/debug.css'
import './styles/styles.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import Home from './pages/Home';
import Login from './pages/Login';
import NewPost from './pages/NewPost';
import { useState } from 'preact/hooks';
import { AuthContext } from './pages/AuthContext';
import PostComments from './pages/PostComments';

export function App() {
  const [token, setToken] = useState<string | null>(localStorage.getItem('token'));

  return (
  <div className="container-fluid d-flex flex-column align-items-center min-vh-100 bg-light py-4 w-100 pt-5">
    <AuthContext.Provider value={{ token, setToken }}>
      <Header />
    <Router>
      <Route path="/" component={Home}/>
      <Route path="/login" component={Login}/>
      <Route path="/create_post" component={NewPost}></Route>
      <Route path="/post/:postId/comments" component={PostComments}></Route>
    </Router>
    </AuthContext.Provider>
  </div>
  );
}

export default App;
