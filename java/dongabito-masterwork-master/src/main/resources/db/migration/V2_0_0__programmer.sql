CREATE TABLE IF NOT EXISTS programmers
(
    `id`                    INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `first_name`            VARCHAR(100),
    `last_name`             VARCHAR(100),
    `email`                 VARCHAR(100) NOT NULL UNIQUE,
    `password`              VARCHAR(100),
    `created_at`            TIMESTAMP NULL,
    `last_login`            TIMESTAMP NULL,
    `cv_id`                 INT,
    `role`                  VARCHAR (100),

    CONSTRAINT fk_programmers_cvs FOREIGN KEY (`cv_id`) REFERENCES cvs (`id`)
    );