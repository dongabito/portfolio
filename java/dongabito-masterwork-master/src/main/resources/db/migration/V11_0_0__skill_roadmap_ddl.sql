CREATE TABLE IF NOT EXISTS skills_roadmaps
(
    `skill_id`           INT NOT NULL,
    `roadmap_id`      INT NOT NULL,
    CONSTRAINT pk_skills_roadmaps PRIMARY KEY (`skill_id`, `roadmap_id`),
    CONSTRAINT fk_skills_roadmaps FOREIGN KEY (`skill_id`) REFERENCES skills(`id`),
    CONSTRAINT fk_roadmaps_skills FOREIGN KEY (`roadmap_id`) REFERENCES roadmaps(`id`)
    );