package org.example.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class UserRepositoryImpl {
    private final EntityManager entityManager;
    //@Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager=entityManager;
    }

/*    public List<User> searchByEmailDomain(String domain) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.like(root.get("email"), "%"+domain));
        return entityManager.createQuery(cr).getResultList();
    }*/

    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    public List<User> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root);

        return entityManager.createQuery(cr).getResultList();
    }
}
