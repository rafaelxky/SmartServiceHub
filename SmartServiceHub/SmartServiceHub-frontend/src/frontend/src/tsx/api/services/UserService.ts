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
        let result = await get<User>(this.baseUrl + "/" + id);
        console.log("getUserById: " + result); 
    }

    async getUsers(){
        get<User>(this.baseUrl) 
    }


    
}
