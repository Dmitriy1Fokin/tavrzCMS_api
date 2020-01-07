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
import ru.fds.tavrzcms3.validate.ValidatorEntity;

import javax.validation.ConstraintViolation;
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
import java.util.Set;

@Service
public class PledgeSubjectService {

    private final RepositoryPledgeSubject repositoryPledgeSubject;
    private final RepositoryPledgeAgreement repositoryPledgeAgreement;
    private final RepositoryCostHistory repositoryCostHistory;
    private final RepositoryMonitoring repositoryMonitoring;

    private final ValidatorEntity validatorEntity;
    private final ExcelColumnNum excelColumnNum;

    public PledgeSubjectService(RepositoryPledgeSubject repositoryPledgeSubject,
                                RepositoryPledgeAgreement repositoryPledgeAgreement,
                                RepositoryCostHistory repositoryCostHistory,
                                RepositoryMonitoring repositoryMonitoring,
                                ValidatorEntity validatorEntity,
                                ExcelColumnNum excelColumnNum) {
        this.repositoryPledgeSubject = repositoryPledgeSubject;
        this.repositoryPledgeAgreement = repositoryPledgeAgreement;
        this.repositoryCostHistory = repositoryCostHistory;
        this.repositoryMonitoring = repositoryMonitoring;
        this.validatorEntity = validatorEntity;
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

            PledgeSubject pledgeSubject = PledgeSubject.builder()
                    .name(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getName()))
                    .liquidity(Liquidity.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getLiquidity())))
                    .zsDz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsDz()))
                    .zsZz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsDz()))
                    .rsDz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsDz()))
                    .rsZz(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsDz()))
                    .ss(fileImporter
                            .getBigDecimal(excelColumnNum.getPledgeSubjectNew().getSs()))
                    .dateMonitoring(fileImporter
                            .getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateMonitoring()))
                    .dateConclusion(fileImporter
                            .getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateConclusion()))
                    .statusMonitoring(StatusOfMonitoring.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getStatusMonitoring())))
                    .typeOfCollateral(typeOfCollateral)
                    .typeOfPledge(TypeOfPledge.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getTypeOfPledge())))
                    .typeOfMonitoring(TypeOfMonitoring.valueOf(fileImporter
                            .getString(excelColumnNum.getPledgeSubjectNew().getTypeOfMonitoring())))
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
                    .pledgeAgreements(repositoryPledgeAgreement.findAllByPledgeAgreementIdIn(fileImporter
                            .getLongList(excelColumnNum.getPledgeSubjectNew().getPledgeAgreementsIds(), excelColumnNum.getDelimiter())))
                    .build();

            if(typeOfCollateral == TypeOfCollateral.AUTO){
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
                        .typeOfAuto(TypeOfAuto.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getAuto().getTypeOfAuto())))
                        .horsepower(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getAuto().getHorsepower()))
                        .build();

                pledgeSubject.setPledgeSubjectAuto(pledgeSubjectAuto);

            }else if(typeOfCollateral == TypeOfCollateral.EQUIPMENT){
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
                        .typeOfEquipment(TypeOfEquip.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getTypeOfEquipment())))
                        .productivity(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getEquipment().getProductivity()))
                        .typeOfProductivity(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getEquipment().getTypeOfProductivity()))
                        .build();

                pledgeSubject.setPledgeSubjectEquipment(pledgeSubjectEquipment);

            }else if(typeOfCollateral == TypeOfCollateral.BUILDING){
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
                        .marketSegment(MarketSegment.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getBuilding().getMarketSegment())))
                        .build();

                pledgeSubject.setPledgeSubjectBuilding(pledgeSubjectBuilding);

            }else if(typeOfCollateral == TypeOfCollateral.LAND_LEASE){

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
                        .landCategory(LandCategory.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandLease().getLandCategory())))
                        .build();

                pledgeSubject.setPledgeSubjectLandLease(pledgeSubjectLandLease);

            }else if(typeOfCollateral == TypeOfCollateral.LAND_OWNERSHIP){

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
                        .landCategory(LandCategory.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getLandOwnership().getLandCategory())))
                        .build();

                pledgeSubject.setPledgeSubjectLandOwnership(pledgeSubjectLandOwnership);

            }else if(typeOfCollateral == TypeOfCollateral.PREMISE){

                PledgeSubjectRoom pledgeSubjectRoom = PledgeSubjectRoom.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getPremise().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getConditionalNum()))
                        .floorLocation(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getFloorLocation()))
                        .marketSegmentRoom(MarketSegment.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getMarketSegmentRoom())))
                        .marketSegmentBuilding(MarketSegment.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getPremise().getMarketSegmentBuilding())))
                        .build();

                pledgeSubject.setPledgeSubjectRoom(pledgeSubjectRoom);

            }else if(typeOfCollateral == TypeOfCollateral.SECURITIES){

                PledgeSubjectSecurities pledgeSubjectSecurities = PledgeSubjectSecurities.builder()
                        .nominalValue(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getSecurities().getNominalValue()))
                        .actualValue(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getSecurities().getActualValue()))
                        .typeOfSecurities(TypeOfSecurities.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getSecurities().getTypeOfSecurities())))
                        .build();

                pledgeSubject.setPledgeSubjectSecurities(pledgeSubjectSecurities);

            }else if(typeOfCollateral == TypeOfCollateral.TBO){

                PledgeSubjectTBO pledgeSubjectTBO = PledgeSubjectTBO.builder()
                        .countOfTBO(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectNew().getTbo().getCount()))
                        .carryingAmount(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectNew().getTbo().getCarryingAmount()))
                        .typeOfTBO(TypeOfTBO.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getTbo().getTypeOfTbo())))
                        .build();

                pledgeSubject.setPledgeSubjectTBO(pledgeSubjectTBO);

            }else if(typeOfCollateral == TypeOfCollateral.VESSEL){

                PledgeSubjectVessel pledgeSubjectVessel = PledgeSubjectVessel.builder()
                        .imo(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getImo()))
                        .mmsi(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getMmsi()))
                        .flag(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getFlag()))
                        .vesselType(TypeOfVessel.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectNew().getVessel().getVesselType())))
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

            Set<ConstraintViolation<PledgeSubject>> violationsPledgeSubject = validatorEntity.validateEntity(pledgeSubject);
            if(!violationsPledgeSubject.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            CostHistory costHistory = CostHistory.builder()
                    .dateConclusion(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateConclusion()))
                    .zsDz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsDz()))
                    .zsZz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getZsZZ()))
                    .rsDz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsDz()))
                    .rsZz(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getRsZZ()))
                    .ss(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getSs()))
                    .appraiser(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getAppraiser()))
                    .appraisalReportNum(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getNumAppraisalReport()))
                    .appraisalReportDate(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateAppraisalReport()))
                    .build();

            Set<ConstraintViolation<CostHistory>> violationsCostHistory = validatorEntity.validateEntity(costHistory);
            if(!violationsCostHistory.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

            Monitoring monitoring = Monitoring.builder()
                    .dateMonitoring(fileImporter.getLocalDate(excelColumnNum.getPledgeSubjectNew().getDateMonitoring()))
                    .statusMonitoring(StatusOfMonitoring.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getStatusMonitoring())))
                    .employee(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getEmployee()))
                    .typeOfMonitoring(TypeOfMonitoring.valueOf(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getTypeOfMonitoring())))
                    .notice(fileImporter.getString(excelColumnNum.getPledgeSubjectNew().getNotice()))
                    .collateralValue(fileImporter.getBigDecimal(excelColumnNum.getPledgeSubjectNew().getCollateralValue()))
                    .build();

            Set<ConstraintViolation<Monitoring>> violationsMonitoring = validatorEntity.validateEntity(monitoring);
            if(!violationsMonitoring.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

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
                        + fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId()) + ") предмета залога");
            }

            Optional<PledgeSubject> pledgeSubject = getPledgeSubjectById(fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId()));
            if(!pledgeSubject.isPresent()){
                throw new IOException("Предмет залога с таким id отсутствует ("
                        + fileImporter.getLong(excelColumnNum.getPledgeSubjectUpdate().getPledgeSubjectId())
                        + "). Строка: " + countRow);
            }

            pledgeSubject.get().setName(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getName()));
            pledgeSubject.get().setLiquidity(Liquidity.valueOf(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getLiquidity())));
            pledgeSubject.get().setTypeOfPledge(TypeOfPledge.valueOf(fileImporter
                    .getString(excelColumnNum.getPledgeSubjectUpdate().getTypeOfPledge())));
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

            if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.AUTO){
                PledgeSubjectAuto pledgeSubjectAuto = PledgeSubjectAuto.builder()
                        .brandAuto(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getBrand()))
                        .modelAuto(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getModel()))
                        .vin(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getVin()))
                        .numOfEngine(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getNumOfEngine()))
                        .numOfPTS(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getNumOfPts()))
                        .yearOfManufactureAuto(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getAuto().getYearOfManufacture()))
                        .inventoryNumAuto(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getInventoryNum()))
                        .typeOfAuto(TypeOfAuto.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getAuto().getTypeOfAuto())))
                        .horsepower(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getAuto().getHorsepower()))
                        .build();

                pledgeSubject.get().setPledgeSubjectAuto(pledgeSubjectAuto);

            }else if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.EQUIPMENT){
                PledgeSubjectEquipment pledgeSubjectEquipment = PledgeSubjectEquipment.builder()
                        .brandEquip(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getBrand()))
                        .modelEquip(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getModel()))
                        .serialNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getSerialNumber()))
                        .yearOfManufactureEquip(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getYearOfManufacture()))
                        .inventoryNumEquip(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getInventoryNum()))
                        .typeOfEquipment(TypeOfEquip.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getTypeOfEquipment())))
                        .productivity(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getProductivity()))
                        .typeOfProductivity(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getEquipment().getTypeOfProductivity()))
                        .build();

                pledgeSubject.get().setPledgeSubjectEquipment(pledgeSubjectEquipment);

            }else if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.BUILDING){
                PledgeSubjectBuilding pledgeSubjectBuilding = PledgeSubjectBuilding.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getConditionalNum()))
                        .readinessDegree(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getReadinessDegree()))
                        .yearOfConstruction(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getYearOfConstruction()))
                        .marketSegment(MarketSegment.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getBuilding().getMarketSegment())))
                        .build();

                pledgeSubject.get().setPledgeSubjectBuilding(pledgeSubjectBuilding);

            }else if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.LAND_LEASE){

                PledgeSubjectLandLease pledgeSubjectLandLease = PledgeSubjectLandLease.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getConditionalNum()))
                        .permittedUse(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getPermittedUse()))
                        .builtUp(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getBuiltUp()))
                        .cadastralNumOfBuilding(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getCadastralNumOfBuilding()))
                        .dateBeginLease(fileImporter
                                .getLocalDate(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getDateBegin()))
                        .dateEndLease(fileImporter
                                .getLocalDate(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getDateEnd()))
                        .landCategory(LandCategory.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandLease().getLandCategory())))
                        .build();

                pledgeSubject.get().setPledgeSubjectLandLease(pledgeSubjectLandLease);

            }else if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.LAND_OWNERSHIP){

                PledgeSubjectLandOwnership pledgeSubjectLandOwnership = PledgeSubjectLandOwnership.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getConditionalNum()))
                        .permittedUse(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getPermittedUse()))
                        .builtUp(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getBuiltUp()))
                        .cadastralNumOfBuilding(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getCadastralNumOfBuilding()))
                        .landCategory(LandCategory.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getLandOwnership().getLandCategory())))
                        .build();

                pledgeSubject.get().setPledgeSubjectLandOwnership(pledgeSubjectLandOwnership);

            }else if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.PREMISE){

                PledgeSubjectRoom pledgeSubjectRoom = PledgeSubjectRoom.builder()
                        .area(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getPremise().getArea()))
                        .cadastralNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getCadastralNum()))
                        .conditionalNum(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getConditionalNum()))
                        .floorLocation(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getFloorLocation()))
                        .marketSegmentRoom(MarketSegment.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getMarketSegmentRoom())))
                        .marketSegmentBuilding(MarketSegment.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getPremise().getMarketSegmentBuilding())))
                        .build();

                pledgeSubject.get().setPledgeSubjectRoom(pledgeSubjectRoom);

            }else if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.SECURITIES){

                PledgeSubjectSecurities pledgeSubjectSecurities = PledgeSubjectSecurities.builder()
                        .nominalValue(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getNominalValue()))
                        .actualValue(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getActualValue()))
                        .typeOfSecurities(TypeOfSecurities.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getSecurities().getTypeOfSecurities())))
                        .build();

                pledgeSubject.get().setPledgeSubjectSecurities(pledgeSubjectSecurities);

            }else if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.TBO){

                PledgeSubjectTBO pledgeSubjectTBO = PledgeSubjectTBO.builder()
                        .countOfTBO(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getTbo().getCount()))
                        .carryingAmount(fileImporter
                                .getDouble(excelColumnNum.getPledgeSubjectUpdate().getTbo().getCarryingAmount()))
                        .typeOfTBO(TypeOfTBO.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getTbo().getTypeOfTbo())))
                        .build();

                pledgeSubject.get().setPledgeSubjectTBO(pledgeSubjectTBO);

            }else if(pledgeSubject.get().getTypeOfCollateral() == TypeOfCollateral.VESSEL){

                PledgeSubjectVessel pledgeSubjectVessel = PledgeSubjectVessel.builder()
                        .imo(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getImo()))
                        .mmsi(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getMmsi()))
                        .flag(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getFlag()))
                        .vesselType(TypeOfVessel.valueOf(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getVesselType())))
                        .grossTonnage(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getGrossTonnage()))
                        .deadweight(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getDeadweight()))
                        .yearBuilt(fileImporter
                                .getInteger(excelColumnNum.getPledgeSubjectUpdate().getVessel().getYearOfBuilt()))
                        .statusVessel(fileImporter
                                .getString(excelColumnNum.getPledgeSubjectUpdate().getVessel().getStatus()))
                        .build();

                pledgeSubject.get().setPledgeSubjectVessel(pledgeSubjectVessel);
            }

            Set<ConstraintViolation<PledgeSubject>> violationsPledgeSubject = validatorEntity.validateEntity(pledgeSubject.get());
            if(!violationsPledgeSubject.isEmpty())
                throw new IOException("В строке:" + countRow + ". " + validatorEntity.getErrorMessage());

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
