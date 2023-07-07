package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorageImpl implements IUserStorage {
    private long id = 1;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    private long generateId() {
        return id++;
    }

    @Override
    public Optional<User> create(User user) {
        long id = generateId();
        user.setId(id);
        users.put(id, user);
        emails.add(user.getEmail());
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        long userId = user.getId();
        if (!users.containsKey(userId)) {
            return Optional.empty();
        }
        User updatingUser = users.get(userId);
        updateUser(updatingUser, user);
        return Optional.of(updatingUser);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(Long userId) {
        emails.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    public boolean isContains(String email) {
        return emails.contains(email);
    }

    private void updateUser(User updatingUser, User user) {
        Optional.ofNullable(user.getName()).ifPresent(updatingUser::setName);
        if (user.getEmail() != null) {
            emails.remove(updatingUser.getEmail());
            if (emails.contains(user.getEmail())) {
                emails.add(updatingUser.getEmail());
                throw new EmailException("Can't update email");
            } else {
                emails.add(user.getEmail());
                updatingUser.setEmail(user.getEmail());
            }
        }
    }
}
