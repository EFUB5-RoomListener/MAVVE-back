package com.efub.mavve.auth.service.jwt;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.global.config.CustomAuthenticationEntryPoint;
import com.efub.mavve.global.exception.CustomAuthenticationException;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtResolver jwtResolver;
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader("Authorization");

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                bearerToken = bearerToken.substring(7);

                if(jwtResolver.isBlacklisted(bearerToken)){
                    throw new CustomAuthenticationException(ExceptionCode.ALREADY_LOGGED_TOKEN, ExceptionCode.ALREADY_LOGGED_TOKEN.getMessage(), null);
                }

                String userId = jwtResolver.resolveAccessToken(bearerToken);
                CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                User user = userDetails.getUser();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);


            } else{
               throw new CustomAuthenticationException(
                       ExceptionCode.AUTH_TOKEN_EMPTY, ExceptionCode.AUTH_TOKEN_EMPTY.getMessage(), null);
            }
//            else{
//               throw new CustomAuthenticationException(
//                       ExceptionCode.AUTH_TOKEN_EMPTY, ExceptionCode.AUTH_TOKEN_EMPTY.getMessage(), null);
//            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException exception){
            authenticationEntryPoint.commence(request, response, exception);
        }
    }


}
