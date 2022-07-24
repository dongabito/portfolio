CREATE TABLE IF NOT EXISTS roadmaps
(
    `id`              INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`            VARCHAR(100) NOT NULL,
    `details`            VARCHAR(100) NULL,
    `programmer_id`      INT NULL,

    CONSTRAINT fk_programmer_roadmap FOREIGN KEY (`programmer_id`) REFERENCES programmers (`id`)
    );