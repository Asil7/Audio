package com.example.book.repository;


import com.example.book.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query(nativeQuery = true, value = "select * from users where role = 'admin'")
    User findAdmin();
}
