package com.learnway.study.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface StudyTagRepository extends JpaRepository<StudyTag, Long>,StudyTagRepositoryCustom {
	
	List<StudyTag> findByStudyPostid(int postid);
	
}
