export interface AuthProvider{
    getAuthHeader(): Promise<string>;
    isLoged(): boolean;
}