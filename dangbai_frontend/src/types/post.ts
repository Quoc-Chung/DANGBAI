// src/types/post.ts

export const ReactionType = {
  LIKE: 'LIKE',
  LOVE: 'LOVE',
  HAHA: 'HAHA',
  WOW: 'WOW',
  SAD: 'SAD',
  ANGRY: 'ANGRY'
} as const;

export type ReactionType = typeof ReactionType[keyof typeof ReactionType];

export interface Reaction {
  reactionId: number;
  userId: number;
  username: string;
  displayName: string;
  reactionType: ReactionType;
}

export interface Comment {
  commentId: number;
  userId: number;
  username: string;
  displayName: string;
  avatarUrl?: string;
  content: string;
  createdAt: string;
  replies?: Comment[];
}

export interface Post {
  postId: number;
  userId: number;
  username: string;
  displayName: string;
  avatarUrl?: string;
  title: string;
  content: string;
  location: string;
  price?: number;
  oldPrice?: number;
  condition?: string;
  images: string[];
  reactions: Reaction[];
  comments: Comment[];
  shares: number;
  createdAt: string;
  updatedAt?: string;
}

