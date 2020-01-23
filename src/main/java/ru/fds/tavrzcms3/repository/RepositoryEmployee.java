package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fds.tavrzcms3.domain.Employee;

import java.util.List;

public interface RepositoryEmployee extends JpaRepository<Employee, Long> {
    List<Employee> findAllByEmployeeIdNot(Long employeeId);

    @Query(nativeQuery = true, value = "select distinct(emp.*)\n" +
                                        "from employee as emp \n" +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id\n" +
                                        "join kd as k on k.loaner_id = cp.client_id\n" +
                                        "where k.kd_id = :loanAgreementId")
    Employee getEmployeeByLoanAgreement(@Param("loanAgreementId") Long loanAgreementId);

    @Query(nativeQuery = true, value = "select distinct(emp.*)\n" +
                                        "from employee as emp \n" +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id\n" +
                                        "join dz as d on d.pledgor_id = cp.client_id\n" +
                                        "where d.dz_id = :pledgeAgreementId")
    Employee getEmployeeByPledgeAgreement(@Param("pledgeAgreementId") Long pledgeAgreementId);

}
