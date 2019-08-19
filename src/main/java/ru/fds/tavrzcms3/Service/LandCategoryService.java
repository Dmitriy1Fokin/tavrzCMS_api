package ru.fds.tavrzcms3.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fds.tavrzcms3.domain.LandCategory;
import ru.fds.tavrzcms3.repository.RepositoryLandCategory;

import java.util.List;

@Service
public class LandCategoryService {

    @Autowired
    RepositoryLandCategory repositoryLandCategory;

    public LandCategory getLandCategory(int landCategoryId){
        return repositoryLandCategory.getOne(landCategoryId);
    }

    public List<LandCategory> getAllLandCategory(){
        return repositoryLandCategory.findAll();
    }
}
