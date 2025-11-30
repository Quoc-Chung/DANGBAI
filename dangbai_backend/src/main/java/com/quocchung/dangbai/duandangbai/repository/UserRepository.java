package com.quocchung.dangbai.duandangbai.repository;

import com.quocchung.dangbai.duandangbai.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /*- Lấy ra người dùng từ username -*/
  Optional<User> findByUsername(String username);

  /*- Lấy ra người dùng từ email -*/
  Optional<User> findByEmail(String email);

  /*- Lấy ra  một user cùng tất cả role và permission của họ trong một query duy nhất -*/
  @Query("  SELECT u FROM User u "
         + "LEFT JOIN FETCH u.roles r "
         + "LEFT JOIN FETCH r.permissions p "
         + "WHERE u.username = :username")
  Optional<User> findByUsernameWithRolesAndPermissions(String username);

  /*- Kiểm tra người dùng có tồn tại không từ username -*/
  boolean existsByUsername(String username);

  /*- Kiểm tra người dùng có tồn tại không từ email -*/
  boolean existsByEmail(String email);
}