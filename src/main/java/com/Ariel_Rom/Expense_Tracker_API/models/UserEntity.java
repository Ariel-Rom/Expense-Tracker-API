package com.Ariel_Rom.Expense_Tracker_API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data // Genera getters, setters, equals, hashCode y toString automáticamente
@Entity // Marca esta clase como entidad JPA que se mapea a la tabla "users"
@Builder // Permite construir objetos con patrón builder
@AllArgsConstructor // Constructor con todos los campos
@NoArgsConstructor // Constructor vacío (requerido por JPA)
@Table(name = "users") // Nombre de la tabla en la base de datos
public class UserEntity implements UserDetails { // Implementa UserDetails para integración con Spring Security

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento para id
    private Long id;

    @NotBlank(message = "Username is required") // Validación: no puede estar vacío
    @Column(nullable = false, unique = true, length = 25) // No nulo, único y con max length 25
    private String username;

    @NotBlank(message = "Password is required") // Validación: no puede estar vacío
    @Column(nullable = false, length = 100) // No nulo y con max length 100
    private String password;

    @OneToMany(mappedBy = "user") // Relación uno a muchos con ExpenseEntity (un usuario tiene muchos gastos)
    private List<ExpenseEntity> expenses;

    // Métodos requeridos por UserDetails para gestionar roles y estado de la cuenta

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aquí asignamos el rol fijo "ROLE_USER" a todos los usuarios
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Cuenta no expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Cuenta no está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciales no expiran
    }

    @Override
    public boolean isEnabled() {
        return true; // Cuenta está habilitada
    }
}
