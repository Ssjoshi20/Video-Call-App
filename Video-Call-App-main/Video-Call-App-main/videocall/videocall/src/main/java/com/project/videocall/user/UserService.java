package com.project.videocall.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserStoreData userStoreData;

    public UserService() {
        this.userStoreData = new UserStoreData();
    }

    public void register(User user) {
        if (!userStoreData.userExists(user.getEmail())) {
            user.setStatus("online");
            userStoreData.saveToDatabase(user);
        }
    }

    public User login(User user) {
        List<User> usersFromDb = userStoreData.getAllUsersFromDatabase();
        Optional<User> matchingUser = usersFromDb.stream()
                .filter(dbUser -> dbUser.getEmail().equals(user.getEmail()) && dbUser.getPassword().equals(user.getPassword()))
                .findFirst();

        if (matchingUser.isPresent()) {
            User cUser = matchingUser.get();
            cUser.setStatus("online");
            userStoreData.updateUserStatus(cUser); // Update the user's status in the database
            return cUser;
        } else {
            throw new RuntimeException("Email or password is incorrect");
        }
    }

    public void logout(String email) {
        List<User> usersFromDb = userStoreData.getAllUsersFromDatabase();
        usersFromDb.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .ifPresent(user -> {
                    user.setStatus("offline");
                    userStoreData.updateUserStatus(user); // Update the user's status in the database
                });
    }

    public List<User> findAll() {
        return userStoreData.getAllUsersFromDatabase();
    }
}
