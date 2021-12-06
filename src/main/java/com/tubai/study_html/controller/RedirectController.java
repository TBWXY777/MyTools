package com.tubai.study_html.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;

@CrossOrigin
@Controller
@Api(tags = "通用的接口,需要接受页面进行重定向")
public class RedirectController {
    @GetMapping("/doc")
    public String toDoc(){
        return "redirect:/doc.html";
    }

    @GetMapping("/")
    public String toLogin(){
        return "redirect:/html/login.html";
    }

    @GetMapping("/main_page")
    public String toMainPage(){
        return "redirect:/main_page.html";
    }

}


