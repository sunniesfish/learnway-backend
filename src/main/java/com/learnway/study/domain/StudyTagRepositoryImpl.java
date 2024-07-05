package com.learnway.study.domain;


import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public class StudyTagRepositoryImpl implements StudyTagRepositoryCustom {

    @PersistenceContext
    private  EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<StudyTag> findByTag(List<String> tags) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<StudyTag> query = cb.createQuery(StudyTag.class);
        Root<StudyTag> root = query.from(StudyTag.class);

        List<Predicate> predicates = new ArrayList<>();
        for (String tag : tags) {
            predicates.add(cb.like(root.get("tag"), "%" + tag + "%"));
        }

        // 모든 조건이 충족되는 경우를 위해 AND로 결합
        query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
    
//    @Override
//    @Transactional(readOnly = true)
//    public List<StudyTag> findByTag(List<String> tags) {
//        // 태그들을 콤마로 연결하여 하나의 문자열로 만듭니다.
//        String tagPattern = String.join(",%", tags) + ",%";
//
//        // 네이티브 SQL 쿼리 작성
//        String sql = "SELECT * FROM study_tag WHERE tag LIKE :tagPattern";
//        
//        // 쿼리 실행	
//        return entityManager.createNativeQuery(sql, StudyTag.class)
//                            .setParameter("tagPattern", "%" + tagPattern)
//                            .getResultList();
//    }
}