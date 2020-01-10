package ru.fds.tavrzcms3.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.dictionary.excelproprities.ExcelColumnNum;
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
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectAuto;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectBuilding;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectEquipment;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectLandLease;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectLandOwnership;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectRoom;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectSecurities;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectTBO;
import ru.fds.tavrzcms3.domain.embedded.PledgeSubjectVessel;
import ru.fds.tavrzcms3.fileimport.FileImporter;
import ru.fds.tavrzcms3.fileimport.FileImporterFactory;
import ru.fds.tavrzcms3.repository.RepositoryCostHistory;
import ru.fds.tavrzcms3.repository.RepositoryMonitoring;
import ru.fds.tavrzcms3.repository.RepositoryPledgeAgreement;
import ru.fds.tavrzcms3.repository.RepositoryPledgeSubject;
import ru.fds.tavrzcms3.specification.SearchCriteria;
import ru.fds.tavrzcms3.specification.SearchCriteriaNestedAttribute;
import ru.fds.tavrzcms3.specification.SpecificationBuilder;
import ru.fds.tavrzcms3.specification.impl.SpecificationBuilderImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class PledgeSubjectService {

    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryCostHistory repositoryCostHistory;
    private final RepositoryMonitoring repositoryMonitoring;

    private final ExcelColumnNum excelColumnNum;

    public PledgeSubjectService(RepositoryPledgeSubject repositoryPledgeSubject,
                                RepositoryPledgeAgreement repositoryPledgeAgreement,
                                RepositoryCostHistory repositoryCostHistory,
                                RepositoryMonitoring repositoryMonitoring,
                                ExcelColumnNum excelColumnNum) {
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryCostHistory = repositoryCostHistory;
        this.repositoryMonitoring = repositoryMonitoring;
        this.excelColumnNum = excelColumnNum;
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

    public List<PledgeSubject> getPledgeSubjectsFromSearch(Map<String, String> searchParam) throws ReflectiveOperationException{
        final String SEARCH_PARAM_TYPE_OF_COLLATERAL = "typeOfCollateral";
        final String SEARCH_PARAM_POSTFIX = "Option";

        SpecificationBuilder builder = new SpecificationBuilderImpl();

        for(Field field : PledgeSubject.class.getDeclaredFields()){
            if(searchParam.containsKey(field.getName())){
                if((field.getType() == String.class || field.getType() == BigDecimal.class)
                        && !searchParam.get(field.getName()).isEmpty()){
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(searchParam.get(field.getName()))
                            .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);

                }else if(field.getType().getSuperclass() == Enum.class && !searchParam.get(field.getName()).isEmpty()){
                    Method method = field.getType().getMethod("valueOf", String.class);
                    Class enumClass = field.getType();
                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(method.invoke(enumClass, searchParam.get(field.getName())))
                            .operation(Operations.EQUAL_IGNORE_CASE)
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                } else if(field.getType() == LocalDate.class && !searchParam.get(field.getName()).isEmpty()){

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(searchParam.get(field.getName()), dateTimeFormatter);

                    SearchCriteria searchCriteria = SearchCriteria.builder()
                            .key(field.getName())
                            .value(localDate)
                            .operation(Operations.valueOf(searchParam.get(field.getName() + "Option")))
                            .predicate(false)
                            .build();
                    builder.with(searchCriteria);
                }
            }
        }

        if(searchParam.containsKey(SEARCH_PARAM_TYPE_OF_COLLATERAL) && !searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).isEmpty()){

            if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.AUTO.name())){
                for(Field field : PledgeSubjectAuto.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == Double.class || field.getType() == Integer.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectAuto")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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

            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.EQUIPMENT.name())){
                for (Field field : PledgeSubjectEquipment.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == Double.class || field.getType() == Integer.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectEquipment")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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

            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.BUILDING.name())){
                for (Field field : PledgeSubjectBuilding.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == double.class || field.getType() == int.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectBuilding")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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

            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.LAND_LEASE.name())){
                for(Field field : PledgeSubjectLandLease.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == double.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectLandLease")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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
                        }else if(field.getType() == LocalDate.class && !searchParam.get(field.getName()).isEmpty()){

                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
                            LocalDate localDate = LocalDate.parse(searchParam.get(field.getName()), dateTimeFormatter);

                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectLandLease")
                                    .key(field.getName())
                                    .value(localDate)
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
                                    .predicate(false)
                                    .build();
                            builder.withNestedAttribute(searchCriteriaNestedAttribute);
                        }
                    }
                }

            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.LAND_OWNERSHIP.name())){
                for(Field field : PledgeSubjectLandOwnership.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == double.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectLandOwnership")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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

            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.PREMISE.name())){
                for(Field field : PledgeSubjectRoom.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == String.class || field.getType() == double.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectRoom")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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

            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.SECURITIES.name())){
                for(Field field : PledgeSubjectSecurities.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if(field.getType() == double.class && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectSecurities")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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

            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.TBO.name())){
                for(Field field : PledgeSubjectTBO.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == double.class || field.getType() == int.class)&& !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectTBO")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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

            }else if(searchParam.get(SEARCH_PARAM_TYPE_OF_COLLATERAL).equals(TypeOfCollateral.VESSEL.name())){
                for(Field field : PledgeSubjectVessel.class.getDeclaredFields()){
                    if(searchParam.containsKey(field.getName())){
                        if((field.getType() == Integer.class || field.getType() == int.class || field.getType() == String.class)
                                && !searchParam.get(field.getName()).isEmpty()){
                            SearchCriteriaNestedAttribute searchCriteriaNestedAttribute = SearchCriteriaNestedAttribute.builder()
                                    .nestedObjectName("pledgeSubjectVessel")
                                    .key(field.getName())
                                    .value(searchParam.get(field.getName()))
                                    .operation(Operations.valueOf(searchParam.get(field.getName() + SEARCH_PARAM_POSTFIX)))
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

    public List<PledgeSubject> getNewPledgeSubjectsFromFile(File file, TypeOfCollateral typeOfCollateral) throws IOException {
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();

        do {
            countRow += 1;

            if(fileImporter.getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter()).isEmpty()){
                throw new IOException("Неверный id{"
                        + fileImporter.getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter())
                        + ") договора залога. Строка: " + countRow);
            }
            List<PledgeAgreement> pledgeAgreementList = repositoryPledgeAgreement
                    .findAllByPledgeAgreementIdIn(fileImporter
                            .getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter()));
            if(pledgeAgreementList.isEmpty()){
                throw new IOException("Договор залога с таким id отсутствует ("
                        + fileImporter.getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter())
                        + "). Строка: " + countRow);
            }

            Liquidity liquidity;
            try {
                liquidity = Liquidity.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getLiquidity()));
            }catch (IllegalArgumentException ex){
                liquidity = null;
            }

            StatusOfMonitoring statusOfMonitoring;
            try {
                statusOfMonitoring = StatusOfMonitoring.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getStatusMonitoring()));
            }catch (IllegalArgumentException ex){
                statusOfMonitoring = null;
            }

            TypeOfPledge typeOfPledge;
            try {
                typeOfPledge = TypeOfPledge.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getTypeOfPledge()));
            }catch (IllegalArgumentException ex){
                typeOfPledge = null;
            }

            TypeOfMonitoring typeOfMonitoring;
            try {
                typeOfMonitoring = TypeOfMonitoring.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getTypeOfMonitoring()));
            }catch (IllegalArgumentException ex){
                typeOfMonitoring = null;
            }

            PledgeSubject pledgeSubject = PledgeSubject.builder()
                    .name(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getName()))
                    .zsDz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsDz()))
                    .zsZz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsZz()))
                    .rsDz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsDz()))
                    .rsZz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsZz()))
                    .ss(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getSs()))
                    .dateMonitoring(fileImporter
                            .getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateMonitoring()))
                    .dateConclusion(fileImporter
                            .getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateConclusion()))
                    .adressRegion(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressRegion()))
                    .adressDistrict(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressDistrict()))
                    .adressCity(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressCity()))
                    .adressStreet(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressStreet()))
                    .adressBuilbing(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressBuilding()))
                    .adressPemises(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getAddressPremises()))
                    .insuranceObligation(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getInsuranceObligation()))
                    .liquidity(liquidity)
                    .statusMonitoring(statusOfMonitoring)
                    .typeOfCollateral(typeOfCollateral)
                    .typeOfPledge(typeOfPledge)
                    .typeOfMonitoring(typeOfMonitoring)
                    .pledgeAgreements(pledgeAgreementList)
                    .build();

            if(typeOfCollateral == TypeOfCollateral.AUTO){

                TypeOfAuto typeOfAuto;
                try {
                    typeOfAuto = TypeOfAuto.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getAuto().getTypeOfAuto()));
                }catch (IllegalArgumentException ex){
                    typeOfAuto = null;
                }

                PledgeSubjectAuto pledgeSubjectAuto = PledgeSubjectAuto.builder()
                        .brandAuto(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getBrand()))
                        .modelAuto(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getModel()))
                        .vin(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getVin()))
                        .numOfEngine(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getNumOfEngine()))
                        .numOfPTS(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getNumOfPts()))
                        .yearOfManufactureAuto(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getAuto().getYearOfManufacture()))
                        .inventoryNumAuto(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getInventoryNum()))
                        .typeOfAuto(typeOfAuto)
                        .horsepower(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getAuto().getHorsepower()))
                        .build();

                pledgeSubject.setPledgeSubjectAuto(pledgeSubjectAuto);

            }else if(typeOfCollateral == TypeOfCollateral.EQUIPMENT){

                TypeOfEquip typeOfEquip;
                try {
                    typeOfEquip = TypeOfEquip.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getTypeOfEquipment()));
                }catch (IllegalArgumentException ex){
                    typeOfEquip = null;
                }

                PledgeSubjectEquipment pledgeSubjectEquipment = PledgeSubjectEquipment.builder()
                        .brandEquip(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getBrand()))
                        .modelEquip(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getModel()))
                        .serialNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getSerialNumber()))
                        .yearOfManufactureEquip(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getEquipment().getYearOfManufacture()))
                        .inventoryNumEquip(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getInventoryNum()))
                        .typeOfEquipment(typeOfEquip)
                        .productivity(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getEquipment().getProductivity()))
                        .typeOfProductivity(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getTypeOfProductivity()))
                        .build();

                pledgeSubject.setPledgeSubjectEquipment(pledgeSubjectEquipment);

            }else if(typeOfCollateral == TypeOfCollateral.BUILDING){

                MarketSegment marketSegment;
                try {
                    marketSegment = MarketSegment.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getBuilding().getMarketSegment()));
                }catch (IllegalArgumentException ex){
                    marketSegment = null;
                }

                PledgeSubjectBuilding pledgeSubjectBuilding = PledgeSubjectBuilding.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getBuilding().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getBuilding().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getBuilding().getConditionalNum()))
                        .readinessDegree(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getBuilding().getReadinessDegree()))
                        .yearOfConstruction(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getBuilding().getYearOfConstruction()))
                        .marketSegment(marketSegment)
                        .build();

                pledgeSubject.setPledgeSubjectBuilding(pledgeSubjectBuilding);

            }else if(typeOfCollateral == TypeOfCollateral.LAND_LEASE){

                LandCategory landCategory;
                try {
                    landCategory = LandCategory.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getLandCategory()));
                }catch (IllegalArgumentException ex){
                    landCategory = null;
                }

                PledgeSubjectLandLease pledgeSubjectLandLease = PledgeSubjectLandLease.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getLandLease().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getConditionalNum()))
                        .permittedUse(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getPermittedUse()))
                        .builtUp(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getBuiltUp()))
                        .cadastralNumOfBuilding(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getCadastralNumOfBuilding()))
                        .dateBeginLease(fileImporter
                                .getLocalDate(excelColumnNum.getPledgeSubjectNew().getLandLease().getDateBegin()))
                        .dateEndLease(fileImporter
                                .getLocalDate(excelColumnNum.getPledgeSubjectNew().getLandLease().getDateEnd()))
                        .landCategory(landCategory)
                        .build();

                pledgeSubject.setPledgeSubjectLandLease(pledgeSubjectLandLease);

            }else if(typeOfCollateral == TypeOfCollateral.LAND_OWNERSHIP){

                LandCategory landCategory;
                try {
                    landCategory = LandCategory.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getLandCategory()));
                }catch (IllegalArgumentException ex){
                    landCategory = null;
                }

                PledgeSubjectLandOwnership pledgeSubjectLandOwnership = PledgeSubjectLandOwnership.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getConditionalNum()))
                        .permittedUse(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getPermittedUse()))
                        .builtUp(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getBuiltUp()))
                        .cadastralNumOfBuilding(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getCadastralNumOfBuilding()))
                        .landCategory(landCategory)
                        .build();

                pledgeSubject.setPledgeSubjectLandOwnership(pledgeSubjectLandOwnership);

            }else if(typeOfCollateral == TypeOfCollateral.PREMISE){

                MarketSegment marketSegmentRoom;
                try {
                    marketSegmentRoom = MarketSegment.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getMarketSegmentRoom()));
                }catch (IllegalArgumentException ex){
                    marketSegmentRoom = null;
                }

                MarketSegment marketSegmentBuilding;
                try {
                    marketSegmentBuilding = MarketSegment.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getMarketSegmentBuilding()));
                }catch (IllegalArgumentException ex){
                    marketSegmentBuilding = null;
                }

                PledgeSubjectRoom pledgeSubjectRoom = PledgeSubjectRoom.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getPremise().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getConditionalNum()))
                        .floorLocation(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getFloorLocation()))
                        .marketSegmentRoom(marketSegmentRoom)
                        .marketSegmentBuilding(marketSegmentBuilding)
                        .build();

                pledgeSubject.setPledgeSubjectRoom(pledgeSubjectRoom);

            }else if(typeOfCollateral == TypeOfCollateral.SECURITIES){

                TypeOfSecurities typeOfSecurities;
                try {
                    typeOfSecurities = TypeOfSecurities.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getSecurities().getTypeOfSecurities()));
                }catch (IllegalArgumentException ex){
                    typeOfSecurities = null;
                }

                PledgeSubjectSecurities pledgeSubjectSecurities = PledgeSubjectSecurities.builder()
                        .nominalValue(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getSecurities().getNominalValue()))
                        .actualValue(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getSecurities().getActualValue()))
                        .typeOfSecurities(typeOfSecurities)
                        .build();

                pledgeSubject.setPledgeSubjectSecurities(pledgeSubjectSecurities);

            }else if(typeOfCollateral == TypeOfCollateral.TBO){

                TypeOfTBO typeOfTBO;
                try {
                    typeOfTBO = TypeOfTBO.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getTbo().getTypeOfTbo()));
                }catch (IllegalArgumentException ex){
                    typeOfTBO = null;
                }

                PledgeSubjectTBO pledgeSubjectTBO = PledgeSubjectTBO.builder()
                        .countOfTBO(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getTbo().getCount()))
                        .carryingAmount(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getTbo().getCarryingAmount()))
                        .typeOfTBO(typeOfTBO)
                        .build();

                pledgeSubject.setPledgeSubjectTBO(pledgeSubjectTBO);

            }else if(typeOfCollateral == TypeOfCollateral.VESSEL){

                TypeOfVessel typeOfVessel;
                try {
                    typeOfVessel = TypeOfVessel.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getVesselType()));
                }catch (IllegalArgumentException ex){
                    typeOfVessel = null;
                }

                String imo = "";
                if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getImo()))){
                    imo = fileImporter.getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getImo()).toString();
                }

                String mmsi = "";
                if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getMmsi()))){
                    mmsi = fileImporter.getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getMmsi()).toString();
                }

                PledgeSubjectVessel pledgeSubjectVessel = PledgeSubjectVessel.builder()
                        .imo(imo)
                        .mmsi(mmsi)
                        .flag(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getFlag()))
                        .vesselType(typeOfVessel)
                        .grossTonnage(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getGrossTonnage()))
                        .deadweight(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getDeadweight()))
                        .yearBuilt(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getVessel().getYearOfBuilt()))
                        .statusVessel(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getStatus()))
                        .build();

                pledgeSubject.setPledgeSubjectVessel(pledgeSubjectVessel);
            }

            CostHistory costHistory = CostHistory.builder()
                    .dateConclusion(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateConclusion()))
                    .zsDz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsDz()))
                    .zsZz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsZz()))
                    .rsDz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsDz()))
                    .rsZz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsZz()))
                    .ss(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getSs()))
                    .appraiser(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getAppraiser()))
                    .appraisalReportNum(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getNumAppraisalReport()))
                    .appraisalReportDate(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateAppraisalReport()))
                    .build();

            Monitoring monitoring = Monitoring.builder()
                    .dateMonitoring(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateMonitoring()))
                    .statusMonitoring(statusOfMonitoring)
                    .employee(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getEmployee()))
                    .typeOfMonitoring(typeOfMonitoring)
                    .notice(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getNotice()))
                    .collateralValue(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getCollateralValue()))
                    .build();

            pledgeSubject.setCostHistories(Collections.singletonList(costHistory));
            pledgeSubject.setMonitorings(Collections.singletonList(monitoring));

            pledgeSubjectList.add(pledgeSubject);

        }while (fileImporter.nextLine());

        return pledgeSubjectList;
    }

    public List<PledgeSubject> getCurrentPledgeSubjectsFromFile(File file) throws IOException{
        FileImporter fileImporter = FileImporterFactory.getInstance(file);
        for(int i = 0; i < excelColumnNum.getStartRow(); i++){
            fileImporter.nextLine();
        }
        int countRow = excelColumnNum.getStartRow();

        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();

        do{
            countRow += 1;

            if(Objects.isNull(fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId()))){
                throw new IOException("Неверный id{"
                        + fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId()) + ") предмета залога. Строка: " + countRow);
            }
            Optional<PledgeSubject> pledgeSubject = getPledgeSubjectById(fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            Liquidity liquidity;
            try {
                liquidity = Liquidity.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectUpdate().getLiquidity()));
            }catch (IllegalArgumentException ex){
                liquidity = null;
            }

            TypeOfPledge typeOfPledge;
            try {
                typeOfPledge = TypeOfPledge.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectUpdate().getTypeOfPledge()));
            }catch (IllegalArgumentException ex){
                typeOfPledge = null;
            }

            pledgeSubject.get().setName(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getName()));
            pledgeSubject.get().setLiquidity(liquidity);
            pledgeSubject.get().setTypeOfPledge(typeOfPledge);
            pledgeSubject.get().setAdressRegion(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressRegion()));
            pledgeSubject.get().setAdressDistrict(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressDistrict()));
            pledgeSubject.get().setAdressCity(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressCity()));
            pledgeSubject.get().setAdressStreet(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressStreet()));
            pledgeSubject.get().setAdressBuilbing(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressBuilding()));
            pledgeSubject.get().setAdressPemises(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getAddressPremises()));
            pledgeSubject.get().setInsuranceObligation(fileImporter.getString(excelColumnNum.getPledgeSubjectUpdate().getInsuranceObligation()));

            if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectAuto())){

                TypeOfAuto typeOfAuto;
                try {
                    typeOfAuto = TypeOfAuto.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getTypeOfAuto()));
                }catch (IllegalArgumentException ex){
                    typeOfAuto = null;
                }

                pledgeSubject.get().getPledgeSubjectAuto().setBrandAuto(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getBrand()));
                pledgeSubject.get().getPledgeSubjectAuto().setModelAuto(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getModel()));
                pledgeSubject.get().getPledgeSubjectAuto().setVin(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getVin()));
                pledgeSubject.get().getPledgeSubjectAuto().setNumOfEngine(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getNumOfEngine()));
                pledgeSubject.get().getPledgeSubjectAuto().setNumOfPTS(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getNumOfPts()));
                pledgeSubject.get().getPledgeSubjectAuto().setYearOfManufactureAuto(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectUpdate().getAuto().getYearOfManufacture()));
                pledgeSubject.get().getPledgeSubjectAuto().setInventoryNumAuto(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getInventoryNum()));
                pledgeSubject.get().getPledgeSubjectAuto().setTypeOfAuto(typeOfAuto);
                pledgeSubject.get().getPledgeSubjectAuto().setHorsepower(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getAuto().getHorsepower()));

            }else if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectEquipment())){

                TypeOfEquip typeOfEquip;
                try {
                    typeOfEquip = TypeOfEquip.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getTypeOfEquipment()));
                }catch (IllegalArgumentException ex){
                    typeOfEquip = null;
                }

                pledgeSubject.get().getPledgeSubjectEquipment().setBrandEquip(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getBrand()));
                pledgeSubject.get().getPledgeSubjectEquipment().setModelEquip(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getModel()));
                pledgeSubject.get().getPledgeSubjectEquipment().setSerialNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getSerialNumber()));
                pledgeSubject.get().getPledgeSubjectEquipment().setYearOfManufactureEquip(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getYearOfManufacture()));
                pledgeSubject.get().getPledgeSubjectEquipment().setInventoryNumEquip(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getInventoryNum()));
                pledgeSubject.get().getPledgeSubjectEquipment().setTypeOfEquipment(typeOfEquip);
                pledgeSubject.get().getPledgeSubjectEquipment().setProductivity(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getProductivity()));
                pledgeSubject.get().getPledgeSubjectEquipment().setTypeOfProductivity(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getTypeOfProductivity()));

            }else if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectBuilding())){

                MarketSegment marketSegment;
                try {
                    marketSegment = MarketSegment.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getMarketSegment()));
                }catch (IllegalArgumentException ex){
                    marketSegment = null;
                }

                pledgeSubject.get().getPledgeSubjectBuilding().setArea(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getArea()));
                pledgeSubject.get().getPledgeSubjectBuilding().setCadastralNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getCadastralNum()));
                pledgeSubject.get().getPledgeSubjectBuilding().setConditionalNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getConditionalNum()));
                pledgeSubject.get().getPledgeSubjectBuilding().setReadinessDegree(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getReadinessDegree()));
                pledgeSubject.get().getPledgeSubjectBuilding().setYearOfConstruction(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getYearOfConstruction()));
                pledgeSubject.get().getPledgeSubjectBuilding().setMarketSegment(marketSegment);

            }else if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectLandLease())){

                LandCategory landCategory;
                try {
                    landCategory = LandCategory.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getLandCategory()));
                }catch (IllegalArgumentException ex){
                    landCategory = null;
                }

                pledgeSubject.get().getPledgeSubjectLandLease().setArea(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getArea()));
                pledgeSubject.get().getPledgeSubjectLandLease().setCadastralNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getCadastralNum()));
                pledgeSubject.get().getPledgeSubjectLandLease().setConditionalNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getConditionalNum()));
                pledgeSubject.get().getPledgeSubjectLandLease().setPermittedUse(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getPermittedUse()));
                pledgeSubject.get().getPledgeSubjectLandLease().setBuiltUp(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getBuiltUp()));
                pledgeSubject.get().getPledgeSubjectLandLease().setCadastralNumOfBuilding(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getCadastralNumOfBuilding()));
                pledgeSubject.get().getPledgeSubjectLandLease().setDateBeginLease(fileImporter
                        .getLocalDate(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getDateBegin()));
                pledgeSubject.get().getPledgeSubjectLandLease().setDateEndLease(fileImporter
                        .getLocalDate(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getDateEnd()));
                pledgeSubject.get().getPledgeSubjectLandLease().setLandCategory(landCategory);

            }else if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectLandOwnership())){

                LandCategory landCategory;
                try {
                    landCategory = LandCategory.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getLandCategory()));
                }catch (IllegalArgumentException ex){
                    landCategory = null;
                }

                pledgeSubject.get().getPledgeSubjectLandOwnership().setArea(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getArea()));
                pledgeSubject.get().getPledgeSubjectLandOwnership().setCadastralNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getCadastralNum()));
                pledgeSubject.get().getPledgeSubjectLandOwnership().setConditionalNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getConditionalNum()));
                pledgeSubject.get().getPledgeSubjectLandOwnership().setPermittedUse(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getPermittedUse()));
                pledgeSubject.get().getPledgeSubjectLandOwnership().setBuiltUp(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getBuiltUp()));
                pledgeSubject.get().getPledgeSubjectLandOwnership().setCadastralNumOfBuilding(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getCadastralNumOfBuilding()));
                pledgeSubject.get().getPledgeSubjectLandOwnership().setLandCategory(landCategory);

            }else if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectRoom())){

                MarketSegment marketSegmentRoom;
                try {
                    marketSegmentRoom = MarketSegment.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getMarketSegmentRoom()));
                }catch (IllegalArgumentException ex){
                    marketSegmentRoom = null;
                }

                MarketSegment marketSegmentBuilding;
                try {
                    marketSegmentBuilding = MarketSegment.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getMarketSegmentBuilding()));
                }catch (IllegalArgumentException ex){
                    marketSegmentBuilding = null;
                }

                pledgeSubject.get().getPledgeSubjectRoom().setArea(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getPremise().getArea()));
                pledgeSubject.get().getPledgeSubjectRoom().setCadastralNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getCadastralNum()));
                pledgeSubject.get().getPledgeSubjectRoom().setConditionalNum(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getConditionalNum()));
                pledgeSubject.get().getPledgeSubjectRoom().setFloorLocation(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getFloorLocation()));
                pledgeSubject.get().getPledgeSubjectRoom().setMarketSegmentRoom(marketSegmentRoom);
                pledgeSubject.get().getPledgeSubjectRoom().setMarketSegmentBuilding(marketSegmentBuilding);

            }else if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectSecurities())){

                TypeOfSecurities typeOfSecurities;
                try {
                    typeOfSecurities = TypeOfSecurities.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getTypeOfSecurities()));
                }catch (IllegalArgumentException ex){
                    typeOfSecurities = null;
                }

                pledgeSubject.get().getPledgeSubjectSecurities().setNominalValue(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getNominalValue()));
                pledgeSubject.get().getPledgeSubjectSecurities().setActualValue(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getActualValue()));
                pledgeSubject.get().getPledgeSubjectSecurities().setTypeOfSecurities(typeOfSecurities);

            }else if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectTBO())){

                TypeOfTBO typeOfTBO;
                try {
                    typeOfTBO = TypeOfTBO.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getTbo().getTypeOfTbo()));
                }catch (IllegalArgumentException ex){
                    typeOfTBO = null;
                }

                pledgeSubject.get().getPledgeSubjectTBO().setCountOfTBO(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectUpdate().getTbo().getCount()));
                pledgeSubject.get().getPledgeSubjectTBO().setCarryingAmount(fileImporter
                        .getDouble(excelColumnNum.getPledgeSubjectUpdate().getTbo().getCarryingAmount()));
                pledgeSubject.get().getPledgeSubjectTBO().setTypeOfTBO(typeOfTBO);

            }else if(Objects.nonNull(pledgeSubject.get().getPledgeSubjectVessel())){

                TypeOfVessel typeOfVessel;
                try {
                    typeOfVessel = TypeOfVessel.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getVesselType()));
                }catch (IllegalArgumentException ex){
                    typeOfVessel = null;
                }

                String imo = "";
                if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getImo()))){
                    imo = fileImporter.getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getImo()).toString();
                }

                String mmsi = "";
                if(Objects.nonNull(fileImporter.getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getMmsi()))){
                    mmsi = fileImporter.getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getMmsi()).toString();
                }

                pledgeSubject.get().getPledgeSubjectVessel().setImo(imo);
                pledgeSubject.get().getPledgeSubjectVessel().setMmsi(mmsi);
                pledgeSubject.get().getPledgeSubjectVessel().setFlag(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getFlag()));
                pledgeSubject.get().getPledgeSubjectVessel().setVesselType(typeOfVessel);
                pledgeSubject.get().getPledgeSubjectVessel().setGrossTonnage(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getGrossTonnage()));
                pledgeSubject.get().getPledgeSubjectVessel().setDeadweight(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getDeadweight()));
                pledgeSubject.get().getPledgeSubjectVessel().setYearBuilt(fileImporter
                        .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getYearOfBuilt()));
                pledgeSubject.get().getPledgeSubjectVessel().setStatusVessel(fileImporter
                        .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getStatus()));
            }

            pledgeSubjectList.add(pledgeSubject.get());

        }while (fileImporter.nextLine());

        return pledgeSubjectList;
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

    @Transactional
    public List<PledgeSubject> insertPledgeSubjects(List<PledgeSubject> pledgeSubjectList){
        for(PledgeSubject ps : pledgeSubjectList){
            ps = repositoryPledgeSubject.save(ps);

            CostHistory costHistory = ps.getCostHistories().get(0);
            costHistory.setPledgeSubject(ps);
            repositoryCostHistory.save(costHistory);

            Monitoring monitoring = ps.getMonitorings().get(0);
            monitoring.setPledgeSubject(ps);
            repositoryMonitoring.save(monitoring);
        }
        return pledgeSubjectList;
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
