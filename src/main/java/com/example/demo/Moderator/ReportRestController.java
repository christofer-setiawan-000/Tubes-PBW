package com.example.demo.Moderator;

import com.example.demo.Moderator.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportRestController {

    @Autowired
    private ReportService service;

    @GetMapping("/category-popularity")
    public Map<String, Integer> categoryPopularity() {
        List<CategoryPopularityDTO> list = service.getCategoryPopularity();
        Map<String,Integer> map = new LinkedHashMap<>();
        list.forEach(d -> map.put(d.getCategory(), d.getTotalMemes()));
        return map;
    }

    @GetMapping("/category-votes")
    public Map<String, Integer> categoryVotes() {
        List<CategoryVotesDTO> list = service.getCategoryVotes();
        Map<String,Integer> map = new LinkedHashMap<>();
        list.forEach(d -> map.put(d.getCategory(), d.getTotalVotes()));
        return map;
    }

    @GetMapping("/top-uploaders")
    public List<TopUploaderDTO> topUploaders(@RequestParam(defaultValue = "10") int limit) {
        return service.getTopUploaders(limit);
    }

    @GetMapping("/trending-memes")
    public List<TrendingMemeDTO> trendingMemes(@RequestParam(defaultValue = "10") int limit) {
        return service.getTrendingMemes(limit);
    }

    @GetMapping("/active-users")
    public List<ActiveUserDTO> activeUsers(@RequestParam(defaultValue = "10") int limit) {
        return service.getMostActiveUsers(limit);
    }
}
