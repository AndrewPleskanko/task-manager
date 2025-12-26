package org.example.authenticationservice.repository;

import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.entity.UserProjectAudit;
import org.example.authenticationservice.entity.UserProjectAuditId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProjectAuditRepository extends JpaRepository<UserProjectAudit, UserProjectAuditId> {
    List<UserProjectAudit> findByUser(User user);
}

