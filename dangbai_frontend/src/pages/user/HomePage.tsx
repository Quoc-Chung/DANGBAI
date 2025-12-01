// src/pages/user/HomePage.tsx
import React, { useState, useEffect } from 'react';
import { FaPlus, FaSpinner } from 'react-icons/fa';
import { UserLayout } from '../../components/layout/UserLayout';
import { PostCard } from '../../components/post/PostCard';
import { FilterCard, type FilterState } from '../../components/post/FilterCard';
import { CreatePostDialog } from '../../components/post/CreatePostDialog';
import { useAuth } from '../../hooks/useAuth';
import { postApi } from '../../api/post.api';
import { ReactionType } from '../../types/post';
import type { Post } from '../../types/post';
import { toast } from 'react-toastify';

export const HomePage: React.FC = () => {
  const { user } = useAuth();
  const [isCreatePostDialogOpen, setIsCreatePostDialogOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [filters, setFilters] = useState<FilterState>({
    search: '',
    location: '',
    category: '',
    priceRange: '',
    condition: '',
    sortBy: 'newest'
  });
  const [posts, setPosts] = useState<Post[]>([]);

  // Load approved posts from API
  useEffect(() => {
    loadPosts();
  }, []);

  const loadPosts = async () => {
    try {
      setIsLoading(true);
      const approvedPosts = await postApi.getApprovedPosts();
      
      // Map AdminPost to Post format
      const mappedPosts: Post[] = approvedPosts.map((apiPost) => ({
        postId: apiPost.id,
        userId: apiPost.author.id,
        username: apiPost.author.username,
        displayName: apiPost.author.username, // Use username as displayName if not available
        avatarUrl: undefined,
        title: apiPost.title,
        content: apiPost.description,
        location: apiPost.location,
        price: apiPost.price,
        oldPrice: undefined, // API doesn't provide oldPrice
        condition: undefined, // API doesn't provide condition
        images: apiPost.media?.map(m => {
          const url = m.thumbnailUrl || m.url;
          if (url.startsWith('http')) return url;
          if (url.startsWith('/')) return `http://localhost:8088${url}`;
          return `http://localhost:8088/${url}`;
        }) || [],
        reactions: [], // Will be loaded separately if needed
        comments: [], // Will be loaded separately if needed
        shares: 0,
        createdAt: apiPost.createdAt,
        updatedAt: apiPost.updatedAt,
      }));

      setPosts(mappedPosts);
    } catch (error: any) {
      console.error('Failed to load posts:', error);
      toast.error('Không thể tải danh sách bài viết');
    } finally {
      setIsLoading(false);
    }
  };

  const handleReaction = (postId: number, reactionType: ReactionType) => {
    setPosts(prevPosts =>
      prevPosts.map(post => {
        if (post.postId === postId) {
          const existingReaction = post.reactions.find(r => r.userId === user?.userId);
          let newReactions = [...post.reactions];

          if (existingReaction) {
            if (existingReaction.reactionType === reactionType) {
              // Remove reaction if clicking the same
              newReactions = newReactions.filter(r => r.userId !== user?.userId);
            } else {
              // Update reaction
              newReactions = newReactions.map(r =>
                r.userId === user?.userId
                  ? { ...r, reactionType }
                  : r
              );
            }
          } else {
            // Add new reaction
            newReactions.push({
              reactionId: Date.now(),
              userId: user?.userId || 0,
              username: user?.username || '',
              displayName: user?.displayName || '',
              reactionType: reactionType,
            });
          }

          return { ...post, reactions: newReactions };
        }
        return post;
      })
    );
  };

  const handleComment = (postId: number, content: string) => {
    setPosts(prevPosts =>
      prevPosts.map(post => {
        if (post.postId === postId) {
          const newComment = {
            commentId: Date.now(),
            userId: user?.userId || 0,
            username: user?.username || '',
            displayName: user?.displayName || '',
            avatarUrl: user?.avatarUrl,
            content,
            createdAt: new Date().toISOString(),
          };
          return { ...post, comments: [...post.comments, newComment] };
        }
        return post;
      })
    );
  };

  const handleReply = (postId: number, commentId: number, content: string) => {
    setPosts(prevPosts =>
      prevPosts.map(post => {
        if (post.postId === postId) {
          const newReply = {
            commentId: Date.now(),
            userId: user?.userId || 0,
            username: user?.username || '',
            displayName: user?.displayName || '',
            avatarUrl: user?.avatarUrl,
            content,
            createdAt: new Date().toISOString(),
          };
          const updatedComments = post.comments.map(comment => {
            if (comment.commentId === commentId) {
              return {
                ...comment,
                replies: [...(comment.replies || []), newReply],
              };
            }
            return comment;
          });
          return { ...post, comments: updatedComments };
        }
        return post;
      })
    );
  };

  const handleContact = (postId: number, userId: number) => {
    // TODO: Implement contact seller functionality
    // This could open a chat modal, show contact info, etc.
    console.log('Contact seller for post:', postId, 'User:', userId);
    // You can add toast notification or open a modal here
    alert(`Liên hệ với người bán của bài viết #${postId}`);
  };

  const handleFilterChange = (newFilters: FilterState) => {
    setFilters(newFilters);
    // TODO: Apply filters to posts
    // This will be implemented when connecting to API
    console.log('Filters changed:', newFilters);
  };

  return (
    <UserLayout>
      <div className="w-full">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start px-6">
          {/* Left Sidebar - Filter Card */}
          <div className="lg:col-span-3 -ml-6">
            <div className="sticky top-24">
              <FilterCard onFilterChange={handleFilterChange} />
            </div>
          </div>

          {/* Center - Posts Feed */}
          <div className="lg:col-span-6">
            {isLoading ? (
              <div className="flex items-center justify-center py-20">
                <FaSpinner className="animate-spin text-4xl text-gray-400" />
              </div>
            ) : posts.length === 0 ? (
              <div className="text-center py-20">
                <p className="text-gray-500 text-lg">Chưa có bài viết nào</p>
              </div>
            ) : (
              <div>
                {posts.map((post) => (
                  <PostCard
                    key={post.postId}
                    post={post}
                    currentUserId={user?.userId}
                    onReaction={handleReaction}
                    onComment={handleComment}
                    onReply={handleReply}
                    onContact={handleContact}
                  />
                ))}
              </div>
            )}
          </div>

          {/* Right Sidebar - Create Post Button */}
          <div className="lg:col-span-3 -mr-6">
            <div className="sticky top-24">
              <button 
                onClick={() => setIsCreatePostDialogOpen(true)}
                className="w-full bg-gradient-to-r from-gray-600 to-gray-700 text-white py-4 rounded-xl font-semibold hover:from-gray-700 hover:to-gray-800 transition-all duration-300 transform hover:scale-[1.02] shadow-lg hover:shadow-xl flex items-center justify-center space-x-2"
              >
                <FaPlus className="text-xl" />
                <span>Tạo bài đăng mới</span>
              </button>
              
              {/* Additional Info Card */}
              <div className="mt-4 bg-gradient-to-br from-gray-50 to-gray-100 rounded-xl p-4 border border-gray-200">
                <h4 className="font-semibold text-gray-800 mb-2">💡 Mẹo tìm kiếm</h4>
                <p className="text-sm text-gray-600">
                  Sử dụng bộ lọc bên trái để tìm kiếm bài đăng phù hợp với nhu cầu của bạn.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Create Post Dialog */}
      <CreatePostDialog
        isOpen={isCreatePostDialogOpen}
        onClose={() => setIsCreatePostDialogOpen(false)}
        onSuccess={() => {
          // Refresh posts list when post is created successfully
          loadPosts();
        }}
      />
    </UserLayout>
  );
};
