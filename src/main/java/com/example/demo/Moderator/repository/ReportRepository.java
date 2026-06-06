package com.example.demo.Moderator.repository;

import com.example.demo.Moderator.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepository {

    @Autowired
    private JdbcTemplate jdbc;

    // Jumlah meme per kategori
    public List<CategoryPopularityDTO> getCategoryPopularity() {
        String sql = """
            SELECT c.name AS category, COALESCE(COUNT(mc.meme_id),0) AS total_memes
            FROM categories c
            LEFT JOIN meme_categories mc ON c.id = mc.category_id
            GROUP BY c.id, c.name
            ORDER BY total_memes DESC
            """;
        return jdbc.query(sql, (rs, rowNum) -> 
            new CategoryPopularityDTO(rs.getString("category"), rs.getInt("total_memes"))
        );
    }

    // Jumlah vote per kategori
    public List<CategoryVotesDTO> getCategoryVotes() {
        String sql = """
            SELECT c.name AS category, COALESCE(SUM(m.vote_count),0) AS total_votes
            FROM categories c
            LEFT JOIN meme_categories mc ON mc.category_id = c.id
            LEFT JOIN memes m ON m.id = mc.meme_id
            GROUP BY c.name
            ORDER BY total_votes DESC
            """;
        return jdbc.query(sql, (rs, rowNum) ->
            new CategoryVotesDTO(rs.getString("category"), rs.getInt("total_votes"))
        );
    }

    // Top 10 uploader berdasarkan jumlah meme yg diupload
    public List<TopUploaderDTO> getTopUploaders(int limit) {
        String sql = """
            SELECT u.username, u.name, COALESCE(COUNT(m.id),0) AS total_uploaded
            FROM users u
            LEFT JOIN memes m ON m.username = u.username
            GROUP BY u.username, u.name
            ORDER BY total_uploaded DESC
            LIMIT ?
            """;
        return jdbc.query(sql, new Object[]{limit}, (rs, rowNum) ->
            new TopUploaderDTO(rs.getString("username"), rs.getString("name"), rs.getInt("total_uploaded"))
        );
    }

    // Top trending memes berdasarkan jumlah vote
    public List<TrendingMemeDTO> getTrendingMemes(int limit) {
        String sql = """
            SELECT id AS meme_id, title, COALESCE(vote_count,0) AS votes
            FROM memes
            ORDER BY votes DESC
            LIMIT ?
            """;
        return jdbc.query(sql, new Object[]{limit}, (rs, rowNum) ->
            new TrendingMemeDTO(rs.getLong("meme_id"), rs.getString("title"), rs.getInt("votes"))
        );
    }

    // User2 paling aktif (comments & votes)
    public List<ActiveUserDTO> getMostActiveUsers(int limit) {
        // comments_count and votes_count
        String sql = """
            SELECT u.username,
                   COALESCE(c.comments_count,0) AS comments_count,
                   COALESCE(v.votes_count,0) AS votes_count,
                   (COALESCE(c.comments_count,0) + COALESCE(v.votes_count,0)) AS activity_score
            FROM users u
            LEFT JOIN (
              SELECT username, COUNT(*) AS comments_count FROM comments GROUP BY username
            ) c ON c.username = u.username
            LEFT JOIN (
              SELECT username, COUNT(*) AS votes_count FROM votes GROUP BY username
            ) v ON v.username = u.username
            ORDER BY activity_score DESC
            LIMIT ?
            """;
        return jdbc.query(sql, new Object[]{limit}, (rs, rowNum) ->
            new ActiveUserDTO(
                rs.getString("username"),
                rs.getInt("comments_count"),
                rs.getInt("votes_count"),
                rs.getInt("activity_score")
            )
        );
    }
}
