package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.MarketSegment;
import ru.fds.tavrzcms3.repository.RepositoryMarketSegment;

import java.util.List;

@Service
public class MarketSegmentService {

    @Autowired
    RepositoryMarketSegment repositoryMarketSegment;

    public List<MarketSegment> getAllMarketSegment(){
        return repositoryMarketSegment.findAll();
    }
}
