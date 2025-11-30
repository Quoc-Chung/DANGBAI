package com.quocchung.dangbai.duandangbai.repository;


import com.quocchung.dangbai.duandangbai.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  /*- Lấy ra role từ name -*/
  Optional<Role> findByName(String name);

  /*- Kiểm tra role có tồn tại không từ name -*/
  boolean existsByName(String name);
}
