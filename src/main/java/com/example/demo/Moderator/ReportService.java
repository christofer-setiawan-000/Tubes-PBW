package com.example.demo.Moderator;

import com.example.demo.Moderator.dto.*;
import com.example.demo.Moderator.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repo;

    public List<CategoryPopularityDTO> getCategoryPopularity() {
        return repo.getCategoryPopularity();
    }

    public List<CategoryVotesDTO> getCategoryVotes() {
        return repo.getCategoryVotes();
    }

    public List<TopUploaderDTO> getTopUploaders(int limit) {
        return repo.getTopUploaders(limit);
    }

    public List<TrendingMemeDTO> getTrendingMemes(int limit) {
        return repo.getTrendingMemes(limit);
    }

    public List<ActiveUserDTO> getMostActiveUsers(int limit) {
        return repo.getMostActiveUsers(limit);
    }
}
