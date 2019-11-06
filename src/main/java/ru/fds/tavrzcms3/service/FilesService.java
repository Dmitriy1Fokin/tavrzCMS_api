package ru.fds.tavrzcms3.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fds.tavrzcms3.domain.ClientIndividual;
import ru.fds.tavrzcms3.domain.ClientLegalEntity;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.PledgeAgreement;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FilesService {

    @Autowired
    ClientManagerService clientManagerService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ClientService clientService;
    @Autowired
    LoanAgreementService loanAgreementService;

    @Value("${path.excel.import}")
    private String pathExcelImport;
    @Value("${excel_table.import.client.client_manager_id}")
    private int numColumnClientManager;
    @Value("${excel_table.import.client.employee_id}")
    private int numColumnEmployee;
    @Value("${excel_table.import.client.legal_entity.organizational_form}")
    private int numColumnOrganizationForm;
    @Value("${excel_table.import.client.legal_entity.name}")
    private int numColumnLegalEntityName;
    @Value("${excel_table.import.client.legal_entity.inn}")
    private int numColumnInn;
    @Value("${excel_table.import.client.individual.surname}")
    private int numColumnIndividualSurname;
    @Value("${excel_table.import.client.individual.name}")
    private int numColumnIndividualName;
    @Value("${excel_table.import.client.individual.patronymic}")
    private int numColumnIndividualPatronymic;
    @Value("${excel_table.import.client.individual.pasport_number}")
    private int numColumnIndividualPasport;
    @Value("${excel_table.import.pledgeAgreement.numPA}")
    private int numColumnPledgeAgreementNumPA;
    @Value("${excel_table.import.pledge_agreement.date_begin_pa}")
    private int numColumnPledgeAgreementDateBegin;
    @Value("${excel_table.import.pledge_agreement.date_end_pa}")
    private int numColumnPledgeAgreementDateEnd;
    @Value("${excel_table.import.pledge_agreement.perv_posl}")
    private int numColumnPledgeAgreementPervPosl;
    @Value("${excel_table.import.pledge_agreement.pledgor_id}")
    private int numColumnPledgeAgreementPledgor;
    @Value("${excel_table.import.pledge_agreement.status}")
    private int numColumnPledgeAgreementStatus;
    @Value("${excel_table.import.pledge_agreement.notice}")
    private int numColumnPledgeAgreementNotice;
    @Value("${excel_table.import.pledge_agreement.la_id}")
    private int numColumnPledgeAgreementLA;



    public File uploadFile(MultipartFile file, String prefixName) throws IOException{

        InputStream inputStream = file.getInputStream();

        File inputDir = new File(pathExcelImport);
        String extension = getExtension(file.getOriginalFilename());
        String fileLocation = inputDir
                + File.separator
                + prefixName
                + new SimpleDateFormat("'_'yyyy-MM-dd'_'HH-mm-ss").format(new Date())
                + "." + extension;

        FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
        int ch = 0;
        while ((ch = inputStream.read()) != -1)
            fileOutputStream.write(ch);

        fileOutputStream.flush();
        fileOutputStream.close();

        return new File(fileLocation);
    }

    private String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = fileName.lastIndexOf(File.separator);

        if (i > p) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }

    private boolean isCellEmpty(Cell cell){
        if(cell == null)
            return true;

        if(cell.getCellType() == CellType.BLANK)
            return true;

        if(cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty())
            return true;

        return false;
    }

    private Workbook getWorkBookExcel(File file) throws IOException{
        FileInputStream fileInputStream = new FileInputStream(file);

        if(getExtension(file.getName()).equalsIgnoreCase("xlsx")){
            return new XSSFWorkbook(fileInputStream);
        }else if (getExtension(file.getName()).equalsIgnoreCase("xls")){
           return new HSSFWorkbook(fileInputStream);
        }else {
            throw new IOException("Не верный формат файла");
        }
    }

    public List<ClientLegalEntity> getClientLegalEntityFromExcel(File file) throws IOException, InvalidFormatException {

        Validator validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();
        Set<ConstraintViolation<ClientLegalEntity>> violations;

        Workbook workbook = getWorkBookExcel(file);
        Sheet sheet = workbook.getSheetAt(0);

        List<ClientLegalEntity> clientLegalEntityList = new ArrayList<>();

        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
            try {
                Row row = sheet.getRow(i);

                ClientLegalEntity clientLegalEntity = new ClientLegalEntity();

                if(isCellEmpty(row.getCell(numColumnClientManager)))
                    throw new InvalidFormatException("Отсутствует id клиентского менеджера в строке " + (i+1));
                else if(row.getCell(numColumnClientManager).getCellType()==CellType.NUMERIC) {
                    clientLegalEntity.
                            setClientManager(clientManagerService.
                                    getClientManager((long) row.getCell(numColumnClientManager).getNumericCellValue()).
                                    orElseThrow(() ->
                                            new InvalidFormatException("Клиентский менеджер с таким id(" +
                                                    row.getCell(numColumnClientManager).getNumericCellValue() +
                                                    ") отсутствует")));
                }
                else
                    throw new InvalidFormatException("Неверный формат/значение id клиентского менеджера в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnEmployee)))
                    throw new InvalidFormatException("Отсутствует id ответственного сотрудника в строке " + (i+1));
                else if(row.getCell(numColumnEmployee).getCellType()==CellType.NUMERIC)
                    clientLegalEntity.setEmployee(employeeService.
                            getEmployee((long) row.getCell(numColumnEmployee).getNumericCellValue()).
                            orElseThrow(() ->
                                    new InvalidFormatException("Ответственный сотрудник с таким id(" +
                                            row.getCell(numColumnEmployee).getNumericCellValue() + ") отсутствует")));
                else
                    throw new InvalidFormatException("Неверный формат/значение id ответственного сотрудника в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnOrganizationForm)))
                    throw new InvalidFormatException("Отсутствует значение правовой формы организации в строке " + (i+1));
                else if(row.getCell(numColumnOrganizationForm).getCellType() == CellType.STRING)
                    clientLegalEntity.setOrganizationalForm(row.getCell(numColumnOrganizationForm).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение правовой формы организации в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnLegalEntityName)))
                    throw new InvalidFormatException("Отсутствует название организации  в строке " + (i+1));
                else if(row.getCell(numColumnLegalEntityName).getCellType() == CellType.STRING)
                    clientLegalEntity.setName(row.getCell(numColumnLegalEntityName).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение названия организации в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnInn))){}
                else if(row.getCell(numColumnInn).getCellType() == CellType.NUMERIC)
                    clientLegalEntity.setInn(String.valueOf(row.getCell(numColumnInn).getNumericCellValue()));
                else if(row.getCell(numColumnInn).getCellType() == CellType.STRING)
                    clientLegalEntity.setInn(row.getCell(numColumnInn).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение ИНН в строке " + (i+1));


                violations = validator.validate(clientLegalEntity);
                if(violations.size() > 0){
                    String message = "Ошибка в строке " + (i+1) + ": ";
                    for (ConstraintViolation<ClientLegalEntity> c : violations)
                        message += c.getMessage() + "(" + c.getInvalidValue() + ") в поле: \"" + c.getPropertyPath() + "\". ";

                    throw new InvalidFormatException(message);
                }

                clientLegalEntityList.add(clientLegalEntity);

            }catch (IllegalStateException ise){
                throw new InvalidFormatException("Неверные данные в строке " + (i+1) + ".");
            }
        }

        return clientLegalEntityList;
    }

    public List<ClientIndividual> getClientIndividualFromExcel(File file) throws IOException, InvalidFormatException{

        Validator validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();
        Set<ConstraintViolation<ClientIndividual>> violations;

        Workbook workbook = getWorkBookExcel(file);
        Sheet sheet = workbook.getSheetAt(0);

        List<ClientIndividual> clientIndividualList = new ArrayList<>();

        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
            try {
                Row row = sheet.getRow(i);

                ClientIndividual clientIndividual = new ClientIndividual();

                if(isCellEmpty(row.getCell(numColumnClientManager)))
                    throw new InvalidFormatException("Отсутствует id клиентского менеджера в строке " + (i+1));
                else if(row.getCell(numColumnClientManager).getCellType()==CellType.NUMERIC) {
                    clientIndividual.
                            setClientManager(clientManagerService.
                                    getClientManager((long) row.getCell(numColumnClientManager).getNumericCellValue()).
                                    orElseThrow(() ->
                                            new InvalidFormatException("Клиентский менеджер с таким id(" +
                                                    row.getCell(numColumnClientManager).getNumericCellValue() +
                                                    ") отсутствует")));
                }
                else
                    throw new InvalidFormatException("Неверный формат/значение id клиентского менеджера в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnEmployee)))
                    throw new InvalidFormatException("Отсутствует id ответственного сотрудника в строке " + (i+1));
                else if(row.getCell(numColumnEmployee).getCellType()==CellType.NUMERIC)
                    clientIndividual.setEmployee(employeeService.
                            getEmployee((long) row.getCell(numColumnEmployee).getNumericCellValue()).
                            orElseThrow(() ->
                                    new InvalidFormatException("Ответственный сотрудник с таким id(" +
                                            row.getCell(numColumnEmployee).getNumericCellValue() + ") отсутствует")));
                else
                    throw new InvalidFormatException("Неверный формат/значение id ответственного сотрудника в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnIndividualSurname)))
                    throw new InvalidFormatException("Отсутствует фамилия в строке " + (i+1));
                else if(row.getCell(numColumnIndividualSurname).getCellType()==CellType.STRING)
                    clientIndividual.setSurname(row.getCell(numColumnIndividualSurname).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение фамилии в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnIndividualName)))
                    throw new InvalidFormatException("Отсутствует имя в строке " + (i+1));
                else if(row.getCell(numColumnIndividualName).getCellType()==CellType.STRING)
                    clientIndividual.setName(row.getCell(numColumnIndividualName).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение имени в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnIndividualPatronymic))){}
                else if(row.getCell(numColumnIndividualPatronymic).getCellType()==CellType.STRING)
                    clientIndividual.setPatronymic(row.getCell(numColumnIndividualPatronymic).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение отчества в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnIndividualPasport))){}
                else if(row.getCell(numColumnIndividualPasport).getCellType()==CellType.STRING)
                    clientIndividual.setPasportNum(row.getCell(numColumnIndividualPasport).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение паспорта в строке " + (i+1));


                violations = validator.validate(clientIndividual);
                if(violations.size() > 0){
                    String message = "Ошибка в строке " + (i+1) + ": ";
                    for (ConstraintViolation<ClientIndividual> c : violations)
                        message += c.getMessage() + "(" + c.getInvalidValue() + ") в поле: \"" + c.getPropertyPath() + "\". ";

                    throw new InvalidFormatException(message);
                }

                clientIndividualList.add(clientIndividual);


            }catch (IllegalStateException ise){
                throw new InvalidFormatException("Неверные данные в строке " + (i+1) + ".");
            }
        }

        return clientIndividualList;
    }

    public List<PledgeAgreement> getPledgeAgreementFromExcel(File file) throws IOException, InvalidFormatException{

        Validator validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();
        Set<ConstraintViolation<PledgeAgreement>> violations;

        Workbook workbook = getWorkBookExcel(file);
        Sheet sheet = workbook.getSheetAt(0);

        List<PledgeAgreement> pledgeAgreementList = new ArrayList<>();

        for(int i = 1; i < sheet.getPhysicalNumberOfRows(); i++){
            try {
                Row row = sheet.getRow(i);

                PledgeAgreement pledgeAgreement = new PledgeAgreement();

                if(isCellEmpty(row.getCell(numColumnPledgeAgreementNumPA)))
                    throw new InvalidFormatException("Отсутствует № договора залога в строке " + (i+1));
                else if(row.getCell(numColumnPledgeAgreementNumPA).getCellType()==CellType.STRING)
                    pledgeAgreement.setNumPA(row.getCell(numColumnPledgeAgreementNumPA).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение № договора залога в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnPledgeAgreementDateBegin)))
                    throw new InvalidFormatException("Отсутствует дата начала ДЗ в строке " + (i+1));
                else if(DateUtil.isCellDateFormatted(row.getCell(numColumnPledgeAgreementDateBegin)))
                    pledgeAgreement.setDateBeginPA(row.getCell(numColumnPledgeAgreementDateBegin).getDateCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение даты начала ДЗ в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnPledgeAgreementDateEnd)))
                    throw new InvalidFormatException("Отсутствует дата окончания ДЗ в строке " + (i+1));
                else if(DateUtil.isCellDateFormatted(row.getCell(numColumnPledgeAgreementDateEnd)))
                    pledgeAgreement.setDateEndPA(row.getCell(numColumnPledgeAgreementDateEnd).getDateCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение даты окончания ДЗ в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnPledgeAgreementPervPosl)))
                    throw new InvalidFormatException("Отсутствует тип договора в строке " + (i+1));
                else if(row.getCell(numColumnPledgeAgreementPervPosl).getCellType()==CellType.STRING)
                    pledgeAgreement.setPervPosl(row.getCell(numColumnPledgeAgreementPervPosl).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение типа договора в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnPledgeAgreementPledgor)))
                    throw new InvalidFormatException("Отсутствует id залогодателя в строке " + (i+1));
                else if(row.getCell(numColumnPledgeAgreementPledgor).getCellType()==CellType.NUMERIC)
                    pledgeAgreement.setClient(clientService.
                            getClientById((long) row.getCell(numColumnPledgeAgreementPledgor).getNumericCellValue()).
                            orElseThrow(()->
                                    new InvalidFormatException("Клиент с таким id(" +
                                            row.getCell(numColumnPledgeAgreementPledgor).getNumericCellValue() + ") отсутствует")));
                else
                    throw new InvalidFormatException("Неверный формат/значение id клиента в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnPledgeAgreementStatus)))
                    throw new InvalidFormatException("Отсутствует статус ДЗ в строке " + (i+1));
                else if(row.getCell(numColumnPledgeAgreementStatus).getCellType()==CellType.STRING)
                    pledgeAgreement.setStatusPA(row.getCell(numColumnPledgeAgreementStatus).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение статуса ДЗ в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnPledgeAgreementNotice))){}
                else if(row.getCell(numColumnPledgeAgreementNotice).getCellType()==CellType.STRING ||
                        row.getCell(numColumnPledgeAgreementNotice).getCellType()==CellType.NUMERIC)
                    pledgeAgreement.setNoticePA(row.getCell(numColumnPledgeAgreementNotice).getStringCellValue());
                else
                    throw new InvalidFormatException("Неверный формат/значение заметки ДЗ в строке " + (i+1));

                if(isCellEmpty(row.getCell(numColumnPledgeAgreementLA))){}
                else if(row.getCell(numColumnPledgeAgreementLA).getCellType()==CellType.NUMERIC){
                    LoanAgreement loanAgreement = loanAgreementService.
                            getLoanAgreementById((long) row.getCell(numColumnPledgeAgreementLA).getNumericCellValue()).
                            orElseThrow(()->
                                    new InvalidFormatException("КД с таким id(" +
                                            row.getCell(numColumnPledgeAgreementLA).getNumericCellValue() + ") отсутствует"));
                    List<LoanAgreement> loanAgreementList = new ArrayList<>();
                    loanAgreementList.add(loanAgreement);
                    pledgeAgreement.setLoanAgreements(loanAgreementList);
                }else
                    throw new InvalidFormatException("Неверный формат/значение id КД в строке " + (i+1));


                violations = validator.validate(pledgeAgreement);
                if(violations.size() > 0){
                    String message = "Ошибка в строке " + (i+1) + ": ";
                    for (ConstraintViolation<PledgeAgreement> c : violations)
                        message += c.getMessage() + "(" + c.getInvalidValue() + ") в поле: \"" + c.getPropertyPath() + "\". ";

                    throw new InvalidFormatException(message);
                }

                pledgeAgreementList.add(pledgeAgreement);


            }catch (IllegalStateException ise){
                throw new InvalidFormatException("Неверные данные в строке " + (i+1) + ".");
            }
        }

        return pledgeAgreementList;
    }
}
