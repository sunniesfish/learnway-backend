package com.learnway.study.service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnway.member.domain.Member;
import com.learnway.member.domain.MemberRepository;
import com.learnway.study.domain.ChatMessage;
import com.learnway.study.domain.ChatMessageRepository;
import com.learnway.study.domain.ChatRoom;
import com.learnway.study.domain.ChatRoomMember;
import com.learnway.study.domain.ChatRoomMemberRepository;
import com.learnway.study.domain.ChatRoomRepository;
import com.learnway.study.domain.Study;
import com.learnway.study.domain.StudyChatRepository;
import com.learnway.study.dto.ChatRoomDto;

@Service
public class StudyChatService {

	@Autowired
	private StudyChatRepository studyChatRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ChatRoomMemberRepository chatRoomMemberRepository;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	
	//postId로 ChatRoomId 조회
	public List<ChatRoom> chatRoomId(int postId) {
	
		return studyChatRepository.findByStudyPostid(postId);
	}
	
	//채팅방 멤버이름값
	public String MemberName(Principal principal) {
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		return member.getMemberName();
	}
	
	//채팅방 리스트 조회
	public List<ChatRoomMember> chatList(Principal principal) {
		
//		List<ChatRoomMember> list = chatRoomMemberRepository.findByMemberId(
//								memberRepository.findByMemberId(principal.getName()).get());
		List<ChatRoomMember> list = chatRoomMemberRepository.findByMember_MemberId(
				principal.getName());
		return list;
	}
	
//	채팅방 참여 메서드
	public ChatRoomMember joinChatRoom(ChatRoomDto dto,Principal principal) {
		
		//현재 로그인사용자정보 조회
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		
		//방번호로 ChatRoom객체생성
		ChatRoom chatRoom = studyChatRepository.findById(dto.getRoomId()).get();
		
		
		ChatRoomMember room = ChatRoomMember.builder().member(member)
				  				.chatRoom(chatRoom).build();
		
		return chatRoomMemberRepository.save(room);
	}
	
	//채팅방 생성 메서드
	public ChatRoom chatRoomCreate(ChatRoomDto dto,Study study,Principal principal) {
		
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		
		
		ChatRoom room = ChatRoom.builder().roomname(dto.getRoomname())
				.study(study).member(member).build();
		System.out.println(room.getRoomname() + "룸이름");
		
		return studyChatRepository.save(ChatRoom.builder().roomname(dto.getRoomname())
				.study(study).member(member).build());
	}
	
	
	
	//채팅 보관 메서드
	public ChatMessage storechat(ChatRoomDto dto,Principal principal) {
		
		//멤버값 넣을예정 임시로 1로지정
		Member member = memberRepository.findByMemberId(principal.getName())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + principal.getName()));
		
		ChatRoom chatRoom = studyChatRepository.findById(dto.getRoomId())
				.orElseThrow(()-> new IllegalArgumentException("채팅방아이디  " + dto.getRoomId()));
		
		ChatMessage msg = ChatMessage.builder().msg(dto.getMessage()).member(member)
										       .chatroom(chatRoom)
											   .datetime(dto.getDatetime()).build();
		return msg;
	}
	
	//채팅방 참여자 리스트(방장)
	public ChatRoom chatListHost(ChatRoomDto dto) {
		
		return chatRoomRepository.findById(dto.getRoomId()).get();
	}
	//채팅방 참여자 리스트(멤버)
	public ChatRoomMember chatListGuest(ChatRoomDto dto) {
		
		return chatRoomMemberRepository.findById((long) dto.getRoomId()).get();
	}
	
	//이전채팅 가져오기
	public List<ChatMessage> chatMessageList(ChatRoomDto dto) {
		return chatMessageRepository.findByChatroom_Chatroomid(dto.getRoomId());
	}
	
}
