package com.learnway.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetUniRepository extends JpaRepository<TargetUni, Long> {
}
