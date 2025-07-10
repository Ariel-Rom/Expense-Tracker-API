package com.Ariel_Rom.Expense_Tracker_API.repositories;

import com.Ariel_Rom.Expense_Tracker_API.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marca la interfaz como componente Spring para acceso a la base de datos
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Busca un usuario por su username, devuelve Optional para manejar si no existe
    Optional<UserEntity> findByUsername(String username);

}
