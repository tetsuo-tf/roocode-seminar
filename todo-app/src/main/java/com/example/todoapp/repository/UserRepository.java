package com.example.todoapp.repository;

import com.example.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ユーザーリポジトリインターフェース
 *
 * @author TodoApp Team
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * メールアドレスでユーザーを検索
     *
     * @param email メールアドレス
     * @return ユーザー（Optional）
     */
    Optional<User> findByEmail(String email);

    /**
     * メールアドレスの存在確認
     *
     * @param email メールアドレス
     * @return 存在する場合true
     */
    boolean existsByEmail(String email);

    /**
     * 有効なユーザーをメールアドレスで検索
     *
     * @param email メールアドレス
     * @return 有効なユーザー（Optional）
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.enabled = true")
    Optional<User> findByEmailAndEnabled(@Param("email") String email);

    /**
     * 名前でユーザーを部分検索
     *
     * @param name 名前（部分一致）
     * @return ユーザーリスト
     */
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name% AND u.enabled = true")
    java.util.List<User> findByNameContainingAndEnabled(@Param("name") String name);

    /**
     * 有効なユーザー数を取得
     *
     * @return 有効なユーザー数
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    long countEnabledUsers();
}
