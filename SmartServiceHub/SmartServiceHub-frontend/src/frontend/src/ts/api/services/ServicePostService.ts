import type { ServicePost } from "../models/ServicePost";
import type { ServicePostCreateDto } from "../models/dto/ServicePostCreateDto";
import type { AuthProvider } from "./auth/AuthInterface";
import { get, post, put, del } from "./ApiService";
import type { ServicePostPublicDto } from "../models/dto/ServicePostPublicDto";

export class ServicePostService{
    baseUrl: string;
    authService: AuthProvider;

   constructor(baseUrl: string, authService: AuthProvider){
     // http://localhost:8080/services
        this.baseUrl = baseUrl;
        this.authService = authService;
   } 

   async createService(service: ServicePostCreateDto){
          return await post<ServicePost>(this.baseUrl, service ,this.authService)
   }

   async getServiceById(id: number){
      return await get<ServicePost>(this.baseUrl + "/" + id, this.authService)
   }

   async updateService( id: number, servicePost: ServicePost){
      return await put<ServicePost>(this.baseUrl + "/" + id, servicePost ,this.authService)
   }

   async deleteServiceById(id: number){
      return await del<ServicePost>(this.baseUrl + "/" + id, this.authService)
   }

   async getUniqueServicePost(limit: number, offset: number){
      return await get<ServicePostPublicDto[]>(this.baseUrl + "/unique?limit=" + limit + "&offset=" + offset)
   }
}