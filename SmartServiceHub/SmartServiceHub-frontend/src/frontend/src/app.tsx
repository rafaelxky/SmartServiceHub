import { h } from 'preact';
import Header from './components/Header';
import Posts from './components/Posts';
import type { Post } from './tsx/models';
import './styles/debug.css'
import './styles/styles.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import { UserService } from './tsx/api/services/UserService';

let userService: UserService = new UserService("http://localhost:8080/users");
userService.getUserById(1);

const posts: Post[] = [
  { id: 1, title: 'First Post', content: 'This is the first post.', author: 'Alice', date: '2025-08-19' },
  { id: 2, title: 'Second Post', content: 'This is another post.', author: 'Bob', date: '2025-08-18' },
];

export function App() {
  return (
    <div className="container-fluid d-flex flex-column align-items-center min-vh-100 bg-light py-4 w-100 pt-5">
      {/* Header */}
      <Header />

      {/* Posts container */}
      <div className="container d-flex flex-column gap-3">
        <Posts posts={posts} />
      </div>
    </div>
  );
}

export default App;
