package org.chiu.micro.gateway.token;

import java.util.List;

public interface TokenUtils<T> {

    String generateToken(String userId, List<String> roles, long expire);

    T getVerifierByToken(String token);
}
