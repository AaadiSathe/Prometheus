package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entity.Reminder;


public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByUserId(int userId);
}