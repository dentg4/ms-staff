package com.codigo.clinica.msstaff.infrastructure.config;

import com.codigo.clinica.msstaff.domain.aggregates.request.TokenRequest;
import com.codigo.clinica.msstaff.domain.aggregates.response.TokenResponse;
import com.codigo.clinica.msstaff.infrastructure.client.ClientMsSecurity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ClientMsSecurity clientMsSecurity;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String autHeader = request.getHeader("Authorization");
        final String jwt;

        if(StringUtils.isEmpty(autHeader) || !StringUtils.startsWithIgnoreCase(autHeader, "Bearer ") ){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = autHeader.substring(7);
        TokenRequest tokenRequest = new TokenRequest(jwt);
        TokenResponse tokenResponse = clientMsSecurity.validateToken(tokenRequest);
        if(!tokenResponse.getIsValid()){
           response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error en el token.");
           return;
        }
        if(tokenResponse.getUsername()!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            Collection<SimpleGrantedAuthority> authorities =tokenResponse.getRoles().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role)).toList();

            UserDetails userDetails = new User(tokenResponse.getUsername(), "", authorities);
            if(!tokenResponse.getIsTokenExpired()){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request,response);
            }
        }

    }

}
