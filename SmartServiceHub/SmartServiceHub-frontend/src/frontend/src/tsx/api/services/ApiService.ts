import type { AuthProvider } from "./auth/AuthInterface";

export type HttpMethod = "GET" | "POST" | "PUT" | "DELETE" | "PATCH";

export async function request<T>(
  url: string,
  method: HttpMethod = "GET",
  body?: any,
  authProvider?: AuthProvider
): Promise<T> {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };

  if (authProvider) {
    headers["Authorization"] = await authProvider.getAuthHeader();
  }

  const options: RequestInit = {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  };

  const response = await fetch(url, options);

  if (!response.ok) {
    throw new Error(`HTTP error! Status: ${response.status}`);
  }

  return response.json() as Promise<T>;
}


export async function get<T>(url: string): Promise<T> {
  return request<T>(url, "GET");
}

export async function post<T>(url: string, body: any): Promise<T> {
  return request<T>(url, "POST", body);
}

export async function put<T>(url: string, body: any): Promise<T> {
  return request<T>(url, "PUT", body);
}

export async function del<T>(url: string): Promise<T> {
  return request<T>(url, "DELETE");
}

export async function patch<T>(url: string, body: any): Promise<T> {
  return request<T>(url, "PATCH", body);
}
