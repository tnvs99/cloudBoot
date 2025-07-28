package com.smhrd.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data // getter / setter
@Table(name = "user") // 이미 만들어 놓은 DB 사용 할 수 없나요? DB 이름을 다르게 지정 하고 싶어요
public class UserEntity {

    // pk 값이 필수
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx; // 객체타입으로 삽입 --> DB에 해당 값이 없으면 null로 값이 넘어옵니다.

    @Column(nullable = false, unique = true)
    private String id;

    private String pw;
    private String name;
    private Integer age;

}
