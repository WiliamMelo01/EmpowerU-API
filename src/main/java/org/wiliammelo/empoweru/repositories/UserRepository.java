package org.wiliammelo.empoweru.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.wiliammelo.empoweru.models.User;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Boolean existsByEmail(String email);

    UserDetails findByEmail(String email);

    @Query("SELECT CASE WHEN u.role = 0 THEN TRUE ELSE FALSE END FROM users u WHERE u.id = :userId")
    boolean isAdmin(@Param("userId") UUID userId);

}
