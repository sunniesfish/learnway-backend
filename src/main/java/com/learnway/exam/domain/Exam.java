package com.learnway.exam.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @Column(name = "member_id", nullable = false)
    private Long memId;

    @Column(name = "exam_name", nullable = false)
    private String examName;

    @Column(name = "exam_type", nullable = false)
    private String examType;

    @Column(name = "exam_range", nullable = true)
    private String examRange;

    @Column(name = "exam_date", nullable = false)
    private Date examDate;

    @Column(name = "exam_memo", nullable = true)
    private String examMemo;

    @Builder
    public Exam(Long examId, Long memId, String examName, String examType, String examRange, Date examDate, String examMemo) {
        this.examId = examId;
        this.memId = memId;
        this.examName = examName;
        this.examType = examType;
        this.examRange = examRange;
        this.examDate = examDate;
        this.examMemo = examMemo;
    }
}
