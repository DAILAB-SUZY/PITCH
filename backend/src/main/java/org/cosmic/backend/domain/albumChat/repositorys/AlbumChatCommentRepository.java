package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumChatCommentRepository extends JpaRepository<AlbumChatComment,Long> {
    Optional<List<AlbumChatComment>> findByAlbumChat_AlbumChatId(Long albumChatid);//key로 찾기
    AlbumChatComment findByAlbumChatCommentId(Long albumChatCommentid);
}