package com.agh.librario.Auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException{
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);                                                //wyrzuć 401
        response.setContentType("application/json");                                                            //typ komunikatu
        response.getWriter().append(json());                                                                    //wyślij komunikat z dołu
    }

    private String json() {
        Date date = new Date();
        return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Unauthorized\", "
                + "\"message\": \"Authentication failed: bad credentials\", "
                + "\"path\": \"/login\"}";
    }
}
