package com.smhrd.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "board")
public class BoardEntity {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increase
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false, columnDefinition = "TEXT") // DB 컬럼을 text로 바꾼다는 코드
    private String content;

    private String imgPath; // 이미지 저장 DB에 이미지 저장X 서버에 이미지 저장하고 서버 경로를 DB에 저장

    // 현재 날짜 (글 작성 일시)
    @Column(nullable = false, updatable = false) // updatable = false update 불가
    private LocalDate writeDay;

    // JPA 글 작성 시 자동으로 오늘 날짜 입력 할 수 있게 하는 코드
    @PrePersist
    protected void onWriteDay() {
        this.writeDay = LocalDate.now();
    }
}
