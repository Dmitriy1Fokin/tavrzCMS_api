package ru.fds.tavrzcms3.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "db_role")
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="role_id")
    private long roleId;

    @Column(name ="name")
    private String name;

    @ManyToMany
    @JoinTable(name = "db_user_role", joinColumns = @JoinColumn(name ="role_id"), inverseJoinColumns = @JoinColumn(name ="user_id"))
    private List<AppUser> appUsers;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AppUser> getAppUsers() {
        return appUsers;
    }

    public void setAppUsers(List<AppUser> appUsers) {
        this.appUsers = appUsers;
    }

    @Override
    public String toString() {
        return "AppRole{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }
}
