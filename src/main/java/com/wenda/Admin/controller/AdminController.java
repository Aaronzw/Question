package com.wenda.Admin.controller;

import com.wenda.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class AdminController {
    private static final Logger logger= LoggerFactory.getLogger(AdminController.class);
    @Autowired
    UserService userService;

    @RequestMapping(path = {"/adminLogin"},method = {RequestMethod.POST,RequestMethod.GET})
    public String adMinLogin(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
        return "admin/login";
    }
    @RequestMapping(path = {"/admin"},method = {RequestMethod.POST,RequestMethod.GET})
    public String adminIndex(Model model, @RequestParam(value = "pop",defaultValue = "0")int pop){
        return "admin/admin";
    }

    /*注销，ticket置为失效*/
    @RequestMapping(path = {"/adLogout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String adlogout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/adminLogin";
    }
    /*提交账号密码接口*/
    @RequestMapping(path = {"/adlogin/"}, method = {RequestMethod.POST})
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next",defaultValue = "") String next,
                        @RequestParam(value="rememberme", defaultValue = "true") boolean rememberme,
                        HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.adlogin(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme) {
                    //cookie缓存时间
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/admin";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "admin/login";
            }

        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "admin/login";
        }
    }
}
