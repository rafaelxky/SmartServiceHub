import "./ApiService"
import { get, post, put, del } from "./ApiService";
import type { AppUser } from "../models/AppUser";
import type { AuthProvider } from "./auth/AuthInterface";
import type { UserCreateDto } from "../models/dto/UserCreateDto";

export class UserService{
    baseUrl: string;
    authService: AuthProvider;

    constructor(baseUrl: string, authService: AuthProvider){
        //localhost/users
        this.baseUrl = baseUrl;
        this.authService = authService; 
    }

    async createUser(user: UserCreateDto) {
        return await post<AppUser>(this.baseUrl, user); 
    }

    async getUserById(id: number){
        return await get<AppUser>(this.baseUrl + "/" + id, this.authService);
    }

    async getAllUsers(){
        return await get<AppUser[]>(this.baseUrl, this.authService) 
    }

    async updateUser(id: number, user: AppUser){
        return await put<AppUser>(this.baseUrl + "/" + id, user, this.authService)
    }

    async deleteUser(id: number){
        return await del<AppUser>(this.baseUrl + "/" + id, this.authService)
    }

    async getUnique(limit: number, offset: number){
        return await get<AppUser[]>(this.baseUrl + "/unique?limit=" + limit + "&offset=" + offset)
    }



    
}
