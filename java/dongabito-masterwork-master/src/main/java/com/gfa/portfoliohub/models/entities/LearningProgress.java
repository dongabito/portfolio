package com.gfa.portfoliohub.models.entities;

public enum LearningProgress {
  UNKNOWN(0) {
    public String getProgress() {
      return "width: " + getLevel() + "%";
    }
  },
  BEGINNER(20) {
    public String getProgress() {
      return "width: " + getLevel() + "%";
    }
  },
  BASIC(33) {
    public String getProgress() {
      return "width: " + getLevel() + "%";
    }
  },
  INTERMEDIATE(50) {
    public String getProgress() {
      return "width: " + getLevel() + "%";
    }
  },
  ADVANCED(80) {
    public String getProgress() {
      return "width: " + getLevel() + "%";
    }
  },
  PROFESSIONAL(100) {
    public String getProgress() {
      return "width: " + getLevel() + "%";
    }
  };

  private Integer level;

  LearningProgress(Integer level) {
    this.level = level;
  }

  public Integer getLevel() {
    return level;
  }

  public abstract String getProgress();
}
