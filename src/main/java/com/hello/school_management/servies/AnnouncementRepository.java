package com.hello.school_management.servies;

import com.hello.school_management.Models.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // Additional query methods if necessary
}
