package com.example.demo.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserRepository implements UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public void save(User user) throws Exception{
        String sql = "INSERT INTO users (username, email, password, name, role) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getName(), user.getRole());
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<User> results = jdbcTemplate.query(sql, this::mapRowToUser, username);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> results = jdbcTemplate.query(sql, this::mapRowToUser, email);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
            resultSet.getString("username"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("role")
        );
    }

    //moderator
    public void deleteByUsername(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        jdbcTemplate.update(sql, username);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    public Page<User> findAll(Pageable pageable) {
        // 1. Hitung dulu TOTAL SEMUA USER (untuk tahu butuh berapa halaman)
        String countSql = "SELECT COUNT(*) FROM users";
        Integer total = jdbcTemplate.queryForObject(countSql, Integer.class);
        
        // 2. Ambil Data User dengan LIMIT dan OFFSET
        // LIMIT = Ukuran per halaman (misal 5)
        // OFFSET = Halaman ke berapa * Ukuran (misal hal 1 * 5 = skip 5 data)
        String sql = "SELECT * FROM users LIMIT ? OFFSET ?";
        
        List<User> users = jdbcTemplate.query(
            sql, 
            new BeanPropertyRowMapper<>(User.class), 
            pageable.getPageSize(),     // Mengisi tanda tanya ke-1 (LIMIT)
            pageable.getOffset()        // Mengisi tanda tanya ke-2 (OFFSET)
        );
        return new PageImpl<>(users, pageable, total);
    }
}
