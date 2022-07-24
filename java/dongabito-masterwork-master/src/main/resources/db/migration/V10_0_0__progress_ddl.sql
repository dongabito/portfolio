CREATE TABLE IF NOT EXISTS progresses
(
    `id`              INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `progress`        VARCHAR (100),
    `details`            VARCHAR(100) NOT NULL,
    `started_at`            TIMESTAMP NULL,
    `technology_id`      INT NULL,
    `roadmap_id`      INT NULL,

    CONSTRAINT fk_roadmap_technology FOREIGN KEY (`technology_id`) REFERENCES technologies (`id`),
    CONSTRAINT fk_roadmap_progress FOREIGN KEY (`roadmap_id`) REFERENCES roadmaps (`id`),
    CONSTRAINT progresses_UN UNIQUE KEY (technology_id,roadmap_id)
    );