package com.casework.hmcts.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casework.hmcts.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
   
    List<Task> findByStatus(String status);

    List<Task> findByDueDateTimeBefore(LocalDateTime date);
    
}
