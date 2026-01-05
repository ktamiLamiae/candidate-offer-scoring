package com.iamco.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Offre {
    private Long id;
    private String titre;
    private String description;
    private String typeContrat;
    private Double salaire;
    private String localisation;
    private boolean active = true;
    private LocalDate datePublication;
    private Recruteur recruteur;
    private List<OffreSkill> skills = new ArrayList<>();

    public Offre() {
    }

    public Offre(String titre, String description, String typeContrat, Double salaire, String localisation,
            Recruteur recruteur) {
        this.titre = titre;
        this.description = description;
        this.typeContrat = typeContrat;
        this.salaire = salaire;
        this.localisation = localisation;
        this.recruteur = recruteur;
        this.datePublication = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public Double getSalaire() {
        return salaire;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public Recruteur getRecruteur() {
        return recruteur;
    }

    public void setRecruteur(Recruteur recruteur) {
        this.recruteur = recruteur;
    }

    public List<OffreSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<OffreSkill> skills) {
        this.skills = skills;
    }
}
