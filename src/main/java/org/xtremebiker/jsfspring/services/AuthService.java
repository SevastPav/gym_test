package org.xtremebiker.jsfspring.services;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import org.xtremebiker.jsfspring.entity.Rle;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;

@Service
@SessionScope
@Getter
public class AuthService {

    public boolean hasRole(String role){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        for (GrantedAuthority rle:roles) {
            if (rle.getAuthority().contains(role))
                return true;
        }
        return false;
    }

    public boolean isAuth(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        for (GrantedAuthority rle:roles) {
            if (rle.getAuthority().contains("ROLE_ANONYMOUS"))
                return false;
        }
        return true;
    }

}
