package ru.fds.tavrzcms3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.fds.tavrzcms3.domain.AppUser;
import ru.fds.tavrzcms3.domain.Employee;

public interface RepositoryEmployee extends JpaRepository<Employee, Long> {
    Employee findByAppUser(AppUser appUser);
    @Query(nativeQuery = true, value = "select distinct(emp.*)\n" +
                                        "from employee as emp \n" +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id\n" +
                                        "join kd as k on k.loaner_id = cp.client_id\n" +
                                        "where k.kd_id = ?1")
    Employee getEmployeeByLoanAgreement(long loanAgreement);

    @Query(nativeQuery = true, value = "select distinct(emp.*)\n" +
                                        "from employee as emp \n" +
                                        "join client_prime as cp on cp.employee_id = emp.employee_id\n" +
                                        "join dz as d on d.pledgor_id = cp.client_id\n" +
                                        "where d.dz_id = ?1")
    Employee getEmployeeByPledgeAgreement(long pledgeAgreement);

}
