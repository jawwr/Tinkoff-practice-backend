package com.project.tinkoff.repository;

import com.project.tinkoff.repository.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = """
            SELECT projects.*
            FROM project_members
            JOIN projects
                ON projects.id = project_members.project_id
            WHERE project_members.user_id = :#{#userId}
            """, nativeQuery = true)
    List<Project> findAllUsersProjects(long userId);
}
