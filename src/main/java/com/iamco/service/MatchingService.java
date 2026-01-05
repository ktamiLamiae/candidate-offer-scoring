package com.iamco.service;

import com.iamco.model.*;

public class MatchingService {

    public MatchResult calculateMatch(Offre offre, Candidat candidat) {
        Profil profil = candidat.getProfil();
        if (profil == null) {
            return new MatchResult(0.0, "Candidat sans profil", "", "");
        }

        return new MatchResult(0.0, "", "", "");
    }

}
