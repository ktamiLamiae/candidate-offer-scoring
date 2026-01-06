package com.iamco.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Profil {
    private Long id;
    private String titre;
    private String description;
    private Integer experienceAnnees;
    private LocalDateTime updatedAt;
    private List<ProfilSkill> skills = new ArrayList<>();

    public Profil() {
    }

    public Profil(String titre, String description, Integer experienceAnnees) {
        this.titre = titre;
        this.description = description;
        this.experienceAnnees = experienceAnnees;
    }

    public void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public Integer getExperienceAnnees() {
        return experienceAnnees;
    }

    public void setExperienceAnnees(Integer experienceAnnees) {
        this.experienceAnnees = experienceAnnees;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ProfilSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<ProfilSkill> skills) {
        this.skills = skills;
    }
}
