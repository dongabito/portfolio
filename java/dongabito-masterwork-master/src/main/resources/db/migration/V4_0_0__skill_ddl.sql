CREATE TABLE IF NOT EXISTS skills
(
    `id`                INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`              VARCHAR(100) NOT NULL UNIQUE,
    `skill_category`    VARCHAR(100),
    programmer_id       INT,
    `roadmap_id`        INT,

    CONSTRAINT fk_roadmap_skill FOREIGN KEY (`roadmap_id`) REFERENCES roadmaps (`id`),
    CONSTRAINT fk_programmer_skill FOREIGN KEY (`programmer_id`) REFERENCES programmers(`id`)
);