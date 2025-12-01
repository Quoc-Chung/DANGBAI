// src/components/admin/PostDetailDialog.tsx
import React, { useState } from 'react';
import { FaTimes, FaMapMarkerAlt, FaClock, FaCheck, FaTimes as FaReject, FaExclamationTriangle } from 'react-icons/fa';
import type { AdminPost } from '../../api/post.api';

interface PostDetailDialogProps {
  isOpen: boolean;
  onClose: () => void;
  post: AdminPost;
  onApprove: () => void;
  onReject: (reason?: string) => void;
  onRefresh?: () => void;
}

export const PostDetailDialog: React.FC<PostDetailDialogProps> = ({
  isOpen,
  onClose,
  post,
  onApprove,
  onReject,
}) => {
  const [showRejectDialog, setShowRejectDialog] = useState(false);
  const [rejectReason, setRejectReason] = useState('Nội dung vi phạm quy định');
  const [error, setError] = useState('');

  if (!isOpen) return null;

  const handleRejectClick = () => {
    setShowRejectDialog(true);
    setRejectReason('Nội dung vi phạm quy định');
    setError('');
  };

  const handleConfirmReject = () => {
    if (!rejectReason.trim()) {
      setError('Vui lòng nhập lý do từ chối');
      return;
    }
    onReject(rejectReason.trim());
    setShowRejectDialog(false);
    onClose();
  };

  const handleCancelReject = () => {
    setShowRejectDialog(false);
    setRejectReason('Nội dung vi phạm quy định');
    setError('');
  };

  const formatPrice = (price: number): string => {
    return price.toLocaleString('vi-VN') + 'đ';
  };

  const getTimeAgo = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffInSeconds = Math.floor((now.getTime() - date.getTime()) / 1000);
    
    if (diffInSeconds < 60) return 'Vừa xong';
    if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)} phút trước`;
    if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)} giờ trước`;
    if (diffInSeconds < 604800) return `${Math.floor(diffInSeconds / 86400)} ngày trước`;
    return date.toLocaleDateString('vi-VN');
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

  const getImageUrl = (url: string): string => {
    if (!url) return '';
    // If URL already starts with http/https, return as is
    if (url.startsWith('http://') || url.startsWith('https://')) {
      return url;
    }
    // If URL starts with /, prepend base URL
    if (url.startsWith('/')) {
      return `http://localhost:8088${url}`;
    }
    // Otherwise, prepend base URL with /
    return `http://localhost:8088/${url}`;
  };

  const getAvatarUrl = () => {
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(post.author.username)}&background=random`;
  };

  const images = post.media?.map(m => getImageUrl(m.thumbnailUrl || m.url)).filter(Boolean) || [];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm">
      <div className="bg-white rounded-2xl shadow-2xl w-full max-w-3xl max-h-[90vh] overflow-hidden flex flex-col">
        {/* Dialog Header */}
        <div className="bg-gradient-to-r from-gray-600 via-gray-500 to-gray-600 p-4 flex items-center justify-between">
          <h2 className="text-xl font-bold text-white">Phê duyệt bài viết</h2>
          <button
            onClick={onClose}
            className="w-8 h-8 bg-white/20 hover:bg-white/30 rounded-full flex items-center justify-center transition text-white"
          >
            <FaTimes />
          </button>
        </div>

        {/* Content - Similar to PostCard */}
        <div className="flex-1 overflow-y-auto">
          <div className="bg-white rounded-xl">
            {/* Header - Author Info */}
            <div className="p-4 border-b border-gray-200">
              <div className="flex items-start justify-between">
                <div className="flex items-center space-x-3">
                  <img
                    src={getAvatarUrl()}
                    alt={post.author.username}
                    className="w-12 h-12 rounded-full object-cover border-2 border-gray-500"
                  />
                  <div className="flex-1">
                    <div className="flex items-center space-x-2">
                      <h3 className="font-semibold text-gray-800 text-lg">{post.author.username}</h3>
                      {getStatusBadge(post.status)}
                    </div>
                    <div className="flex items-center space-x-2 text-sm text-gray-500 mt-1">
                      <span className="flex items-center">
                        <FaClock className="mr-1" />
                        {getTimeAgo(post.createdAt)}
                      </span>
                      <span className="flex items-center">
                        <FaMapMarkerAlt className="mr-1 ml-2" />
                        {post.location}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            {/* Title */}
            <div className="px-4 pt-4 pb-2">
              <h2 className="text-xl font-bold text-gray-800 mb-2">{post.title}</h2>
            </div>

            {/* Content */}
            <div className="px-4 pb-4">
              <p className="text-gray-700 leading-relaxed whitespace-pre-wrap">{post.description}</p>
            </div>

            {/* Price */}
            {post.price && (
              <div className="px-4 pb-4">
                <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-lg p-4 border border-gray-200">
                  <div className="flex items-baseline space-x-3">
                    <span className="text-3xl font-bold text-gray-700">
                      {formatPrice(post.price)}
                    </span>
                  </div>
                </div>
              </div>
            )}

            {/* Images */}
            {images.length > 0 && (
              <div className={`px-4 pb-4 ${images.length === 1 ? '' : 'grid gap-2'}`}>
                {images.length === 1 ? (
                  <img
                    src={images[0]}
                    alt={post.title}
                    className="w-full rounded-lg object-cover max-h-96"
                    onError={(e) => {
                      (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x300?text=Image+Not+Found';
                    }}
                  />
                ) : images.length === 2 ? (
                  <div className="grid grid-cols-2 gap-2">
                    {images.map((img, idx) => (
                      <img
                        key={idx}
                        src={img}
                        alt={`${post.title} ${idx + 1}`}
                        className="w-full h-64 rounded-lg object-cover"
                        onError={(e) => {
                          (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x300?text=Image+Not+Found';
                        }}
                      />
                    ))}
                  </div>
                ) : (
                  <div className="grid grid-cols-2 gap-2">
                    {images.slice(0, 4).map((img, idx) => (
                      <div key={idx} className={idx === 3 && images.length > 4 ? 'relative' : ''}>
                        <img
                          src={img}
                          alt={`${post.title} ${idx + 1}`}
                          className="w-full h-48 rounded-lg object-cover"
                          onError={(e) => {
                            (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x300?text=Image+Not+Found';
                          }}
                        />
                        {idx === 3 && images.length > 4 && (
                          <div className="absolute inset-0 bg-black/50 rounded-lg flex items-center justify-center">
                            <span className="text-white font-bold text-xl">
                              +{images.length - 4}
                            </span>
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}

            {/* Stats */}
            <div className="px-4 pb-3 border-b border-gray-200">
              <div className="flex items-center justify-between text-sm text-gray-600">
                <span>{post.totalViews || 0} lượt xem</span>
                <span>{post.totalComments || 0} bình luận</span>
              </div>
            </div>

            {/* Additional Info */}
            <div className="px-4 py-3 bg-gray-50 border-t border-gray-200">
              <div className="grid grid-cols-2 gap-4 text-sm">
                <div>
                  <span className="text-gray-500">Danh mục:</span>
                  <span className="ml-2 font-medium text-gray-700">{post.category.name}</span>
                </div>
                <div>
                  <span className="text-gray-500">ID:</span>
                  <span className="ml-2 font-medium text-gray-700">#{post.id}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Footer with Actions */}
        <div className="border-t border-gray-200 p-4 bg-gray-50">
          <div className="flex items-center justify-end space-x-3">
            {post.status === 'PENDING' && (
              <>
                <button
                  onClick={() => {
                    onApprove();
                    onClose();
                  }}
                  className="px-6 py-3 bg-gradient-to-r from-green-600 to-green-700 hover:from-green-700 hover:to-green-800 text-white rounded-xl font-semibold transition-all duration-300 transform hover:scale-[1.02] shadow-lg hover:shadow-xl flex items-center space-x-2"
                >
                  <FaCheck />
                  <span>Duyệt bài viết</span>
                </button>
                <button
                  onClick={handleRejectClick}
                  className="px-6 py-3 bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 text-white rounded-xl font-semibold transition-all duration-300 transform hover:scale-[1.02] shadow-lg hover:shadow-xl flex items-center space-x-2"
                >
                  <FaReject />
                  <span>Từ chối bài viết</span>
                </button>
              </>
            )}
            <button
              onClick={onClose}
              className="px-6 py-3 border-2 border-gray-300 text-gray-700 rounded-xl font-semibold hover:bg-gray-100 transition"
            >
              Đóng
            </button>
          </div>
        </div>
      </div>

      {/* Reject Reason Dialog */}
      {showRejectDialog && (
        <div className="fixed inset-0 z-[60] flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
          <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md">
            {/* Header */}
            <div className="bg-gradient-to-r from-red-600 via-red-500 to-red-600 p-5 flex items-center justify-between rounded-t-2xl">
              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 bg-white/20 rounded-full flex items-center justify-center">
                  <FaExclamationTriangle className="text-white text-lg" />
                </div>
                <h3 className="text-xl font-bold text-white">Từ chối bài viết</h3>
              </div>
              <button
                onClick={handleCancelReject}
                className="w-8 h-8 bg-white/20 hover:bg-white/30 rounded-full flex items-center justify-center transition text-white"
              >
                <FaTimes />
              </button>
            </div>

            {/* Content */}
            <div className="p-6">
              <p className="text-gray-700 mb-4">
                Vui lòng nhập lý do từ chối bài viết này:
              </p>
              <textarea
                value={rejectReason}
                onChange={(e) => {
                  setRejectReason(e.target.value);
                  setError('');
                }}
                rows={4}
                className={`w-full px-4 py-3 border-2 ${
                  error ? 'border-red-500' : 'border-gray-200'
                } rounded-xl focus:ring-2 focus:ring-red-500 focus:border-red-500 outline-none transition resize-none`}
                placeholder="Nhập lý do từ chối..."
              />
              {error && (
                <p className="mt-2 text-sm text-red-500">{error}</p>
              )}
            </div>

            {/* Footer */}
            <div className="border-t border-gray-200 p-5 flex items-center justify-end space-x-3 bg-gray-50 rounded-b-2xl">
              <button
                onClick={handleCancelReject}
                className="px-5 py-2.5 border-2 border-gray-300 text-gray-700 rounded-lg font-semibold hover:bg-gray-100 transition"
              >
                Hủy
              </button>
              <button
                onClick={handleConfirmReject}
                className="px-5 py-2.5 bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 text-white rounded-lg font-semibold transition-all duration-300 transform hover:scale-[1.02] shadow-lg hover:shadow-xl flex items-center space-x-2"
              >
                <FaReject />
                <span>Xác nhận từ chối</span>
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
