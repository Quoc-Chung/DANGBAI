// src/api/post.api.ts
import axiosInstance from './axios.config';
import type { ApiResponse } from '../types/auth';

export interface CreatePostRequest {
  title: string;
  description: string;
  price: number;
  location: string;
  categoryId: number;
}

export interface CreatePostResponse {
  id: number;
  title: string;
  description: string;
  price: number;
  location: string;
  status: string;
  createdAt: string;
  updatedAt: string;
  author: {
    email: string;
    id: number;
    username: string;
  };
  category: {
    id: number;
    name: string;
  };
  media: Array<{
    id: number;
    position: number;
    thumbnailUrl: string;
    type: string;
    url: string;
  }>;
  totalViews: number;
  totalComments: number;
}

export interface Category {
  id: number;
  name: string;
}

// Post for admin management
export interface AdminPost {
  id: number;
  title: string;
  description: string;
  price: number;
  location: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  createdAt: string;
  updatedAt: string;
  author: {
    id: number;
    username: string;
    email: string;
  };
  category: {
    id: number;
    name: string;
  };
  media: Array<{
    id: number;
    position: number;
    thumbnailUrl: string;
    type: string;
    url: string;
  }>;
  totalViews: number;
  totalComments: number;
}

// Paginated response
export interface PaginatedResponse<T> {
  items: T[];
  page: number;
  size: number;
  totalItems: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

// API response format (with capital Id)
interface CategoryApiResponse {
  Id: number;
  name: string;
}

export const postApi = {
  // Create post with images
  createPost: async (
    postData: CreatePostRequest,
    images: File[]
  ): Promise<CreatePostResponse> => {
    const formData = new FormData();
    
    // Add post data as JSON string with proper Content-Type
    // Create a Blob/File with application/json type to match curl's --form 'post=...;type=application/json'
    const postJsonString = JSON.stringify(postData);
    const postBlob = new Blob([postJsonString], { type: 'application/json' });
    // Create a File from Blob to ensure proper Content-Type is set
    const postFile = new File([postBlob], 'post.json', { type: 'application/json' });
    formData.append('post', postFile);
    
    // Add images
    images.forEach((image) => {
      formData.append('images', image);
    });

    // Get token from localStorage
    const token = localStorage.getItem('accessToken');
    
    // Content-Type will be automatically set by axios interceptor for FormData
    // But we explicitly add Authorization header to ensure token is sent
    const response = await axiosInstance.post<ApiResponse<CreatePostResponse>>(
      '/posts/create',
      formData,
      {
        headers: {
          ...(token && { Authorization: `Bearer ${token}` }),
        },
      }
    );
    
    return response.data.data;
  },

  // Get categories from API
  getCategories: async (): Promise<Category[]> => {
    try {
      const response = await axiosInstance.get<ApiResponse<CategoryApiResponse[]>>(
        '/category/all'
      );
      // Map from API format (Id) to our format (id)
      return response.data.data.map(cat => ({
        id: cat.Id,
        name: cat.name,
      }));
    } catch (error) {
      console.error('Failed to load categories:', error);
      // Fallback to default categories if API fails
      return [
        { id: 1, name: 'Laptop' },
        { id: 2, name: 'Điện thoại' },
        { id: 3, name: 'Linh kiện' },
        { id: 4, name: 'Phụ kiện' },
        { id: 5, name: 'Tablet' },
      ];
    }
  },

  // Get all posts (for admin)
  getAllPosts: async (): Promise<AdminPost[]> => {
    // Get token from localStorage
    const token = localStorage.getItem('accessToken');
    
    const response = await axiosInstance.get<ApiResponse<PaginatedResponse<AdminPost>>>(
      '/posts/filter',
      {
        headers: {
          ...(token && { Authorization: `Bearer ${token}` }),
        },
      }
    );
    // Response structure: data.data.items (nested structure)
    return response.data.data.items || [];
  },

  // Get approved posts (for homepage)
  getApprovedPosts: async (): Promise<AdminPost[]> => {
    // Get token from localStorage
    const token = localStorage.getItem('accessToken');
    
    const response = await axiosInstance.get<ApiResponse<PaginatedResponse<AdminPost>>>(
      '/posts/filter?status=APPROVED',
      {
        headers: {
          ...(token && { Authorization: `Bearer ${token}` }),
        },
      }
    );
    // Response structure: data.data.items (nested structure)
    return response.data.data.items || [];
  },

  // Get post by ID
  getPostById: async (postId: number): Promise<AdminPost> => {
    const response = await axiosInstance.get<ApiResponse<AdminPost>>(
      `/posts/${postId}`
    );
    return response.data.data;
  },

  // Approve post
  approvePost: async (postId: number): Promise<void> => {
    const token = localStorage.getItem('accessToken');
    await axiosInstance.post(
      '/posts/approve_post',
      {
        postId: postId,
        approved: true,
      },
      {
        headers: {
          ...(token && { Authorization: `Bearer ${token}` }),
        },
      }
    );
  },

  // Reject post
  rejectPost: async (postId: number, rejectedReason?: string): Promise<void> => {
    const token = localStorage.getItem('accessToken');
    await axiosInstance.post(
      '/posts/approve_post',
      {
        postId: postId,
        approved: false,
        rejectedReason: rejectedReason || 'Nội dung vi phạm quy định',
      },
      {
        headers: {
          ...(token && { Authorization: `Bearer ${token}` }),
        },
      }
    );
  },

  // Delete post
  deletePost: async (postId: number): Promise<void> => {
    await axiosInstance.delete(`/posts/${postId}`);
  },
};

