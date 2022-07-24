INSERT INTO skills (id, name, skill_category)
values
(1, 'Java', 'PROGRAMMING_LANGUAGE'),
(2, 'Design Patterns', 'PROGRAMMING_LANGUAGE'),
(3, 'IDE', 'PROGRAMMING_LANGUAGE'),
(4, 'Project tools', 'JAVA'),
(5, 'Collections', 'JAVA'),
(6, 'Concurrency', 'JAVA'),
(7, 'IO', 'JAVA'),
(8, 'JAVA 8 features', 'JAVA'),
(9, 'Frameworks', 'JAVA'),
(10, 'Desktop applications', 'JAVA'),
(11, 'Testing', 'JAVA'),
(12, 'Database', 'JAVA'),
(13, 'Utility Libraries', 'JAVA');

INSERT INTO technologies (name, description)
VALUES
('Eclipse', ''),
('IDEA', ''),
('Netbeans', ''),
('Maven', ''),
('Gradle', ''),
('ANT', ''),
('Lambdas', ''),
('Stream API', ''),
('Time API', ''),
('Optional', ''),
('Java NIO', ''),
('File IO', ''),
('Networking IO', ''),
('Collection IF', ''),
('Map IF', ''),
('Junit5', ''),
('AssertJ', ''),
('Mockito', ''),
('Selenium', ''),
('JDBC', ''),
('Spring Data JPA', ''),
('MySQL', ''),
('PostgreSQL', ''),
('H2', ''),
('SpringBoot', ''),
('Docker', ''),
('JavaFX', ''),
('Swing', '');

INSERT INTO skills_technologies (skill_id, technology_id)
VALUES
(4, 4),
(4, 5),
(4, 6);

INSERT INTO keywords VALUES
(4, 'POM XML'),
(4, 'Maven Repo');