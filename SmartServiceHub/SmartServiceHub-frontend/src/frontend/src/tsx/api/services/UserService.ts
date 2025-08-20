import "./ApiService"
import { get } from "./ApiService";
import type { User } from "../ApiDto";

export class UserService{
    baseUrl: string;

    constructor(baseUrl: string){
        //localhost/users
        this.baseUrl = baseUrl;
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
