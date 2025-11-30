// src/components/post/ReactionButton.tsx
import React, { useState, useRef, useEffect } from 'react';
import { FaThumbsUp } from 'react-icons/fa';
import { ReactionType } from '../../types/post';
import type { ReactionType as ReactionTypeType } from '../../types/post';

interface ReactionButtonProps {
  postId: number;
  currentReaction: ReactionTypeType | null;
  onReaction: (reactionType: ReactionTypeType) => void;
  showPicker: boolean;
  onTogglePicker: () => void;
}

const reactions: { type: ReactionTypeType; emoji: string; label: string; color: string }[] = [
  { type: ReactionType.LIKE, emoji: '👍', label: 'Thích', color: 'bg-blue-500' },
  { type: ReactionType.LOVE, emoji: '❤️', label: 'Yêu thích', color: 'bg-red-500' },
  { type: ReactionType.HAHA, emoji: '😂', label: 'Haha', color: 'bg-yellow-500' },
  { type: ReactionType.WOW, emoji: '😮', label: 'Wow', color: 'bg-yellow-400' },
  { type: ReactionType.SAD, emoji: '😢', label: 'Buồn', color: 'bg-yellow-300' },
  { type: ReactionType.ANGRY, emoji: '😡', label: 'Phẫn nộ', color: 'bg-red-600' },
];

export const ReactionButton: React.FC<ReactionButtonProps> = ({
  postId,
  currentReaction,
  onReaction,
  showPicker,
  onTogglePicker
}) => {
  const pickerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (pickerRef.current && !pickerRef.current.contains(event.target as Node)) {
        if (showPicker) {
          onTogglePicker();
        }
      }
    };

    if (showPicker) {
      document.addEventListener('mousedown', handleClickOutside);
      return () => document.removeEventListener('mousedown', handleClickOutside);
    }
  }, [showPicker, onTogglePicker]);

  const getButtonContent = () => {
    if (currentReaction) {
      const reaction = reactions.find(r => r.type === currentReaction);
      return (
        <div className="flex items-center space-x-2">
          <span className="text-lg">{reaction?.emoji}</span>
          <span className="font-medium" style={{ color: reaction?.color.replace('bg-', '') }}>
            {reaction?.label}
          </span>
        </div>
      );
    }
    return (
      <div className="flex items-center space-x-2">
        <FaThumbsUp className="text-xl" />
        <span className="font-medium">Thích</span>
      </div>
    );
  };

  return (
    <div className="relative" ref={pickerRef}>
      <button
        onMouseEnter={() => !currentReaction && onTogglePicker()}
        onClick={() => {
          if (currentReaction) {
            onReaction(ReactionType.LIKE); // Remove reaction by clicking again
          } else {
            onReaction(ReactionType.LIKE);
          }
        }}
        className={`flex items-center space-x-2 px-4 py-2 rounded-lg transition ${
          currentReaction
            ? 'bg-gray-50 text-gray-700 hover:bg-gray-100'
            : 'text-gray-600 hover:bg-gray-100'
        }`}
      >
        {getButtonContent()}
      </button>

      {/* Reaction Picker */}
      {showPicker && (
        <div className="absolute bottom-full left-0 mb-2 bg-white rounded-full shadow-2xl border border-gray-200 p-2 flex items-center space-x-1 z-50">
          {reactions.map((reaction) => (
            <button
              key={reaction.type}
              onClick={() => onReaction(reaction.type)}
              onMouseEnter={() => {}}
              className="w-10 h-10 flex items-center justify-center rounded-full hover:scale-125 transition-transform text-2xl"
              title={reaction.label}
            >
              {reaction.emoji}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

