import type { AppUser } from "../models/AppUser";
import type { UserCreateDto } from "../models/dto/UserCreateDto";
import { del, post } from "./ApiService";
import type { AuthProvider } from "./auth/AuthInterface";

export class AdminService{
    baseUrl: number;
    authService: AuthProvider;

    constructor(baseUrl: number, authService: AuthProvider){
        // http://localhost:8080/admin
        this.baseUrl = baseUrl;
        this.authService = authService;
    }

    async createAdmin(user: UserCreateDto){
        return await post<AppUser>(this.baseUrl + "/create", user, this.authService)
    }

    async deleteAllUsers(){
        return await post<AppUser>(this.baseUrl + "/users/deleteAll", this.authService)
    }

    async deleteAllComments(){
        return await del<AppUser>(this.baseUrl + "/comments/deleteAll", this.authService)
    }

    async deleteAllPosts(){
        return await del<AppUser>(this.baseUrl + "/posts/deleteAll", this.authService)
    }
}