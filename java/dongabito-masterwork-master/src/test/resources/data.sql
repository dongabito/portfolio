DELETE FROM skills_programmers;
DELETE FROM skills_roadmaps;
DELETE FROM skills_technologies;
DELETE FROM progresses;
DELETE FROM keywords;
DELETE FROM roadmaps;
DELETE FROM social_presence;
DELETE FROM technologies;
DELETE FROM skills;
DELETE FROM programmers;
DELETE FROM cvs;

INSERT INTO cvs (id, title, content, image_source)
VALUES
    (1, 'Az én életem...', 'jó', 'http://...');

INSERT INTO programmers (id, first_name, last_name, email, password, created_at, cv_id, role)
VALUES
    (1, 'John', 'Doe', 'foo@example.org', '$2a$12$I2xWhRND3kfarNZX4KWTZujdnRNzULzOU2WifmFftAgJalRGzUtje', current_timestamp, 1,  'ROLE_USER'),
    (2, 'Gyula', 'Vitéz', 'foo@example.com', 'password', current_timestamp, 1,  'ROLE_USER');

INSERT INTO skills (id, name, skill_category)
VALUES
    (1, 'Java', 'PROGRAMMING_LANGUAGE'),
    (2, 'Design Patterns', 'PROGRAMMING_LANGUAGE'),
    (3, 'IDE', 'PROGRAMMING_LANGUAGE'),
    (4, 'Project tools', 'JAVA');

INSERT INTO skills_programmers (skill_id, programmer_id)
VALUES
    (1, 1),
    (2, 1),
    (2, 2),
    (3, 2);

INSERT INTO technologies (id, name, description)
VALUES
    (1, 'Eclipse', ''),
    (2, 'IDEA', ''),
    (3, 'Netbeans', ''),
    (4, 'Maven', ''),
    (5, 'Gradle', ''),
    (6, 'ANT', '');

INSERT INTO skills_technologies (skill_id, technology_id)
VALUES
    (1, 1),
    (1, 5),
    (1, 3),
    (1, 6),
    (2, 3),
    (2, 4);

INSERT INTO keywords VALUES
                         (4, 'POM XML'),
                         (4, 'Maven Repo');

INSERT INTO roadmaps VALUES
                         (1, 'Java Roadmap', 'Roadmap to learn Java basics', 1),
                         (2, 'Java Roadmap advanced ', 'Roadmap to learn Java', 1);

INSERT INTO skills_roadmaps (skill_id, roadmap_id)
VALUES
    (1, 1),
    (1, 2);

INSERT INTO progresses VALUES
                         (2, 'BEGINNER', 'Bla-Bla', current_timestamp, 3, 1),
                         (3, 'BEGINNER', 'Bla-Bla', current_timestamp, 3, 2),
                         (4, 'BEGINNER', 'Bla-Bla', current_timestamp, 5, 2),
                         (5, 'BEGINNER', 'Bla-Bla', current_timestamp, 5, 1);