package org.wiliammelo.empoweru.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a User.
 */
@Data
@Entity(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100)
    private String name;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 1)
    private String gender;

    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role.getRole().equals("admin")) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_PROFESSOR"), new SimpleGrantedAuthority("ROLE_STUDENT"));
        } else if (role.getRole().equals("professor")) {
            return List.of(new SimpleGrantedAuthority("ROLE_PROFESSOR"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
        }
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
