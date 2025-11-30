package com.quocchung.dangbai.duandangbai.model;



import java.util.ArrayList;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
/*- Lưu trữ danh mục sản phẩm: laptop, điện thoại, linh kiện, phụ kiện… -*/
public class Category {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @CreatedDate
  private LocalDateTime createdAt;

  /*- 1 danh mục có nhiều bài viết liên quan -*/
  @OneToMany(mappedBy = "category")
  private List<Post> posts = new ArrayList<>();
}
