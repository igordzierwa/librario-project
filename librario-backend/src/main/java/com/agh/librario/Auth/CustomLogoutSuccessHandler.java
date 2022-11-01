package com.agh.librario.Auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.getDetails() != null) {
            try {
                httpServletRequest.getSession().invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
        //redirect to login
        httpServletResponse.setContentType("application/json");                                                            //typ komunikatu
        httpServletResponse.getWriter().append(json());                                                                    //wyślij komunikat z dołu
    }

    private String json() {
        return "{\"logout\": " + "\"successful\"}";
    }
}
