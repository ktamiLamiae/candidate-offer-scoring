package com.iamco.gui;

import com.iamco.dao.GenericDAO;
import com.iamco.model.Offre;
import com.iamco.model.OffreSkill;
import com.iamco.model.Recruteur;
import com.iamco.model.Skill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import com.iamco.util.HibernateUtil;
import org.hibernate.Session;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class OffreCRUDController {

    @FXML
    private TableView<Offre> offreTable;
    @FXML
    private TableColumn<Offre, Long> colId;
    @FXML
    private TableColumn<Offre, String> colTitre;
    @FXML
    private TableColumn<Offre, Double> colSalaire;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField titreField;
    @FXML
    private TextField salaireField;
    @FXML
    private ListView<Skill> skillListView;

    private final GenericDAO<Offre> offreDAO = new GenericDAO<>(Offre.class);
    private final GenericDAO<Skill> skillDAO = new GenericDAO<>(Skill.class);
    private ObservableList<Offre> offreList;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colSalaire.setCellValueFactory(new PropertyValueFactory<>("salaire"));

        skillListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        loadOffres();
        loadSkills();

        offreTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titreField.setText(newSelection.getTitre());
                salaireField.setText(String.valueOf(newSelection.getSalaire()));

                // Select skills associated with the offer
                skillListView.getSelectionModel().clearSelection();
                List<Long> associatedSkillIds = newSelection.getSkills().stream()
                        .map(os -> os.getSkill().getId())
                        .collect(Collectors.toList());

                for (Skill skill : skillListView.getItems()) {
                    if (associatedSkillIds.contains(skill.getId())) {
                        skillListView.getSelectionModel().select(skill);
                    }
                }
            }
        });
    }

    private void loadSkills() {
        List<Skill> allSkills = skillDAO.findAll();
        skillListView.setItems(FXCollections.observableArrayList(allSkills));
    }

    private void loadOffres() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Offre> offres = session.createQuery("from Offre", Offre.class).list();
            // Force loading of skills while session is open
            offres.forEach(o -> o.getSkills().forEach(os -> os.getSkill().getNom()));
            offreList = FXCollections.observableArrayList(offres);
            offreTable.setItems(offreList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des offres : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave() {
        try {
            String titre = titreField.getText();
            Double salaire = Double.parseDouble(salaireField.getText());

            if (MainApp.currentUser instanceof Recruteur) {
                Offre nouvelleOffre = new Offre();
                nouvelleOffre.setTitre(titre);
                nouvelleOffre.setSalaire(salaire);
                nouvelleOffre.setRecruteur((Recruteur) MainApp.currentUser);
                nouvelleOffre.setActive(true);
                nouvelleOffre.setDescription("Nouvelle offre ajoutée via CRUD");
                nouvelleOffre.setTypeContrat("CDI");
                nouvelleOffre.setLocalisation("Casablanca");

                // Add skills
                List<OffreSkill> offreSkills = skillListView.getSelectionModel().getSelectedItems().stream()
                        .map(skill -> new OffreSkill(nouvelleOffre, skill, 1, true))
                        .collect(Collectors.toList());
                nouvelleOffre.setSkills(offreSkills);

                offreDAO.save(nouvelleOffre);
                loadOffres();
                handleClear();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre ajoutée avec succès !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Seul un recruteur peut ajouter une offre.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le salaire doit être un nombre valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Offre selectedOffre = offreTable.getSelectionModel().getSelectedItem();
        if (selectedOffre == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une offre à modifier.");
            return;
        }

        try {
            selectedOffre.setTitre(titreField.getText());
            selectedOffre.setSalaire(Double.parseDouble(salaireField.getText()));

            // Update skills
            selectedOffre.getSkills().clear();
            List<OffreSkill> newOffreSkills = skillListView.getSelectionModel().getSelectedItems().stream()
                    .map(skill -> new OffreSkill(selectedOffre, skill, 1, true))
                    .collect(Collectors.toList());
            selectedOffre.getSkills().addAll(newOffreSkills);

            offreDAO.update(selectedOffre);
            loadOffres();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre mise à jour avec succès !");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le salaire doit être un nombre valide.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Offre selectedOffre = offreTable.getSelectionModel().getSelectedItem();
        if (selectedOffre == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner une offre à supprimer.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette offre ?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    offreDAO.delete(selectedOffre);
                    loadOffres();
                    handleClear();
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Offre supprimée avec succès !");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression : " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleClear() {
        titreField.clear();
        salaireField.clear();
        offreTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBack() throws IOException {
        MainApp.setRoot("recruiter_dashboard");
    }

    @FXML
    private void handleLogout() throws IOException {
        MainApp.currentUser = null;
        MainApp.setRoot("login");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
