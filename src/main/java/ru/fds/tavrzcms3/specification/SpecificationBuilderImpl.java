package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SpecificationBuilderImpl implements SpecificationBuilder {

    private final List<SearchCriteria> params;
    private SearchCriteria criteria;

    public SpecificationBuilderImpl(){
        params = new ArrayList<>();
    }

    @Override
    public void with(String key, String operation, Object value, boolean predicate) {
        params.add(new SearchCriteria(key, operation, value, predicate));
    }

    @Override
    public Specification build() {
        if(params.size() == 0){
            return null;
        }

        List<Specification> specs = params.stream().map(SpecificationImpl::new).collect(Collectors.toList());

        Specification result = specs.get(0);

        for(int i = 1; i < params.size(); i++){
            result = params.get(i).isPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }

        return result;
    }


}
