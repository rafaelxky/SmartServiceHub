import type { AuthProvider } from "./auth/AuthInterface";
import { get, post, put, del } from "./ApiService";


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
        return await get<Comment>(this.baseUrl + "/" + id, this.authService)
    }

    async getAllComments(){
        return await get<Comment[]>(this.baseUrl, this.authService)
    }

    async deleteComment(id: number){
        return await del<Comment>(this.baseUrl, this.authService)
    }

    async getPostComments(id:number){
        return await get<Comment>(this.baseUrl + "/posts/" + id, this.authService)
    }

    async getPostUniqueComments(limit: number, offset: number, post_id: number){
        return await get<Comment[]>(this.baseUrl + "/unique?limit=" + limit + "&offset=" + offset + "&post_id=" + post_id)
    }
}