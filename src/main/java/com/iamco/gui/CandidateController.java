package com.iamco.gui;

import com.iamco.exception.AppException;
import com.iamco.model.*;
import com.iamco.service.MatchingService;
import com.iamco.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CandidateController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ListView<String> myMatchesListView;

    private final MatchingService matchingService = new MatchingService();

    @FXML
    public void initialize() {
        if (MainApp.currentUser instanceof Candidat) {
            welcomeLabel.setText(
                    "Bienvenue , " + MainApp.currentUser.getPrenom() + " (Candidat)");
            loadMatches();
        }
    }

    private void loadMatches() {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Long candidateId = MainApp.currentUser.getId();

            Candidat me = session.get(Candidat.class, candidateId);

            if (me.getProfil() != null) {
                me.getProfil().getSkills().forEach(ps -> ps.getSkill().getNom());
            }

            List<Offre> offres = session
                    .createQuery("from Offre", Offre.class)
                    .list();

            offres.forEach(o -> {
                o.getSkills().forEach(os -> os.getSkill().getNom());
                o.getRecruteur().getEntreprise();
            });

            List<String> results = offres.stream()
                    .map(offre -> {
                        MatchResult result = matchingService.calculateMatch(offre, me);

                        String reqSkills = offre.getSkills().stream()
                                .map(os -> os.getSkill().getNom() + (os.isObligatoire() ? "*" : ""))
                                .collect(Collectors.joining(", "));

                        return new Object[] {
                                result.getScore(),
                                String.format(
                                        "%s - Score: %.1f%% (%s)\n   Requis: %s",
                                        offre.getTitre(),
                                        result.getScore(),
                                        offre.getRecruteur().getEntreprise(),
                                        reqSkills)
                        };
                    })
                    .sorted(Comparator.comparingDouble(o -> -(double) o[0]))
                    .map(o -> (String) o[1])
                    .collect(Collectors.toList());

            myMatchesListView.setItems(FXCollections.observableArrayList(results));

        } catch (AppException e) {
            myMatchesListView.setItems(
                    FXCollections.observableArrayList("Attention: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            myMatchesListView.setItems(
                    FXCollections.observableArrayList("Erreur critique: " + e.getMessage()));
        } finally {
            session.close();
        }
    }

    @FXML
    private void handleEditProfile() throws IOException {
        MainApp.setRoot("profile_edit");
    }

    @FXML
    private void handleLogout() throws IOException {
        MainApp.currentUser = null;
        MainApp.setRoot("login");
    }
}
