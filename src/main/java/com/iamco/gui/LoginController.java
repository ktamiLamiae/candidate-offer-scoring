package com.iamco.gui;

import com.iamco.exception.AppException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private com.iamco.dao.GenericDAO<com.iamco.model.User> userDAO = new com.iamco.dao.GenericDAO<>(
            com.iamco.model.User.class);

    @FXML
    private void handleLogin() throws IOException {
        String email = usernameField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        try {
            com.iamco.model.User user = userDAO.findByField("email", email);
            if (user != null) {
                if (user.getPassword().equals(password)) {
                    MainApp.currentUser = user;
                    if (user instanceof com.iamco.model.Recruteur) {
                        MainApp.setRoot("recruiter_dashboard");
                    } else if (user instanceof com.iamco.model.Candidat) {
                        MainApp.setRoot("candidate_dashboard");
                    } else {
                        errorLabel.setText("Rôle inconnu sur ce compte.");
                    }
                } else {
                    errorLabel.setText("Email ou mot de passe incorrect.");
                }
            } else {
                errorLabel.setText("Email ou mot de passe incorrect.");
            }
        } catch (AppException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Une erreur inattendue est survenue.");
        }
    }
}
