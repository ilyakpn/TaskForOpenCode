package com.test.taskopencode.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//
//        if (o == null || getClass() != o.getClass())
//            return false;
//
//        User user = (User) o;
//        return Objects.equals(id, user.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, email, password, role, status);
//    }
}
