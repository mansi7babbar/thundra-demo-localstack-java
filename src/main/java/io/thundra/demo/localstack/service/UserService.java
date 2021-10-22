package io.thundra.demo.localstack.service;

import io.thundra.demo.localstack.model.User;
import io.thundra.demo.localstack.model.UserRepository;

import java.util.Map;

public class UserService {
    UserRepository userRepository = new UserRepository();

    public UserService() {
        userRepository.createTable();
    }

    public Boolean insertUser(User user) {
        return userRepository.insertItem(user);
    }

    public User getUser(int id) {
        Iterable<Map.Entry<String, Object>> userAttributes = userRepository.getItem(id).attributes();
        User user = new User();

        for (Map.Entry<String, Object> attribute : userAttributes) {
            switch (attribute.getKey()) {
                case "id":
                    user.setId((int) attribute.getValue());
                    break;
                case "name":
                    user.setName((String) attribute.getValue());
                    break;
                case "email":
                    user.setEmail((String) attribute.getValue());
                    break;
                case "address":
                    user.setAddress((String) attribute.getValue());
                    break;
            }
        }

        return user;
    }
}
