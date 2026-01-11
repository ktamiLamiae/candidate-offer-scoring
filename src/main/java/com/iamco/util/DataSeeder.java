package com.iamco.util;

import com.iamco.dao.GenericDAO;
import com.iamco.model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Random;

public class DataSeeder {

    private static final Random rand = new Random();

    public static void seed() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            try {
                // Vider les tables existantes pour repartir sur une base propre
                // session.createNativeQuery("TRUNCATE TABLE profil_skills
                // CASCADE").executeUpdate();
                // session.createNativeQuery("TRUNCATE TABLE offre_skills
                // CASCADE").executeUpdate();
                // session.createNativeQuery("TRUNCATE TABLE offres CASCADE").executeUpdate();
                // session.createNativeQuery("TRUNCATE TABLE candidats
                // CASCADE").executeUpdate();
                // session.createNativeQuery("TRUNCATE TABLE recruteurs
                // CASCADE").executeUpdate();
                // session.createNativeQuery("TRUNCATE TABLE users CASCADE").executeUpdate();
                // session.createNativeQuery("TRUNCATE TABLE profils CASCADE").executeUpdate();

                System.out.println(">> Database cleared.");

                // 1️⃣ Skills
                Skill[] skills = {
                        getOrCreateSkill(session, "Java", "Tech"),
                        getOrCreateSkill(session, "SQL", "Tech"),
                        getOrCreateSkill(session, "JavaFX", "Tech"),
                        getOrCreateSkill(session, "Laravel", "Tech"),
                        getOrCreateSkill(session, ".NET Core", "Tech"),
                        getOrCreateSkill(session, "React", "Tech"),
                        getOrCreateSkill(session, "Angular", "Tech"),
                        getOrCreateSkill(session, "Dev Mobile", "Tech")
                };
                getOrCreateSkill(session, "Communication", "Soft");

                // 2️⃣ Recruiter
                Recruteur recruteur = new Recruteur(
                        "admin@iamco.com", "admin123",
                        "Admin", "Admin", "0616253988",
                        "IAMCO Corp", "HR");
                session.persist(recruteur);

                // 3️⃣ Main candidate
                createMainCandidate(session, skills);

                // 4️⃣ Random candidates (exactly 1 more for a total of 2)
                generateRandomCandidates(session, skills, 1);

                // 5️⃣ Job offers
                // generateRandomOffers(session, recruteur, 10, skills);

                tx.commit();
                System.out.println(">> DataSeeder finished successfully (Reset & Seed).");

            } catch (Exception e) {
                if (tx != null)
                    tx.rollback();
                e.printStackTrace();
            }
        }
    }

    // -------------------- HELPERS (Using local session) --------------------

    private static Skill getOrCreateSkill(Session session, String nom, String categorie) {
        Skill skill = session.createQuery(
                "FROM Skill s WHERE s.nom = :nom", Skill.class).setParameter("nom", nom).uniqueResult();

        if (skill == null) {
            skill = new Skill(nom, categorie);
            session.persist(skill);
        }
        return skill;
    }

    private static void createMainCandidate(Session session, Skill[] skills) {
        Candidat c = new Candidat(
                "candidat@test.com", "1234",
                "Doe", "John", "0612345678");

        Profil p = new Profil("Développeur Fullstack", "Expert Java / Angular", 5);
        p.getSkills().add(new ProfilSkill(p, skills[0], 4, 5)); // Java
        p.getSkills().add(new ProfilSkill(p, skills[1], 3, 3)); // SQL

        c.setProfil(p);
        session.persist(c);
    }

    private static void generateRandomCandidates(Session session, Skill[] skills, int count) {
        for (int i = 1; i <= count; i++) {
            String email = "candidat" + i + "@test.com";

            Candidat c = new Candidat(
                    email, "1234",
                    "Nom" + i, "Prenom" + i,
                    "06000000" + i);

            Profil p = new Profil(
                    "Profil " + i,
                    "Description " + i,
                    rand.nextInt(10) + 1);

            for (Skill s : skills) {
                if (rand.nextBoolean()) {
                    p.getSkills().add(
                            new ProfilSkill(p, s, rand.nextInt(5) + 1, rand.nextInt(5) + 1));
                }
            }

            c.setProfil(p);
            session.persist(c);
        }
    }

    // private static void generateRandomOffers(
    // Session session,
    // Recruteur recruteur,
    // int count,
    // Skill[] skills) {
    // String[] titles = {
    // "Dev Java", "Dev Fullstack", "Tech Lead",
    // "Data Engineer", "DevOps", "Architecte",
    // "Expert .NET", "Dev Frontend",
    // "Dev Laravel", "Mobile Expert"
    // };

    // for (int i = 0; i < count; i++) {
    // String title = titles[rand.nextInt(titles.length)] + " " + (rand.nextInt(900)
    // + 100);

    // Offre o = new Offre(
    // title,
    // "Mission de développement " + title,
    // "CDI",
    // 5000 + rand.nextDouble() * 45000,
    // "Paris / Remote",
    // recruteur);

    // int skillCount = rand.nextInt(4) + 1;
    // for (int j = 0; j < skillCount; j++) {
    // Skill s = skills[rand.nextInt(skills.length)];
    // o.getSkills().add(
    // new OffreSkill(o, s, rand.nextInt(4) + 1, rand.nextBoolean()));
    // }

    // session.save(o);
    // }
    // }
}
