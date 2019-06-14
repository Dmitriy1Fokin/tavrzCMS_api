package ru.fds.tavrzcms3.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.Employee;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional
public class EmployeeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void insert(Employee employee) {
        entityManager.persist(employee);
    }

    public void update(Employee employee){
        entityManager.merge(employee);
    }

    public Employee getById(long id) {
        return entityManager.find(Employee.class, id);
    }

    public List<Employee> getAll() {
        TypedQuery<Employee> query = entityManager.createQuery("select e from Employee e", Employee.class);
        return query.getResultList();
    }

    public Employee getBySurname(String surname) {
        TypedQuery<Employee> query = entityManager.createQuery("select e from Employee e where e.surname = :surname", Employee.class);
        query.setParameter("surname", surname);
        return query.getSingleResult();
    }

    public void delete(Employee employee){
        entityManager.remove(entityManager.contains(employee) ? employee : entityManager.merge(employee));
    }
}
