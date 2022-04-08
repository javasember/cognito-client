package com.gys.cognitoclient;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.UserType;

public class AwsUserHelper {

    public static User build(UserType userType) {
        var user = new User();
        user.setUsername(userType.getUsername());

        var map = userType.getAttributes()
            .stream()
            .collect(Collectors.toMap(AttributeType::getName, AttributeType::getValue));

        user.setEmail(map.get("email"));
        user.setPhoneNumber(map.get("phone_number"));

        return user;
    }

    public static User build(AdminGetUserResult userResult) {
        var user = new User();
        user.setUsername(userResult.getUsername());

        var map = userResult.getUserAttributes()
            .stream()
            .collect(Collectors.toMap(AttributeType::getName, AttributeType::getValue));

        user.setEmail(map.get("email"));
        user.setPhoneNumber(map.get("phone_number"));

        return user;
    }

    public static List<AttributeType> getAttributes(User user) {
        return List.of(
            new AttributeType().withName("email")                .withValue(user.getEmail()),
            new AttributeType().withName("email_verified")       .withValue("true"),
            new AttributeType().withName("phone_number")         .withValue(user.getPhoneNumber()),
            new AttributeType().withName("phone_number_verified").withValue("true")
        );
    }
}
