package com.smhrd.board.controller;

import com.smhrd.board.config.FileUploadConfig;
import com.smhrd.board.entity.BoardEntity;
import com.smhrd.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
public class BoardRestController {

    @Autowired
    BoardService boardService;

    @Autowired
    FileUploadConfig fileUploadConfig;

    @DeleteMapping("/delete/{id}")
    public void deleteBoard(@PathVariable Long id) {

        // 1. 필요한거 --> id,

        // 이미지 삭제
        // id를 바탕으로 select
        // Optional<BoardEntity> board = boardService.detailPage(id);
        // BoardEntity board = boardService.detailPage(id).get();
        String imgPath = boardService.detailPage(id).get().getImgPath();
        String uploadDir = fileUploadConfig.getUploadDir();

        if (imgPath != null && !imgPath.isEmpty()) {
            // 경로 접근 get(경로, 파일 이름)
            // imgPath -> uploads/~파~일~명~
            // 실제 파일 명은 uploads를 제외한 나머지 문자들
            Path filePath = Paths.get(uploadDir, imgPath.replace("/uploads/", ""));

            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 2. service 연결
        // DB에 삭제는 완료
        boardService.deleteBoard(id);
    }

    // 검색 기능
    @GetMapping("/search")
    public List<BoardEntity> search(@RequestParam String type,
                                    @RequestParam String keyword) {
        // 1. 필요한거 -- key, keyword
        // 2. service 연결
        return boardService.searchResult(type, keyword);
    }
}
