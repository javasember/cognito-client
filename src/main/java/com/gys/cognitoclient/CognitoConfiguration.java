package com.gys.cognitoclient;

import lombok.Data;

@Data
public class CognitoConfiguration {
    private String key;
    private String secret;
    private String region;
    private String poolId;
}