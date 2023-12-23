package com.project.tinkoff.repository;

import com.project.tinkoff.repository.models.ProjectInviteLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectInviteLinkRepository extends JpaRepository<ProjectInviteLink, Long> {
    @Query(value = """
            SELECT *
            FROM project_invite_links
            WHERE link = :#{#link}
              and expire_time > now();
            """, nativeQuery = true)
    Optional<ProjectInviteLink> findProjectInviteLinkByLink(String link);
}
