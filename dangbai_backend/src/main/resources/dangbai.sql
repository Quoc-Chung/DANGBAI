-- DATABASE (tạo schema)
CREATE DATABASE IF NOT EXISTS marketplace CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
USE marketplace;

-- 1. users
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) UNIQUE,
                       password_hash VARCHAR(255),
                       username VARCHAR(100),
                       display_name VARCHAR(255),
                       phone VARCHAR(50),
                       avatar_url TEXT,
                       bio TEXT,
                       account_status ENUM('active','banned','deleted') DEFAULT 'active',
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. categories
CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(200) NOT NULL,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. posts
CREATE TABLE posts (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       author_id BIGINT NOT NULL,
                       category_id INT,
                       title VARCHAR(500),
                       description LONGTEXT,
                       price DECIMAL(18,2),
                       location VARCHAR(255),
                       status ENUM('pending','approved','rejected') DEFAULT 'pending',
                       rejected_reason TEXT,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       approved_at DATETIME,
                       FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                       FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL ON UPDATE NO ACTION,
                       INDEX (author_id),
                       INDEX (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. post_media (hỗ trợ nhiều ảnh hoặc video per post)
CREATE TABLE post_media (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            post_id BIGINT NOT NULL,
                            type ENUM('image','video','file') NOT NULL,
                            url TEXT NOT NULL,
                            thumbnail_url TEXT,
                            width BIGINT,
                            height BIGINT,
                            duration_seconds BIGINT, -- dùng cho video
                            position INT DEFAULT 0,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE ON UPDATE NO ACTION,
                            INDEX (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. comments (có parent_id để reply)
CREATE TABLE comments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          post_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          parent_id BIGINT DEFAULT NULL,
                          content TEXT,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE ON UPDATE NO ACTION,
                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                          FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE SET NULL ON UPDATE NO ACTION,
                          INDEX (post_id),
                          INDEX (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. reactions (composite PK: post_id + user_id)
CREATE TABLE reactions (
                           post_id BIGINT NOT NULL,
                           user_id BIGINT NOT NULL,
                           type ENUM('like','love','haha','wow','sad','angry') NOT NULL,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           PRIMARY KEY (post_id, user_id),
                           FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE ON UPDATE NO ACTION,
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. notifications
CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               type ENUM('post_approved','post_rejected','new_comment','contact') NOT NULL,
                               post_id BIGINT DEFAULT NULL,
                               message TEXT,
                               is_read TINYINT(1) DEFAULT 0,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION,
                               FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE SET NULL ON UPDATE NO ACTION,
                               INDEX (user_id),
                               INDEX (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. contacts (liên hệ người mua -> người bán về 1 bài)
CREATE TABLE contacts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          post_id BIGINT NOT NULL,
                          buyer_id BIGINT NOT NULL,
                          seller_id BIGINT NOT NULL,
                          message TEXT,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE ON UPDATE NO ACTION,
                          FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                          FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                          INDEX (post_id),
                          INDEX (buyer_id),
                          INDEX (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
