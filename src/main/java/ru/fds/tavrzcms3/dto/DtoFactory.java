package ru.fds.tavrzcms3.dto;

import org.springframework.stereotype.Component;
import ru.fds.tavrzcms3.converver.dtoconverter.ClientConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.ClientManagerConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.CostHistoryConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.EmployeeConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.EncumbranceConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.InsuranceConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.LoanAgreementConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.MonitoringConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.PledgeAgreementConverterDto;
import ru.fds.tavrzcms3.converver.dtoconverter.PledgeSubjectConverterDto;
import ru.fds.tavrzcms3.domain.Client;
import ru.fds.tavrzcms3.domain.ClientManager;
import ru.fds.tavrzcms3.domain.CostHistory;
import ru.fds.tavrzcms3.domain.Employee;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.Insurance;
import ru.fds.tavrzcms3.domain.LoanAgreement;
import ru.fds.tavrzcms3.domain.Monitoring;
import ru.fds.tavrzcms3.domain.PledgeAgreement;
import ru.fds.tavrzcms3.domain.PledgeSubject;

import java.util.ArrayList;
import java.util.List;

@Component
public class DtoFactory {

    private final PledgeSubjectConverterDto pledgeSubjectConverterDto;
    private final LoanAgreementConverterDto loanAgreementConverterDto;
    private final PledgeAgreementConverterDto pledgeAgreementConverterDto;
    private final CostHistoryConverterDto costHistoryConverterDto;
    private final MonitoringConverterDto monitoringConverterDto;
    private final ClientConverterDto clientConverterDto;
    private final ClientManagerConverterDto clientManagerConverterDto;
    private final EmployeeConverterDto employeeConverterDto;
    private final EncumbranceConverterDto encumbranceConverterDto;
    private final InsuranceConverterDto insuranceConverterDto;

    public DtoFactory(PledgeSubjectConverterDto pledgeSubjectConverterDto,
                      LoanAgreementConverterDto loanAgreementConverterDto,
                      PledgeAgreementConverterDto pledgeAgreementConverterDto,
                      CostHistoryConverterDto costHistoryConverterDto,
                      MonitoringConverterDto monitoringConverterDto,
                      ClientConverterDto clientConverterDto,
                      ClientManagerConverterDto clientManagerConverterDto,
                      EmployeeConverterDto employeeConverterDto,
                      EncumbranceConverterDto encumbranceConverterDto,
                      InsuranceConverterDto insuranceConverterDto) {
        this.pledgeSubjectConverterDto = pledgeSubjectConverterDto;
        this.loanAgreementConverterDto = loanAgreementConverterDto;
        this.pledgeAgreementConverterDto = pledgeAgreementConverterDto;
        this.costHistoryConverterDto = costHistoryConverterDto;
        this.monitoringConverterDto = monitoringConverterDto;
        this.clientConverterDto = clientConverterDto;
        this.clientManagerConverterDto = clientManagerConverterDto;
        this.employeeConverterDto = employeeConverterDto;
        this.encumbranceConverterDto = encumbranceConverterDto;
        this.insuranceConverterDto = insuranceConverterDto;
    }

    public PledgeSubjectDto getPledgeSubjectDto(PledgeSubject pledgeSubject){

        return pledgeSubjectConverterDto.toDto(pledgeSubject);
    }


    public List<PledgeSubjectDto> getPledgeSubjectsDto(List<PledgeSubject> pledgeSubjectList){
        List<PledgeSubjectDto> dtoList = new ArrayList<>();
        for(PledgeSubject pledgeSubject: pledgeSubjectList)
            dtoList.add(getPledgeSubjectDto(pledgeSubject));

        return dtoList;
    }

    public PledgeSubject getPledgeSubjectEntity(PledgeSubjectDto pledgeSubjectDto){

        return pledgeSubjectConverterDto.toEntity(pledgeSubjectDto);
    }

    public List<PledgeSubject> getPledgeSubjectsEntity(List<PledgeSubjectDto> pledgeSubjectDtoList){
        List<PledgeSubject> pledgeSubjectList = new ArrayList<>();
        for(PledgeSubjectDto psDto : pledgeSubjectDtoList)
            pledgeSubjectList.add(getPledgeSubjectEntity(psDto));

        return pledgeSubjectList;
    }

    public LoanAgreementDto getLoanAgreementDto(LoanAgreement loanAgreement){
        return loanAgreementConverterDto.toDto(loanAgreement);
    }

    public List<LoanAgreementDto> getLoanAgreementsDto(List<LoanAgreement> loanAgreementList){
        List<LoanAgreementDto> dtoList = new ArrayList<>(    );
        for(LoanAgreement loanAgreement: loanAgreementList)
            dtoList.add(loanAgreementConverterDto.toDto(loanAgreement));

        return dtoList;
    }

    public LoanAgreement getLoanAgreementEntity(LoanAgreementDto loanAgreementDto){
        return loanAgreementConverterDto.toEntity(loanAgreementDto);
    }

    public PledgeAgreementDto getPledgeAgreementDto(PledgeAgreement pledgeAgreement){
        return pledgeAgreementConverterDto.toDto(pledgeAgreement);
    }

    public List<PledgeAgreementDto> getPledgeAgreementsDto(List<PledgeAgreement> pledgeAgreementList){
        List<PledgeAgreementDto> dtoList = new ArrayList<>();
        for(PledgeAgreement pledgeAgreement: pledgeAgreementList)
            dtoList.add(getPledgeAgreementDto(pledgeAgreement));

        return dtoList;
    }

    public PledgeAgreement getPledgeAgreementEntity(PledgeAgreementDto pledgeAgreementDto){
        return pledgeAgreementConverterDto.toEntity(pledgeAgreementDto);
    }

    public CostHistoryDto getCostHistoryDto(CostHistory costHistory){
        return costHistoryConverterDto.toDto(costHistory);
    }

    public List<CostHistoryDto> getCostHistoriesDto(List<CostHistory> costHistoryList){
        List<CostHistoryDto> dtoList = new ArrayList<>();
        for (CostHistory ch : costHistoryList)
            dtoList.add(getCostHistoryDto(ch));

        return dtoList;
    }

    public CostHistory getCostHistoryEntity(CostHistoryDto costHistoryDto){
        return costHistoryConverterDto.toEntity(costHistoryDto);
    }

    public List<CostHistory> getCostHistoriesEntity(List<CostHistoryDto> costHistoryDtoList){
        List<CostHistory> dtoList = new ArrayList<>();
        for (CostHistoryDto ch : costHistoryDtoList)
            dtoList.add(getCostHistoryEntity(ch));

        return dtoList;
    }

    public MonitoringDto getMonitoringDto(Monitoring monitoring){
        return monitoringConverterDto.toDto(monitoring);
    }

    public List<MonitoringDto> getMonitoringsDto(List<Monitoring> monitoringList){
        List<MonitoringDto> dtoList = new ArrayList<>();
        for (Monitoring mon : monitoringList)
            dtoList.add(getMonitoringDto(mon));

        return dtoList;
    }

    public Monitoring getMonitoringEntity(MonitoringDto monitoringDto){
        return monitoringConverterDto.toEntity(monitoringDto);
    }

    public List<Monitoring> getMonitoringsEntity(List<MonitoringDto> monitoringDtoList){
        List<Monitoring> dtoList = new ArrayList<>();
        for (MonitoringDto mon : monitoringDtoList)
            dtoList.add(getMonitoringEntity(mon));

        return dtoList;
    }

    public ClientDto getClientDto(Client client){
        return clientConverterDto.toDto(client);
    }

    public List<ClientDto> getClientsDto(List<Client> clientList){
        return clientConverterDto.toDto(clientList);
    }

    public Client getClientEntity(ClientDto clientDto){
        return clientConverterDto.toEntity(clientDto);
    }

    public ClientManagerDto getClientManagerDto(ClientManager clientManager){
        return clientManagerConverterDto.toDto(clientManager);
    }

    public List<ClientManagerDto> getClientManagersDto(List<ClientManager> clientManagerList){
        return clientManagerConverterDto.toDto(clientManagerList);
    }

    public EmployeeDto getEmployeeDto(Employee employee){
        return employeeConverterDto.toDto(employee);
    }

    public List<EmployeeDto> getEmployeesDto(List<Employee> employeeList){
        return employeeConverterDto.toDto(employeeList);
    }

    public EncumbranceDto getEncumbranceDto(Encumbrance encumbrance){
        return encumbranceConverterDto.toDto(encumbrance);
    }

    public List<EncumbranceDto> getEncumbrancesDto(List<Encumbrance> encumbranceList){
        return encumbranceConverterDto.toDto(encumbranceList);
    }

    public Encumbrance getEncumbranceEntity(EncumbranceDto encumbranceDto){
        return encumbranceConverterDto.toEntity(encumbranceDto);
    }

    public InsuranceDto getInsuranceDto(Insurance insurance){
        return insuranceConverterDto.toDto(insurance);
    }

    public List<InsuranceDto> getInsurancesDto(List<Insurance> insuranceList){
        return insuranceConverterDto.toDto(insuranceList);
    }

    public Insurance getInsuranceEntity(InsuranceDto insuranceDto){
        return insuranceConverterDto.toEntity(insuranceDto);
    }

}
