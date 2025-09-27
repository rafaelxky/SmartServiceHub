import type { AuthProvider } from "./auth/AuthInterface";
import { get, post, put, del } from "./ApiService";
import type { CommentPublicDto } from "../models/dto/CommentPublicDto";


export class CommentService{
    baseUrl: string;
    authService: AuthProvider;

    constructor(baseUrl: string, authService: AuthProvider){
        //http://localhost:8080/comments
        this.baseUrl = baseUrl;
        this.authService = authService; 
    }

    async createComment(comment: Comment){
        return await post<Comment>(this.baseUrl, comment ,this.authService) 
    }

    async getComment(id: number){
        return await get<CommentPublicDto>(this.baseUrl + "/" + id, this.authService)
    }
    
    async updateComment(id: number, comment: Comment){
        return await put<Comment>(this.baseUrl + "/" + id, comment)
    }

    async deleteComment(id: number){
        return await del<Comment>(this.baseUrl + "/" + id, this.authService)
    }

    async getPostComments(id:number){
        return await get<CommentPublicDto[]>(this.baseUrl + "/post/" + id, this.authService)
    }

    async getPostUniqueComments(limit: number, offset: number, post_id: number){
        return await get<Comment[]>(this.baseUrl + "/unique?limit=" + limit + "&offset=" + offset + "&post_id=" + post_id)
    }
}