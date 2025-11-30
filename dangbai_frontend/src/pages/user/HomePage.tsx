// src/pages/user/HomePage.tsx
import React, { useState } from 'react';
import { FaPlus } from 'react-icons/fa';
import { UserLayout } from '../../components/layout/UserLayout';
import { PostCard } from '../../components/post/PostCard';
import { FilterCard, type FilterState } from '../../components/post/FilterCard';
import { useAuth } from '../../hooks/useAuth';
import { ReactionType } from '../../types/post';
import type { Post } from '../../types/post';

export const HomePage: React.FC = () => {
  const { user } = useAuth();
  const [filters, setFilters] = useState<FilterState>({
    search: '',
    location: '',
    category: '',
    priceRange: '',
    condition: '',
    sortBy: 'newest'
  });
  const [posts, setPosts] = useState<Post[]>([
    {
      postId: 1,
      userId: 1,
      username: 'nguyenvana',
      displayName: 'Nguyễn Văn A',
      title: 'Bán iPhone 13 Pro Max - 256GB',
      content: 'Mình cần bán iPhone 13 Pro Max 256GB màu xanh, máy còn bảo hành đến tháng 6/2024. Máy đã dùng được 8 tháng, còn 99% như mới, không trầy xước, pin còn 98%. Đầy đủ phụ kiện trong hộp.\n\nLý do bán: Nâng cấp lên iPhone 15 Pro Max.',
      location: 'Hà Nội',
      price: 18500000,
      oldPrice: 25000000,
      condition: '99%',
      images: [
        'https://images.unsplash.com/photo-1632661674596-df8be070a5c5?w=800',
        'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=800'
      ],
      reactions: [
        { reactionId: 1, userId: 2, username: 'user2', displayName: 'Trần Thị B', reactionType: ReactionType.LOVE },
        { reactionId: 2, userId: 3, username: 'user3', displayName: 'Lê Văn C', reactionType: ReactionType.LIKE },
        { reactionId: 3, userId: 4, username: 'user4', displayName: 'Phạm Thị D', reactionType: ReactionType.WOW },
      ],
      comments: [
        {
          commentId: 1,
          userId: 2,
          username: 'user2',
          displayName: 'Trần Thị B',
          content: 'Máy còn bảo hành không bạn?',
          createdAt: new Date(Date.now() - 3600000).toISOString(),
          replies: [
            {
              commentId: 11,
              userId: 1,
              username: 'nguyenvana',
              displayName: 'Nguyễn Văn A',
              content: 'Còn bảo hành đến tháng 6/2024 bạn nhé!',
              createdAt: new Date(Date.now() - 3300000).toISOString(),
            }
          ]
        },
        {
          commentId: 2,
          userId: 3,
          username: 'user3',
          displayName: 'Lê Văn C',
          content: 'Giá có thương lượng được không ạ?',
          createdAt: new Date(Date.now() - 1800000).toISOString(),
        }
      ],
      shares: 5,
      createdAt: new Date(Date.now() - 7200000).toISOString(),
    },
    {
      postId: 2,
      userId: 2,
      username: 'tranthib',
      displayName: 'Trần Thị B',
      title: 'MacBook Air M1 2020 - 256GB',
      content: 'Bán MacBook Air M1 2020, 256GB SSD, 8GB RAM. Máy còn 95% như mới, không trầy xước, pin còn 92%. Đầy đủ hộp và sạc.\n\nMáy chạy rất mượt, pin trâu, phù hợp cho học tập và làm việc văn phòng.',
      location: 'TP.HCM',
      price: 16800000,
      oldPrice: 24000000,
      condition: '95%',
      images: [
        'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800',
        'https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=800'
      ],
      reactions: [
        { reactionId: 4, userId: 1, username: 'nguyenvana', displayName: 'Nguyễn Văn A', reactionType: ReactionType.LIKE },
        { reactionId: 5, userId: 3, username: 'user3', displayName: 'Lê Văn C', reactionType: ReactionType.LOVE },
      ],
      comments: [],
      shares: 2,
      createdAt: new Date(Date.now() - 10800000).toISOString(),
    },
    {
      postId: 3,
      userId: 3,
      username: 'levanc',
      displayName: 'Lê Văn C',
      title: 'AirPods Pro Gen 2 - Hàng chính hãng',
      content: 'Bán AirPods Pro Gen 2 hàng chính hãng Apple, còn bảo hành 8 tháng. Tai nghe còn 98% như mới, không trầy xước, đầy đủ phụ kiện trong hộp.\n\nÂm thanh cực kỳ hay, noise cancellation tốt, pin trâu.',
      location: 'Đà Nẵng',
      price: 4200000,
      oldPrice: 5800000,
      condition: '98%',
      images: [
        'https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=800'
      ],
      reactions: [
        { reactionId: 6, userId: 1, username: 'nguyenvana', displayName: 'Nguyễn Văn A', reactionType: ReactionType.HAHA },
        { reactionId: 7, userId: 2, username: 'tranthib', displayName: 'Trần Thị B', reactionType: ReactionType.WOW },
        { reactionId: 8, userId: 4, username: 'user4', displayName: 'Phạm Thị D', reactionType: ReactionType.LIKE },
      ],
      comments: [
        {
          commentId: 3,
          userId: 1,
          username: 'nguyenvana',
          displayName: 'Nguyễn Văn A',
          content: 'Bạn ship được không?',
          createdAt: new Date(Date.now() - 900000).toISOString(),
        }
      ],
      shares: 8,
      createdAt: new Date(Date.now() - 14400000).toISOString(),
    },
  ]);

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
          </div>

          {/* Right Sidebar - Create Post Button */}
          <div className="lg:col-span-3 -mr-6">
            <div className="sticky top-24">
              <button className="w-full bg-gradient-to-r from-gray-600 to-gray-700 text-white py-4 rounded-xl font-semibold hover:from-gray-700 hover:to-gray-800 transition-all duration-300 transform hover:scale-[1.02] shadow-lg hover:shadow-xl flex items-center justify-center space-x-2">
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
    </UserLayout>
  );
};
