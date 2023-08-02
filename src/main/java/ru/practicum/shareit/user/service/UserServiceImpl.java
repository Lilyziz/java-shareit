package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InternalServerErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User create(User user) throws InternalServerErrorException {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) throws NotFoundException {
        User updatedUser = updateUser(user);
        return userRepository.save(updatedUser);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("There is no user with this ID"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    private User updateUser(User user) {
        User updatingUser = getById(user.getId());
        Optional.ofNullable(user.getName()).ifPresent(updatingUser::setName);
        String oldEmail = updatingUser.getEmail();
        String newEmail = user.getEmail();
        if (newEmail != null && !newEmail.isBlank() && !oldEmail.equals(newEmail)) {
            updatingUser.setEmail(newEmail);
        }
        return updatingUser;
    }
}
