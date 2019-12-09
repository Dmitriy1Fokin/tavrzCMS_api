package ru.fds.tavrzcms3.service;

import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.LandCategory;
import ru.fds.tavrzcms3.repository.RepositoryLandCategory;

import java.util.List;
import java.util.Optional;

@Service
public class LandCategoryService {

    private final RepositoryLandCategory repositoryLandCategory;

    public LandCategoryService(RepositoryLandCategory repositoryLandCategory) {
        this.repositoryLandCategory = repositoryLandCategory;
    }

    public Optional<LandCategory> getLandCategoryById(int landcategoryId){
        return repositoryLandCategory.findById(landcategoryId);
    }

    public List<LandCategory> getAllLandCategory(){
        return repositoryLandCategory.findAll();
    }
}
