package com.smhrd.board.controller;

import com.smhrd.board.config.FileUploadConfig;
import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.entity.UserEntity;
import com.smhrd.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

// controller의 default mapping 주소 설정
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    FileUploadConfig fileUploadConfig;

    // 게시글 작성 기능
    @PostMapping("/write")
    public String write(@RequestParam String title,
                        @RequestParam String content,
                        HttpSession session,
                        @RequestParam MultipartFile image)  {
        // 1. 필요한거 ?? input 태그에서는 File 넘어 오는 중(file을 받아주어야 합니다)
        // title, writer, content, imgPath
        //       (session)

        // image 처리
        String imgPath = "";
        // 이미지를 저장 할 경로(window 기준) - C:upload/
        String uploadDir = fileUploadConfig.getUploadDir();

        if(!image.isEmpty()) {
            // 1. 파일의 이름 설정
            // uuid: 고유 식별자 (중복을 막을려고)
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

            // 2. 파일 저장될 이름과 경로 설정
            String filePath = Paths.get(uploadDir, fileName).toString();

            // 3. 서버에 저장 및 경로 설정
            try {
                image.transferTo(new File(filePath));
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 4. DB에 저장 될 경로 문자열 설정
            imgPath = "/uploads/" + fileName;
        }

        BoardEntity board = new BoardEntity();
        // 2. DB 연결 -> repository, service
        board.setTitle(title);
        board.setContent(content);
        board.setImgPath(imgPath);
        // writer
        // 다운캐스팅
        UserEntity user = (UserEntity) session.getAttribute("user");
        board.setWriter(user.getId());

        boardService.write(board);
        return "redirect:/";
    }

    // 게시글 상세 페이지 이동
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id,
                         Model model) { // URL에 담긴 값 가지고 오는 방법 @PathVariable
        System.out.println(id);
        // 1. 필요한 거
        // --> id, 다음 페이지로 값을 보내기 위한 Model 객체
        // 2. service 연결
        Optional<BoardEntity> detail = boardService.detailPage(id);
        // detail --> Optional 타입 --> BoardEntity 타입
        // .get() (BoardEntity 객체를 가져오기 위해)
        model.addAttribute("detail", detail.get());
        return "detail";
    }

    // 게시판 수정 기능 edit.html
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {
        // 1. 필요한거

        // 2. service 연결
        Optional<BoardEntity> board = boardService.detailPage(id);

        model.addAttribute("board", board.get());

        return "edit";
    }

    // 게시판 수정 기능
    @PostMapping("/update")
    public String update(@RequestParam String title,
                         @RequestParam String content,
                         @RequestParam MultipartFile image,
                         @RequestParam String oldImgPath,
                         @RequestParam Long id) {

        // 1. title, content, imgPath, id, oldImgPath
        // db 접근 해서 게시글의 정보를 다시 가지고 오겠습니다.
        BoardEntity entity = boardService.detailPage(id).get();

        // File 업로드 경로
        String uploadDir = fileUploadConfig.getUploadDir();
        // 기존 이미지 처리
        if (!image.isEmpty()) { // 새롭게 이미지를 업로드
            // 기존에 있던 이미지를 삭제
                if (oldImgPath != null && !oldImgPath.isEmpty()) {
                    Path oldFilePath = Paths.get(uploadDir, oldImgPath.replace("/uploads/", ""));
                    try {
                        Files.deleteIfExists(oldFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 새로운 이미지 저장
                String newFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path newFilePath = Paths.get(uploadDir, newFileName);

            try {
                image.transferTo(newFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            entity.setImgPath("/uploads/" + newFileName);
        }


//        BoardEntity entity = new BoardEntity();

        entity.setTitle(title);
        entity.setTitle(content);

        // update문 실행 -- service에 연결
        /*
        * JPA에서 save 했을 때 insert문이 아닌 update 문이 실행되는 조건
        * findById() 해준 후 save 코드를 실행하면
        * JPA가 자동으로 update라고 인식을 함
        *
        * 대규모로 update 복잡한 update @Query("sql문 작성")
        * */
        boardService.write(entity);
        return "redirect:/board/detail/" + id;
    }
}
