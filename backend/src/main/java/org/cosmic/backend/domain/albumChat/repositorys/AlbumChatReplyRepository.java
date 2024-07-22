package org.cosmic.backend.domain.albumChat.repositorys;

import org.cosmic.backend.domain.albumChat.domains.AlbumChatReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumChatReplyRepository extends JpaRepository<AlbumChatReply,Long> {
    Optional<List<AlbumChatReply>> findByAlbumChatComment_AlbumChatCommentId(Long albumchatCommentId);//key로 찾기
    AlbumChatReply findByAlbumChatReplyId(Long albumChatReplyId);
}