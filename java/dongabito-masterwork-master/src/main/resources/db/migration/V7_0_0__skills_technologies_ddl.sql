CREATE TABLE IF NOT EXISTS skills_technologies
(
    `skill_id`           INT NOT NULL,
    `technology_id`      INT NOT NULL,
    CONSTRAINT pk_skills_tehcnologies PRIMARY KEY (`skill_id`, `technology_id`),
    CONSTRAINT fk_skills FOREIGN KEY (`skill_id`) REFERENCES skills(`id`),
    CONSTRAINT fk_technologies FOREIGN KEY (`technology_id`) REFERENCES technologies(`id`)
    );
