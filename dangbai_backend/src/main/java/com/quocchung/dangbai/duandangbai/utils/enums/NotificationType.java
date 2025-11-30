package com.quocchung.dangbai.duandangbai.utils.enums;

/*- Loại thông báo -*/
public enum NotificationType {
  POST_CREATE,      // bài đăng được tạo - dành cho thông báo admin xem
  POST_APPROVED,    // bài đăng được duyệt
  POST_REJECTED,    // bài đăng bị từ chối
  NEW_COMMENT,      // có bình luận mới
  COMMENT_REACTION, // có người thả cảm xúc trên bình luận của bạn
  POST_REACTION,    // có người thả cảm xúc trên bài đăng của bạn
  CONTACT           // người mua liên hệ với người bán
}
