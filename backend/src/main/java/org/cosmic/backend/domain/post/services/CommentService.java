package org.cosmic.backend.domain.post.services;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Service
public class CommentService {

    @Autowired
    ReplyService replyService=new ReplyService();
    @Autowired
    LikeService likeService=new LikeService();
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public List<CommentReq> getCommentsByPostId(Long postId) {
        List<CommentReq> comments = new ArrayList<>();
        if(!postRepository.findById(postId).isPresent()) {
            throw new NotFoundPostException();
        }
        else {
            List<Comment> commentList = commentRepository.findByPost_PostId(postId);
            for (Comment comment : commentList) {
                CommentReq commentReq = new CommentReq();
                commentReq.setCommentId(comment.getCommentId());
                commentReq.setContent(comment.getContent());
                commentReq.setCreateTime(comment.getUpdateTime());
                commentReq.setUserId(comment.getUser().getUserId());
                comments.add(commentReq);
            }
            return comments;
        }
    }

    public CommentDto createComment(CreateCommentReq comment) {//누가 쓴 comment인지 나와야하기 때문에 userId필요
        Comment commentEntity=new Comment();

        if(!postRepository.findById(comment.getPostId()).isPresent()) {
            throw new NotFoundPostException();
        }
        else if(!userRepository.findById(comment.getUserId()).isPresent())
        {
            throw new NotFoundUserException();
        }
        else{
            commentEntity.setContent(comment.getContent());
            commentEntity.setUpdateTime(Instant.now());
            commentEntity.setUser(userRepository.findByUserId(comment.getUserId()).get());//누구의 post인지 알려줘야함.
            commentEntity.setReplies(null);
            commentEntity.setPost(postRepository.findByPostId(comment.getPostId()));
            commentRepository.save(commentEntity);
            CommentDto commentDto=new CommentDto();
            commentDto.setCommentId(commentEntity.getCommentId());
            return commentDto;
        }
    }

    public void updateComment(UpdateCommentReq comment) {
        //해당 commentId를 받고 commentId를 통해 comment내부 set
        if(!commentRepository.findById(comment.getCommentId()).isPresent()) {
            throw new NotFoundCommentException();
        }
        else {
            Comment comment1 = commentRepository.findByCommentId(comment.getCommentId());
            if(comment1.getUser().getUserId()!=comment.getUserId())
            {
                throw new NotMatchUserException();
            }
            else if(comment1.getPost().getPostId()!=comment.getPostId())
            {
                throw new NotMatchPostException();
            }
            else{
                comment1.setContent(comment.getContent());
                commentRepository.save(comment1);//새로생기는지 업데이트만 되는지 만약 새로생기는거면업데이트만 되게만들어야함.
            }
        }
    }

    public void deleteComment(CommentDto commentdto) {
        if(!commentRepository.findById(commentdto.getCommentId()).isPresent()) {
            throw new NotFoundCommentException();
        }
        else{
            commentRepository.deleteById(commentdto.getCommentId());
        }
    }
}
