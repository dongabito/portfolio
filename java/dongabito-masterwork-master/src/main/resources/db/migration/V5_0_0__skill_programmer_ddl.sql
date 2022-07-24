CREATE TABLE IF NOT EXISTS skills_programmers
(
    `skill_id`           INT NOT NULL,
    `programmer_id`      INT NOT NULL,
    CONSTRAINT pk_skills_programmers PRIMARY KEY (`skill_id`, `programmer_id`),
    CONSTRAINT fk_skills_programmers FOREIGN KEY (`skill_id`) REFERENCES skills(`id`),
    CONSTRAINT fk_programmers_skills FOREIGN KEY (`programmer_id`) REFERENCES programmers(`id`)
    );