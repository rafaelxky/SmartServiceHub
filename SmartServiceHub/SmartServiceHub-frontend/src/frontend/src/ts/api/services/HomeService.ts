import { get } from "./ApiService";
import type { AuthProvider } from "./auth/AuthInterface";

export class HomeService{
    baseUrl: string;
    authService: AuthProvider;

   constructor(baseUrl: string, authService: AuthProvider){
        //http://localhost:8080
        this.baseUrl = baseUrl;
        this.authService = authService; 
    }

    async listAllEndpoints(){
        return await get<string[]>(this.baseUrl + "/home", this.authService)
    }
    async getServerStatus(){
        return await get<string>(this.baseUrl + "/status", this.authService)
    }
}