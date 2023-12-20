package com.project.tinkoff.repository;

import com.project.tinkoff.repository.models.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    Optional<ProjectMember> findProjectMemberByProjectIdAndUserId(long projectId, long userId);
}
