CREATE TABLE IF NOT EXISTS social_presence
(
    `cv_id`                 INT NOT NULL ,
    `provider`                VARCHAR(50),
    `link`                    VARCHAR(50),
    FOREIGN KEY (`cv_id`) REFERENCES cvs(`id`)
    );