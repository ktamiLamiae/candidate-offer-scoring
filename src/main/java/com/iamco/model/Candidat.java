package com.iamco.model;

import java.util.List;

public class Candidat extends User {
    private Profil profil;
    private List<Candidature> candidatures;

    public Candidat() {
        super();
    }

    public Candidat(String email, String password, String nom, String prenom, String telephone) {
        super(email, password, nom, prenom, telephone);
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public List<Candidature> getCandidatures() {
        return candidatures;
    }

    public void setCandidatures(List<Candidature> candidatures) {
        this.candidatures = candidatures;
    }
}
