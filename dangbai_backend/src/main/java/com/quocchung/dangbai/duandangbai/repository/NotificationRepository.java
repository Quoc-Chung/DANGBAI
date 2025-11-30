package com.quocchung.dangbai.duandangbai.repository;

import com.quocchung.dangbai.duandangbai.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query("SELECT n FROM Notification  n "
         + "WHERE n.user.id =  :userId "
         + "ORDER BY n.createdAt DESC")
  List<Notification> findAllByUserIdOrderByCreatedAtDesc(Long userId);

  @Query("SELECT COUNT(n) FROM Notification n "
         + "WHERE   n.user.id = :userId AND n.isRead = false ")
  long countByUserIdAndIsReadFalse(Long userId);

}
