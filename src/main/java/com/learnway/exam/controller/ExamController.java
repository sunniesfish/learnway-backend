package com.learnway.exam.controller;

import com.learnway.exam.domain.Exam;
import com.learnway.exam.dto.ExamDetailDTO;
import com.learnway.exam.service.ExamService;
import com.learnway.member.service.CustomUserDetails;
import com.learnway.member.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@Controller
@AllArgsConstructor
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    /*
    * 시험등록 모달에서 시험 종류 및 이름 등록 하고 시험 리스트 페이지로 리다이렉트
    */
    @PostMapping("/register")
    public String registerExam(
            @RequestParam(name = "examName") String examName,
            @RequestParam(name = "examType") String examType,
            @RequestParam(name = "examRange") String examRange,
            @RequestParam(name = "examDate") Date examDate,
            @RequestParam(name = "examMemo") String examMemo,
            Authentication authentication
            ){
        // get memId
        Member member = null;
        Long memId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
            memId = member.getId();
        }
        if(memId != null){
            examService.writeExam(
                    new Exam().builder()
                            .memId(memId)
                            .examName(examName)
                            .examType(examType)
                            .examRange(examRange)
                            .examDate(examDate)
                            .examMemo(examMemo).build()
            );
        }

        return "redirect:/exam/list/1";
    }


    /**
     * 사용자의 시험 리스트 가져옴
     */
    @GetMapping("/list/{pageNo}")
    public String getExamList(@PathVariable("pageNo") Integer pageNo, Model model, Authentication authentication){
        //get memId
        Member member = null;
        Long memId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
            memId = member.getId();
        }
        if(memId != null){
            System.out.println("getting exam list : "+memId);
            pageNo = pageNo == null ? pageNo = 1 : pageNo;
            model.addAttribute("examList", examService.readExam(memId,pageNo,8));
        }
        return "exam/exam";
    }

    /*
    *  시험 내용 수정
    * */
    @PostMapping("/update")
    public String updateExam(
            @RequestParam(name = "examName") String examName,
            @RequestParam(name = "examType") String examType,
            @RequestParam(name = "examRange") String examRange,
            @RequestParam(name = "examDate") Date examDate,
            @RequestParam(name = "examMemo") String examMemo,
            Authentication authentication
    ){
        // get memId
        Member member = null;
        Long memId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
            memId = member.getId();
        }

        if(memId != null){
            examService.updateExam(
                    new Exam().builder()
                            .memId(memId)
                            .examName(examName)
                            .examType(examType)
                            .examRange(examRange)
                            .examDate(examDate)
                            .examMemo(examMemo).build()
            );
        }
        return "redirect:/exam/list/1";
    }

    /*
    * 시험 삭제 후 시험리스트 페이지로 리다이렉트
    * */
    @PostMapping("/delete")
    public String deleteExam(@RequestParam("examId") Long examId, Authentication authentication){
        //get memId
        Member member = null;
        Long memId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
            memId = member.getId();
        }

        if(memId != null) {
            examService.deleteExam(examId, memId);
        }
        return "redirect:/exam/list/1";
    }

    /*
    * 시험 상세 페이지 memId 또는 examId가 없는 경우 시험 리스트로 리다이렉트
    * */
    @GetMapping("/detail/{examId}")
    public String getDetailExam(@PathVariable("examId") Long examId, Model model, Authentication authentication){
        //get memId
        Member member = null;
        Long memId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            member = userDetails.getMember();
            memId = member.getId();
        }

        ExamDetailDTO dto = null;
        if(memId != null){
            dto = examService.getExamDetail(examId, memId);
        }
        if(dto != null) {
            model.addAttribute("exam", dto);
            return "exam/exam-detail";
        } else {
            return "redirect:/exam/list/1";
        }
    }


}