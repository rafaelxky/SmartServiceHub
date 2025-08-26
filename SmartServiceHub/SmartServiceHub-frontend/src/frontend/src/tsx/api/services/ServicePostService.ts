import type { ServicePost } from "../models/ServicePost";
import type { ServicePostCreateDto } from "../models/dto/ServicePostCreateDto";
import type { AuthProvider } from "./auth/AuthInterface";
import { get, post, put, del } from "./ApiService";

export class ServicePostService{
    baseUrl: string;
    authService: AuthProvider;

   constructor(baseUrl: string, authService: AuthProvider){
     // http://localhost:8080/services
        this.baseUrl = baseUrl;
        this.authService = authService;
   } 

   async createService(service: ServicePostCreateDto){
          return await post<ServicePost>(this.baseUrl, this.authService)
   }

}