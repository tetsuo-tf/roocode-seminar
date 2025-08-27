package com.example.todoapp.repository;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * ToDoリポジトリインターフェース
 *
 * @author TodoApp Team
 * @version 1.0
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * ユーザーのToDoをページング付きで取得
     *
     * @param user ユーザー
     * @param pageable ページング情報
     * @return ToDoページ
     */
    Page<Todo> findByUser(User user, Pageable pageable);

    /**
     * ユーザーのToDoを完了状態で絞り込んでページング付きで取得
     *
     * @param user ユーザー
     * @param completed 完了状態
     * @param pageable ページング情報
     * @return ToDoページ
     */
    Page<Todo> findByUserAndCompleted(User user, boolean completed, Pageable pageable);

    /**
     * ユーザーのToDoをタイトルで部分検索
     *
     * @param user ユーザー
     * @param title タイトル（部分一致）
     * @param pageable ページング情報
     * @return ToDoページ
     */
    Page<Todo> findByUserAndTitleContainingIgnoreCase(User user, String title, Pageable pageable);

    /**
     * ユーザーの期限日でToDoを検索
     *
     * @param user ユーザー
     * @param dueDate 期限日
     * @param pageable ページング情報
     * @return ToDoページ
     */
    Page<Todo> findByUserAndDueDate(User user, LocalDate dueDate, Pageable pageable);

    /**
     * ユーザーの期限切れToDoを取得
     *
     * @param user ユーザー
     * @param currentDate 現在日付
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Query("SELECT t FROM Todo t WHERE t.user = :user AND t.dueDate < :currentDate AND t.completed = false")
    Page<Todo> findOverdueTodos(@Param("user") User user, @Param("currentDate") LocalDate currentDate, Pageable pageable);

    /**
     * ユーザーの今日期限のToDoを取得
     *
     * @param user ユーザー
     * @param today 今日の日付
     * @return ToDoリスト
     */
    @Query("SELECT t FROM Todo t WHERE t.user = :user AND t.dueDate = :today AND t.completed = false")
    List<Todo> findTodayTodos(@Param("user") User user, @Param("today") LocalDate today);

    /**
     * ユーザーの近日期限のToDoを取得
     *
     * @param user ユーザー
     * @param startDate 開始日
     * @param endDate 終了日
     * @return ToDoリスト
     */
    @Query("SELECT t FROM Todo t WHERE t.user = :user AND t.dueDate BETWEEN :startDate AND :endDate AND t.completed = false ORDER BY t.dueDate ASC")
    List<Todo> findUpcomingTodos(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * ユーザーのToDoを作成日順で取得
     *
     * @param user ユーザー
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Query("SELECT t FROM Todo t WHERE t.user = :user ORDER BY t.createdAt DESC")
    Page<Todo> findByUserOrderByCreatedAtDesc(@Param("user") User user, Pageable pageable);

    /**
     * ユーザーのToDoを期限日順で取得
     *
     * @param user ユーザー
     * @param pageable ページング情報
     * @return ToDoページ
     */
    @Query("SELECT t FROM Todo t WHERE t.user = :user ORDER BY t.dueDate ASC NULLS LAST")
    Page<Todo> findByUserOrderByDueDateAsc(@Param("user") User user, Pageable pageable);

    /**
     * ユーザーの完了済みToDoの数を取得
     *
     * @param user ユーザー
     * @return 完了済みToDo数
     */
    long countByUserAndCompleted(User user, boolean completed);

    /**
     * ユーザーのToDo総数を取得
     *
     * @param user ユーザー
     * @return ToDo総数
     */
    long countByUser(User user);

    /**
     * ユーザーIDとToDoIDでToDoを検索（認可チェック用）
     *
     * @param id ToDoID
     * @param user ユーザー
     * @return ToDo（Optional）
     */
    Optional<Todo> findByIdAndUser(Long id, User user);

    /**
     * ユーザーの期限切れToDo数を取得
     *
     * @param user ユーザー
     * @return 期限切れToDo数
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.user = :user AND t.dueDate < CURRENT_DATE() AND t.completed = false")
    long countOverdueTodos(@Param("user") User user);
}
