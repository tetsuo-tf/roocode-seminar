package com.example.todoapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring Boot アプリケーションの統合テスト
 *
 * @author TodoApp Team
 * @version 1.0
 */
@SpringBootTest
@ActiveProfiles("test")
class TodoAppApplicationTests {

    /**
     * アプリケーションコンテキストが正常にロードされることを確認
     */
    @Test
    void contextLoads() {
        // このテストは、Spring Bootアプリケーションのコンテキストが
        // 正常にロードされることを確認します。
        // テストが成功すれば、基本的な設定に問題がないことを示します。
    }

}
