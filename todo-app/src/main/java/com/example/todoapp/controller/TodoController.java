package com.example.todoapp.controller;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.service.CustomUserDetailsService.CustomUserPrincipal;
import com.example.todoapp.service.TodoService;
import com.example.todoapp.service.TodoService.TodoStatistics;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * ToDoコントローラー
 * ToDo関連のWebリクエストを処理
 *
 * @author TodoApp Team
 * @version 1.0
 */
@Controller
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    /**
     * ToDo一覧ページ表示（ページング対応）
     *
     * @param principal 認証済みユーザー
     * @param page ページ番号（デフォルト: 0）
     * @param size ページサイズ（デフォルト: 10）
     * @param sort ソート条件（デフォルト: createdAt）
     * @param direction ソート方向（デフォルト: desc）
     * @param completed 完了状態フィルター
     * @param search 検索キーワード
     * @param model モデル
     * @return ToDo一覧ページテンプレート
     */
    @GetMapping
    public String listTodos(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String search,
            Model model) {

        User user = principal.getUser();

        // ページサイズの制限
        size = Math.min(size, 100);

        // ソート設定
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ?
            Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Todo> todoPage;

        // 検索・フィルター条件に応じてデータを取得
        if (search != null && !search.trim().isEmpty()) {
            todoPage = todoService.searchByTitle(user, search.trim(), pageable);
            model.addAttribute("search", search);
        } else if (completed != null) {
            todoPage = todoService.findByUserAndCompleted(user, completed, pageable);
            model.addAttribute("completed", completed);
        } else {
            todoPage = todoService.findByUser(user, pageable);
        }

        // 統計情報を取得
        TodoStatistics statistics = todoService.getTodoStatistics(user);

        // 今日期限と近日期限のToDoを取得
        List<Todo> todayTodos = todoService.findTodayTodos(user);
        List<Todo> upcomingTodos = todoService.findUpcomingTodos(user);

        // モデルに属性を追加
        model.addAttribute("todoPage", todoPage);
        model.addAttribute("statistics", statistics);
        model.addAttribute("todayTodos", todayTodos);
        model.addAttribute("upcomingTodos", upcomingTodos);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sort);
        model.addAttribute("sortDirection", direction);
        model.addAttribute("totalPages", todoPage.getTotalPages());
        model.addAttribute("totalElements", todoPage.getTotalElements());

        return "todo/list";
    }

    /**
     * ToDo作成フォーム表示
     *
     * @param model モデル
     * @return ToDo作成フォームテンプレート
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("todo", new Todo());
        model.addAttribute("isEdit", false);
        return "todo/form";
    }

    /**
     * ToDo作成処理
     *
     * @param principal 認証済みユーザー
     * @param todo ToDo情報
     * @param bindingResult バリデーション結果
     * @param redirectAttributes リダイレクト属性
     * @param model モデル
     * @return リダイレクト先またはテンプレート
     */
    @PostMapping
    public String createTodo(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @ModelAttribute("todo") Todo todo,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "todo/form";
        }

        // 期限日の妥当性チェック
        if (!todoService.isValidDueDate(todo.getDueDate())) {
            model.addAttribute("dueDateError", "期限日は今日以降の日付を設定してください。");
            model.addAttribute("isEdit", false);
            return "todo/form";
        }

        try {
            User user = principal.getUser();
            todoService.createTodo(todo, user);
            redirectAttributes.addFlashAttribute("successMessage", "ToDoを作成しました。");
            return "redirect:/todos";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "ToDo作成中にエラーが発生しました。");
            model.addAttribute("isEdit", false);
            return "todo/form";
        }
    }

    /**
     * ToDo編集フォーム表示
     *
     * @param principal 認証済みユーザー
     * @param id ToDoID
     * @param model モデル
     * @return ToDo編集フォームテンプレートまたはリダイレクト
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long id,
            Model model) {

        User user = principal.getUser();
        Optional<Todo> todoOpt = todoService.findByIdAndUser(id, user);

        if (todoOpt.isEmpty()) {
            return "redirect:/todos?error=notfound";
        }

        model.addAttribute("todo", todoOpt.get());
        model.addAttribute("isEdit", true);
        return "todo/form";
    }

    /**
     * ToDo更新処理
     *
     * @param principal 認証済みユーザー
     * @param id ToDoID
     * @param todo ToDo情報
     * @param bindingResult バリデーション結果
     * @param redirectAttributes リダイレクト属性
     * @param model モデル
     * @return リダイレクト先またはテンプレート
     */
    @PostMapping("/{id}")
    public String updateTodo(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long id,
            @Valid @ModelAttribute("todo") Todo todo,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        User user = principal.getUser();
        Optional<Todo> existingTodoOpt = todoService.findByIdAndUser(id, user);

        if (existingTodoOpt.isEmpty()) {
            return "redirect:/todos?error=notfound";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "todo/form";
        }

        // 期限日の妥当性チェック
        if (!todoService.isValidDueDate(todo.getDueDate())) {
            model.addAttribute("dueDateError", "期限日は今日以降の日付を設定してください。");
            model.addAttribute("isEdit", true);
            return "todo/form";
        }

        try {
            Todo existingTodo = existingTodoOpt.get();
            existingTodo.setTitle(todo.getTitle());
            existingTodo.setDescription(todo.getDescription());
            existingTodo.setDueDate(todo.getDueDate());

            todoService.updateTodo(existingTodo);
            redirectAttributes.addFlashAttribute("successMessage", "ToDoを更新しました。");
            return "redirect:/todos";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "ToDo更新中にエラーが発生しました。");
            model.addAttribute("isEdit", true);
            return "todo/form";
        }
    }

    /**
     * ToDo削除処理
     *
     * @param principal 認証済みユーザー
     * @param id ToDoID
     * @param redirectAttributes リダイレクト属性
     * @return リダイレクト先
     */
    @PostMapping("/{id}/delete")
    public String deleteTodo(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            User user = principal.getUser();
            todoService.deleteTodo(id, user);
            redirectAttributes.addFlashAttribute("successMessage", "ToDoを削除しました。");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ToDoが見つかりません。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ToDo削除中にエラーが発生しました。");
        }

        return "redirect:/todos";
    }

    /**
     * ToDo完了状態切り替え（Ajax対応）
     *
     * @param principal 認証済みユーザー
     * @param id ToDoID
     * @return リダイレクト先
     */
    @PostMapping("/{id}/toggle")
    public String toggleCompletion(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            User user = principal.getUser();
            Todo updatedTodo = todoService.toggleCompletion(id, user);

            String message = updatedTodo.isCompleted() ?
                "ToDoを完了にしました。" : "ToDoを未完了にしました。";
            redirectAttributes.addFlashAttribute("successMessage", message);

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ToDoが見つかりません。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ToDo更新中にエラーが発生しました。");
        }

        return "redirect:/todos";
    }

    /**
     * ToDo詳細表示
     *
     * @param principal 認証済みユーザー
     * @param id ToDoID
     * @param model モデル
     * @return ToDo詳細ページテンプレートまたはリダイレクト
     */
    @GetMapping("/{id}")
    public String showTodoDetail(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long id,
            Model model) {

        User user = principal.getUser();
        Optional<Todo> todoOpt = todoService.findByIdAndUser(id, user);

        if (todoOpt.isEmpty()) {
            return "redirect:/todos?error=notfound";
        }

        model.addAttribute("todo", todoOpt.get());
        return "todo/detail";
    }

    /**
     * 期限切れToDo一覧表示
     *
     * @param principal 認証済みユーザー
     * @param page ページ番号
     * @param size ページサイズ
     * @param model モデル
     * @return 期限切れToDo一覧ページテンプレート
     */
    @GetMapping("/overdue")
    public String listOverdueTodos(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        User user = principal.getUser();
        size = Math.min(size, 100);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "dueDate"));
        Page<Todo> overduePage = todoService.findOverdueTodos(user, pageable);

        model.addAttribute("todoPage", overduePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", overduePage.getTotalPages());
        model.addAttribute("totalElements", overduePage.getTotalElements());
        model.addAttribute("pageTitle", "期限切れToDo");

        return "todo/overdue";
    }
}
