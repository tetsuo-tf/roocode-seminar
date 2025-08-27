package com.example.todoapp.service;

import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ユーザーサービスクラス
 * ユーザー関連のビジネスロジックを処理
 *
 * @author TodoApp Team
 * @version 1.0
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 新規ユーザーを登録
     *
     * @param user ユーザー情報
     * @return 登録されたユーザー
     * @throws IllegalArgumentException メールアドレスが既に存在する場合
     */
    public User registerUser(User user) {
        // メールアドレスの重複チェック
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています: " + user.getEmail());
        }

        // パスワードをハッシュ化
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ユーザーを有効化
        user.setEnabled(true);

        return userRepository.save(user);
    }

    /**
     * メールアドレスでユーザーを検索
     *
     * @param email メールアドレス
     * @return ユーザー（Optional）
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 有効なユーザーをメールアドレスで検索
     *
     * @param email メールアドレス
     * @return 有効なユーザー（Optional）
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmailAndEnabled(String email) {
        return userRepository.findByEmailAndEnabled(email);
    }

    /**
     * ユーザーIDでユーザーを検索
     *
     * @param id ユーザーID
     * @return ユーザー（Optional）
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * すべてのユーザーを取得
     *
     * @return ユーザーリスト
     */
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 名前でユーザーを部分検索
     *
     * @param name 名前（部分一致）
     * @return ユーザーリスト
     */
    @Transactional(readOnly = true)
    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContainingAndEnabled(name);
    }

    /**
     * ユーザー情報を更新
     *
     * @param user ユーザー情報
     * @return 更新されたユーザー
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * ユーザーのパスワードを変更
     *
     * @param userId ユーザーID
     * @param newPassword 新しいパスワード
     * @return 更新されたユーザー
     * @throws IllegalArgumentException ユーザーが見つからない場合
     */
    public User changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    /**
     * ユーザーを無効化
     *
     * @param userId ユーザーID
     * @throws IllegalArgumentException ユーザーが見つからない場合
     */
    public void disableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません: " + userId));

        user.setEnabled(false);
        userRepository.save(user);
    }

    /**
     * ユーザーを有効化
     *
     * @param userId ユーザーID
     * @throws IllegalArgumentException ユーザーが見つからない場合
     */
    public void enableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません: " + userId));

        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * メールアドレスの存在確認
     *
     * @param email メールアドレス
     * @return 存在する場合true
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 有効なユーザー数を取得
     *
     * @return 有効なユーザー数
     */
    @Transactional(readOnly = true)
    public long countEnabledUsers() {
        return userRepository.countEnabledUsers();
    }

    /**
     * パスワードの妥当性をチェック
     *
     * @param password パスワード
     * @return 妥当な場合true
     */
    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    /**
     * メールアドレスの妥当性をチェック
     *
     * @param email メールアドレス
     * @return 妥当な場合true
     */
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
