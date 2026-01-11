package com.iamco.service;

import com.iamco.exception.ValidationException;
import com.iamco.model.*;
import java.util.ArrayList;
import java.util.List;

public class MatchingService {

    public MatchResult calculateMatch(Offre offre, Candidat candidat) {
        if (offre == null) {
            throw new ValidationException("L'offre ne peut pas être nulle");
        }
        if (candidat == null) {
            throw new ValidationException("Le candidat ne peut pas être nul");
        }

        Profil profil = candidat.getProfil();
        if (profil == null) {
            throw new ValidationException("Le candidat " + candidat.getNom() + " n'a pas de profil configuré");
        }

        List<OffreSkill> requiredSkills = offre.getSkills();
        if (requiredSkills.isEmpty()) {
            return new MatchResult(100.0, "Aucune compétence requise pour cette offre", "N/A", "N/A");
        }

        double totalPossiblePoints = 0;
        double pointsEarned = 0.0;
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        for (OffreSkill os : requiredSkills) {
            double weight = os.isObligatoire() ? 2.0 : 1.0;
            totalPossiblePoints += weight;

            String skillName = os.getSkill().getNom();
            boolean found = false;
            for (ProfilSkill ps : profil.getSkills()) {
                if (ps.getSkill().getNom().equalsIgnoreCase(skillName)) {
                    found = true;
                    // Score for presence
                    double presenceScore = 0.4 * weight;
                    pointsEarned += presenceScore;

                    // Score for level
                    if (ps.getNiveau() >= os.getNiveauRequis()) {
                        pointsEarned += 0.6 * weight;
                        matched.add(skillName + " (Niveau OK)");
                    } else {
                        double partialLevel = (double) ps.getNiveau() / os.getNiveauRequis();
                        pointsEarned += (0.6 * weight) * partialLevel;
                        matched.add(skillName + " (Niveau " + ps.getNiveau() + "/" + os.getNiveauRequis() + ")");
                    }
                    break;
                }
            }
            if (!found) {
                missing.add(skillName + (os.isObligatoire() ? " [OBLIGATOIRE]" : ""));
            }
        }

        double score = (pointsEarned / totalPossiblePoints) * 100.0;

        // Penalize heavily if mandatory skills are missing
        long missingMandatory = requiredSkills.stream()
                .filter(os -> os.isObligatoire()
                        && !matched.stream().anyMatch(m -> m.startsWith(os.getSkill().getNom())))
                .count();
        if (missingMandatory > 0) {
            score = score * Math.pow(0.5, missingMandatory); // Lose 50% for each missing mandatory skill
        }

        String explication = String.format("Score: %.1f%%. Analyse de %d skills (%d obligatoires).",
                score, requiredSkills.size(), (int) requiredSkills.stream().filter(OffreSkill::isObligatoire).count());

        return new MatchResult(
                score,
                explication,
                matched.isEmpty() ? "Aucun" : String.join(", ", matched),
                missing.isEmpty() ? "Aucun" : String.join(", ", missing));
    }

}
