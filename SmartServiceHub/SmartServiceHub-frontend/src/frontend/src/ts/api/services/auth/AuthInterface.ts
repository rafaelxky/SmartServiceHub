export interface AuthProvider{
    getAuthHeader(): Promise<string>;
}