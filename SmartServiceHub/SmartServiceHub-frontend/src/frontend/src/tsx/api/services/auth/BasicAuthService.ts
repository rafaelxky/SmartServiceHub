import { Injectable } from '@nestjs/common';
import type { AuthProvider } from './AuthInterface';

export class BasicAuthService implements AuthProvider{
    username: string;
    password: string;

    constructor(username: string, password: string){
        this.username = username;
        this.password = password;
    }
    async getAuthHeader(): Promise<string> {
        const encoded = btoa(this.username + ":" + this.password);
        return "Basic" + encoded;
    }
}