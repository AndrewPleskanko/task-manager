package org.example.authenticationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project extends AbstractEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<UserProjectAudit> userProjectAudits;

}

