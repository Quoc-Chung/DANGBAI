// src/components/post/PostCard.tsx
import React, { useState, useRef, useEffect } from 'react';
import { FaMapMarkerAlt, FaShare, FaComment, FaEllipsisH, FaPhone, FaEnvelope, FaUser } from 'react-icons/fa';
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
    <div className="bg-white rounded-lg shadow-sm hover:shadow-md border border-gray-200 overflow-hidden mb-4 transition-all duration-200">
      {/* Header */}
      <div className="px-4 py-3 border-b border-gray-200">
        <div className="flex items-start justify-between">
          <div className="flex items-center space-x-3 flex-1">
            <img
              src={getAvatarUrl()}
              alt={post.displayName}
              className="w-10 h-10 rounded-full object-cover cursor-pointer hover:opacity-90 transition-opacity"
            />
            <div className="flex-1 min-w-0">
              <div className="flex items-center space-x-2">
                <h3 className="font-semibold text-gray-900 text-[15px] cursor-pointer hover:underline">
                  {post.displayName}
                </h3>
                {post.condition && (
                  <span className="px-2 py-0.5 bg-emerald-100 text-emerald-700 text-xs font-semibold rounded-full">
                    {post.condition}
                  </span>
                )}
              </div>
              <div className="flex items-center space-x-1 text-[13px] text-gray-500 mt-0.5">
                <span>{getTimeAgo(post.createdAt)}</span>
                <span>·</span>
                <span className="flex items-center">
                  <FaMapMarkerAlt className="mr-1 text-xs" />
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

      {/* Title & Price */}
      <div className="px-4 pt-3 pb-2">
        <div className="flex items-start justify-between gap-3">
          <h2 className="text-[17px] font-semibold text-gray-900 leading-snug flex-1">
            {post.title}
          </h2>
          {post.price && (
            <div className="flex-shrink-0 text-right">
              <div className="flex items-baseline gap-2">
                <span className="text-lg font-bold text-blue-600">
                  {formatPrice(post.price)}
                </span>
                {post.oldPrice && (
                  <span className="text-sm text-gray-400 line-through">
                    {formatPrice(post.oldPrice)}
                  </span>
                )}
              </div>
              {post.oldPrice && (
                <p className="text-xs text-green-600 font-medium mt-0.5">
                  Giảm {Math.round(((post.oldPrice - post.price) / post.oldPrice) * 100)}%
                </p>
              )}
            </div>
          )}
        </div>
      </div>

      {/* Content */}
      {post.content && (
        <div className="px-4 pb-3">
          <p className="text-[15px] text-gray-800 leading-relaxed whitespace-pre-wrap break-words">
            {post.content}
          </p>
        </div>
      )}

      {/* Images - Facebook Style Layout */}
      {post.images && post.images.length > 0 && (
        <div className="px-4 pb-4">
          {post.images.length === 1 ? (
            // Single image - Full width, large
            <div className="w-full rounded-lg overflow-hidden bg-gray-100">
              <img
                src={post.images[0]}
                alt={post.title}
                className="w-full h-auto max-h-[600px] object-contain cursor-pointer hover:opacity-95 transition-opacity"
                onError={(e) => {
                  (e.target as HTMLImageElement).src = 'https://via.placeholder.com/800x600?text=Image+Not+Found';
                }}
              />
            </div>
          ) : post.images.length === 2 ? (
            // Two images - Side by side
            <div className="grid grid-cols-2 gap-1 rounded-lg overflow-hidden">
              {post.images.map((img, idx) => (
                <div key={idx} className="bg-gray-100">
                  <img
                    src={img}
                    alt={`${post.title} ${idx + 1}`}
                    className="w-full h-[400px] object-cover cursor-pointer hover:opacity-95 transition-opacity"
                    onError={(e) => {
                      (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x400?text=Image+Not+Found';
                    }}
                  />
                </div>
              ))}
            </div>
          ) : post.images.length === 3 ? (
            // Three images - Facebook style: Large left, 2 small right
            <div className="grid grid-cols-3 gap-1 rounded-lg overflow-hidden">
              <div className="col-span-2 row-span-2 bg-gray-100">
                <img
                  src={post.images[0]}
                  alt={`${post.title} 1`}
                  className="w-full h-full min-h-[500px] object-cover cursor-pointer hover:opacity-95 transition-opacity"
                  onError={(e) => {
                    (e.target as HTMLImageElement).src = 'https://via.placeholder.com/600x500?text=Image+Not+Found';
                  }}
                />
              </div>
              <div className="bg-gray-100">
                <img
                  src={post.images[1]}
                  alt={`${post.title} 2`}
                  className="w-full h-[249px] object-cover cursor-pointer hover:opacity-95 transition-opacity"
                  onError={(e) => {
                    (e.target as HTMLImageElement).src = 'https://via.placeholder.com/300x250?text=Image+Not+Found';
                  }}
                />
              </div>
              <div className="bg-gray-100">
                <img
                  src={post.images[2]}
                  alt={`${post.title} 3`}
                  className="w-full h-[249px] object-cover cursor-pointer hover:opacity-95 transition-opacity"
                  onError={(e) => {
                    (e.target as HTMLImageElement).src = 'https://via.placeholder.com/300x250?text=Image+Not+Found';
                  }}
                />
              </div>
            </div>
          ) : post.images.length === 4 ? (
            // Four images - Perfect 2x2 grid
            <div className="grid grid-cols-2 gap-1 rounded-lg overflow-hidden">
              {post.images.map((img, idx) => (
                <div key={idx} className="bg-gray-100">
                  <img
                    src={img}
                    alt={`${post.title} ${idx + 1}`}
                    className="w-full h-[300px] object-cover cursor-pointer hover:opacity-95 transition-opacity"
                    onError={(e) => {
                      (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x300?text=Image+Not+Found';
                    }}
                  />
                </div>
              ))}
            </div>
          ) : (
            // More than 4 images - 2x2 grid with overlay
            <div className="grid grid-cols-2 gap-1 rounded-lg overflow-hidden">
              {post.images.slice(0, 4).map((img, idx) => (
                <div 
                  key={idx} 
                  className={`bg-gray-100 ${idx === 3 ? 'relative' : ''}`}
                >
                  <img
                    src={img}
                    alt={`${post.title} ${idx + 1}`}
                    className="w-full h-[300px] object-cover cursor-pointer hover:opacity-95 transition-opacity"
                    onError={(e) => {
                      (e.target as HTMLImageElement).src = 'https://via.placeholder.com/400x300?text=Image+Not+Found';
                    }}
                  />
                  {idx === 3 && post.images.length > 4 && (
                    <div className="absolute inset-0 bg-black/60 flex items-center justify-center cursor-pointer hover:bg-black/70 transition">
                      <span className="text-white font-bold text-3xl">
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

