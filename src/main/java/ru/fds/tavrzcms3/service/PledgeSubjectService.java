package ru.fds.tavrzcms3.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.LandCategory;
import ru.fds.tavrzcms3.dictionary.Liquidity;
import ru.fds.tavrzcms3.dictionary.MarketSegment;
import ru.fds.tavrzcms3.dictionary.Operations;
import ru.fds.tavrzcms3.dictionary.StatusOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfAuto;
import ru.fds.tavrzcms3.dictionary.TypeOfCollateral;
import ru.fds.tavrzcms3.dictionary.TypeOfEquip;
import ru.fds.tavrzcms3.dictionary.TypeOfMonitoring;
import ru.fds.tavrzcms3.dictionary.TypeOfPledge;
import ru.fds.tavrzcms3.dictionary.TypeOfSecurities;
import ru.fds.tavrzcms3.dictionary.TypeOfTBO;
import ru.fds.tavrzcms3.dictionary.TypeOfVessel;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.domain.PledgeSubjectAuto;
import ru.fds.tavrzcms3.domain.PledgeSubjectBuilding;
import ru.fds.tavrzcms3.domain.PledgeSubjectEquipment;
import ru.fds.tavrzcms3.domain.PledgeSubjectLandLease;
import ru.fds.tavrzcms3.domain.PledgeSubjectLandOwnership;
import ru.fds.tavrzcms3.domain.PledgeSubjectRoom;
import ru.fds.tavrzcms3.domain.PledgeSubjectSecurities;
import ru.fds.tavrzcms3.domain.PledgeSubjectTBO;
import ru.fds.tavrzcms3.domain.PledgeSubjectVessel;
import ru.fds.tavrzcms3.repository.RepositoryCostHistory;
import ru.fds.tavrzcms3.repository.RepositoryMonitoring;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.specification.SearchCriteria;
import ru.fds.tavrzcms3.specification.SearchCriteriaNestedAttribute;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.SpecificationBuilderImpl;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class PledgeSubjectService {

    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryCostHistory repositoryCostHistory;
    private final RepositoryMonitoring repositoryMonitoring;

    public PledgeSubjectService(RepositoryPledgeSubject repositoryPledgeSubject,
                                RepositoryPledgeAgreement repositoryPledgeAgreement,
                                RepositoryCostHistory repositoryCostHistory,
                                RepositoryMonitoring repositoryMonitoring) {
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryCostHistory = repositoryCostHistory;
        this.repositoryMonitoring = repositoryMonitoring;
    }

    public Optional<PledgeSubject> getPledgeSubjectById(Long pledgeSubjectId){
        if(pledgeSubjectId == null)
            return Optional.empty();
        
        return repositoryPledgeSubject.findById(pledgeSubjectId);
    }

    public List<PledgeSubject> getPledgeSubjectByIds(List<Long> ids){
        return repositoryPledgeSubject.findAllByPledgeSubjectIdIn(ids);
    }

    public List<PledgeSubject> getPledgeSubjectsForPledgeAgreement(long pledgeAgreementId){
        PledgeAgreement pledgeAgreement = repositoryPledgeAgreement.getOne(pledgeAgreementId);
        return repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
    }

    public List<PledgeSubject> getPledgeSubjectByPledgeAgreement(PledgeAgreement pledgeAgreement){
        return repositoryPledgeSubject.findByPledgeAgreements(pledgeAgreement);
    }

    public List<PledgeSubject> getPledgeSubjectsForPledgeAgreements(List<PledgeAgreement> pledgeAgreementList){
        return repositoryPledgeSubject.findByPledgeAgreementsIn(pledgeAgreementList);
    }

    public List<PledgeSubject> getPledgeSubjectsFromSearch(Map<String, String> searchParam){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        Class pledgeSubjectClass = PledgeSubject.class;
        Field[] pledgeSubjectFields = pledgeSubjectClass.getDeclaredFields();
        for(Field field : pledgeSubjectFields){
            if(searchParam.containsKey(field.getName())){
                if(field.getType() == Liquidity.class && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(Liquidity.valueOf(searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);

                }else if(field.getType() == Date.class && !searchParam.get(field.getName()).isEmpty()){
                    try {
                        Date date = simpleDateFormat.parse(searchParam.get(field.getName()));
                        SearchCriteria searchCriteria = SearchCriteria.builder()
                                .key(field.getName())
                                .value(date)
                                .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                .predicate(false)
                                .build();
                        builder.with(searchCriteria);
                    } catch (ParseException e) {
                        return Collections.emptyList();
                    }

                }else if(field.getType() == StatusOfMonitoring.class && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(StatusOfMonitoring.valueOf(searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);

                }else if(field.getType() == TypeOfCollateral.class && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(TypeOfCollateral.valueOf(searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);

                }else if(field.getType() == TypeOfPledge.class && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(TypeOfPledge.valueOf(searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);

                }else if(field.getType() == TypeOfMonitoring.class && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(TypeOfMonitoring.valueOf(searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);

                }else if((field.getType() == String.class || field.getType() == double.class)
                        && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(searchParam.get(field.getName()))
                            .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                }
            }
        }

        if(searchParam.containsKey("typeOfCollateral") && !searchParam.get("typeOfCollateral").isEmpty()){

            if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.AUTO.name())){
                Class psAutoClass = PledgeSubjectAuto.class;
                Field[] psAutoFields = psAutoClass.getDeclaredFields();
                for(Field field : psAutoFields){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == Double.class || field.getType() == Integer.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectAuto")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == TypeOfAuto.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectAuto")
                                    .key(field.getName())
                                    .value(TypeOfAuto.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.EQUIPMENT.name())){
                Class psEquipmentClass = PledgeSubjectEquipment.class;
                Field[] psEquipmentFields = psEquipmentClass.getDeclaredFields();
                for (Field field : psEquipmentFields){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == Double.class || field.getType() == Integer.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectEquipment")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == TypeOfEquip.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectEquipment")
                                    .key(field.getName())
                                    .value(TypeOfEquip.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.BUILDING.name())){
                for (Field field : PledgeSubjectBuilding.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == double.class || field.getType() == int.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectBuilding")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == MarketSegment.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectBuilding")
                                    .key(field.getName())
                                    .value(MarketSegment.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.LAND_LEASE.name())){
                for(Field field : PledgeSubjectLandLease.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == double.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectLandLease")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == LandCategory.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectLandLease")
                                    .key(field.getName())
                                    .value(LandCategory.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == Date.class && !searchParam.get(field.getName()).isEmpty()){
                            try {
                                Date date = simpleDateFormat.parse(searchParam.get(field.getName()));
                                SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                        .nestedObjectName("pledgeSubjectLandLease")
                                        .key(field.getName())
                                        .value(date)
                                        .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                        .predicate(false)
                                        .build();
                                builder.withNestedAttribute(searchCriteriaNestedAttribute);
                            } catch (ParseException e) {
                                return Collections.emptyList();
                            }
                        }
                    }
                }

            }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.LAND_OWNERSHIP.name())){
                for(Field field : PledgeSubjectLandOwnership.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == double.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectLandOwnership")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == LandCategory.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectLandOwnership")
                                    .key(field.getName())
                                    .value(LandCategory.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.PREMISE.name())){
                for(Field field : PledgeSubjectRoom.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == double.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectRoom")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == MarketSegment.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectRoom")
                                    .key(field.getName())
                                    .value(MarketSegment.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.SECURITIES.name())){
                for(Field field : PledgeSubjectSecurities.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if(field.getType() == double.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectSecurities")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == TypeOfSecurities.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectSecurities")
                                    .key(field.getName())
                                    .value(TypeOfSecurities.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.TBO.name())){
                for(Field field : PledgeSubjectTBO.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == double.class || field.getType() == int.class)&& !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectTBO")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == TypeOfTBO.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectTBO")
                                    .key(field.getName())
                                    .value(TypeOfTBO.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }else if(searchParam.get("typeOfCollateral").equals(TypeOfCollateral.VESSEL.name())){
                for(Field field : PledgeSubjectVessel.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == Integer.class || field.getType() == int.class || field.getType() == String.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectVessel")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }else if(field.getType() == TypeOfVessel.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectVessel")
                                    .key(field.getName())
                                    .value(TypeOfVessel.valueOf(searchParam.get(field.getName())))
                                    .operation(Operations.EQUAL_IGNORE_CASE)
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }
        }

        Specification<PledgeSubject> spec = builder.build();

        return repositoryPledgeSubject.findAll(spec);

    }

    @Transactional
    public PledgeSubject updatePledgeSubject(PledgeSubject pledgeSubject) {
        return repositoryPledgeSubject.save(pledgeSubject);
    }

    @Transactional
    public PledgeSubject insertPledgeSubject(PledgeSubject pledgeSubject, CostHistory costHistory, Monitoring monitoring){

        pledgeSubject = repositoryPledgeSubject.save(pledgeSubject);

        costHistory.setPledgeSubject(pledgeSubject);
        repositoryCostHistory.save(costHistory);

        monitoring.setPledgeSubject(pledgeSubject);
        repositoryMonitoring.save(monitoring);

        return pledgeSubject;
    }


    public List<PledgeSubject> getPledgeSubjectByCadastralNum(String cadastralNum){
        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectBuilding_CadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectLandLease_CadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectLandOwnership_CadastralNumContainingIgnoreCase(cadastralNum));
        pledgeSubjectList.addAll(repositoryPledgeSubject.findByPledgeSubjectRoom_CadastralNumContainingIgnoreCase(cadastralNum));

        return pledgeSubjectList;
    }

    public List<PledgeSubject> getPledgeSubjectByName(String name){
        return repositoryPledgeSubject.findAllByNameContainingIgnoreCase(name);
    }
}
