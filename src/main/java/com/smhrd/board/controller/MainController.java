package com.smhrd.board.controller;

import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class MainController {
    @Autowired
    BoardService boardService;

    // 매핑
    @GetMapping("/")
    public String index(Model model){
        // board service 연결 --> @Autowired 코드

        // BoardService.java에 코드 작성
        // board service - repository 연결해서 데이터 가지고 오기
        // board repository - findAll() --> 새롭게 만들 필요 X
        // ArrayList에 담은 후
        // model 객체에 담아주세요 -> list

        //List<BoardEntity> list = boardService.selectAll();
        ArrayList<BoardEntity> list = boardService.selectAll();

        model.addAttribute("boardList", list);



        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/write")
    public String write() {
        return "write";
    }
}
