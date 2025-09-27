
import "./ApiService"
import {post} from "./ApiService";
import type { AuthProvider } from "./auth/AuthInterface";

export class JWTLoginService{
    baseUrl: string;
    authService: AuthProvider;

    constructor(baseUrl: string, authService: AuthProvider){
        this.baseUrl = baseUrl;
        this.authService = authService; 
    }

    async log_in(name: string, password: string) {
        return post<{ token: string }>(this.baseUrl, {
            username: name,
            password: password,
        });
    }

}
