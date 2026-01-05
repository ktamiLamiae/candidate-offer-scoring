package com.iamco.model;

import java.util.List;

public class Recruteur extends User {
    private String entreprise;
    private String posteActuel;
    private List<Offre> offres;

    public Recruteur() {
        super();
    }

    public Recruteur(String email, String password, String nom, String prenom, String telephone, String entreprise,
            String posteActuel) {
        super(email, password, nom, prenom, telephone);
        this.entreprise = entreprise;
        this.posteActuel = posteActuel;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public String getPosteActuel() {
        return posteActuel;
    }

    public void setPosteActuel(String posteActuel) {
        this.posteActuel = posteActuel;
    }

    public List<Offre> getOffres() {
        return offres;
    }

    public void setOffres(List<Offre> offres) {
        this.offres = offres;
    }
}
