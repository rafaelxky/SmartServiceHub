import Router, { Route } from 'preact-router';
import Header from './components/Header';
import './styles/debug.css'
import './styles/styles.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import Home from './pages/Home';
import Login from './pages/Login';
import NewPost from './pages/NewPost';

export function App() {
  return (
  <div className="container-fluid d-flex flex-column align-items-center min-vh-100 bg-light py-4 w-100 pt-5">
    <Header />
    <Router>
      <Route path="/" component={Home}/>
      <Route path="/login" component={Login}/>
      <Route path="/create_post" component={NewPost}></Route>
    </Router>
  </div>
  );
}

export default App;
