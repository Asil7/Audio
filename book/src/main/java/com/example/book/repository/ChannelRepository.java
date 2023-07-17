package com.example.book.repository;

import com.example.book.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ChannelRepository extends JpaRepository<Channel,Integer> {
    Channel findByName(String name);

    void deleteByName(String text);

}
