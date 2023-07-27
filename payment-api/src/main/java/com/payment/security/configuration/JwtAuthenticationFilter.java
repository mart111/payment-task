package com.payment.security.configuration;

import com.payment.exception.AuthenticationTokenNotValidException;
import com.payment.model.entity.User;
import com.payment.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";

    private final JwtService jwtService;

    @Qualifier("appUserDetailsService")
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final var header = request.getHeader(AUTHORIZATION_HEADER);
        if (!hasText(header)) {
            filterChain.doFilter(request, response);
            return;
        }
        final var token = header.replace(BEARER, "");
        if (!hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final var username = jwtService.extractUsername(token);
            final var user = ((User) userDetailsService.loadUserByUsername(username));
            if (!jwtService.isTokenValid(user, token)) {
                filterChain.doFilter(request, response);
                return;
            }
            final var authentication = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        } catch (AuthenticationTokenNotValidException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
