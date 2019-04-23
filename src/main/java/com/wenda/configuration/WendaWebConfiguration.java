package com.wenda.configuration;

import com.wenda.interceptor.AdminRequiredInterceptor;
import com.wenda.interceptor.LoginRequiedInterceptor;
import com.wenda.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiedInterceptor loginRequiredInterceptor;

    @Autowired
    AdminRequiredInterceptor adminRequiredInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        //登陆控制
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/msg/*");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/follow/*");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/history");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/dynamic");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/admin");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/admin");
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/admin");
        //管理员权限控制
        registry.addInterceptor(adminRequiredInterceptor).addPathPatterns("/admin");
        super.addInterceptors(registry);
    }
}
