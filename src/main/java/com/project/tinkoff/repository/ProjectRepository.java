package com.project.tinkoff.repository;

import com.project.tinkoff.repository.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = """
            SELECT COUNT(*)<>0
            FROM projects
            WHERE id = :#{#projectId}
            """, nativeQuery = true)
    boolean isProjectExist(long projectId);
}
