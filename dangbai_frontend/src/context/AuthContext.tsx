// src/context/AuthContext.tsx
import React, { createContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { authApi } from '../api/auth.api';
import { tokenService } from '../api/token.service';
import type { User, LoginRequest, RegisterRequest, AuthContextType } from '../types/auth';
import { Role } from '../types/auth';

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  // Load user from localStorage on mount
  useEffect(() => {
    const loadUser = () => {
      const savedUser = tokenService.getUser();
      if (savedUser && tokenService.isAuthenticated()) {
        setUser(savedUser);
      }
      setIsLoading(false);
    };

    loadUser();
  }, []);

  // Login
  const login = async (credentials: LoginRequest) => {
    try {
      setIsLoading(true);
      const response = await authApi.login(credentials);
      
      // Save tokens
      tokenService.saveTokens(response);
      
      // Set user
      const loggedInUser: User = {
        userId: response.userId,
        username: response.username,
        email: response.email,
        displayName: response.displayName,
        roles: response.roles
      };
      setUser(loggedInUser);

      toast.success(`Chào mừng ${response.displayName}!`);

      // Redirect based on role
      if (response.roles.includes(Role.ADMIN)) {
        navigate('/admin/dashboard');
      } else {
        navigate('/');
      }
    } catch (error) {
      console.error('Login failed:', error);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  // Register
  const register = async (data: RegisterRequest) => {
    try {
      setIsLoading(true);
      const response = await authApi.register(data);
      
      // Save tokens
      tokenService.saveTokens(response);
      
      // Set user
      const newUser: User = {
        userId: response.userId,
        username: response.username,
        email: response.email,
        displayName: response.displayName,
        roles: response.roles
      };
      setUser(newUser);

      toast.success('Đăng ký thành công!');

      // Redirect based on role
      if (response.roles.includes(Role.ADMIN)) {
        navigate('/admin/dashboard');
      } else {
        navigate('/');
      }
    } catch (error) {
      console.error('Register failed:', error);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  // Logout
  const logout = async () => {
    try {
      await authApi.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      // Clear tokens and user
      tokenService.clearTokens();
      setUser(null);
      navigate('/login');
      toast.info('Đã đăng xuất');
    }
  };

  const value: AuthContextType = {
    user,
    isAuthenticated: !!user && tokenService.isAuthenticated(),
    isAdmin: user?.roles.includes(Role.ADMIN) || false,
    isLoading,
    login,
    register,
    logout
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};