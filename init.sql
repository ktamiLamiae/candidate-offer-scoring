-- INIT SQL SCRIPT

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    telephone VARCHAR(50),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS candidats (
    id INT PRIMARY KEY REFERENCES users(id),
    profil_id INT
);

CREATE TABLE IF NOT EXISTS recruteurs (
    id INT PRIMARY KEY REFERENCES users(id),
    entreprise VARCHAR(255),
    poste_actuel VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS profils (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255),
    description TEXT,
    cv_path VARCHAR(255),
    experience_annees INT,
    updated_at TIMESTAMP
);

ALTER TABLE candidats ADD CONSTRAINT fk_profil FOREIGN KEY (profil_id) REFERENCES profils(id);

CREATE TABLE IF NOT EXISTS skills (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL UNIQUE,
    categorie VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS profil_skills (
    id SERIAL PRIMARY KEY,
    profil_id INT NOT NULL REFERENCES profils(id),
    skill_id INT NOT NULL REFERENCES skills(id),
    niveau INT,
    annees_experience INT
);

CREATE TABLE IF NOT EXISTS offres (
    id SERIAL PRIMARY KEY,
    titre VARCHAR(255),
    description TEXT,
    type_contrat VARCHAR(50),
    salaire DOUBLE PRECISION,
    localisation VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    date_publication DATE,
    recruteur_id INT NOT NULL REFERENCES recruteurs(id)
);

CREATE TABLE IF NOT EXISTS offre_skills (
    id SERIAL PRIMARY KEY,
    offre_id INT NOT NULL REFERENCES offres(id),
    skill_id INT NOT NULL REFERENCES skills(id),
    niveau_requis INT,
    obligatoire BOOLEAN
);

CREATE TABLE IF NOT EXISTS match_results (
    id SERIAL PRIMARY KEY,
    score DOUBLE PRECISION,
    explication TEXT,
    skills_matches TEXT,
    skills_manquants TEXT,
    date_calcul TIMESTAMP
);

CREATE TABLE IF NOT EXISTS candidatures (
    id SERIAL PRIMARY KEY,
    candidat_id INT NOT NULL REFERENCES candidats(id),
    offre_id INT NOT NULL REFERENCES offres(id),
    date_candidature TIMESTAMP,
    statut VARCHAR(50),
    motivation TEXT,
    match_result_id INT UNIQUE REFERENCES match_results(id)
);
