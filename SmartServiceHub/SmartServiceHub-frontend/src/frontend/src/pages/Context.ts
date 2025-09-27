import '../styles/debug.css'
import '../styles/styles.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import { UserService } from '../ts/api/services/UserService';
import { ServicePostService } from '../ts/api/services/ServicePostService';
import { JwtAuthService } from '../ts/api/services/auth/JWTAuth';
import { JWTLoginService } from '../ts/api/services/JWTLoginService';

const baseUrl = window.location.hostname.endsWith('.onion')
  ? import.meta.env.VITE_PROTOCOL + "://" + import.meta.env.VITE_ONION_URL + ":" + import.meta.env.VITE_ONION_PORT + "/api"
  : import.meta.env.VITE_PROTOCOL + "://" + import.meta.env.VITE_IP + ":" + import.meta.env.VITE_PORT +"/api";

let authService = new JwtAuthService();
let userService: UserService = new UserService(baseUrl+ "/users", authService);
let servicePostService: ServicePostService = new ServicePostService(baseUrl+ "/services", authService);
let loginService: JWTLoginService = new JWTLoginService(baseUrl + "/log-in", authService)

export {
    authService,
    userService,
    servicePostService,
    loginService,
}