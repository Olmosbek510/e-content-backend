package com.inha.os.econtentbackend.service;

import java.util.Set;

public interface AuthenticationService {
    String authenticate(String username, String password) throws Exception;

    /**
     * Checks if the user can perform the given action based on their roles.
     * @param action The action to be performed.
     * @param token The JWT token of the user.
     * @return true if the user has the required role for the action, false otherwise.
     */
    boolean canPerformAction(String action, String token);

    /**
     * Determines whether the roles are sufficient for the action.
     * You can extend this to have more complex role checks.
     * @param action The action to be performed.
     * @param roles The roles of the user.
     * @return true if authorized, false otherwise.
     */
    boolean isAuthorizedForAction(String action, Set<String> roles);
}
