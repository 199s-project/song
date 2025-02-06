package com.example.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.dto.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false; // 요청 중단
        }
        
        // 세션에서 사용자 가져오기
        Object user = session.getAttribute("user");
        if (user != null) {
        	MemberVO LoginUser = (MemberVO)user;
        	request.setAttribute("member_num", LoginUser.getMember_num());
        	request.setAttribute("member_name", LoginUser.getMember_name());
        	request.setAttribute("member_grade", LoginUser.getMember_grade());
        	request.setAttribute("member_dept", LoginUser.getMember_dept());
			/*
			 * System.out.println("로그인이 되었다면 user 정보 입니다............." + user.toString());
			 */
        }
        return true; // 요청 계속 진행
    }
}