package com.example.todoapp.service;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.TodoRepository;
import com.example.todoapp.service.TodoService.TodoStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * TodoServiceのテストクラス
 *
 * @author TodoApp Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private User testUser;
    private Todo testTodo;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("テストユーザー");

        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setTitle("テストToDo");
        testTodo.setDescription("テストの説明");
        testTodo.setDueDate(LocalDate.now().plusDays(1));
        testTodo.setCompleted(false);
        testTodo.setUser(testUser);
        testTodo.setCreatedAt(LocalDateTime.now());
        testTodo.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createTodo_成功() {
        // Given
        Todo newTodo = new Todo();
        newTodo.setTitle("新しいToDo");
        newTodo.setDescription("新しい説明");
        newTodo.setDueDate(LocalDate.now().plusDays(2));

        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // When
        Todo result = todoService.createTodo(newTodo, testUser);

        // Then
        assertNotNull(result);
        assertEquals(testUser, newTodo.getUser());
        assertFalse(newTodo.isCompleted());
        verify(todoRepository).save(newTodo);
    }

    @Test
    void updateTodo_成功() {
        // Given
        when(todoRepository.save(testTodo)).thenReturn(testTodo);

        // When
        Todo result = todoService.updateTodo(testTodo);

        // Then
        assertNotNull(result);
        assertEquals(testTodo.getId(), result.getId());
        verify(todoRepository).save(testTodo);
    }

    @Test
    void deleteTodo_成功() {
        // Given
        when(todoRepository.findByIdAndUser(testTodo.getId(), testUser))
            .thenReturn(Optional.of(testTodo));

        // When
        todoService.deleteTodo(testTodo.getId(), testUser);

        // Then
        verify(todoRepository).findByIdAndUser(testTodo.getId(), testUser);
        verify(todoRepository).delete(testTodo);
    }

    @Test
    void deleteTodo_ToDoが見つからない() {
        // Given
        Long todoId = 999L;
        when(todoRepository.findByIdAndUser(todoId, testUser))
            .thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> todoService.deleteTodo(todoId, testUser)
        );

        assertEquals("ToDoが見つかりません: " + todoId, exception.getMessage());
        verify(todoRepository).findByIdAndUser(todoId, testUser);
        verify(todoRepository, never()).delete(any(Todo.class));
    }

    @Test
    void findByIdAndUser_ToDoが存在する() {
        // Given
        when(todoRepository.findByIdAndUser(testTodo.getId(), testUser))
            .thenReturn(Optional.of(testTodo));

        // When
        Optional<Todo> result = todoService.findByIdAndUser(testTodo.getId(), testUser);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testTodo.getId(), result.get().getId());
        verify(todoRepository).findByIdAndUser(testTodo.getId(), testUser);
    }

    @Test
    void findByUser_ページング付きで取得() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Todo> todos = Arrays.asList(testTodo);
        Page<Todo> todoPage = new PageImpl<>(todos, pageable, 1);

        when(todoRepository.findByUser(testUser, pageable)).thenReturn(todoPage);

        // When
        Page<Todo> result = todoService.findByUser(testUser, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testTodo.getId(), result.getContent().get(0).getId());
        verify(todoRepository).findByUser(testUser, pageable);
    }

    @Test
    void findByUserAndCompleted_完了状態で絞り込み() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Todo> todos = Arrays.asList(testTodo);
        Page<Todo> todoPage = new PageImpl<>(todos, pageable, 1);

        when(todoRepository.findByUserAndCompleted(testUser, false, pageable))
            .thenReturn(todoPage);

        // When
        Page<Todo> result = todoService.findByUserAndCompleted(testUser, false, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(todoRepository).findByUserAndCompleted(testUser, false, pageable);
    }

    @Test
    void toggleCompletion_未完了から完了へ() {
        // Given
        testTodo.setCompleted(false);
        when(todoRepository.findByIdAndUser(testTodo.getId(), testUser))
            .thenReturn(Optional.of(testTodo));
        when(todoRepository.save(testTodo)).thenReturn(testTodo);

        // When
        Todo result = todoService.toggleCompletion(testTodo.getId(), testUser);

        // Then
        assertTrue(result.isCompleted());
        verify(todoRepository).findByIdAndUser(testTodo.getId(), testUser);
        verify(todoRepository).save(testTodo);
    }

    @Test
    void toggleCompletion_完了から未完了へ() {
        // Given
        testTodo.setCompleted(true);
        when(todoRepository.findByIdAndUser(testTodo.getId(), testUser))
            .thenReturn(Optional.of(testTodo));
        when(todoRepository.save(testTodo)).thenReturn(testTodo);

        // When
        Todo result = todoService.toggleCompletion(testTodo.getId(), testUser);

        // Then
        assertFalse(result.isCompleted());
        verify(todoRepository).findByIdAndUser(testTodo.getId(), testUser);
        verify(todoRepository).save(testTodo);
    }

    @Test
    void markAsCompleted_成功() {
        // Given
        testTodo.setCompleted(false);
        when(todoRepository.findByIdAndUser(testTodo.getId(), testUser))
            .thenReturn(Optional.of(testTodo));
        when(todoRepository.save(testTodo)).thenReturn(testTodo);

        // When
        Todo result = todoService.markAsCompleted(testTodo.getId(), testUser);

        // Then
        assertTrue(result.isCompleted());
        assertNotNull(result.getCompletedAt());
        verify(todoRepository).findByIdAndUser(testTodo.getId(), testUser);
        verify(todoRepository).save(testTodo);
    }

    @Test
    void getTodoStatistics_統計情報取得() {
        // Given
        when(todoRepository.countByUser(testUser)).thenReturn(5L);
        when(todoRepository.countByUserAndCompleted(testUser, true)).thenReturn(3L);
        when(todoRepository.countByUserAndCompleted(testUser, false)).thenReturn(2L);
        when(todoRepository.countOverdueTodos(testUser)).thenReturn(1L);

        // When
        TodoStatistics result = todoService.getTodoStatistics(testUser);

        // Then
        assertNotNull(result);
        assertEquals(5L, result.getTotalCount());
        assertEquals(3L, result.getCompletedCount());
        assertEquals(2L, result.getIncompleteCount());
        assertEquals(1L, result.getOverdueCount());
        assertEquals(60.0, result.getCompletionRate(), 0.01);
        verify(todoRepository).countByUser(testUser);
        verify(todoRepository).countByUserAndCompleted(testUser, true);
        verify(todoRepository).countByUserAndCompleted(testUser, false);
        verify(todoRepository).countOverdueTodos(testUser);
    }

    @Test
    void getTodoStatistics_ToDoが0件の場合() {
        // Given
        when(todoRepository.countByUser(testUser)).thenReturn(0L);
        when(todoRepository.countByUserAndCompleted(testUser, true)).thenReturn(0L);
        when(todoRepository.countByUserAndCompleted(testUser, false)).thenReturn(0L);
        when(todoRepository.countOverdueTodos(testUser)).thenReturn(0L);

        // When
        TodoStatistics result = todoService.getTodoStatistics(testUser);

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getTotalCount());
        assertEquals(0.0, result.getCompletionRate(), 0.01);
        verify(todoRepository).countByUser(testUser);
        verify(todoRepository).countByUserAndCompleted(testUser, true);
        verify(todoRepository).countByUserAndCompleted(testUser, false);
        verify(todoRepository).countOverdueTodos(testUser);
    }

    @Test
    void isValidTodo_有効なToDo() {
        // Given
        Todo validTodo = new Todo();
        validTodo.setTitle("有効なタイトル");

        // When
        boolean result = todoService.isValidTodo(validTodo);

        // Then
        assertTrue(result);
    }

    @Test
    void isValidTodo_無効なToDo_nullタイトル() {
        // Given
        Todo invalidTodo = new Todo();
        invalidTodo.setTitle(null);

        // When
        boolean result = todoService.isValidTodo(invalidTodo);

        // Then
        assertFalse(result);
    }

    @Test
    void isValidTodo_無効なToDo_空タイトル() {
        // Given
        Todo invalidTodo = new Todo();
        invalidTodo.setTitle("   ");

        // When
        boolean result = todoService.isValidTodo(invalidTodo);

        // Then
        assertFalse(result);
    }

    @Test
    void isValidTodo_無効なToDo_長すぎるタイトル() {
        // Given
        Todo invalidTodo = new Todo();
        invalidTodo.setTitle("a".repeat(201)); // 201文字

        // When
        boolean result = todoService.isValidTodo(invalidTodo);

        // Then
        assertFalse(result);
    }

    @Test
    void isValidDueDate_有効な期限日() {
        // Given
        LocalDate validDate = LocalDate.now().plusDays(1);

        // When
        boolean result = todoService.isValidDueDate(validDate);

        // Then
        assertTrue(result);
    }

    @Test
    void isValidDueDate_null期限日() {
        // When
        boolean result = todoService.isValidDueDate(null);

        // Then
        assertTrue(result); // nullは有効
    }

    @Test
    void isValidDueDate_過去の期限日() {
        // Given
        LocalDate pastDate = LocalDate.now().minusDays(1);

        // When
        boolean result = todoService.isValidDueDate(pastDate);

        // Then
        assertFalse(result);
    }

    @Test
    void findTodayTodos_今日期限のToDo取得() {
        // Given
        List<Todo> todayTodos = Arrays.asList(testTodo);
        when(todoRepository.findTodayTodos(eq(testUser), any(LocalDate.class)))
            .thenReturn(todayTodos);

        // When
        List<Todo> result = todoService.findTodayTodos(testUser);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(todoRepository).findTodayTodos(eq(testUser), any(LocalDate.class));
    }

    @Test
    void findUpcomingTodos_近日期限のToDo取得() {
        // Given
        List<Todo> upcomingTodos = Arrays.asList(testTodo);
        when(todoRepository.findUpcomingTodos(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(upcomingTodos);

        // When
        List<Todo> result = todoService.findUpcomingTodos(testUser);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(todoRepository).findUpcomingTodos(eq(testUser), any(LocalDate.class), any(LocalDate.class));
    }
}
