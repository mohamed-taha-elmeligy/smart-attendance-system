package com.emts.smart_attendance_system.entities;
import com.emts.smart_attendance_system.auditing.AuditableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * *******************************************************************
 * File: Role.java
 * Package: com.emts.smart_attendance_system.entities
 * Project: eMTS Smart Attendance System
 * © 2025 Mohamed Taha Elmeligy - eMTS (e Modern Tech Solutions)
 * This file is part of the eMTS Smart Attendance System.
 * Created on: 24/10/2025
 * Port Number: 8083
 * *******************************************************************
 */

@Table(name = "roles")
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false,of = "roleId")
@NoArgsConstructor
public class Role extends AuditableEntity {

    // ===== Attributes =====

    @Id
    @ToString.Include
    @Column(value = "role_id")
    private UUID roleId;

    @Setter
    @ToString.Include
    @Column(value = "name")
    private String name;

    @Setter
    @Column(value = "description")
    private String description;

    @Setter
    @ToString.Include
    @Column(value = "soft_delete")
    private boolean softDelete = false;

    // ===== Builder =====
    @Builder
    public Role (@NonNull String name,
                 @NonNull String description){
        this.name = name;
        this.description = description;
        this.softDelete = false;
    }


    // ===== Helper Methods for Soft Delete =====
    public String getRoleDeleteStatus() {
        return softDelete
                ? "Role Deleted: " + roleId
                : "Role Undeleted: " + roleId;
    }

    public boolean isDeleted() {
        return softDelete;
    }

    public Role undelete() {
        this.softDelete = false;
        return this;
    }

    public Role delete() {
        this.softDelete = true;
        return this;
    }
}
