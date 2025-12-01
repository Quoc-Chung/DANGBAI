// src/api/axios.config.ts
import axios, { AxiosError } from 'axios';
import type { InternalAxiosRequestConfig } from 'axios';
import { toast } from 'react-toastify';

const API_BASE_URL = 'http://localhost:8088/api/v1';

// Create axios instance
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Request Interceptor - Add token to every request
axiosInstance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('accessToken');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // If data is FormData, remove Content-Type header to let browser set it
    // with the correct boundary for multipart/form-data
    if (config.data instanceof FormData && config.headers) {
      delete config.headers['Content-Type'];
    }
    
    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

// Response Interceptor - Handle errors globally
axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

    // Handle 401 Unauthorized
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        // Try to refresh token
        const refreshToken = localStorage.getItem('refreshToken');
        
        if (refreshToken) {
          const response = await axios.post<{ code: number; status: number; message: string; data: { accessToken: string; refreshToken: string; tokenType: string; userId: number; username: string; email: string; displayName: string; roles: string[] }; timestamp: number }>(`${API_BASE_URL}/auth/refresh-token`, {
            refreshToken
          });

          const { accessToken, refreshToken: newRefreshToken, userId, username, email, displayName, roles } = response.data.data;
          
          // Update tokens
          localStorage.setItem('accessToken', accessToken);
          localStorage.setItem('refreshToken', newRefreshToken);
          
          // Update user info in localStorage
          const user = {
            userId,
            username,
            email,
            displayName,
            roles
          };
          localStorage.setItem('user', JSON.stringify(user));

          // Retry original request
          if (originalRequest.headers) {
            originalRequest.headers.Authorization = `Bearer ${accessToken}`;
          }
          return axiosInstance(originalRequest);
        }
      } catch (refreshError) {
        // Refresh token failed, logout user
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        window.location.href = '/login';
        toast.error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
        return Promise.reject(refreshError);
      }
    }

    // Handle other errors
    const errorData = error.response?.data as { message?: string } | undefined;
    const errorMessage = errorData?.message || 'Đã xảy ra lỗi. Vui lòng thử lại.';
    
    if (error.response?.status === 403) {
      toast.error('Bạn không có quyền truy cập.');
    } else if (error.response?.status === 404) {
      toast.error('Không tìm thấy tài nguyên.');
    } else if (error.response?.status && error.response.status >= 500) {
      toast.error('Lỗi server. Vui lòng thử lại sau.');
    } else {
      toast.error(errorMessage);
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;