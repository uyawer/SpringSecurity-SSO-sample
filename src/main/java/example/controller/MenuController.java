package example.controller;

import example.util.ScreenPathConstants;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MenuController {

    /**
     * メニュー画面へ遷移
     * @return メニュー画面のHTMLファイル名
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String menu() {
        // メニュー画面を表示
        return ScreenPathConstants.SCREEN_MENU;
    }

    /**
     * 管理者用画面へ遷移
     * @return 管理者用画面のHTMLファイル名
     */
    @RequestMapping(value = "/showAdministrator", method = {RequestMethod.POST, RequestMethod.GET})
    public String admin() {
        return ScreenPathConstants.SCREEN_ADMINISTRATOR;
    }
}
