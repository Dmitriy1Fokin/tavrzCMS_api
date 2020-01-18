package ru.fds.tavrzcms3.specification.impl;

import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.specification.SearchCriteria;
import ru.fds.tavrzcms3.specification.SearchCriteriaNestedAttribute;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilderImpl<T> implements SpecificationBuilder<T> {

    private final List<SearchCriteria> searchCriteriaList;
    private final List<SearchCriteriaNestedAttribute> searchCriteriaNestedAttributeList;

    public SpecificationBuilderImpl(){
        searchCriteriaList = new ArrayList<>();
        searchCriteriaNestedAttributeList = new ArrayList<>();
    }

    @Override
    public void withCriteria(SearchCriteria searchCriteria) {
        searchCriteriaList.add(searchCriteria);
    }

    @Override
    public void withNestedAttributeCriteria(SearchCriteriaNestedAttribute searchCriteriaNestedAttribute) {
        searchCriteriaNestedAttributeList.add(searchCriteriaNestedAttribute);
    }

    @Override
    public Specification<T> buildSpecification() {
        if(searchCriteriaList.isEmpty() && searchCriteriaNestedAttributeList.isEmpty()){
            return null;
        }

        List<Specification<T>> specs = new ArrayList<>(searchCriteriaList.size());
        searchCriteriaList.forEach(searchCriteria -> specs.add(new SpecificationImpl<>(searchCriteria)));

        List<Specification<T>> specsNested = new ArrayList<>(searchCriteriaNestedAttributeList.size());
        searchCriteriaNestedAttributeList.forEach(criteria -> specsNested.add(new SpecificationNestedAttributeImpl<>(criteria)));

        Specification<T> result = specs.get(0);

        for(int i = 1; i < searchCriteriaList.size(); i++){
            result = searchCriteriaList.get(i).isPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }

        for (int i = 0; i < searchCriteriaNestedAttributeList.size(); i++){
            result = searchCriteriaNestedAttributeList.get(i).isPredicate() ? Specification.where(result).or(specsNested.get(i)) : Specification.where(result).and(specsNested.get(i));
        }

        return result;
    }


}
