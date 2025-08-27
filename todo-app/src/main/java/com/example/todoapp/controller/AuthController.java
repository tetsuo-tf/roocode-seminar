package com.example.todoapp.controller;

import com.example.todoapp.entity.User;
import com.example.todoapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 認証関連のコントローラー
 * ログイン、ユーザー登録を処理
 *
 * @author TodoApp Team
 * @version 1.0
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * ホームページ表示
     *
     * @return ホームページテンプレート
     */
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    /**
     * ログインページ表示
     *
     * @param error エラーパラメータ
     * @param logout ログアウトパラメータ
     * @param expired セッション期限切れパラメータ
     * @param model モデル
     * @return ログインページテンプレート
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが正しくありません。");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "正常にログアウトしました。");
        }

        if (expired != null) {
            model.addAttribute("warningMessage", "セッションが期限切れです。再度ログインしてください。");
        }

        return "auth/login";
    }

    /**
     * ユーザー登録ページ表示
     *
     * @param model モデル
     * @return ユーザー登録ページテンプレート
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    /**
     * ユーザー登録処理
     *
     * @param user ユーザー情報
     * @param bindingResult バリデーション結果
     * @param confirmPassword パスワード確認
     * @param model モデル
     * @param redirectAttributes リダイレクト属性
     * @return リダイレクト先またはテンプレート
     */
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        // バリデーションエラーチェック
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // パスワード確認チェック
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordError", "パスワードが一致しません。");
            return "auth/register";
        }

        // パスワード強度チェック
        if (!userService.isValidPassword(user.getPassword())) {
            model.addAttribute("passwordError", "パスワードは8文字以上で入力してください。");
            return "auth/register";
        }

        // メールアドレス形式チェック
        if (!userService.isValidEmail(user.getEmail())) {
            model.addAttribute("emailError", "有効なメールアドレスを入力してください。");
            return "auth/register";
        }

        try {
            // ユーザー登録
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage",
                "ユーザー登録が完了しました。ログインしてください。");
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("emailError", e.getMessage());
            return "auth/register";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "ユーザー登録中にエラーが発生しました。");
            return "auth/register";
        }
    }

    /**
     * アクセス拒否ページ表示
     *
     * @return アクセス拒否ページテンプレート
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }

    /**
     * エラーページ表示
     *
     * @param model モデル
     * @return エラーページテンプレート
     */
    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("errorMessage", "予期しないエラーが発生しました。");
        return "error/error";
    }
}
