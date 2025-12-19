package com.example.demo.infrastructure.persistence.user;

import com.example.demo.domain.user.model.Email;
import com.example.demo.domain.user.model.Password;
import com.example.demo.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String country;

    @Column
    private String phoneNumber;

    private UserEntity(Long id, String email, String password, String country, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }


     //Domain -> Entity 변환

    public static UserEntity from(User user) {
        return new UserEntity(
                user.getId(),
                user.getEmail().value(),
                user.getPassword().value(),
                user.getCountry(),
                user.getPhoneNumber()
        );
    }


     //Entity -> Domain 변환

    public User toDomain() {
        return User.reconstitute(
                this.id,
                new Email(this.email),
                new Password(this.password),
                this.country,
                this.phoneNumber
        );
    }
}