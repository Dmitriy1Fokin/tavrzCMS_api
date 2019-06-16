package ru.fds.tavrzcms3.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fds.tavrzcms3.domain.ClientLegalEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional
public class ClientLegalEntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void insert(ClientLegalEntity clientLegalEntity) {
        entityManager.persist(clientLegalEntity);
    }

    public void update(ClientLegalEntity clientLegalEntity){
        entityManager.merge(clientLegalEntity);
    }

    public ClientLegalEntity getById(long id) {
        return entityManager.find(ClientLegalEntity.class, id);
    }

    public List<ClientLegalEntity> getAll() {
        TypedQuery<ClientLegalEntity> query = entityManager.createQuery("select cle from ClientLegalEntity cle", ClientLegalEntity.class);
        return query.getResultList();
    }

    public ClientLegalEntity getByName(String name) {
        TypedQuery<ClientLegalEntity> query = entityManager.createQuery("select cle from ClientLegalEntity cle where cle.name = :name", ClientLegalEntity.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public void delete(ClientLegalEntity clientLegalEntity){
        entityManager.remove(entityManager.contains(clientLegalEntity) ? clientLegalEntity : entityManager.merge(clientLegalEntity));
    }
}
