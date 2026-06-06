package com.example.demo.Vote;

import com.example.demo.User.User;
import com.example.demo.common.RequiredRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VoteController {

    @Autowired
    private JdbcVoteRepository voteRepository;

    @PostMapping("/memes/{memeId}/vote")
    @RequiredRole({"user", "moderator"})
    public String vote(@PathVariable Long memeId, HttpSession session) {
        User user = (User) session.getAttribute("user");
    
        if (voteRepository.hasVoted(memeId, user.getUsername())) {
             voteRepository.removeVote(memeId, user.getUsername());
        } else {
             voteRepository.addVote(memeId, user.getUsername());
        }
        
        return "redirect:/memes/" + memeId;
    }
}