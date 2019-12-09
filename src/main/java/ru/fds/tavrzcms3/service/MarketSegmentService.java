package ru.fds.tavrzcms3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.MarketSegment;
import ru.fds.tavrzcms3.repository.RepositoryMarketSegment;

import java.util.List;
import java.util.Optional;

@Service
public class MarketSegmentService {

    @Autowired
    RepositoryMarketSegment repositoryMarketSegment;

    public Optional<MarketSegment> getMarketSegmentById(int marketSegmentId){
        return repositoryMarketSegment.findById(marketSegmentId);
    }

    public List<MarketSegment> getAllMarketSegment(){
        return repositoryMarketSegment.findAll();
    }
}
