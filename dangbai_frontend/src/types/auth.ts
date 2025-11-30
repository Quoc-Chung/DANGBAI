// src/types/auth.types.ts

export interface User {
    userId: number;
    username: string;
    email: string;
    displayName: string;
    roles: string[];
    avatarUrl?: string;
  }
  
  export interface LoginRequest {
    username: string;
    password: string;
  }
  
  export interface RegisterRequest {
    username: string;
    email: string;
    password: string;
    displayName: string;
    phone?: string;
  }
  
  export interface AuthResponse {
    accessToken: string;
    refreshToken: string;
    tokenType: string;
    userId: number;
    username: string;
    email: string;
    displayName: string;
    roles: string[];
  }
  
  export interface ApiResponse<T> {
    code: number;
    status: number;
    message: string;
    data: T;
    timestamp: number;
    path?: string;
  }
  
  export interface AuthContextType {
    user: User | null;
    isAuthenticated: boolean;
    isAdmin: boolean;
    isLoading: boolean;
    login: (credentials: LoginRequest) => Promise<void>;
    register: (data: RegisterRequest) => Promise<void>;
    logout: () => void;
  }
  
  export const Role = {
    USER: 'ROLE_USER',
    ADMIN: 'ROLE_ADMIN',
    EMPLOYEE: 'ROLE_EMPLOYEE'
  } as const;

  export type Role = typeof Role[keyof typeof Role];