CREATE TABLE IF NOT EXISTS technologies
(
    `id`            INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`          VARCHAR(255) NOT NULL UNIQUE,
    `description`   TEXT
);

