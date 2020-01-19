package ru.fds.tavrzcms3.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.specification.impl.SpecificationBuilderImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Search<T> {
    private final Class<T> clazz;
    private static final String SEARCH_PARAM_POSTFIX = "Option";
    private SpecificationBuilder<T> builder;

    public Search(Class<T> clazz) {
        builder = new SpecificationBuilderImpl<>();
        this.clazz = clazz;
    }

    public void withParam(Map<String, String> searchParam) throws ReflectiveOperationException {

        for(Field field : clazz.getDeclaredFields()){
            if(searchParam.containsKey(field.getName()) && !searchParam.get(field.getName()).isEmpty()){
                if(field.getType() == String.class || field.getType().getSuperclass() == Number.class){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(searchParam.get(field.getName()))
                            .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
                            .predicate(false)
                            .build();
                    builder.withCriteria(searchCriteria);
                }else if(field.getType().getSuperclass() == Enum.class){
                    Method method = field.getType().getMethod("valueOf", String.class);
                    Class enumClass = field.getType();
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(method.invoke(enumClass, searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.withCriteria(searchCriteria);
                } else if(field.getType() == LocalDate.class){
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(searchParam.get(field.getName()), dateTimeFormatter);
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(localDate)
                            .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
                            .predicate(false)
                            .build();
                    builder.withCriteria(searchCriteria);
                }
            }
        }
    }

    public void withNestedAttributeParam(Map<String, String> searchParam, String nestedAttribute) throws ReflectiveOperationException {
        Class nestedClass = clazz.getDeclaredField(nestedAttribute).getType();
        for(Field field : nestedClass.getDeclaredFields()){
            if(searchParam.containsKey(field.getName()) && !searchParam.get(field.getName()).isEmpty()){
                if(field.getType() == String.class || field.getType().getSuperclass() == Number.class){
                    SearchCriteriaNestedAttribute searchCriteria = SearchCriteriaNestedAttribute.builder()
                            .nestedObjectName(nestedAttribute)
                            .key(field.getName())
                            .value(searchParam.get(field.getName()))
                            .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
                            .predicate(false)
                            .build();
                    builder.withNestedAttributeCriteria(searchCriteria);
                }else if(field.getType().getSuperclass() == Enum.class){
                    Method method = field.getType().getMethod("valueOf", String.class);
                    Class enumClass = field.getType();
                    SearchCriteriaNestedAttribute searchCriteria = SearchCriteriaNestedAttribute.builder()
                            .nestedObjectName(nestedAttribute)
                            .key(field.getName())
                            .value(method.invoke(enumClass, searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.withNestedAttributeCriteria(searchCriteria);
                } else if(field.getType() == LocalDate.class){
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(searchParam.get(field.getName()), dateTimeFormatter);
                    SearchCriteriaNestedAttribute searchCriteria = SearchCriteriaNestedAttribute.builder()
                            .nestedObjectName(nestedAttribute)
                            .key(field.getName())
                            .value(localDate)
                            .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
                            .predicate(false)
                            .build();
                    builder.withNestedAttributeCriteria(searchCriteria);
                }
            }
        }
    }

    public void withParamClient(List<Client> clientList){
        final String CLIENT = "client";

        if(clientList.isEmpty()){
            SearchCriteria searchCriteria = SearchCriteria.builder()
                    .key(CLIENT)
                    .value(null)
                    .operation(Operations.EQUAL_IGNORE_CASE)
                    .predicate(false)
                    .build();
            builder.withCriteria(searchCriteria);
        }else {
            SearchCriteria searchCriteriaFirst = SearchCriteria.builder()
                    .key(CLIENT)
                    .value(clientList.get(0))
                    .operation(Operations.EQUAL_IGNORE_CASE)
                    .predicate(false)
                    .build();
            builder.withCriteria(searchCriteriaFirst);
            for(int i = 1; i < clientList.size(); i++){
                SearchCriteria searchCriteria = SearchCriteria.builder()
                        .key(CLIENT)
                        .value(clientList.get(i))
                        .operation(Operations.EQUAL_IGNORE_CASE)
                        .predicate(true)
                        .build();
                builder.withCriteria(searchCriteria);
            }
        }
    }

    public Specification<T> getSpecification(){
        return builder.buildSpecification();
    }
}
