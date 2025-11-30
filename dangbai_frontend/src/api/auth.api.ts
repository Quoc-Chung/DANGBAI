// src/api/auth.api.ts
import axiosInstance from './axios.config';
import type { LoginRequest, RegisterRequest, AuthResponse, ApiResponse } from '../types/auth';

export const authApi = {
  // Login
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await axiosInstance.post<ApiResponse<AuthResponse>>(
      '/auth/login',
      credentials
    );
    return response.data.data;
  },

  // Register
  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await axiosInstance.post<ApiResponse<AuthResponse>>(
      '/auth/register',
      data
    );
    return response.data.data;
  },

  // Logout
  logout: async (): Promise<void> => {
    await axiosInstance.post('/auth/logout');
  },

  // Refresh Token
  refreshToken: async (refreshToken: string): Promise<AuthResponse> => {
    const response = await axiosInstance.post<ApiResponse<AuthResponse>>(
      '/auth/refresh-token',
      { refreshToken }
    );
    return response.data.data;
  },

  // Logout All Devices
  logoutAllDevices: async (): Promise<void> => {
    await axiosInstance.post('/auth/logout-all');
  }
};