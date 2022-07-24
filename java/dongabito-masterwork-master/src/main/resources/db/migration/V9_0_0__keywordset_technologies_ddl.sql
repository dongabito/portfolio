CREATE TABLE IF NOT EXISTS keywords
(
    `technology_id`                 INT NOT NULL ,
    `keyword`                  VARCHAR(100) UNIQUE,

    FOREIGN KEY (technology_id) REFERENCES technologies(id)
    );