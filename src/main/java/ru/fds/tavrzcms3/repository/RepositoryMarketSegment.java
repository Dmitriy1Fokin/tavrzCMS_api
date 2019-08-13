package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fds.tavrzcms3.domain.MarketSegment;

public interface RepositoryMarketSegment extends JpaRepository<MarketSegment, Integer> {
    MarketSegment findByName(String name);
}
