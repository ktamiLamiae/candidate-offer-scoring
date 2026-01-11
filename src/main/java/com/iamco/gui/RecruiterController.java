package com.iamco.gui;

import com.iamco.exception.AppException;
import com.iamco.model.*;
import com.iamco.service.MatchingService;
import com.iamco.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RecruiterController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ListView<Candidat> candidateListView;

    @FXML
    private ComboBox<Offre> offerComboBox;

    @FXML
    private TextArea resultArea;

    @FXML
    private Label scoreLabel;

    @FXML
    private ProgressBar scoreBar;

    private final MatchingService matchingService = new MatchingService();

    @FXML
    public void initialize() {
        loadData();

        candidateListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Candidat item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String skillsStr = "Aucune";
                    if (item.getProfil() != null) {
                        try {
                            skillsStr = item.getProfil().getSkills().stream()
                                    .map(ps -> ps.getSkill().getNom())
                                    .collect(Collectors.joining(", "));
                        } catch (Exception e) {
                            skillsStr = "Chargement...";
                        }
                    }
                    setText(String.format(
                            "%s %s (%s)\n   Skills: %s",
                            item.getPrenom(),
                            item.getNom(),
                            item.getEmail(),
                            skillsStr));
                }
            }
        });

        offerComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Offre item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null
                        ? null
                        : item.getTitre() + " - " + item.getSalaire());
            }
        });

        offerComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Offre item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitre());
            }
        });

        candidateListView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> clearResult());
        offerComboBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> clearResult());
    }

    private void loadData() {
        var session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Candidat> candidats = session
                    .createQuery("from Candidat", Candidat.class)
                    .list();

            candidats.forEach(c -> {
                if (c.getProfil() != null) {
                    c.getProfil().getSkills().forEach(ps -> ps.getSkill().getNom());
                }
            });

            candidateListView.setItems(FXCollections.observableArrayList(candidats));

            List<Offre> offres = session
                    .createQuery("from Offre", Offre.class)
                    .list();

            offres.forEach(o -> o.getSkills().forEach(os -> os.getSkill().getNom()));

            offerComboBox.setItems(FXCollections.observableArrayList(offres));

        } catch (AppException e) {
            resultArea.setText("Problème : " + e.getMessage());
        } catch (Exception e) {
            resultArea.setText("Erreur critique lors du chargement des données : " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    private void handleCalculateMatch() {
        Candidat candidat = candidateListView.getSelectionModel().getSelectedItem();
        Offre offre = offerComboBox.getSelectionModel().getSelectedItem();

        if (candidat == null || offre == null) {
            resultArea.setText("Veuillez sélectionner un candidat et une offre.");
            return;
        }

        try {
            MatchResult result = matchingService.calculateMatch(offre, candidat);

            scoreLabel.setText(String.format("Score : %.1f %%", result.getScore()));
            scoreBar.setProgress(result.getScore() / 100.0);

            resultArea.setText(
                    "Explication : " + result.getExplication() + "\n\n" +
                            "Compétences matchées : " + result.getSkillsMatches() + "\n" +
                            "Compétences manquantes : " + result.getSkillsManquants());

        } catch (AppException e) {
            scoreLabel.setText("N/A");
            resultArea.setText("Attention : " + e.getMessage());
        } catch (Exception e) {
            scoreLabel.setText("Erreur");
            resultArea.setText("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearResult() {
        scoreLabel.setText("");
        if (scoreBar != null)
            scoreBar.setProgress(0);
        resultArea.clear();
    }

    @FXML
    private void handleGoToCRUD() throws IOException {
        MainApp.setRoot("offre_crud");
    }

    @FXML
    private void handleLogout() throws IOException {
        MainApp.setRoot("login");
    }
}
