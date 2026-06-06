package com.example.demo.Vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public class JdbcVoteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Cek apakah user sudah pernah vote di meme ini
    public boolean hasVoted(Long memeId, String username) {
        String sql = "SELECT COUNT(*) FROM votes WHERE meme_id = ? AND username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memeId, username);
        return count != null && count > 0;
    }

    // Tambah Vote
    public void addVote(Long memeId, String username) {
        if (!hasVoted(memeId, username)) {
            // 1. Masukkan ke tabel votes
            String sqlVote = "INSERT INTO votes (meme_id, username, voted_at) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlVote, memeId, username, LocalDateTime.now());

            // 2. Update counter di tabel memes (agar query trending lebih cepat)
            String sqlMeme = "UPDATE memes SET vote_count = vote_count + 1 WHERE id = ?";
            jdbcTemplate.update(sqlMeme, memeId);
        }
    }
    
    // Hapus Vote (Unvote) - Optional, tapi bagus ada
    public void removeVote(Long memeId, String username) {
        if (hasVoted(memeId, username)) {
            String sqlVote = "DELETE FROM votes WHERE meme_id = ? AND username = ?";
            jdbcTemplate.update(sqlVote, memeId, username);

            String sqlMeme = "UPDATE memes SET vote_count = vote_count - 1 WHERE id = ? AND vote_count > 0";
            jdbcTemplate.update(sqlMeme, memeId);
        }
    }
}