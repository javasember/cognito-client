package com.gys.cognitoclient;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;

import org.springframework.stereotype.Component;

@Component
public class Client {
    private final CognitoConfiguration config;
    private AWSCognitoIdentityProvider client;

    public Client(CognitoConfiguration config) {
        this.config = config;
        this.client = buildClient(config);
    }

    private AWSCognitoIdentityProvider buildClient(CognitoConfiguration config) {
        var credentials         = new BasicAWSCredentials(config.getKey(), config.getSecret());
		var credentialsProvider = new AWSStaticCredentialsProvider(credentials);

		return AWSCognitoIdentityProviderClientBuilder.standard()
			.withCredentials(credentialsProvider)
			.withRegion(config.getRegion())
			.build();
    }

    public void create(User user) {
        var createRequest = new AdminCreateUserRequest()
            .withUserPoolId(config.getPoolId())
            .withUsername(user.getUsername())
            .withDesiredDeliveryMediums("EMAIL")
            .withMessageAction("SUPPRESS")
            .withUserAttributes(
                new AttributeType().withName("email")                .withValue(user.getEmail()),
                new AttributeType().withName("email_verified")       .withValue("true"),
                new AttributeType().withName("phone_number")         .withValue(user.getPhoneNumber()),
                new AttributeType().withName("phone_number_verified").withValue("true")
            );

        client.adminCreateUser(createRequest);

        var passwordRequest = new AdminSetUserPasswordRequest()
            .withUsername(user.getUsername())
            .withPassword(user.getPassword())
            .withPermanent(true)
            .withUserPoolId(config.getPoolId());
        
        client.adminSetUserPassword(passwordRequest);
    }

    public User get(String login) {
        var userRequest = new AdminGetUserRequest()
            .withUserPoolId(config.getPoolId())
            .withUsername(login);

        var result = client.adminGetUser(userRequest);

        return AwsUserHelper.build(result);
    }

    public List<User> getAll() {
        var request = new ListUsersRequest()
            .withUserPoolId(config.getPoolId());

        return client.listUsers(request)
            .getUsers()
            .stream()
            .map(AwsUserHelper::build)
            .collect(Collectors.toList());
    }

    public void update(User user) {
        var request = new AdminUpdateUserAttributesRequest()
            .withUserPoolId(config.getPoolId())
            .withUsername(user.getUsername())
            .withUserAttributes(AwsUserHelper.getAttributes(user));

        client.adminUpdateUserAttributes(request);
    }

    public void delete(String login) {
        var request = new AdminDeleteUserRequest()
            .withUserPoolId(config.getPoolId())
            .withUsername(login);

        client.adminDeleteUser(request);
    }
}
