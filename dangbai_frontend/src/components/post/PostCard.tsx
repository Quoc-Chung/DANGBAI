// src/components/post/PostCard.tsx
import React, { useState, useRef, useEffect } from 'react';
import { FaMapMarkerAlt, FaClock, FaShare, FaComment, FaEllipsisH, FaPhone, FaEnvelope, FaUser } from 'react-icons/fa';
import { ReactionButton } from './ReactionButton';
import { CommentSection } from './CommentSection';
import { ReactionType } from '../../types/post';
import type { Post } from '../../types/post';

interface PostCardProps {
  post: Post;
  currentUserId?: number;
  onReaction?: (postId: number, reactionType: ReactionType) => void;
  onComment?: (postId: number, content: string) => void;
  onReply?: (postId: number, commentId: number, content: string) => void;
  onContact?: (postId: number, userId: number) => void;
}

export const PostCard: React.FC<PostCardProps> = ({ 
  post, 
  currentUserId,
  onReaction,
  onComment,
  onReply,
  onContact
}) => {
  const [showComments, setShowComments] = useState(false);
  const [showReactionPicker, setShowReactionPicker] = useState(false);
  const [showMenu, setShowMenu] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);

  // Close menu when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setShowMenu(false);
      }
    };

    if (showMenu) {
      document.addEventListener('mousedown', handleClickOutside);
      return () => document.removeEventListener('mousedown', handleClickOutside);
    }
  }, [showMenu]);

  const getAvatarUrl = () => {
    if (post.avatarUrl) return post.avatarUrl;
    return `https://ui-avatars.com/api/?name=${encodeURIComponent(post.displayName)}&background=random`;
  };

  const formatPrice = (price: number) => {
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

  const getReactionCounts = () => {
    const counts: Partial<Record<ReactionType, number>> = {};
    
    Object.values(ReactionType).forEach(type => {
      counts[type] = 0;
    });
    
    post.reactions.forEach(reaction => {
      counts[reaction.reactionType] = (counts[reaction.reactionType] || 0) + 1;
    });
    
    return counts;
  };

  const getTotalReactions = () => {
    return post.reactions.length;
  };

  const getUserReaction = (): ReactionType | null => {
    if (!currentUserId) return null;
    const userReaction = post.reactions.find(r => r.userId === currentUserId);
    return userReaction ? userReaction.reactionType : null;
  };

  const reactionCounts = getReactionCounts();
  const totalReactions = getTotalReactions();
  const userReaction = getUserReaction();

  return (
    <div className="bg-white rounded-xl shadow-lg border border-gray-100 overflow-hidden mb-6">
      {/* Header */}
      <div className="p-4 border-b border-gray-200">
        <div className="flex items-start justify-between">
          <div className="flex items-center space-x-3">
            <img
              src={getAvatarUrl()}
              alt={post.displayName}
              className="w-12 h-12 rounded-full object-cover border-2 border-gray-500"
            />
            <div className="flex-1">
              <div className="flex items-center space-x-2">
                <h3 className="font-semibold text-gray-800 text-lg">{post.displayName}</h3>
                {post.condition && (
                  <span className="px-2 py-0.5 bg-green-100 text-green-800 text-xs font-medium rounded-full">
                    {post.condition}
                  </span>
                )}
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
          <div className="relative" ref={menuRef}>
            <button
              onClick={() => setShowMenu(!showMenu)}
              className="p-2 hover:bg-gray-100 rounded-full transition"
            >
              <FaEllipsisH className="text-gray-500" />
            </button>

            {/* Popover Menu */}
            {showMenu && (
              <div className="absolute right-0 top-full mt-2 w-56 bg-white rounded-lg shadow-xl border border-gray-200 py-2 z-50">
                {/* Contact Seller Button */}
                <button
                  onClick={() => {
                    onContact?.(post.postId, post.userId);
                    setShowMenu(false);
                  }}
                  className="w-full flex items-center px-4 py-2.5 text-base text-gray-700 hover:bg-blue-50 transition text-left"
                >
                  <FaPhone className="mr-3 text-gray-600" />
                  <span className="font-medium">Liên hệ người bán</span>
                </button>

                {/* View Profile */}
                <button
                  onClick={() => {
                    // Navigate to user profile
                    setShowMenu(false);
                  }}
                  className="w-full flex items-center px-4 py-2.5 text-base text-gray-700 hover:bg-gray-100 transition text-left"
                >
                  <FaUser className="mr-3 text-gray-500" />
                  <span>Xem trang cá nhân</span>
                </button>

                {/* Report */}
                {currentUserId !== post.userId && (
                  <>
                    <hr className="my-1 border-gray-200" />
                    <button
                      onClick={() => {
                        // Handle report
                        setShowMenu(false);
                      }}
                      className="w-full flex items-center px-4 py-2.5 text-base text-red-600 hover:bg-red-50 transition text-left"
                    >
                      <FaEnvelope className="mr-3" />
                      <span>Báo cáo bài viết</span>
                    </button>
                  </>
                )}

                {/* Edit/Delete (if own post) */}
                {currentUserId === post.userId && (
                  <>
                    <hr className="my-1 border-gray-200" />
                    <button
                      onClick={() => {
                        // Handle edit
                        setShowMenu(false);
                      }}
                      className="w-full flex items-center px-4 py-2.5 text-base text-gray-700 hover:bg-gray-100 transition text-left"
                    >
                      <span>Chỉnh sửa bài viết</span>
                    </button>
                    <button
                      onClick={() => {
                        // Handle delete
                        setShowMenu(false);
                      }}
                      className="w-full flex items-center px-4 py-2.5 text-base text-red-600 hover:bg-red-50 transition text-left"
                    >
                      <span>Xóa bài viết</span>
                    </button>
                  </>
                )}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Title */}
      <div className="px-4 pt-4 pb-2">
        <h2 className="text-xl font-bold text-gray-800 mb-2">{post.title}</h2>
      </div>

      {/* Content */}
      <div className="px-4 pb-4">
        <p className="text-gray-700 leading-relaxed whitespace-pre-wrap">{post.content}</p>
      </div>

      {/* Price (if available) */}
      {post.price && (
        <div className="px-4 pb-4">
          <div className="bg-gradient-to-r from-gray-50 to-gray-100 rounded-lg p-4 border border-gray-200">
            <div className="flex items-baseline space-x-3">
                      <span className="text-3xl font-bold text-gray-700">
                {formatPrice(post.price)}
              </span>
              {post.oldPrice && (
                <span className="text-lg text-gray-400 line-through">
                  {formatPrice(post.oldPrice)}
                </span>
              )}
            </div>
            {post.oldPrice && (
              <p className="text-sm text-green-600 font-medium mt-1">
                Giảm {Math.round(((post.oldPrice - post.price) / post.oldPrice) * 100)}%
              </p>
            )}
          </div>
        </div>
      )}

      {/* Images */}
      {post.images && post.images.length > 0 && (
        <div className={`px-4 pb-4 ${post.images.length === 1 ? '' : 'grid gap-2'}`}>
          {post.images.length === 1 ? (
            <img
              src={post.images[0]}
              alt={post.title}
              className="w-full rounded-lg object-cover max-h-96"
            />
          ) : post.images.length === 2 ? (
            <div className="grid grid-cols-2 gap-2">
              {post.images.map((img, idx) => (
                <img
                  key={idx}
                  src={img}
                  alt={`${post.title} ${idx + 1}`}
                  className="w-full h-64 rounded-lg object-cover"
                />
              ))}
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-2">
              {post.images.slice(0, 4).map((img, idx) => (
                <div key={idx} className={idx === 3 && post.images.length > 4 ? 'relative' : ''}>
                  <img
                    src={img}
                    alt={`${post.title} ${idx + 1}`}
                    className="w-full h-48 rounded-lg object-cover"
                  />
                  {idx === 3 && post.images.length > 4 && (
                    <div className="absolute inset-0 bg-black/50 rounded-lg flex items-center justify-center">
                      <span className="text-white font-bold text-xl">
                        +{post.images.length - 4}
                      </span>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Reaction Summary */}
      {totalReactions > 0 && (
        <div className="px-4 pb-3 border-b border-gray-200">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <div className="flex -space-x-1">
                {Object.entries(reactionCounts)
                  .filter(([_, count]) => count > 0)
                  .slice(0, 3)
                  .map(([type, count]) => (
                    <div
                      key={type}
                      className="w-6 h-6 bg-white rounded-full border-2 border-white flex items-center justify-center"
                      title={type}
                    >
                      <span className="text-xs">{
                        type === ReactionType.LIKE ? '👍' :
                        type === ReactionType.LOVE ? '❤️' :
                        type === ReactionType.HAHA ? '😂' :
                        type === ReactionType.WOW ? '😮' :
                        type === ReactionType.SAD ? '😢' : '😡'
                      }</span>
                    </div>
                  ))}
              </div>
              <span className="text-sm text-gray-600 font-medium">
                {totalReactions} {totalReactions === 1 ? 'cảm xúc' : 'cảm xúc'}
              </span>
            </div>
            <div className="text-sm text-gray-600">
              {post.comments.length} {post.comments.length === 1 ? 'bình luận' : 'bình luận'}
              {post.shares > 0 && ` · ${post.shares} chia sẻ`}
            </div>
          </div>
        </div>
      )}

      {/* Action Buttons */}
      <div className="px-4 py-2 border-b border-gray-200">
        <div className="flex items-center justify-around">
          <ReactionButton
            postId={post.postId}
            currentReaction={userReaction}
            onReaction={(reactionType) => {
              onReaction?.(post.postId, reactionType);
              setShowReactionPicker(false);
            }}
            showPicker={showReactionPicker}
            onTogglePicker={() => setShowReactionPicker(!showReactionPicker)}
          />
          
          <button
            onClick={() => setShowComments(!showComments)}
            className="flex items-center space-x-2 px-4 py-2 rounded-lg hover:bg-gray-100 transition text-gray-600"
          >
            <FaComment className="text-xl" />
            <span className="font-medium">Bình luận</span>
          </button>

          <button className="flex items-center space-x-2 px-4 py-2 rounded-lg hover:bg-gray-100 transition text-gray-600">
            <FaShare className="text-xl" />
            <span className="font-medium">Chia sẻ</span>
          </button>
        </div>
      </div>

      {/* Comments Section */}
      {showComments && (
        <CommentSection
          postId={post.postId}
          comments={post.comments}
          currentUserId={currentUserId}
          onComment={(content) => onComment?.(post.postId, content)}
          onReply={(commentId, content) => onReply?.(post.postId, commentId, content)}
        />
      )}
    </div>
  );
};

