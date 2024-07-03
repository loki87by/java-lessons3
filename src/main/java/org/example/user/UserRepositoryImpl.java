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
    private final UserJPARepository userJPARepository;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager, UserJPARepository userJPARepository) {
        this.entityManager = entityManager;
        this.userJPARepository = userJPARepository;
    }

    @Transactional
    public User save(User user) {
        final User savedUser;

        if (user.getId() == null) {
            savedUser = userJPARepository.saveAndFlush(user);
        } else {
            savedUser = entityManager.merge(user);
        }
        return savedUser;
    }

    public List<User> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root);

        return entityManager.createQuery(cr).getResultList();
    }

/*    public User findByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.equal(root.get("email"), email));
        return entityManager.createQuery(cr).getSingleResult();
    }*/
}
