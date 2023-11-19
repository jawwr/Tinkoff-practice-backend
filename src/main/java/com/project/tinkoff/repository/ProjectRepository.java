package com.project.tinkoff.repository;

import com.project.tinkoff.repository.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
