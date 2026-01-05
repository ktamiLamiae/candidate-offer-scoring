package com.iamco.model;

public class ProfilSkill {
    private Long id;
    private Profil profil;
    private Skill skill;
    private int niveau;
    private int anneesExperience;

    public ProfilSkill() {
    }

    public ProfilSkill(Profil profil, Skill skill, int niveau, int anneesExperience) {
        this.profil = profil;
        this.skill = skill;
        this.niveau = niveau;
        this.anneesExperience = anneesExperience;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public int getAnneesExperience() {
        return anneesExperience;
    }

    public void setAnneesExperience(int anneesExperience) {
        this.anneesExperience = anneesExperience;
    }
}
