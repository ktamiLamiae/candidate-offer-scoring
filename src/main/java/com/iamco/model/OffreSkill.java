package com.iamco.model;

public class OffreSkill {
    private Long id;
    private Offre offre;
    private Skill skill;
    private int niveauRequis;
    private boolean obligatoire;

    public OffreSkill() {
    }

    public OffreSkill(Offre offre, Skill skill, int niveauRequis, boolean obligatoire) {
        this.offre = offre;
        this.skill = skill;
        this.niveauRequis = niveauRequis;
        this.obligatoire = obligatoire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public int getNiveauRequis() {
        return niveauRequis;
    }

    public void setNiveauRequis(int niveauRequis) {
        this.niveauRequis = niveauRequis;
    }

    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }
}
