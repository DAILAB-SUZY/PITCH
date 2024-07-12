package org.cosmic.backend.domain.albumChat.repository;

import org.cosmic.backend.domain.albumChat.domain.AlbumChatComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumChatCommentRepository extends JpaRepository<AlbumChatComment,Long> {
    List<AlbumChatComment> findByAlbumChat_AlbumChatId(Long albumChatid);//key로 찾기
    AlbumChatComment findByAlbumChatCommentId(Long albumChatCommentid);
}