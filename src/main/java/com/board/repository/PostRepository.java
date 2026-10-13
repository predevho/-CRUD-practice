package com.board.repository;

import com.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * SELECT *
     * FROM post
     * WHERE title LIKE '%검색어%';
     * */
    List<Post> findByTitleContaining(String keyword);
}
