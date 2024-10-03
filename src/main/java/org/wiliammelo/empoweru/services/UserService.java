package org.wiliammelo.empoweru.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.clients.MessagePublisher;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.repositories.UserRepository;

import java.util.UUID;

/**
 * Service class for managing user-related operations.
 * Provides functionality to create, find by ID, and delete users.
 * Utilizes {@link UserRepository} for persistence operations.
 */
@Service
public class UserService {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final UserRepository userRepository;
    private final MessagePublisher greetingsMessagePublisher;

    public UserService(UserRepository userRepository, @Qualifier("greetingsSQSPublisher") MessagePublisher greetingsMessagePublisher) {
        this.userRepository = userRepository;
        this.greetingsMessagePublisher = greetingsMessagePublisher;
    }

    /**
     * Creates a new user with the provided {@link User} object.
     *
     * @param user The user to be created.
     * @return The saved user entity.
     * @throws UserAlreadyExistsException if a user with the same email already exists.
     */
    public User create(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail()).equals(Boolean.TRUE)) {
            throw new UserAlreadyExistsException("This email is already registered.");
        }

        if (activeProfile.equals("prod")) {
            greetingsMessagePublisher.publish(user.getEmail());
        }
        return this.userRepository.save(user);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id The UUID of the user to find.
     * @return The found user entity.
     * @throws UserNotFoundException if no user with the given ID is found.
     */
    private User findById(UUID id) throws UserNotFoundException {
        return this.userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id The UUID of the user to delete.
     * @throws UserNotFoundException if no user with the given ID is found.
     */
    public void deleteById(UUID id) throws UserNotFoundException {
        User user = this.findById(id);
        this.userRepository.delete(user);
    }

}
