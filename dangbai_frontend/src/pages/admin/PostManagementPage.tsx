// src/pages/admin/PostManagementPage.tsx
import React, { useState, useEffect } from 'react';
import { FaEye, FaTrash, FaSpinner, FaSearch } from 'react-icons/fa';
import { AdminLayout } from '../../components/layout/AdminLayout';
import { postApi, type AdminPost } from '../../api/post.api';
import { PostDetailDialog } from '../../components/admin/PostDetailDialog';
import { toast } from 'react-toastify';

export const PostManagementPage: React.FC = () => {
  const [posts, setPosts] = useState<AdminPost[]>([]);
  const [filteredPosts, setFilteredPosts] = useState<AdminPost[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [selectedPost, setSelectedPost] = useState<AdminPost | null>(null);
  const [isDetailDialogOpen, setIsDetailDialogOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');

  useEffect(() => {
    loadPosts();
  }, []);

  useEffect(() => {
    filterPosts();
  }, [posts, searchTerm, statusFilter]);

  const loadPosts = async () => {
    try {
      setIsLoading(true);
      const data = await postApi.getAllPosts();
      // Ensure data is an array
      if (Array.isArray(data)) {
        setPosts(data);
      } else {
        console.error('API returned non-array data:', data);
        setPosts([]);
        toast.error('Dữ liệu trả về không đúng định dạng');
      }
    } catch (error: any) {
      toast.error('Không thể tải danh sách bài viết');
      console.error('Failed to load posts:', error);
      setPosts([]); // Set empty array on error
    } finally {
      setIsLoading(false);
    }
  };

  const filterPosts = () => {
    // Ensure posts is an array
    if (!Array.isArray(posts)) {
      setFilteredPosts([]);
      return;
    }
    
    let filtered = [...posts];

    // Filter by search term
    if (searchTerm) {
      filtered = filtered.filter(
        post =>
          post.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
          post.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
          post.author.username.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Filter by status
    if (statusFilter !== 'ALL') {
      filtered = filtered.filter(post => post.status === statusFilter);
    }

    setFilteredPosts(filtered);
  };

  const handleViewDetail = async (post: AdminPost) => {
    try {
      // Load full post details
      const fullPost = await postApi.getPostById(post.id);
      setSelectedPost(fullPost);
      setIsDetailDialogOpen(true);
    } catch (error: any) {
      toast.error('Không thể tải chi tiết bài viết');
      console.error('Failed to load post details:', error);
    }
  };

  const handleApprove = async (postId: number) => {
    try {
      await postApi.approvePost(postId);
      toast.success('Duyệt bài viết thành công');
      await loadPosts();
      if (selectedPost?.id === postId) {
        setIsDetailDialogOpen(false);
      }
    } catch (error: any) {
      toast.error('Không thể duyệt bài viết');
      console.error('Failed to approve post:', error);
    }
  };

  const handleReject = async (postId: number, reason?: string) => {
    try {
      await postApi.rejectPost(postId, reason);
      toast.success('Từ chối bài viết thành công');
      await loadPosts();
      if (selectedPost?.id === postId) {
        setIsDetailDialogOpen(false);
      }
    } catch (error: any) {
      toast.error('Không thể từ chối bài viết');
      console.error('Failed to reject post:', error);
    }
  };

  const handleDelete = async (postId: number) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa bài viết này?')) {
      return;
    }

    try {
      await postApi.deletePost(postId);
      toast.success('Xóa bài viết thành công');
      await loadPosts();
      if (selectedPost?.id === postId) {
        setIsDetailDialogOpen(false);
      }
    } catch (error: any) {
      toast.error('Không thể xóa bài viết');
      console.error('Failed to delete post:', error);
    }
  };

  const getStatusBadge = (status: string) => {
    const statusMap: Record<string, { label: string; className: string }> = {
      PENDING: { label: 'Chờ duyệt', className: 'bg-yellow-100 text-yellow-800' },
      APPROVED: { label: 'Đã duyệt', className: 'bg-green-100 text-green-800' },
      REJECTED: { label: 'Đã từ chối', className: 'bg-red-100 text-red-800' },
    };
    const statusInfo = statusMap[status] || { label: status, className: 'bg-gray-100 text-gray-800' };
    return (
      <span className={`px-3 py-1 rounded-full text-xs font-semibold ${statusInfo.className}`}>
        {statusInfo.label}
      </span>
    );
  };

  const formatPrice = (price: number): string => {
    return new Intl.NumberFormat('vi-VN').format(price);
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <AdminLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-800">Quản lý bài viết</h1>
            <p className="text-gray-600 mt-1">Danh sách và quản lý tất cả bài viết</p>
          </div>
        </div>

        {/* Filters */}
        <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {/* Search */}
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                <FaSearch className="text-gray-400" />
              </div>
              <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder="Tìm kiếm theo tiêu đề, mô tả, tác giả..."
                className="w-full pl-12 pr-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
              />
            </div>

            {/* Status Filter */}
            <div>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="w-full px-4 py-3 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition cursor-pointer"
              >
                <option value="ALL">Tất cả trạng thái</option>
                <option value="PENDING">Chờ duyệt</option>
                <option value="APPROVED">Đã duyệt</option>
                <option value="REJECTED">Đã từ chối</option>
              </select>
            </div>
          </div>
        </div>

        {/* Table */}
        <div className="bg-white rounded-xl shadow-lg border border-gray-100 overflow-hidden">
          {isLoading ? (
            <div className="flex items-center justify-center py-20">
              <FaSpinner className="animate-spin text-4xl text-gray-400" />
            </div>
          ) : filteredPosts.length === 0 ? (
            <div className="text-center py-20">
              <p className="text-gray-500 text-lg">Không có bài viết nào</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gradient-to-r from-gray-600 via-gray-500 to-gray-600">
                  <tr>
                    <th className="px-6 py-4 text-left text-white font-semibold">ID</th>
                    <th className="px-6 py-4 text-left text-white font-semibold">Tiêu đề</th>
                    <th className="px-6 py-4 text-left text-white font-semibold">Tác giả</th>
                    <th className="px-6 py-4 text-left text-white font-semibold">Danh mục</th>
                    <th className="px-6 py-4 text-left text-white font-semibold">Giá</th>
                    <th className="px-6 py-4 text-left text-white font-semibold">Vị trí</th>
                    <th className="px-6 py-4 text-left text-white font-semibold">Trạng thái</th>
                    <th className="px-6 py-4 text-left text-white font-semibold">Ngày tạo</th>
                    <th className="px-6 py-4 text-center text-white font-semibold">Thao tác</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {filteredPosts.map((post) => (
                    <tr key={post.id} className="hover:bg-gray-50 transition">
                      <td className="px-6 py-4 text-gray-800 font-medium">{post.id}</td>
                      <td className="px-6 py-4">
                        <div className="max-w-xs">
                          <p className="text-gray-800 font-medium truncate">{post.title}</p>
                        </div>
                      </td>
                      <td className="px-6 py-4 text-gray-700">{post.author.username}</td>
                      <td className="px-6 py-4 text-gray-700">{post.category.name}</td>
                      <td className="px-6 py-4 text-gray-800 font-semibold">
                        {formatPrice(post.price)} ₫
                      </td>
                      <td className="px-6 py-4 text-gray-700">{post.location}</td>
                      <td className="px-6 py-4">{getStatusBadge(post.status)}</td>
                      <td className="px-6 py-4 text-gray-600 text-sm">
                        {formatDate(post.createdAt)}
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex items-center justify-center space-x-2">
                          <button
                            onClick={() => handleViewDetail(post)}
                            className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition"
                            title="Xem chi tiết"
                          >
                            <FaEye />
                          </button>
                          <button
                            onClick={() => handleDelete(post.id)}
                            className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition"
                            title="Xóa"
                          >
                            <FaTrash />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Post Detail Dialog */}
        {selectedPost && (
          <PostDetailDialog
            isOpen={isDetailDialogOpen}
            onClose={() => {
              setIsDetailDialogOpen(false);
              setSelectedPost(null);
            }}
            post={selectedPost}
            onApprove={() => handleApprove(selectedPost.id)}
            onReject={(reason) => handleReject(selectedPost.id, reason)}
            onRefresh={loadPosts}
          />
        )}
      </div>
    </AdminLayout>
  );
};

