package org.wiliammelo.empoweru.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wiliammelo.empoweru.dtos.student.CreateStudentDTO;
import org.wiliammelo.empoweru.dtos.student.UpdateStudentDTO;
import org.wiliammelo.empoweru.exceptions.UserAlreadyExistsException;
import org.wiliammelo.empoweru.exceptions.UserNotFoundException;
import org.wiliammelo.empoweru.models.User;
import org.wiliammelo.empoweru.repositories.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User user) throws UserAlreadyExistsException {
        Boolean userAlreadyExists = this.userRepository.existsByEmail(user.getEmail());

        if(userAlreadyExists){
            throw new UserAlreadyExistsException("This email is already registered.");
        }

        return this.userRepository.save(user);
    }

    public User findById(UUID id)throws UserNotFoundException {
        return this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void deleteById(UUID id) throws UserNotFoundException {
        User user = this.findById(id);
        this.userRepository.delete(user);
    }

}
