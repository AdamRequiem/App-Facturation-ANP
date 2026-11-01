-- ============================================================================
-- Système de facturation des droits de port sur les navires — ANP
-- Script de base : schéma MySQL + données d'exemple couvrant le Chapitre I
-- (tarification 2026) : stationnement standard, terminal pétrolier de
-- Mohammédia, terminaux charbonniers de Jorf-Lasfar et Safi Atlantique,
-- mouillage, entrée, ristournes, abonnement, touchées, facturation.
-- ============================================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS detail_facture;
DROP TABLE IF EXISTS entete_facture;
DROP TABLE IF EXISTS touchee;
DROP TABLE IF EXISTS stationnement;
DROP TABLE IF EXISTS mouillage;
DROP TABLE IF EXISTS entree;
DROP TABLE IF EXISTS escale;
DROP TABLE IF EXISTS tarif_tranche;
DROP TABLE IF EXISTS tarif;
DROP TABLE IF EXISTS navire;
DROP TABLE IF EXISTS marchandise;
DROP TABLE IF EXISTS port;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- SCHEMA
-- ============================================================================

CREATE TABLE port (
    id_port        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom            VARCHAR(100) NOT NULL,
    type_terminal  VARCHAR(30)  NOT NULL   -- STANDARD | PETROLIER_MOHAMMEDIA | CHARBONNIER_JORF | CHARBONNIER_SAFI
) ENGINE=InnoDB;

CREATE TABLE marchandise (
    id_marchandise BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom            VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE navire (
    id_navire   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    tjb         DOUBLE       NOT NULL,
    longueur    DOUBLE,                    -- L, mètres — requis pour VG (terminaux spéciaux)
    largeur     DOUBLE,                    -- b, mètres
    tirant_eau  DOUBLE,                    -- Te, mètres
    armateur    VARCHAR(150),
    type_navire VARCHAR(20) NOT NULL       -- TRANSBORDEUR | AUTRE
) ENGINE=InnoDB;

-- Tarif : une ligne = un taux pour (opération, type navire, port [NULL = "Autres Ports"/wildcard])
CREATE TABLE tarif (
    id_tarif        BIGINT AUTO_INCREMENT PRIMARY KEY,
    description     VARCHAR(255),
    type_operation  VARCHAR(50) NOT NULL,  -- cf. enum TypeOperation
    type_navire     VARCHAR(20) NOT NULL,  -- TRANSBORDEUR | AUTRE | TOUS
    unite           VARCHAR(20) NOT NULL,  -- TJB_JOUR | VG
    id_port         BIGINT NULL,           -- NULL = tarif par défaut ("Autres Ports", Art 2.1.a / Art 5)
    id_marchandise  BIGINT NULL,
    CONSTRAINT fk_tarif_port        FOREIGN KEY (id_port)        REFERENCES port(id_port),
    CONSTRAINT fk_tarif_marchandise FOREIGN KEY (id_marchandise) REFERENCES marchandise(id_marchandise)
) ENGINE=InnoDB;

CREATE TABLE tarif_tranche (
    id_tarif_tranche BIGINT AUTO_INCREMENT PRIMARY KEY,
    valeur_min       DOUBLE NOT NULL,
    valeur_max       DOUBLE NULL,          -- NULL = pas de plafond (dernière tranche)
    montant          DOUBLE NOT NULL,
    unite            VARCHAR(20),
    id_tarif         BIGINT NOT NULL,
    CONSTRAINT fk_tranche_tarif FOREIGN KEY (id_tarif) REFERENCES tarif(id_tarif)
) ENGINE=InnoDB;

CREATE TABLE escale (
    id_escale         BIGINT AUTO_INCREMENT PRIMARY KEY,
    provenance        VARCHAR(100),
    destination       VARCHAR(100),
    date_entree       DATETIME NOT NULL,
    date_sortie       DATETIME NOT NULL,
    statut            VARCHAR(30) DEFAULT 'EN_COURS',
    id_navire         BIGINT NOT NULL,
    id_port           BIGINT NOT NULL,
    id_entete_facture BIGINT NULL,
    CONSTRAINT fk_escale_navire FOREIGN KEY (id_navire) REFERENCES navire(id_navire),
    CONSTRAINT fk_escale_port   FOREIGN KEY (id_port)   REFERENCES port(id_port)
) ENGINE=InnoDB;

CREATE TABLE entree (
    id_entree        BIGINT AUTO_INCREMENT PRIMARY KEY,
    raison           VARCHAR(60) NULL,     -- cf. enum RaisonEntree, NULL = entrée "normale"
    facturation_next  BOOLEAN NOT NULL DEFAULT TRUE,
    exoneration      BOOLEAN NOT NULL DEFAULT FALSE,
    type_ristourne   VARCHAR(30) NULL,
    id_navire        BIGINT NOT NULL,
    id_escale        BIGINT NOT NULL,
    CONSTRAINT fk_entree_navire FOREIGN KEY (id_navire) REFERENCES navire(id_navire),
    CONSTRAINT fk_entree_escale FOREIGN KEY (id_escale) REFERENCES escale(id_escale)
) ENGINE=InnoDB;

CREATE TABLE mouillage (
    id_mouillage    BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_accostage  DATETIME NOT NULL,     -- fin du mouillage = début du stationnement
    abonnement      BOOLEAN NOT NULL DEFAULT FALSE,
    exoneration     BOOLEAN NOT NULL DEFAULT FALSE,
    type_ristourne  VARCHAR(30) NULL,
    id_navire       BIGINT NOT NULL,
    id_marchandise  BIGINT NULL,
    id_escale       BIGINT NOT NULL,
    CONSTRAINT fk_mouillage_navire      FOREIGN KEY (id_navire)      REFERENCES navire(id_navire),
    CONSTRAINT fk_mouillage_marchandise FOREIGN KEY (id_marchandise) REFERENCES marchandise(id_marchandise),
    CONSTRAINT fk_mouillage_escale      FOREIGN KEY (id_escale)      REFERENCES escale(id_escale)
) ENGINE=InnoDB;

CREATE TABLE stationnement (
    id_stationnement BIGINT AUTO_INCREMENT PRIMARY KEY,
    embarquement     BOOLEAN NOT NULL DEFAULT FALSE,   -- Mohammédia uniquement : true = régime embarquement (Art 2.2)
    abonnement       BOOLEAN NOT NULL DEFAULT FALSE,
    exoneration      BOOLEAN NOT NULL DEFAULT FALSE,
    type_ristourne   VARCHAR(30) NULL,
    id_navire        BIGINT NOT NULL,
    id_marchandise   BIGINT NULL,
    id_escale        BIGINT NOT NULL,
    CONSTRAINT fk_stationnement_navire      FOREIGN KEY (id_navire)      REFERENCES navire(id_navire),
    CONSTRAINT fk_stationnement_marchandise FOREIGN KEY (id_marchandise) REFERENCES marchandise(id_marchandise),
    CONSTRAINT fk_stationnement_escale      FOREIGN KEY (id_escale)      REFERENCES escale(id_escale)
) ENGINE=InnoDB;

CREATE TABLE touchee (
    id_touchees     BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_navire       BIGINT NOT NULL,
    id_port         BIGINT NOT NULL,
    annee           INT NOT NULL,
    nombre_touchees INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_touchee_navire FOREIGN KEY (id_navire) REFERENCES navire(id_navire),
    CONSTRAINT fk_touchee_port   FOREIGN KEY (id_port)   REFERENCES port(id_port),
    CONSTRAINT uk_touchee_navire_port_annee UNIQUE (id_navire, id_port, annee)
) ENGINE=InnoDB;

CREATE TABLE entete_facture (
    id_entete_facture BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero            BIGINT,
    date              DATETIME NOT NULL,
    client            VARCHAR(150),
    total_ht          DOUBLE,
    taux_tva          DOUBLE,
    montant_tva       DOUBLE,
    tr                DOUBLE,
    total_ttc         DOUBLE,
    statut            VARCHAR(30) DEFAULT 'BROUILLON'
) ENGINE=InnoDB;

CREATE TABLE detail_facture (
    id_detail_facture BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_droit        VARCHAR(50),
    description       VARCHAR(255),
    quantite_heure    INT,
    taux_horaire      DOUBLE,
    unite             VARCHAR(20),
    montant_ht        DOUBLE,
    id_entree         BIGINT NULL,
    id_mouillage      BIGINT NULL,
    id_stationnement  BIGINT NULL,
    id_entete_facture BIGINT NOT NULL,
    id_tarif          BIGINT NULL,
    CONSTRAINT fk_detail_entree         FOREIGN KEY (id_entree)         REFERENCES entree(id_entree),
    CONSTRAINT fk_detail_mouillage      FOREIGN KEY (id_mouillage)      REFERENCES mouillage(id_mouillage),
    CONSTRAINT fk_detail_stationnement  FOREIGN KEY (id_stationnement)  REFERENCES stationnement(id_stationnement),
    CONSTRAINT fk_detail_entete         FOREIGN KEY (id_entete_facture) REFERENCES entete_facture(id_entete_facture),
    CONSTRAINT fk_detail_tarif          FOREIGN KEY (id_tarif)          REFERENCES tarif(id_tarif)
) ENGINE=InnoDB;

ALTER TABLE escale
    ADD CONSTRAINT fk_escale_entete_facture FOREIGN KEY (id_entete_facture) REFERENCES entete_facture(id_entete_facture);

-- ============================================================================
-- DONNEES : PORTS
-- ============================================================================
-- Standards : chacun a son propre tarif de stationnement/entrée, sauf ceux qui
-- retombent sur le tarif par défaut "Autres Ports" (id_port NULL côté tarif).
INSERT INTO port (id_port, nom, type_terminal) VALUES
(1, 'Casablanca', 'STANDARD'),
(2, 'Nador',      'STANDARD'),
(3, 'Safi',       'STANDARD'),
(4, 'Agadir',     'STANDARD'),
(5, 'Tan-Tan',    'STANDARD'),
(6, 'Dakhla',     'STANDARD'),
(7, 'Laâyoune',   'STANDARD'),
(8, 'Kenitra',    'STANDARD'),
(9, 'Mehdia',     'STANDARD'),
(10, 'Mohammédia (Terminal Pétrolier)',      'PETROLIER_MOHAMMEDIA'),
(11, 'Jorf-Lasfar (Terminal Charbonnier)',   'CHARBONNIER_JORF'),
(12, 'Safi Atlantique (Terminal Charbonnier)', 'CHARBONNIER_SAFI');

-- ============================================================================
-- DONNEES : MARCHANDISES (Art 2.1.b + Art 6.1)
-- ============================================================================
INSERT INTO marchandise (id_marchandise, nom) VALUES
(1, 'HYDROCARBURES'),
(2, 'BOIS'),
(3, 'SUCRE'),
(4, 'CHARBON'),
(5, 'CLINKER'),
(6, 'AGRUMES & PRIMEURS'),
(7, 'CONTENEURS'),
(8, 'CEREALES'),
(9, 'FERRAILLE');

-- ============================================================================
-- DONNEES : NAVIRES — un jeu couvrant les cas de test utiles :
-- petit/moyen/gros TJB, transbordeur vs autre, dimensions pour calcul VG.
-- ============================================================================
INSERT INTO navire (id_navire, nom, tjb, longueur, largeur, tirant_eau, armateur, type_navire) VALUES
(1, 'MSC ATLAS',        3200,  120, 20, 7.5,  'MSC',                 'AUTRE'),
(2, 'GNV SPLENDID',      8500,  180, 26, 6.8,  'GNV Ferries',         'TRANSBORDEUR'),
(3, 'CMA CGM RABAT',    15500,  260, 32, 11.0, 'CMA CGM',             'AUTRE'),
(4, 'ATLAS PETROLEUM',  42000,  245, 34, 13.5, 'Atlas Shipping',      'AUTRE'),
(5, 'CHARBON EXPRESS',  38000,  230, 32, 12.8, 'Bulk Carriers Intl',  'AUTRE'),
(6, 'PETIT CABOTEUR',     650,   55, 10, 3.5,  'Marocaine de Cabotage','AUTRE'),
(7, 'LIGNE REGULIERE 1', 9800,  150, 24, 7.0,  'Comanav',             'AUTRE');

-- ============================================================================
-- DONNEES : TARIFS — Art 2.1.a Stationnement standard (TJB_JOUR)
-- ============================================================================

-- Tan-Tan / Dakhla / Laâyoune : pas de distinction transbordeur/autre (TOUS)
INSERT INTO tarif (id_tarif, description, type_operation, type_navire, unite, id_port, id_marchandise) VALUES
(1, 'Stationnement Tan-Tan',  'STATIONNEMENT', 'TOUS', 'TJB_JOUR', 5, NULL),
(2, 'Stationnement Dakhla',   'STATIONNEMENT', 'TOUS', 'TJB_JOUR', 6, NULL),
(3, 'Stationnement Laâyoune', 'STATIONNEMENT', 'TOUS', 'TJB_JOUR', 7, NULL),
(4, 'Stationnement Kenitra',  'STATIONNEMENT', 'TOUS', 'TJB_JOUR', 8, NULL),
(5, 'Stationnement Mehdia',   'STATIONNEMENT', 'TOUS', 'TJB_JOUR', 9, NULL),
-- Autres Ports (Casablanca, Nador, Safi, Agadir, etc.) : id_port NULL = wildcard, distingue TRANSBORDEUR/AUTRE
(6, 'Stationnement Autres Ports - Transbordeurs', 'STATIONNEMENT', 'TRANSBORDEUR', 'TJB_JOUR', NULL, NULL),
(7, 'Stationnement Autres Ports - Autres navires', 'STATIONNEMENT', 'AUTRE', 'TJB_JOUR', NULL, NULL);

INSERT INTO tarif_tranche (valeur_min, valeur_max, montant, unite, id_tarif) VALUES
(0, 1000, 0.1088, 'EUR', 1),  (1000, NULL, 0.0599, 'EUR', 1),   -- Tan-Tan
(0, 1000, 0.1088, 'EUR', 2),  (1000, NULL, 0.0599, 'EUR', 2),   -- Dakhla (même groupe que Tan-Tan, Art 2.1.a)
(0, 1000, 0.1088, 'EUR', 3),  (1000, NULL, 0.0599, 'EUR', 3),   -- Laâyoune (même groupe)
(0, 1000, 0.1369, 'EUR', 4),  (1000, NULL, 0.0807, 'EUR', 4),   -- Kenitra
(0, 1000, 0.1369, 'EUR', 5),  (1000, NULL, 0.0807, 'EUR', 5),   -- Mehdia (même groupe que Kenitra)
(0, 1000, 0.1590, 'EUR', 6),  (1000, NULL, 0.0858, 'EUR', 6),   -- Autres Ports, transbordeurs
(0, 1000, 0.1670, 'EUR', 7),  (1000, NULL, 0.0901, 'EUR', 7);   -- Autres Ports, autres navires

-- ============================================================================
-- DONNEES : TARIFS — Art 5 Droit d'entrée (tous ports standards, wildcard id_port NULL)
-- ============================================================================
INSERT INTO tarif (id_tarif, description, type_operation, type_navire, unite, id_port, id_marchandise) VALUES
(8, 'Entrée port - Transbordeurs', 'ENTREE_PORT', 'TRANSBORDEUR', 'TJB_JOUR', NULL, NULL),
(9, 'Entrée port - Autres navires', 'ENTREE_PORT', 'AUTRE', 'TJB_JOUR', NULL, NULL);

INSERT INTO tarif_tranche (valeur_min, valeur_max, montant, unite, id_tarif) VALUES
(0, 1000, 0.1021, 'EUR', 8),  (1000, NULL, 0.0549, 'EUR', 8),   -- transbordeurs
(0, 1000, 0.1072, 'EUR', 9),  (1000, NULL, 0.0576, 'EUR', 9);   -- autres navires

-- ============================================================================
-- DONNEES : TARIFS — Art 2.2 Terminal Pétrolier de Mohammédia (VG en m3)
-- ============================================================================
INSERT INTO tarif (id_tarif, description, type_operation, type_navire, unite, id_port, id_marchandise) VALUES
(10, 'Mohammédia - Entrée débarquement',      'ENTREE_PETROLIER',                     'TOUS', 'VG', 10, 1),
(11, 'Mohammédia - Sortie débarquement',      'SORTIE_PETROLIER',                     'TOUS', 'VG', 10, 1),
(12, 'Mohammédia - Séjour >72h (EUR/heure)',  'SEJOUR_PETROLIER',                     'TOUS', 'VG', 10, 1),
(13, 'Mohammédia - Stationnement embarquement', 'STATIONNEMENT_EMBARQUEMENT_PETROLIER','TOUS', 'TJB_JOUR', 10, 1);

INSERT INTO tarif_tranche (valeur_min, valeur_max, montant, unite, id_tarif) VALUES
(0, 100000, 0.1926, 'EUR', 10), (100000, NULL, 0.2477, 'EUR', 10),  -- entrée
(0, 100000, 0.0957, 'EUR', 11), (100000, NULL, 0.1227, 'EUR', 11),  -- sortie
(0, NULL, 434.90, 'EUR', 12),                                       -- séjour, taux horaire fixe
(0, 1000, 0.1277, 'EUR', 13), (1000, 5000, 0.0688, 'EUR', 13), (5000, NULL, 0.0289, 'EUR', 13); -- embarquement, 3 tranches

-- ============================================================================
-- DONNEES : TARIFS — Art 2.3 Terminal Charbonnier de Jorf-Lasfar (VG en m3, taux fixes)
-- ============================================================================
INSERT INTO tarif (id_tarif, description, type_operation, type_navire, unite, id_port, id_marchandise) VALUES
(14, 'Jorf-Lasfar - Entrée', 'ENTREE_CHARBON_JORF', 'TOUS', 'VG', 11, 4),
(15, 'Jorf-Lasfar - Sortie', 'SORTIE_CHARBON_JORF', 'TOUS', 'VG', 11, 4),
(16, 'Jorf-Lasfar - Séjour journalier', 'SEJOUR_CHARBON_JORF', 'TOUS', 'VG', 11, 4);

INSERT INTO tarif_tranche (valeur_min, valeur_max, montant, unite, id_tarif) VALUES
(0, NULL, 0.1588, 'EUR', 14),
(0, NULL, 0.0800, 'EUR', 15),
(0, NULL, 0.0522, 'EUR', 16);

-- ============================================================================
-- DONNEES : TARIFS — Art 2.4 Terminal Charbonnier du Port de Safi Atlantique
-- ============================================================================
INSERT INTO tarif (id_tarif, description, type_operation, type_navire, unite, id_port, id_marchandise) VALUES
(17, 'Safi Atlantique - Entrée', 'ENTREE_CHARBON_SAFI', 'TOUS', 'VG', 12, 4),
(18, 'Safi Atlantique - Sortie', 'SORTIE_CHARBON_SAFI', 'TOUS', 'VG', 12, 4),
(19, 'Safi Atlantique - Séjour journalier', 'SEJOUR_CHARBON_SAFI', 'TOUS', 'VG', 12, 4);

INSERT INTO tarif_tranche (valeur_min, valeur_max, montant, unite, id_tarif) VALUES
(0, NULL, 0.1748, 'EUR', 17),
(0, NULL, 0.0875, 'EUR', 18),
(0, NULL, 0.0574, 'EUR', 19);

-- ============================================================================
-- DONNEES : ESCALES — un scénario par cas testé dans le moteur
-- ============================================================================
INSERT INTO escale (id_escale, provenance, destination, date_entree, date_sortie, statut, id_navire, id_port) VALUES
-- 1. Cas simple : stationnement standard, Casablanca, "Autres navires", 4 jours
(1, 'Marseille', 'Casablanca', '2026-03-01 08:00:00', '2026-03-05 14:00:00', 'FACTUREE', 1, 1),
-- 2. Transbordeur à Tan-Tan (taux TOUS, pas de distinction navire)
(2, 'Las Palmas', 'Tan-Tan', '2026-03-10 06:00:00', '2026-03-11 18:00:00', 'FACTUREE', 2, 5),
-- 3. Escale avec mouillage avant accostage (Casablanca) : arrivée 00h, accostage 14h le lendemain
(3, 'Le Havre', 'Casablanca', '2026-04-01 00:00:00', '2026-04-06 10:00:00', 'FACTUREE', 3, 1),
-- 4. Terminal Pétrolier Mohammédia, débarquement, VG < 100 000 m3, séjour < 72h (pas de supplément)
(4, 'Rotterdam', 'Mohammédia', '2026-02-01 09:00:00', '2026-02-03 09:00:00', 'FACTUREE', 4, 10),
-- 5. Terminal Pétrolier Mohammédia, débarquement, séjour > 72h (déclenche le supplément horaire)
(5, 'Fos-sur-Mer', 'Mohammédia', '2026-02-10 09:00:00', '2026-02-14 15:00:00', 'FACTUREE', 4, 10),
-- 6. Terminal Pétrolier Mohammédia, embarquement (stationnement TJB progressif)
(6, 'Mohammédia', 'Alger', '2026-02-20 07:00:00', '2026-02-22 07:00:00', 'FACTUREE', 4, 10),
-- 7. Terminal Charbonnier Jorf-Lasfar
(7, 'Newcastle', 'Jorf-Lasfar', '2026-03-15 05:00:00', '2026-03-18 20:00:00', 'FACTUREE', 5, 11),
-- 8. Terminal Charbonnier Safi Atlantique
(8, 'Richards Bay', 'Safi Atlantique', '2026-03-20 05:00:00', '2026-03-22 11:00:00', 'FACTUREE', 5, 12),
-- 9. Petit caboteur, Dakhla (test ristourne A - caboteurs, hors hydrocarbures = 60%)
(9, 'Agadir', 'Dakhla', '2026-04-05 10:00:00', '2026-04-06 22:00:00', 'FACTUREE', 6, 6),
-- 10. Navire à touchées répétées pour tester ristourne B/C (à combiner avec la table touchee ci-dessous)
(10, 'Barcelone', 'Casablanca', '2026-05-01 08:00:00', '2026-05-03 08:00:00', 'FACTUREE', 7, 1),
-- 11. Test abonnement (Art 3) : séjour de 60 jours couvrant 2 mois calendaires complets à Nador
(11, 'Alger', 'Nador', '2026-06-01 00:00:00', '2026-07-31 00:00:00', 'FACTUREE', 1, 2);

-- ============================================================================
-- DONNEES : ENTREES (Art 5) — une par escale standard, avec un cas de non-refacturation
-- ============================================================================
INSERT INTO entree (raison, facturation_next, exoneration, type_ristourne, id_navire, id_escale) VALUES
(NULL, TRUE, FALSE, NULL, 1, 1),                                        -- entrée normale, facturée
(NULL, TRUE, FALSE, 'D_TRANSBORDEURS', 2, 2),                           -- transbordeur, ristourne D 67%
(NULL, TRUE, FALSE, NULL, 3, 3),
('RELACHE', FALSE, FALSE, NULL, 3, 3),                                  -- 2e entrée même escale, non refacturée (Art 5)
(NULL, TRUE, FALSE, 'A_CABOTEURS', 6, 9),
(NULL, TRUE, FALSE, 'C_LIGNES_REGULIERES', 7, 10),
(NULL, TRUE, FALSE, NULL, 1, 11);

-- ============================================================================
-- DONNEES : MOUILLAGE — uniquement l'escale 3 (navire passé par le mouillage)
-- ============================================================================
INSERT INTO mouillage (date_accostage, abonnement, exoneration, type_ristourne, id_navire, id_marchandise, id_escale) VALUES
('2026-04-02 14:00:00', FALSE, FALSE, NULL, 3, NULL, 3);   -- 38h de mouillage avant accostage

-- ============================================================================
-- DONNEES : STATIONNEMENT
-- ============================================================================
INSERT INTO stationnement (embarquement, abonnement, exoneration, type_ristourne, id_navire, id_marchandise, id_escale) VALUES
(FALSE, FALSE, FALSE, NULL,                    1, NULL, 1),   -- standard
(FALSE, FALSE, FALSE, 'D_TRANSBORDEURS',       2, NULL, 2),   -- transbordeur
(FALSE, FALSE, FALSE, NULL,                    3, NULL, 3),   -- standard, après mouillage
(FALSE, FALSE, FALSE, NULL,                    4, 1,    4),   -- Mohammédia débarquement, séjour < 72h
(FALSE, FALSE, FALSE, NULL,                    4, 1,    5),   -- Mohammédia débarquement, séjour > 72h
(TRUE,  FALSE, FALSE, NULL,                    4, 1,    6),   -- Mohammédia embarquement
(FALSE, FALSE, FALSE, NULL,                    5, 4,    7),   -- Jorf-Lasfar
(FALSE, FALSE, FALSE, NULL,                    5, 4,    8),   -- Safi Atlantique
(FALSE, FALSE, FALSE, 'A_CABOTEURS',           6, 6,    9),   -- caboteur, bois (ristourne A 60%)
(FALSE, FALSE, FALSE, 'C_LIGNES_REGULIERES',   7, NULL, 10),  -- ligne régulière
(FALSE, TRUE,  FALSE, NULL,                    1, NULL, 11);  -- abonnement, 60 jours à Nador (2 mois calendaires complets)

-- ============================================================================
-- DONNEES : TOUCHEES — pour tester les ristournes B et C (navire 7 à Casablanca)
-- ============================================================================
INSERT INTO touchee (id_navire, id_port, annee, nombre_touchees) VALUES
(1, 1, 2026, 55),   -- navire 1 à Casablanca : 55 touchées 2026 -> éligible ristourne B (>50)
(7, 1, 2026, 14);   -- navire 7 à Casablanca : 14 touchées 2026 -> éligible ristourne C (12-35 -> 30%)

-- ============================================================================
-- NOTE IMPORTANTE
-- ============================================================================
-- Ce script couvre uniquement les opérations que le moteur de calcul sait
-- traiter aujourd'hui (Art 2.1.a, 2.2, 2.3, 2.4, 4, 5, ristournes, abonnement).
-- Il NE contient PAS les minimums de perception de l'Art 2.1.b (conteneurs,
-- trafics >= 80% de la cargaison par marchandise/port) : cette règle n'a pas
-- encore de calculateur dédié dans le moteur, donc l'ajouter ici créerait des
-- données orphelines. À prévoir si tu implémentes cette partie de l'article.