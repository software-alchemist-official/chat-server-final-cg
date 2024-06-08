package main.service;

import main.exceptions.DatabaseException;
import main.model.User;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationService {

    private static final Logger LOG = Logger.getLogger(RegistrationService.class.getName());
    private final UserService userService;

    public RegistrationService() {
        this.userService = new UserService();
    }

    public boolean register(User user) throws Exception {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            userService.saveUser(user);
            LOG.info("Se ha registrado un usuario: " + user.toString());
            future.complete(true);
        } catch (DatabaseException e) {
            LOG.log(Level.SEVERE, "Error al registrar al usuario " + user, e);
            future.complete(false);
        }

        return future.get();
    }
}
