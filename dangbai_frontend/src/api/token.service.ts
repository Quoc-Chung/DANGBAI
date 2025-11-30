// src/api/token.service.ts
import type { User, AuthResponse } from '../types/auth';

export const tokenService = {
  // Save tokens to localStorage
  saveTokens: (authResponse: AuthResponse): void => {
    localStorage.setItem('accessToken', authResponse.accessToken);
    localStorage.setItem('refreshToken', authResponse.refreshToken);
    
    const user: User = {
      userId: authResponse.userId,
      username: authResponse.username,
      email: authResponse.email,
      displayName: authResponse.displayName,
      roles: authResponse.roles
    };
    
    localStorage.setItem('user', JSON.stringify(user));
  },

  // Get access token
  getAccessToken: (): string | null => {
    return localStorage.getItem('accessToken');
  },

  // Get refresh token
  getRefreshToken: (): string | null => {
    return localStorage.getItem('refreshToken');
  },

  // Get user from localStorage
  getUser: (): User | null => {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        return JSON.parse(userStr);
      } catch {
        return null;
      }
    }
    return null;
  },

  // Clear all tokens
  clearTokens: (): void => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  },

  // Check if user is authenticated
  isAuthenticated: (): boolean => {
    return !!tokenService.getAccessToken();
  },

  // Check if user has role
  hasRole: (role: string): boolean => {
    const user = tokenService.getUser();
    return user?.roles.includes(role) || false;
  },

  // Check if user is admin
  isAdmin: (): boolean => {
    return tokenService.hasRole('ROLE_ADMIN');
  }
};