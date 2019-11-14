package ru.fds.tavrzcms3.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "db_role")
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="role_id")
    private long roleId;

    @Column(name ="name")
    private String name;

    @Singular
    @ManyToMany
    @JoinTable(name = "db_user_role", joinColumns = @JoinColumn(name ="role_id"), inverseJoinColumns = @JoinColumn(name ="user_id"))
    private List<AppUser> appUsers;



    @Override
    public String toString() {
        return "AppRole{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }
}
