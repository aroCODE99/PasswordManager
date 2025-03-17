package com.aro.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Column(name = "roleName")
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;
    // so i, can see this is the major cause of the problem correct
    // so i, want their mapping to be uni-directional
    // Hibernate: select u1_0.user_id,u1_0.email,u1_0.password,u1_0.version from users u1_0 where u1_0.email=? // this is for user
    // Hibernate: select p1_0.password_id,p1_0.created_at,p1_0.password,p1_0.url,p1_0.user_id from passwords p1_0 where p1_0.user_id=? // password_manager
    // Hibernate: select r1_0.user_user_id,r1_0.role_id,r1_0.role_name from user_roles r1_0 where r1_0.user_user_id=? // this is for role

    public UserRoles() {
    }

    public UserRoles(String name) {
        this.roleName = name;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}