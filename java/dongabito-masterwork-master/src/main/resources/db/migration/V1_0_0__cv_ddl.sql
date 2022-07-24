CREATE TABLE IF NOT EXISTS cvs
(
    `id`            INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title`         VARCHAR(100),
    `content`       TEXT,
    `image_source`  VARCHAR (100)
    );
