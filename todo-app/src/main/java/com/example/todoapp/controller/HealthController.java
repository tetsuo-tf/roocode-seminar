package com.example.todoapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.repository.TodoRepository;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

/**
 * ヘルスチェック用コントローラー
 * アプリケーションの動作状況を確認するためのエンドポイントを提供
 */
@RestController
public class HealthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    /**
     * 簡易ヘルスチェック
     * @return アプリケーションの基本状態
     */
    @GetMapping("/api/health/simple")
    public ResponseEntity<Map<String, Object>> simpleHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "Todo App");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * 詳細ヘルスチェック
     * @return アプリケーションの詳細状態
     */
    @GetMapping("/api/health/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        try {
            // データベース接続確認
            long userCount = userRepository.count();
            long todoCount = todoRepository.count();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("timestamp", LocalDateTime.now());
            response.put("database", "Connected");
            response.put("userCount", userCount);
            response.put("todoCount", todoCount);
            response.put("message", "All systems operational");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "DOWN");
            response.put("timestamp", LocalDateTime.now());
            response.put("database", "Connection failed");
            response.put("error", e.getMessage());
            return ResponseEntity.status(503).body(response);
        }
    }
}
