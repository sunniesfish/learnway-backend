package com.learnway.study.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.learnway.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="study_chatroom")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="study_chatroomid", nullable = false)
    private Integer chatroomid;
    
    @ManyToOne
    @JoinColumn(name = "study_postid", nullable = false)
    private Study study;
    
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "chatroom")
    @JsonManagedReference
    private List<ChatMessage> messages;

    @Column(name="study_roomname", nullable = false)
    private String roomname;
}
