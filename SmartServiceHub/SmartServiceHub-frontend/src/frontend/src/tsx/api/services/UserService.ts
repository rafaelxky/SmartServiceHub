import "./ApiService"
import { get } from "./ApiService";
import type { User } from "../ApiDto";
import type { AuthProvider } from "./auth/AuthInterface";

export class UserService{
    baseUrl: string;
    authService: AuthProvider;

    constructor(baseUrl: string, authService: AuthProvider){
        //localhost/users
        this.baseUrl = baseUrl;
        this.authService = authService; 
    }

    async createUser() {

    }

    async getUserById(id: number){
        return await get<User>(this.baseUrl + "/" + id, this.authService);
    }

    async getUsers(){
        return await get<User>(this.baseUrl, this.authService) 
    }


    
}
