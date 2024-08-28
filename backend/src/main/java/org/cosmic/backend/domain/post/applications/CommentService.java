package org.cosmic.backend.domain.post.applications;

import org.cosmic.backend.domain.playList.exceptions.NotFoundUserException;
import org.cosmic.backend.domain.post.dtos.Comment.CommentDto;
import org.cosmic.backend.domain.post.dtos.Comment.CommentReq;
import org.cosmic.backend.domain.post.dtos.Comment.CreateCommentReq;
import org.cosmic.backend.domain.post.dtos.Comment.UpdateCommentReq;
import org.cosmic.backend.domain.post.entities.Comment;
import org.cosmic.backend.domain.post.exceptions.NotFoundCommentException;
import org.cosmic.backend.domain.post.exceptions.NotFoundPostException;
import org.cosmic.backend.domain.post.exceptions.NotMatchPostException;
import org.cosmic.backend.domain.post.exceptions.NotMatchUserException;
import org.cosmic.backend.domain.post.repositories.CommentRepository;
import org.cosmic.backend.domain.post.repositories.PostRepository;
import org.cosmic.backend.domain.user.repositorys.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UsersRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UsersRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<CommentReq> getCommentsByPostId(Long postId) {
        if(postRepository.findById(postId).isEmpty()) {
            throw new NotFoundPostException();
        }
        return commentRepository.findByPost_PostId(postId).stream().map(Comment::toCommentReq).toList();
    }

    public CommentDto createComment(CreateCommentReq commentReq) {//누가 쓴 comment인지 나와야하기 때문에 userId필요
        if(postRepository.findById(commentReq.getPostId()).isEmpty()) {
            throw new NotFoundPostException();
        }
        if(userRepository.findById(commentReq.getUserId()).isEmpty()) {
            throw new NotFoundUserException();
        }
        return Comment.toCommentDto(commentRepository.save(Comment.builder()
                .content(commentReq.getContent())
                .updateTime(Instant.now())
                .user(userRepository.findById(commentReq.getUserId()).get())
                .post(postRepository.findById(commentReq.getPostId()).get()).build()));
    }

    public void updateComment(UpdateCommentReq commentReq) {
        //해당 commentId를 받고 commentId를 통해 comment내부 set
        if(commentRepository.findById(commentReq.getCommentId()).isEmpty()) {
            throw new NotFoundCommentException();
        }
        Comment comment = commentRepository.findById(commentReq.getCommentId()).get();
        if(!Objects.equals(comment.getUser().getUserId(), commentReq.getUserId())) {
            throw new NotMatchUserException();
        }
        if(!Objects.equals(comment.getPost().getPostId(), commentReq.getPostId())) {
            throw new NotMatchPostException();
        }
        comment.setContent(commentReq.getContent());
        commentRepository.save(comment);//새로생기는지 업데이트만 되는지 만약 새로생기는거면업데이트만 되게만들어야함.
    }

    public void deleteComment(CommentDto commentdto) {
        if(commentRepository.findById(commentdto.getCommentId()).isEmpty()) {
            throw new NotFoundCommentException();
        }
        commentRepository.deleteById(commentdto.getCommentId());
    }
}
