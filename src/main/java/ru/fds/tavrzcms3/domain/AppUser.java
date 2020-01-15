package ru.fds.tavrzcms3.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "db_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    private long userId;

    @NotBlank
    @Column(name ="name")
    private String name;

    @NotBlank
    @Column(name ="password")
    private String password;

    @OneToOne(mappedBy = "appUser")
    private Employee employee;

    @Singular
    @ManyToMany()
    @JoinTable(name = "db_user_role", joinColumns = @JoinColumn(name ="user_id"), inverseJoinColumns = @JoinColumn(name ="role_id"))
    private List<AppRole> appRoles;




    @Override
    public String toString() {
        return "AppUser{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
