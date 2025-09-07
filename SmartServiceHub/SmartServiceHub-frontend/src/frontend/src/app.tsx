import { h } from 'preact';
import Header from './components/Header';
import Posts from './components/Posts';
import type { ServicePost } from './tsx/api/models/ServicePost';
import './styles/debug.css'
import './styles/styles.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import { UserService } from './tsx/api/services/UserService';
import { BasicAuthService } from './tsx/api/services/auth/BasicAuthService';
import { ServicePostService } from './tsx/api/services/ServicePostService';
import { CommentService } from './tsx/api/services/CommentService';

// error here
let authService = new BasicAuthService("admin", "admin123");
let userService: UserService = new UserService("http://localhost:8080/users", authService);
console.log(userService.getUserById(1));
/*
let servicePostService: ServicePostService= new ServicePostService("http://localhost:8080/services", authService);
let commentService: CommentService= new CommentService("http://localhost:8080/comments", authService);
let user = await userService.getUserById(1);
console.log("User: ", JSON.stringify(user, null, 2))
*/

const posts: ServicePost[] = [
  { id: 1, title: 'First Post', content: 'This is the first post.', creatorId: 1, timestamp: '2025-08-19' },
  { id: 2, title: 'Second Post', content: 'This is another post.', creatorId: 1, timestamp: '2025-08-18' },
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
