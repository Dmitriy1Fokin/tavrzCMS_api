package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.domain.PledgeSubjectSecurities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PledgeSubjectSecuritiesSpecificationBuilder {

    private final List<SearchCriteria> params;

    public PledgeSubjectSecuritiesSpecificationBuilder(){
        params = new ArrayList<SearchCriteria>();
    }

    public PledgeSubjectSecuritiesSpecificationBuilder with(String key, String operation, Object value, boolean predicate){
        params.add(new SearchCriteria(key, operation, value, predicate));
        return this;
    }

    public Specification<PledgeSubjectSecurities> build(){
        if(params.size() == 0){
            return null;
        }

        List<Specification> specs = params.stream().map(PledgeSubjectSecuritiesSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for(int i = 1; i < params.size(); i++){
            result = params.get(i).isPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }

        return result;
    }

}
