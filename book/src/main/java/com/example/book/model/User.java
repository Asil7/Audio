package com.example.book.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "users")
public class User {

    public User(Long id, String firstName, String role) {
        this.id = id;
        this.firstName = firstName;
        this.role = role;

    }

    public User setStep(int step) {
        this.step = step;
        return this;
    }


    @Id
    private Long id;
    private int step = 0;
    private String firstName;
    private String phoneNumber;
    private String languageCode;
    private String role;
    private String channelLink;


    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;
}
