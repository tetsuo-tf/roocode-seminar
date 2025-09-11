package com.example.todoapp.service;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * ToDoサービスクラス
 * ToDo関連のビジネスロジックを処理
 *
 * @author TodoApp Team
 * @version 1.0
 */
@Service
@Transactional
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    /**
     * 新しいToDoを作成
     *
     * @param todo ToDo情報
     * @param user ユーザー
     * @return 作成されたToDo
     */
    public Todo createTodo(Todo todo, User user) {
        todo.setUser(user);
        todo.setCompleted(false);
        return todoRepository.save(todo);
    }

    /**
     * ToDoを更新
     *
     * @param todo ToDo情報
     * @return 更新されたToDo
     */
    public Todo updateTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    /**
     * ToDoを削除
     *
     * @param todoId ToDoID
     * @param user ユーザー
     * @throws IllegalArgumentException ToDoが見つからない場合
     */
    public void deleteTodo(Long todoId, User user) {
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new IllegalArgumentException("ToDoが見つかりません: " + todoId));
        todoRepository.delete(todo);
    }

    /**
     * ユーザーのToDoをIDで検索
     *
     * @param todoId ToDoID
     * @param user ユーザー
     * @return ToDo（Optional）
     */
    @Transactional(readOnly = true)
    public Optional<Todo> findByIdAndUser(Long todoId, User user) {
        return todoRepository.findByIdAndUser(todoId, user);
    }

    /**
     * ユーザーのToDoをページング付きで取得
     *
     * @param user ユーザー
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Transactional(readOnly = true)
    public Page<Todo> findByUser(User user, Pageable pageable) {
        return todoRepository.findByUser(user, pageable);
    }

    /**
     * ユーザーのToDoを完了状態で絞り込んでページング付きで取得
     *
     * @param user ユーザー
     * @param completed 完了状態
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Transactional(readOnly = true)
    public Page<Todo> findByUserAndCompleted(User user, boolean completed, Pageable pageable) {
        return todoRepository.findByUserAndCompleted(user, completed, pageable);
    }

    /**
     * ユーザーのToDoをタイトルで検索
     *
     * @param user ユーザー
     * @param title タイトル（部分一致）
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Transactional(readOnly = true)
    public Page<Todo> searchByTitle(User user, String title, Pageable pageable) {
        return todoRepository.findByUserAndTitleContainingIgnoreCase(user, title, pageable);
    }

    /**
     * ユーザーのToDoを作成日順で取得
     *
     * @param user ユーザー
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Transactional(readOnly = true)
    public Page<Todo> findByUserOrderByCreatedAt(User user, Pageable pageable) {
        return todoRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    /**
     * ユーザーのToDoを期限日順で取得
     *
     * @param user ユーザー
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Transactional(readOnly = true)
    public Page<Todo> findByUserOrderByDueDate(User user, Pageable pageable) {
        return todoRepository.findByUserOrderByDueDateAsc(user, pageable);
    }

    /**
     * ユーザーの期限切れToDoを取得
     *
     * @param user ユーザー
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Transactional(readOnly = true)
    public Page<Todo> findOverdueTodos(User user, Pageable pageable) {
        return todoRepository.findOverdueTodos(user, LocalDate.now(), pageable);
    }

    /**
     * ユーザーの今日期限のToDoを取得
     *
     * @param user ユーザー
     * @return ToDoリスト
     */
    @Transactional(readOnly = true)
    public List<Todo> findTodayTodos(User user) {
        return todoRepository.findTodayTodos(user, LocalDate.now());
    }

    /**
     * ユーザーの近日期限のToDoを取得（今日から3日以内）
     *
     * @param user ユーザー
     * @return ToDoリスト
     */
    @Transactional(readOnly = true)
    public List<Todo> findUpcomingTodos(User user) {
        LocalDate today = LocalDate.now();
        LocalDate threeDaysLater = today.plusDays(3);
        return todoRepository.findUpcomingTodos(user, today.plusDays(1), threeDaysLater);
    }

    /**
     * ToDoの完了状態を切り替え
     *
     * @param todoId ToDoID
     * @param user ユーザー
     * @return 更新されたToDo
     * @throws IllegalArgumentException ToDoが見つからない場合
     */
    public Todo toggleCompletion(Long todoId, User user) {
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new IllegalArgumentException("ToDoが見つかりません: " + todoId));

        todo.setCompleted(!todo.isCompleted());
        return todoRepository.save(todo);
    }

    /**
     * ToDoを完了にマーク
     *
     * @param todoId ToDoID
     * @param user ユーザー
     * @return 更新されたToDo
     * @throws IllegalArgumentException ToDoが見つからない場合
     */
    public Todo markAsCompleted(Long todoId, User user) {
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new IllegalArgumentException("ToDoが見つかりません: " + todoId));

        todo.markAsCompleted();
        return todoRepository.save(todo);
    }

    /**
     * ToDoを未完了にマーク
     *
     * @param todoId ToDoID
     * @param user ユーザー
     * @return 更新されたToDo
     * @throws IllegalArgumentException ToDoが見つからない場合
     */
    public Todo markAsIncomplete(Long todoId, User user) {
        Todo todo = todoRepository.findByIdAndUser(todoId, user)
                .orElseThrow(() -> new IllegalArgumentException("ToDoが見つかりません: " + todoId));

        todo.markAsIncomplete();
        return todoRepository.save(todo);
    }

    /**
     * ユーザーのToDo統計情報を取得
     *
     * @param user ユーザー
     * @return 統計情報 [総数, 完了数, 未完了数, 期限切れ数]
     */
    @Transactional(readOnly = true)
    public TodoStatistics getTodoStatistics(User user) {
        try {
            // 個別のクエリで統計情報を取得（より安全）
            long totalCount = todoRepository.countByUser(user);
            long completedCount = todoRepository.countByUserAndCompleted(user, true);
            long incompleteCount = todoRepository.countByUserAndCompleted(user, false);
            long overdueCount = todoRepository.countOverdueTodos(user);

            return new TodoStatistics(totalCount, completedCount, incompleteCount, overdueCount);
        } catch (Exception e) {
            // エラーが発生した場合はデフォルト値を返す
            return TodoStatistics.EMPTY;
        }
    }

    /**
     * ToDo統計情報クラス
     */
    public static class TodoStatistics {
        /**
         * 全ての値が0の空の統計情報
         */
        public static final TodoStatistics EMPTY = new TodoStatistics(0, 0, 0, 0);

        private final long totalCount;
        private final long completedCount;
        private final long incompleteCount;
        private final long overdueCount;

        public TodoStatistics(long totalCount, long completedCount, long incompleteCount, long overdueCount) {
            this.totalCount = totalCount;
            this.completedCount = completedCount;
            this.incompleteCount = incompleteCount;
            this.overdueCount = overdueCount;
        }

        public long getTotalCount() { return totalCount; }
        public long getCompletedCount() { return completedCount; }
        public long getIncompleteCount() { return incompleteCount; }
        public long getOverdueCount() { return overdueCount; }

        public double getCompletionRate() {
            return totalCount > 0 ? (double) completedCount / totalCount * 100 : 0.0;
        }
    }

    /**
     * ToDoの妥当性をチェック
     *
     * @param todo ToDo
     * @return 妥当な場合true
     */
    public boolean isValidTodo(Todo todo) {
        return todo != null &&
               todo.getTitle() != null &&
               !todo.getTitle().trim().isEmpty() &&
               todo.getTitle().length() <= 200;
    }

    /**
     * 期限日の妥当性をチェック
     *
     * @param dueDate 期限日
     * @return 妥当な場合true
     */
    public boolean isValidDueDate(LocalDate dueDate) {
        return dueDate == null || !dueDate.isBefore(LocalDate.now());
    }
}
