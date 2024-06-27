package org.example.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final EntityManager entityManager;
    private final UserJPARepository userJPARepository ;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager, UserJPARepository userJPARepository) {
        this.entityManager = entityManager;
        this.userJPARepository = userJPARepository;
    }

/*    public List<User> searchByEmailDomain(String domain) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.like(root.get("email"), "%"+domain));
        return entityManager.createQuery(cr).getResultList();
    }*/

    @Transactional
    public User save(User user) {
        System.out.println("id: "+user.getId());
        if (user.getId() == null) {
            return userJPARepository.saveAndFlush(user);
            //entityManager.persist(user);
        } else {
            user = entityManager.merge(user);
            return user;
        }
    }

    public List<User> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root);

        return entityManager.createQuery(cr).getResultList();
    }
}
