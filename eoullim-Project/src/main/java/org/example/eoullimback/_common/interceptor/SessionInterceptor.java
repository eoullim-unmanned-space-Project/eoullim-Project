package org.example.eoullimback._common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView
    ) throws Exception {
//        if (modelAndView != null) {
//            HttpSession session = request.getSession(false);
//
//            if (session != null) {
//                User sessionUser = (User) session.getAttribute("sessionUser");
//
//                if (session != null && !modelAndView.getModel().containsKey("sessionUser")) {
//                    modelAndView.addObject("sessionUser", sessionUser);
//                }
//            }
//
//        }
    }
}
