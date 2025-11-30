package com.quocchung.dangbai.duandangbai.repository;

import com.quocchung.dangbai.duandangbai.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

  /*- Lấy ra permission từ name -*/
  Optional<Permission> findByName(String name);

  /*- Kiểm tra permission có tồn tại không từ name -*/
  boolean existsByName(String name);
}