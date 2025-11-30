package com.quocchung.dangbai.duandangbai.service.impl;

import com.quocchung.dangbai.duandangbai.model.Comment;
import com.quocchung.dangbai.duandangbai.model.Notification;
import com.quocchung.dangbai.duandangbai.model.Post;
import com.quocchung.dangbai.duandangbai.model.Reaction;
import com.quocchung.dangbai.duandangbai.model.User;
import com.quocchung.dangbai.duandangbai.repository.NotificationRepository;
import com.quocchung.dangbai.duandangbai.service.NotificationService;
import com.quocchung.dangbai.duandangbai.utils.enums.NotificationType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl  implements NotificationService {

  private final NotificationRepository notificationRepository;

  /**
   * Tạo một thông báo mới
   * @param user
   * @param type
   * @param message
   * @param post
   */
  @Override
  public void createNotification(User user, NotificationType type, String message, Post post) {
    Notification notification = Notification.builder()
        .user(user)
        .type(type)
        .message(message)
        .post(post)
        .isRead(false)
        .createdAt(LocalDateTime.now())
        .build();
    notificationRepository.save(notification);
    log.info("Created notification for user {} - {}", user.getId(), type);
  }

  /**
   * Toong bao khi người dùng đã tạo bài
   * @param post
   */
  @Override
  public void notifyPostCreate(Post post) {
      User user = post.getAuthor();

    String msg = "Bài đăng của bạn \"" + post.getTitle() + "\" đang chờ duyệt.";

    createNotification(
        user,
        NotificationType.POST_CREATE,
        msg,
        post
    );

  }

  /**
   * Thông báo khi admin duyệt bài
   * @param post
   */
  @Override
  public void notifyPostApproved(Post post) {
    User user = post.getAuthor();

    String msg = "Bài đăng \"" + post.getTitle() + "\" đã được duyệt.";

    createNotification(
        user,
        NotificationType.POST_APPROVED,
        msg,
        post
    );

  }

  /**
   * Thông báo khi bài viết bị từ chối
   * @param post
   * @param reason
   */
  @Override
  public void notifyPostRejected(Post post, String reason) {
    User user = post.getAuthor();

    String msg = "Bài đăng \"" + post.getTitle() + "\" bị từ chối: " + reason;

    createNotification(
        user,
        NotificationType.POST_REJECTED,
        msg,
        post
    );
  }

  /**
   * Thông báo comment vào bài của người khác
   * @param post
   * @param comment
   */
  @Override
  public void notifyComment(Post post, Comment comment) {
    User author = post.getAuthor();

    // Nếu người comment chính là chủ bài thì không cần gửi
    if (author.getId().equals(comment.getUser().getId()))
      return;

    String msg = comment.getUser().getUsername()
                 + " đã bình luận vào bài đăng của bạn: \"" + post.getTitle() + "\"";

    Notification notification = Notification.builder()
        .user(author)
        .type(NotificationType.NEW_COMMENT)
        .message(msg)
        .post(post)
        .comment(comment)
        .isRead(false)
        .createdAt(LocalDateTime.now())
        .build();

    notificationRepository.save(notification);
  }

  /**
   * Thông báo khi thả cảm xúc  (like , love, ...)
   * @param post
   * @param reaction
   */
  @Override
  public void notifyReaction(Post post, Reaction reaction) {
    User author = post.getAuthor();

    if (author.getId().equals(reaction.getUser().getId()))
      return;

    String msg = reaction.getUser().getUsername()
                 + " đã bày tỏ cảm xúc về bài đăng của bạn.";

    Notification notification = Notification.builder()
        .user(author)
        .type(NotificationType.POST_REACTION)
        .message(msg)
        .post(post)
        .reaction(reaction)
        .isRead(false)
        .createdAt(LocalDateTime.now())
        .build();

    notificationRepository.save(notification);
  }

  /**
   * Lấy tất cả thông báo của một người dùng
   * @param userId
   * @return
   */
  @Override
  public List<Notification> getUserNotifications(Long userId) {
     return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
  }
}
