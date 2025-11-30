package com.quocchung.dangbai.duandangbai.controller;

import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.NOTIFICATION;
import static com.quocchung.dangbai.duandangbai.contants.ApiConstant.NOTIFICATION_BY_USER;

import com.quocchung.dangbai.duandangbai.dtos.response.ApiResponse;
import com.quocchung.dangbai.duandangbai.model.Notification;
import com.quocchung.dangbai.duandangbai.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(NOTIFICATION)
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationService notificationService;

  @GetMapping(NOTIFICATION_BY_USER)
  public ApiResponse<List<Notification>> getUserNotifications(@PathVariable Long userId) {
    List<Notification> notifications = notificationService.getUserNotifications(userId);
    return ApiResponse.success("Lấy thông báo thành công", notifications);
  }
}
