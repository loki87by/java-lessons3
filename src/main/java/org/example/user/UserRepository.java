package org.example.user;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository /*extends JpaRepository<User, Long>*/ {
    /*@Transactional
    User save(User user);

    @Transactional
    List<User> findAll();*/
   /* private static final List<User> FAKE_USERS = createManyFakeUsers(3);
    //private static final List<User> users = new ArrayList<>();

    public List<User> findAll() {
        return FAKE_USERS;
    }

    public User save(User user) {
        //throw new UnsupportedOperationException("Метод save() еще не готов.");
      //  users.add(user);
        return user;
    }

    private static List<User> createManyFakeUsers(int count) {
        List<User> fakeUsers = new ArrayList<>();
        for(long id = 1; id <= count; id++) {
            fakeUsers.add(createFakeUser(id));
        }
        return Collections.unmodifiableList(fakeUsers);
    }

    private static User createFakeUser(long id) {
        String name = "Akakiy Akakievich #"+id;
        String email = "mail"+id+"@example.com";
        User user = new User();
        user.setId(id);
        //user.setName(name);
        user.setEmail(email);
        return user;
    }*/
}
