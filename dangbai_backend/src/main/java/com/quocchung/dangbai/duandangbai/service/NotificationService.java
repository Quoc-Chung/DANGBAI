package com.quocchung.dangbai.duandangbai.service;

import com.quocchung.dangbai.duandangbai.model.Comment;
import com.quocchung.dangbai.duandangbai.model.Notification;
import com.quocchung.dangbai.duandangbai.model.Post;
import com.quocchung.dangbai.duandangbai.model.Reaction;
import com.quocchung.dangbai.duandangbai.model.User;
import com.quocchung.dangbai.duandangbai.utils.enums.NotificationType;
import java.util.List;

public interface NotificationService {
  void createNotification(User user, NotificationType type, String message, Post post);

  void notifyPostCreate(Post post);

  void notifyPostApproved(Post post);

  void notifyPostRejected(Post post, String reason);

  void notifyComment(Post post, Comment comment);

  void notifyReaction(Post post, Reaction reaction);

  List<Notification> getUserNotifications(Long userId);
}
