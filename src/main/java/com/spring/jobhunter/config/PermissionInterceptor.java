package com.spring.jobhunter.config;

import com.spring.jobhunter.domain.Permission;
import com.spring.jobhunter.domain.Role;
import com.spring.jobhunter.domain.User;
import com.spring.jobhunter.service.UserService;
import com.spring.jobhunter.util.SecurityUtil;
import com.spring.jobhunter.util.error.IdInvalidException;
import com.spring.jobhunter.util.error.PermissionException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if(email != null && !email.isEmpty()) {
            User user = userService.getUserByUsername(email);
            if(user != null) {
                Role role = user.getRole();
                if(role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAllow = permissions.stream().anyMatch(item ->
                            item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));
                    if(isAllow == false)
                        throw new PermissionException("You don't have permission to access this resource");
                }else
                    throw new PermissionException("You don't have permission to access this resource");
            }
        }
        return true;
    }
}
