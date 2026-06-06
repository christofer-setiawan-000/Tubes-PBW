package com.example.demo.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCommentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Simpan komentar baru
    public void save(Comment comment) {
        String sql = "INSERT INTO comments (meme_id, username, content, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, comment.getMemeId(), comment.getUsername(), comment.getContent(), LocalDateTime.now());
    }

    // Ambil komentar berdasarkan ID Meme
    public List<Comment> findByMemeId(Long memeId) {
        String sql = "SELECT * FROM comments WHERE meme_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToComment, memeId);
    }

    // Cari komentar by ID (untuk cek pemilik sebelum edit/hapus)
    public Optional<Comment> findById(Long id) {
        String sql = "SELECT * FROM comments WHERE id = ?";
        List<Comment> results = jdbcTemplate.query(sql, this::mapRowToComment, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // Hapus komentar
    public void deleteById(Long id) {
        String sql = "DELETE FROM comments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    // Mapper
    private Comment mapRowToComment(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(
            rs.getLong("id"),
            rs.getLong("meme_id"),
            rs.getString("username"),
            rs.getString("content"),
            rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    
    //edit comment
    public void update(Long id, String newContent) {
        String sql = "UPDATE comments SET content = ? WHERE id = ?";
        jdbcTemplate.update(sql, newContent, id);
    }
}