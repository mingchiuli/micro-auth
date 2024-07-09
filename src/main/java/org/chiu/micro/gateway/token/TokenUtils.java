package org.chiu.micro.gateway.token;

import java.util.List;

import org.chiu.micro.gateway.exception.AuthException;

public interface TokenUtils<T> {

    String generateToken(String userId, List<String> roles, long expire);

    T getVerifierByToken(String token) throws AuthException;
}
