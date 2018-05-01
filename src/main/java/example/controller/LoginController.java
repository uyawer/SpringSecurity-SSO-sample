package example.controller;

import example.util.ScreenPathConstants;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    /**
     * ログイン画面へ遷移
     * @return ログイン画面のHTMLファイル名
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public String login() {
        return ScreenPathConstants.SCREEN_LOGIN;
    }

    /**
     * SpringConfigで設定したログインできなかった場合の処理を定義する
     * @param model 画面モデル
     */
    @RequestMapping(value = "/login-error")
    public String loginError(Model model) {
        // エラーを通知するためにloginErrorをtrueに設定
        model.addAttribute("loginError", true);
        return ScreenPathConstants.SCREEN_LOGIN;
    }
}
