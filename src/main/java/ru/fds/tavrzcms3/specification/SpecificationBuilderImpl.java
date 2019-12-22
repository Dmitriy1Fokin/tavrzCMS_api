package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilderImpl implements SpecificationBuilder {

    private final List<SearchCriteria> searchCriteriaList;
    private final List<SearchCriteriaNestedAttribute> searchCriteriaNestedAttributeList;

    public SpecificationBuilderImpl(){
        searchCriteriaList = new ArrayList<>();
        searchCriteriaNestedAttributeList = new ArrayList<>();
    }

    @Override
    public void with(SearchCriteria searchCriteria) {
        searchCriteriaList.add(searchCriteria);
    }

    @Override
    public void withNestedAttribute(SearchCriteriaNestedAttribute searchCriteriaNestedAttribute) {
        searchCriteriaNestedAttributeList.add(searchCriteriaNestedAttribute);
    }

    @Override
    public Specification build() {
        if(searchCriteriaList.isEmpty() && searchCriteriaNestedAttributeList.isEmpty()){
            return null;
        }

        List<Specification> specs = new ArrayList<>();
        for (SearchCriteria sc : searchCriteriaList)
            specs.add(new SpecificationImpl(sc));

        List<Specification> specsNested = new ArrayList<>();
        for (SearchCriteriaNestedAttribute sc : searchCriteriaNestedAttributeList)
            specsNested.add(new SpecificationNestedAttribute(sc));


        Specification result = specs.get(0);

        for(int i = 1; i < searchCriteriaList.size(); i++){
            result = searchCriteriaList.get(i).isPredicate() ? Specification.where(result).or(specs.get(i)) : Specification.where(result).and(specs.get(i));
        }

        for (int i = 0; i < searchCriteriaNestedAttributeList.size(); i++){
            result = searchCriteriaNestedAttributeList.get(i).isPredicate() ? Specification.where(result).or(specsNested.get(i)) : Specification.where(result).and(specsNested.get(i));
        }

        return result;
    }


}
