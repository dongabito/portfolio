package com.gfa.portfoliohub.util;

import com.gfa.portfoliohub.models.entities.CurriculumVitae;
import com.gfa.portfoliohub.models.entities.LearningProgress;
import com.gfa.portfoliohub.models.entities.Programmer;
import com.gfa.portfoliohub.models.entities.ProgressInTechnology;
import com.gfa.portfoliohub.models.entities.Roadmap;
import com.gfa.portfoliohub.models.entities.Skill;
import com.gfa.portfoliohub.models.entities.SkillCategory;
import com.gfa.portfoliohub.models.entities.Technology;
import com.gfa.portfoliohub.models.entities.UserRole;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class TestEntities {

  public static int random(int from, int to) {
    return (int) ((Math.random() * (from - to)) + from);
  }

  public static int randomId() {
    return (int) random(9000, 10000);
  }

  public static Programmer defaultProgrammer() {
    return Programmer.builder()
        .id(randomId())
        .firstName("John")
        .lastName("Doe")
        .email("foo@example.hu")
        .password("password")
        .createdAt(Instant.now())
        .lastLogin(Instant.now())
        .cv(defaultCV())
        .skills(new ArrayList<>(Arrays.asList(defaultSkill())))
        .role(UserRole.ROLE_USER)
        .roadmaps(Arrays.asList(defaultRoadmap()))
        .build();
  }

  public static Skill defaultSkill() {
    return Skill.builder()
        .id(randomId())
        .name("Java8")
        .skillCategory(SkillCategory.PROGRAMMING_LANGUAGE)
        .technologies(new ArrayList<>(Arrays.asList(defaultTechnology())))
        .programmer(new ArrayList<>())
        .roadmaps(new ArrayList<>())
        .build();
  }

  public static Technology defaultTechnology() {
    return Technology.builder()
        .id(randomId())
        .name("Testing")
        .description("JUNIT5, AssertJ, Integration test")
        .keyWord(new HashSet<String>() {{
          add("Junit5");
        }})
        .skills(new ArrayList<>())
        .build();
  }


  public static CurriculumVitae defaultCV() {
    return CurriculumVitae.builder()
        .id(randomId())
        .title("Az én éeletem...")
        .content("jó")
        .imageSource("http://...")
        .socialPresence(new HashMap<String, String>() {{
          put("GitHub", "http://github.com");
        }})
        .build();
  }

  public static Roadmap defaultRoadmap() {
    return Roadmap.builder()
        .id(randomId())
        .name("Java roadmap")
        .details("bla-bla")
        .skills(new ArrayList<>(Arrays.asList(defaultSkill())))
        .progresses(new ArrayList<>())
        .build();
  }

  public static ProgressInTechnology defaultProgress() {
    return ProgressInTechnology.builder()
        .id(randomId())
        .progress(LearningProgress.BEGINNER)
        .details("bla-bla")
        .startedAt(Instant.now())
        .build();
  }

  public static Authentication defaultAuthentication(Programmer programmer) {
    return new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(UserRole.ROLE_USER.getAuthority()));
  }

  public static Authentication defaultAuthentication(Programmer programmer, UserRole authority) {
    return new UsernamePasswordAuthenticationToken(programmer, null,
        Arrays.asList(authority.getAuthority()));
  }

  public static Programmer dbProgrammer() {
    return Programmer.builder()
        .id(1)
        .firstName("John")
        .lastName("Doe")
        .email("foo@example.org")
        .password("password")
        .role(UserRole.ROLE_USER)
        .roadmaps(new ArrayList<>())
        .skills(new ArrayList<>())
        .lastLogin(Instant.now())
        .createdAt(Instant.now())
        .build();
  }

  public static Roadmap dbRoadmap() {
    return Roadmap.builder()
        .id(1)
        .name("Java Roadmap")
        .details("Roadmap to learn Java basics")
        .skills(new ArrayList<>())
        .progresses(new ArrayList<>())
        .build();
  }

  public static Skill dbSkill() {
    return Skill.builder()
        .id(1)
        .name("Java")
        .skillCategory(SkillCategory.PROGRAMMING_LANGUAGE)
        .technologies(new ArrayList<>())
        .programmer(new ArrayList<>())
        .roadmaps(new ArrayList<>())
        .build();
  }

  public static Technology dbTechnology() {
    return Technology.builder()
        .id(1)
        .name("Eclipse")
        .description("bla-bla")
        .skills(new ArrayList<>())
        .keyWord(new HashSet<>())
        .build();
  }

  public static ProgressInTechnology dbProgress() {
    return ProgressInTechnology.builder()
        .id(2)
        .progress(LearningProgress.BEGINNER)
        .details("bla-bla")
        .technology(dbTechnology())
        .roadmap(dbRoadmap())
        .build();
  }

}
