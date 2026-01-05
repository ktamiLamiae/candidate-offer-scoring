package com.iamco.model;

import java.time.LocalDateTime;

public class Candidature {
    private Long id;
    private Candidat candidat;
    private Offre offre;
    private LocalDateTime dateCandidature;
    private String statut;
    private String motivation;
    private MatchResult matchResult;

    public Candidature() {
    }

    public Candidature(Candidat candidat, Offre offre, String motivation) {
        this.candidat = candidat;
        this.offre = offre;
        this.motivation = motivation;
    }

    public void onCreate() {
        if (statut == null)
            statut = "EN_ATTENTE";
        dateCandidature = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public LocalDateTime getDateCandidature() {
        return dateCandidature;
    }

    public void setDateCandidature(LocalDateTime dateCandidature) {
        this.dateCandidature = dateCandidature;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public MatchResult getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
    }
}
