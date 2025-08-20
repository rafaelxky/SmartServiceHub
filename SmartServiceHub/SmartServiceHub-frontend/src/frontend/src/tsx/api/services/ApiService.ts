export type HttpMethod = "GET" | "POST" | "PUT" | "DELETE" | "PATCH";

export async function request<T>(url: string, method: HttpMethod = "GET", body?: any): Promise<T> {
  const options: RequestInit = {
    method,
    headers: {
      "Content-Type": "application/json",
    },
  };

  if (body) {
    options.body = JSON.stringify(body);
  }

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
