package com.iamco.model;

public class Skill {
    private Long id;
    private String nom;
    private String categorie;

    public Skill() {
    }

    public Skill(String nom, String categorie) {
        this.nom = nom;
        this.categorie = categorie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return nom;
    }
}
