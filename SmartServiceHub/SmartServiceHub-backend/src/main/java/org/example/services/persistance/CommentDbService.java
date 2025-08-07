package org.example.services.persistance;

import org.example.models.Comment;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentDbService {

    private final CommentRepository commentRepository;

    public CommentDbService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getServiceWithLimit(int limit){
        return commentRepository.findAll(PageRequest.of(0, limit)).getContent();
    }

    public List<Comment> getCommentUnique(int limit, int offset, long postId){
        return commentRepository.findUniqueCommentsByPostId(postId, limit, offset);
    }

    public List<Comment> getCommentsFromPost(Long postId){
        return commentRepository.findCommentsByPostId(postId);
    }
}
