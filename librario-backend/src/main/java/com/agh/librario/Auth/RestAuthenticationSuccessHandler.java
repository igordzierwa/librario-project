package com.agh.librario.Auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Set<String> roles = authentication.getAuthorities().stream().map(r->r.getAuthority()).collect(Collectors.toSet());      //pobieranie ról
        response.setStatus(HttpServletResponse.SC_ACCEPTED);                                                                    //wystaw http202
        response.setContentType("application/json");                                                                            //typ odpowiedzi
        response.getWriter().append(json(roles));                                                                               //wyslij odpowiedz
        clearAuthenticationAttributes(request);                                                                                 //clearowanie "SPRING_SECURITY_LAST_EXCEPTION"
    }

    private String json(Set<String> role) {
        String roleString = String.join(",", role);
        return roleString;
    }
}
