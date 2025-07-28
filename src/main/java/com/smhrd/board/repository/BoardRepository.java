package com.smhrd.board.repository;

import com.smhrd.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    // 게시글이 역순으로 출력 될 수 있는 기능
    // select * from board order by writeDay desc
    ArrayList<BoardEntity> findAllByOrderByWriteDayDesc();

    // like 문 사용해서 title을 가지고 오는 메소드
    // containing --> like문 (매개변수 안의 값을 바탕으로)
    List<BoardEntity> findByTitleContaining(String keyword);

    // 내가 원하는 쿼리문 바로 실행
    // like문 사용하여 content 가지고 오는 메소드
    // :변수 --> 매개변수로 가지고 온 값을 사용
    @Query("SELECT b FROM BoardEntity b where b.content like %:keyword%")
    List<BoardEntity> searchContent(@Param("keyword") String keyword);

}
