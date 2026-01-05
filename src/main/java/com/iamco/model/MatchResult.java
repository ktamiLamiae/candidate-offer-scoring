package com.iamco.model;

import java.time.LocalDateTime;

public class MatchResult {
    private Long id;
    private double score;
    private String explication;
    private String skillsMatches;
    private String skillsManquants;
    private LocalDateTime dateCalcul;
    private Candidature candidature;

    public MatchResult() {
    }

    public MatchResult(double score, String explication, String skillsMatches, String skillsManquants) {
        this.score = score;
        this.explication = explication;
        this.skillsMatches = skillsMatches;
        this.skillsManquants = skillsManquants;
        this.dateCalcul = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getExplication() {
        return explication;
    }

    public void setExplication(String explication) {
        this.explication = explication;
    }

    public String getSkillsMatches() {
        return skillsMatches;
    }

    public void setSkillsMatches(String skillsMatches) {
        this.skillsMatches = skillsMatches;
    }

    public String getSkillsManquants() {
        return skillsManquants;
    }

    public void setSkillsManquants(String skillsManquants) {
        this.skillsManquants = skillsManquants;
    }

    public LocalDateTime getDateCalcul() {
        return dateCalcul;
    }

    public void setDateCalcul(LocalDateTime dateCalcul) {
        this.dateCalcul = dateCalcul;
    }

    public Candidature getCandidature() {
        return candidature;
    }

    public void setCandidature(Candidature candidature) {
        this.candidature = candidature;
    }
}
