// src/components/post/CommentSection.tsx
import React, { useState } from 'react';
import { FaReply } from 'react-icons/fa';
import { useAuth } from '../../hooks/useAuth';
import type { Comment } from '../../types/post';

interface CommentSectionProps {
  postId: number;
  comments: Comment[];
  currentUserId?: number;
  onComment: (content: string) => void;
  onReply: (commentId: number, content: string) => void;
}

export const CommentSection: React.FC<CommentSectionProps> = ({
  postId,
  comments,
  currentUserId,
  onComment,
  onReply
}) => {
  const { user } = useAuth();
  const [newComment, setNewComment] = useState('');
  const [replyingTo, setReplyingTo] = useState<number | null>(null);
  const [replyContent, setReplyContent] = useState('');

  const getAvatarUrl = (comment: Comment) => {
    if (comment.avatarUrl) return comment.avatarUrl;
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(comment.displayName)}&background=random`;
  };

  const handleSubmitComment = (e: React.FormEvent) => {
    e.preventDefault();
    if (newComment.trim()) {
      onComment(newComment.trim());
      setNewComment('');
    }
  };

  const handleSubmitReply = (e: React.FormEvent, commentId: number) => {
    e.preventDefault();
    if (replyContent.trim()) {
      onReply(commentId, replyContent.trim());
      setReplyContent('');
      setReplyingTo(null);
    }
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

  return (
    <div className="px-4 py-4 bg-gray-50">
      {/* Comment Input */}
      <form onSubmit={handleSubmitComment} className="mb-4">
        <div className="flex items-start space-x-3">
          <img
            src={user?.avatarUrl || `https://ui-avatars.com/api/?name=${encodeURIComponent(user?.displayName || 'User')}&background=random`}
            alt={user?.displayName}
            className="w-10 h-10 rounded-full object-cover border-2 border-gray-500 flex-shrink-0"
          />
          <div className="flex-1">
            <input
              type="text"
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              placeholder="Viết bình luận..."
              className="w-full px-4 py-2 border-2 border-gray-200 rounded-full focus:ring-2 focus:ring-gray-500 focus:border-gray-500 outline-none"
            />
          </div>
          {newComment.trim() && (
            <button
              type="submit"
                      className="px-4 py-2 bg-gray-600 text-white rounded-full font-medium hover:bg-gray-700 transition"
            >
              Đăng
            </button>
          )}
        </div>
      </form>

      {/* Comments List */}
      <div className="space-y-4">
        {comments.map((comment) => (
          <div key={comment.commentId} className="flex items-start space-x-3">
            <img
              src={getAvatarUrl(comment)}
              alt={comment.displayName}
              className="w-10 h-10 rounded-full object-cover border-2 border-gray-300 flex-shrink-0"
            />
            <div className="flex-1">
              <div className="bg-white rounded-lg p-3 shadow-sm">
                <div className="flex items-center space-x-2 mb-1">
                  <span className="font-semibold text-gray-800">{comment.displayName}</span>
                  <span className="text-xs text-gray-500">{getTimeAgo(comment.createdAt)}</span>
                </div>
                <p className="text-gray-700">{comment.content}</p>
              </div>
              
              {/* Reply Button */}
              <div className="mt-1 ml-3">
                <button
                  onClick={() => setReplyingTo(replyingTo === comment.commentId ? null : comment.commentId)}
                  className="flex items-center space-x-1 text-sm text-gray-600 hover:text-gray-800 transition"
                >
                  <FaReply className="text-xs" />
                  <span>Trả lời</span>
                </button>
              </div>

              {/* Reply Input */}
              {replyingTo === comment.commentId && (
                <form
                  onSubmit={(e) => handleSubmitReply(e, comment.commentId)}
                  className="mt-2 ml-3 flex items-start space-x-2"
                >
                  <input
                    type="text"
                    value={replyContent}
                    onChange={(e) => setReplyContent(e.target.value)}
                    placeholder="Viết phản hồi..."
                    className="flex-1 px-3 py-1.5 border-2 border-gray-200 rounded-full focus:ring-2 focus:ring-gray-500 focus:border-gray-500 outline-none text-sm"
                  />
                  {replyContent.trim() && (
                    <button
                      type="submit"
                      className="px-3 py-1.5 bg-gray-600 text-white rounded-full text-sm font-medium hover:bg-gray-700 transition"
                    >
                      Gửi
                    </button>
                  )}
                </form>
              )}

              {/* Replies */}
              {comment.replies && comment.replies.length > 0 && (
                <div className="mt-2 ml-3 space-y-2">
                  {comment.replies.map((reply) => (
                    <div key={reply.commentId} className="flex items-start space-x-2">
                      <img
                        src={getAvatarUrl(reply)}
                        alt={reply.displayName}
                        className="w-8 h-8 rounded-full object-cover border border-gray-300 flex-shrink-0"
                      />
                      <div className="flex-1">
                        <div className="bg-gray-100 rounded-lg p-2">
                          <div className="flex items-center space-x-2 mb-1">
                            <span className="font-semibold text-gray-800 text-sm">{reply.displayName}</span>
                            <span className="text-xs text-gray-500">{getTimeAgo(reply.createdAt)}</span>
                          </div>
                          <p className="text-gray-700 text-sm">{reply.content}</p>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

