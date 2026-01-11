package com.iamco.gui;

import com.iamco.dao.GenericDAO;
import com.iamco.model.*;
import com.iamco.util.HibernateUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;

public class ProfileEditController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<ProfilSkill> skillTable;
    @FXML
    private TableColumn<ProfilSkill, String> colSkill;
    @FXML
    private TableColumn<ProfilSkill, Integer> colLevel;

    @FXML
    private ComboBox<Skill> skillComboBox;
    @FXML
    private ComboBox<Integer> levelComboBox;

    private final GenericDAO<Skill> skillDAO = new GenericDAO<>(Skill.class);

    private Candidat currentCandidat;
    private Profil currentProfil;

    @FXML
    public void initialize() {
        if (!(MainApp.currentUser instanceof Candidat)) {
            return;
        }

        if (welcomeLabel != null) {
            welcomeLabel.setText("Bienvenue , " + MainApp.currentUser.getPrenom() + " (Candidat)");
        }

        colSkill.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSkill().getNom()));
        colLevel.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNiveau()).asObject());

        levelComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        levelComboBox.getSelectionModel().select(0);

        loadCurrentCandidat();
        loadSkills();
        loadProfilSkills();

        skillTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                skillComboBox.getSelectionModel().select(newSel.getSkill());
                levelComboBox.getSelectionModel().select(Integer.valueOf(newSel.getNiveau()));
            }
        });
    }

    private void loadCurrentCandidat() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            currentCandidat = session.get(Candidat.class, MainApp.currentUser.getId());
            if (currentCandidat.getProfil() == null) {
                // Create profile if missing
                Transaction tx = session.beginTransaction();
                Profil profil = new Profil("Mon Profil", "Description par défaut", 0);
                session.persist(profil);
                currentCandidat.setProfil(profil);
                session.merge(currentCandidat);
                tx.commit();
            }
            currentProfil = currentCandidat.getProfil();
            // Force load skills while session is open
            currentProfil.getSkills().forEach(ps -> ps.getSkill().getNom());
        }
    }

    private void loadSkills() {
        List<Skill> skills = skillDAO.findAll();
        skillComboBox.setItems(FXCollections.observableArrayList(skills));
    }

    private void loadProfilSkills() {
        // We need to refresh currentProfil since we might have made changes
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Profil p = session.get(Profil.class, currentProfil.getId());
            p.getSkills().forEach(ps -> ps.getSkill().getNom());
            skillTable.setItems(FXCollections.observableArrayList(p.getSkills()));
            currentProfil = p;
        }
    }

    @FXML
    private void handleSave() {
        Skill selectedSkill = skillComboBox.getSelectionModel().getSelectedItem();
        Integer level = levelComboBox.getSelectionModel().getSelectedItem();

        if (selectedSkill == null || level == null) {
            showAlert("Erreur", "Veuillez sélectionner une compétence et un niveau.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Profil p = session.get(Profil.class, currentProfil.getId());

            ProfilSkill existing = p.getSkills().stream()
                    .filter(ps -> ps.getSkill().getId().equals(selectedSkill.getId()))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existing.setNiveau(level);
                session.merge(existing);
            } else {
                ProfilSkill ps = new ProfilSkill(p, selectedSkill, level, 0);
                p.getSkills().add(ps);
                session.merge(p);
            }

            tx.commit();
            loadProfilSkills();
            showAlert("Succès", "Compétence mise à jour !");
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de sauvegarder : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        ProfilSkill selected = skillTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Sélectionnez une compétence à supprimer.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Profil p = session.get(Profil.class, currentProfil.getId());

            p.getSkills().removeIf(ps -> ps.getId().equals(selected.getId()));
            session.merge(p);

            tx.commit();
            loadProfilSkills();
            handleClear();
            showAlert("Succès", "Compétence supprimée !");
        } catch (Exception e) {
            showAlert("Erreur", "Désolé, une erreur est survenue.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        skillComboBox.getSelectionModel().clearSelection();
        levelComboBox.getSelectionModel().select(0);
        skillTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() throws IOException {
        MainApp.setRoot("candidate_dashboard");
    }

    @FXML
    private void handleLogout() throws IOException {
        MainApp.currentUser = null;
        MainApp.setRoot("login");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
