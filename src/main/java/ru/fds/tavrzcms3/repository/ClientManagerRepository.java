package ru.fds.tavrzcms3.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.ClientManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional
public class ClientManagerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void insert(ClientManager clientManager) {
        entityManager.persist(clientManager);
    }

    public void update(ClientManager clientManager){
        entityManager.merge(clientManager);
    }

    public ClientManager getById(long id) {
        return entityManager.find(ClientManager.class, id);
    }

    public List<ClientManager> getAll() {
        TypedQuery<ClientManager> query = entityManager.createQuery("select cm from ClientManager cm", ClientManager.class);
        return query.getResultList();
    }

    public ClientManager getBySurname(String surname) {
        TypedQuery<ClientManager> query = entityManager.createQuery("select cm from ClientManager cm where cm.surname = :surname", ClientManager.class);
        query.setParameter("surname", surname);
        return query.getSingleResult();
    }

    public void delete(ClientManager clientManager){
        entityManager.remove(entityManager.contains(clientManager) ? clientManager : entityManager.merge(clientManager));
    }
}
