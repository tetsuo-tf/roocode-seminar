package com.example.todoapp.service;

import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

/**
 * カスタムユーザー詳細サービス
 * Spring Securityの認証で使用されるUserDetailsServiceの実装
 *
 * @author TodoApp Team
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * メールアドレスでユーザーを検索し、UserDetailsを返す
     *
     * @param email メールアドレス
     * @return UserDetails
     * @throws UsernameNotFoundException ユーザーが見つからない場合
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndEnabled(email)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));

        return new CustomUserPrincipal(user);
    }

    /**
     * カスタムユーザープリンシパルクラス
     * UserDetailsインターフェースの実装
     */
    public static class CustomUserPrincipal implements UserDetails {
        private final User user;

        public CustomUserPrincipal(User user) {
            this.user = user;
        }

        /**
         * ユーザーエンティティを取得
         *
         * @return User
         */
        public User getUser() {
            return user;
        }

        /**
         * ユーザーID取得
         *
         * @return ユーザーID
         */
        public Long getUserId() {
            return user.getId();
        }

        /**
         * ユーザー名（表示名）取得
         *
         * @return ユーザー名
         */
        public String getName() {
            return user.getName();
        }

        /**
         * メールアドレス取得
         *
         * @return メールアドレス
         */
        public String getEmail() {
            return user.getEmail();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // 基本的にすべてのユーザーにUSERロールを付与
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getEmail(); // メールアドレスをユーザー名として使用
        }

        @Override
        public boolean isAccountNonExpired() {
            return true; // アカウント期限切れは実装しない
        }

        @Override
        public boolean isAccountNonLocked() {
            return true; // アカウントロックは実装しない
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true; // 認証情報期限切れは実装しない
        }

        @Override
        public boolean isEnabled() {
            return user.isEnabled();
        }

        @Override
        public String toString() {
            return "CustomUserPrincipal{" +
                    "userId=" + user.getId() +
                    ", email='" + user.getEmail() + '\'' +
                    ", name='" + user.getName() + '\'' +
                    ", enabled=" + user.isEnabled() +
                    '}';
        }
    }
}
