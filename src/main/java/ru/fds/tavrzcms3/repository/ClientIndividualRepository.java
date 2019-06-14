package ru.fds.tavrzcms3.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.ClientIndividual;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional
public class ClientIndividualRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void insert(ClientIndividual clientIndividual) {
        entityManager.persist(clientIndividual);
    }

    public void update(ClientIndividual clientIndividual){
        entityManager.merge(clientIndividual);
    }

    public ClientIndividual getById(long id) {
        return entityManager.find(ClientIndividual.class, id);
    }

    public List<ClientIndividual> getAll() {
        TypedQuery<ClientIndividual> query = entityManager.createQuery("select ci from ClientIndividual ci", ClientIndividual.class);
        return query.getResultList();
    }

    public ClientIndividual getBySurname(String surname) {
        TypedQuery<ClientIndividual> query = entityManager.createQuery("select ci from ClientIndividual ci where ci.surname = :surname", ClientIndividual.class);
        query.setParameter("surname", surname);
        return query.getSingleResult();
    }

    public void delete(ClientIndividual clientIndividual){
        entityManager.remove(entityManager.contains(clientIndividual) ? clientIndividual : entityManager.merge(clientIndividual));
    }
}
