import type { AuthProvider } from './AuthInterface';

export class JwtAuthService implements AuthProvider {
    private token: string | null = null;

  setToken(token: string) {
    this.token = token;
    localStorage.setItem('token', token); 
  }

  clearToken() {
    this.token = null;
    localStorage.removeItem('token');
  }

  async getAuthHeader(): Promise<string> {
    if (!this.getToken()) throw new Error('No JWT token set');
    return `Bearer ${this.getToken()}`;
  }

  getToken(): string | null {
    return this.token || localStorage.getItem('token');
  }
}
