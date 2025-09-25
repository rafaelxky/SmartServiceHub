import Header from '../components/Header';
import Posts from '../components/Posts';
import type { ServicePost } from '../tsx/api/models/ServicePost';
import '../styles/debug.css'
import '../styles/styles.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import { UserService } from '../tsx/api/services/UserService';
import { BasicAuthService } from '../tsx/api/services/auth/BasicAuthService';
import { ServicePostService } from '../tsx/api/services/ServicePostService';

const baseUrl = window.location.hostname.endsWith('.onion')
  ? "http://" + import.meta.env.VITE_ONION_URL + ":81/api"
  : "http://localhost:8081/api";

let authService = new BasicAuthService("admin", "admin123");
let userService: UserService = new UserService(baseUrl+ "/users", authService);
let servicePostService: ServicePostService = new ServicePostService(baseUrl+ "/users", authService);

const posts: ServicePost[] = await servicePostService.getUniqueServicePost(5, 0);
console.log("posts" + posts);

const Home = () => (
    <div>
        <div className="container d-flex flex-column gap-3">
            <Posts posts={posts} />
        </div>
    </div>
);

export default Home;