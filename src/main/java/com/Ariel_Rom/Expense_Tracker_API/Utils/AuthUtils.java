package com.Ariel_Rom.Expense_Tracker_API.Utils;

// Importaciones necesarias
import com.Ariel_Rom.Expense_Tracker_API.models.UserEntity;
import com.Ariel_Rom.Expense_Tracker_API.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/*
 * Componente de utilidad para obtener información del usuario autenticado.
 * Se marca con @Component para que Spring lo detecte e inyecte donde sea necesario.
 * Gracias a @RequiredArgsConstructor, Spring inyecta el UserRepository por constructor automáticamente.
 */
@Component
@RequiredArgsConstructor
public class AuthUtils {

    // Repositorio para acceder a los datos de los usuarios en la base de datos
    private final UserRepository userRepository;

    /*
     * Obtiene el usuario actualmente autenticado usando el contexto de seguridad de Spring Security.
     * @return Entidad del usuario actual.
     * @throws EntityNotFoundException si el usuario no se encuentra en la base de datos.
     */
    public UserEntity getCurrentUser() {
        // Obtiene el nombre de usuario del usuario autenticado desde el contexto de Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Busca en la base de datos el usuario correspondiente
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}