package org.example.user;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private UserMapper userMapper;

    //@Transactional(readOnly = true)
    /*
    @Transactional(@Transactional(propagation Propagation.REQUIRED)
    Параметр propagation может принимать следующие значения:
        * REQUIRED (англ. «необходимая») — это значение по умолчанию для аннотации.
    Если транзакции не существует, Spring создаст новую. Если метод вызывается из другого транзакционного метода,
    будет использована уже существующая транзакция.
        * SUPPORTS (англ. «поддерживается»). - это значение указывает Spring, что выполнять метод в режиме транзакции
    нужно только тогда, когда она уже существует. Новую транзакцию создавать не нужно.
        MANDATORY (англ. «обязательная») — метод будет выполнен при наличии - транзакции.
    Если транзакции не существует, метод сгенерирует исключение.
        * NEVER (англ. «Никогда») — этот режим является противоположностью предыдущего.
    Метод не выполнится, если транзакция существует.
    Такой режим полезен, если разработчик точно знает, что для этого метода транзакция не нужна.
        * NOT_SUPPORTED (англ. «не поддерживается») — этот режим менее строгий, чем NEVER.
    В нём уже существующая транзакция будет приостановлена и savepoint), в которую база данных вернется,
    даже если вызываемый метод сгенерирует исключение.
    */

    /*
    Первое важное свойство транзакции - режим доступа (англ. access mode). Режимы доступа бывают следующие:
        * READ WRITE (англ. «чтение и запись») — значение по умолчанию. В этом режиме возможно выполнение всех команд.
        * READ ONLY (англ. «только чтение») - в этом режиме выполнение команд INSERT, UPDATE, DELETE,
    CREATE и других будет запрещено.
    */

    /*
    Уровень изоляции (англ. isolation level) определяет, какие данные транзакция может видеть в процессе выполнения
    параллельной транзакции. Уровни бывают такие:
        * READ COMMITTED (англ. «чтение фиксированных данных») — это уровень по умолчанию.
    В текущей транзакции будут видны только те строки, которые были зафиксированы до начала её выполнения.
        * REPEATABLE READ (англ. «повторяющееся чтение») — читающая транзакция не увидит изменений данных, которые были прочитаны ею ранее.
        * SERIALIZABLE (англ. «сохраняемые») — высший уровень изоляции. Транзакции полностью изолируются друг от друга -
    каждая будет выполняться так, как будто параллельных транзакций не существует.
        * READ UNCOMMITTED (англ. «чтение нефиксированных данных») - низший уровень изоляции.
    Если несколько параллельных транзакций попытаются изменить одну и ту же строку таблицы, в окончательном варианте
    строка будет иметь то значение, которое определено всем набором успешно выполненных транзакций.
    В PostgreSQL этот уровень не реализован.
    */

    /*
    @Transactional (isolation Isolation.DEFAULT)
    Этому параметру соответствует еnum Isolation, который может принимать следующие значения:
        * DEFAULT - значение по умолчанию. Транзакция создаётся с уровнем изоляции, заданным в настройках базы данных.
    Использование этого значения требует контроля, потому что БД может быть настроена не так, как ожидает разработчик.
        * READ_COMMITTED - Этот уровень изоляции предотвращает проблему «грязного» чтения.
        * REPEATABLE_READ - этот уровень предотвращает проблему «грязного» и неповторяющегося чтения.
        * SERIALIZABLE - на этом уровне не возникает перечисленных проблем. Однако из-за необходимости последовательно
    выполнять часть параллельных транзакций существенно падает скорость взаимодействия с БД.
        * READ_UNCOMMITTED - соответствует низшему уровню изоляции. На нём возможны все перечисленные проблемы,
    возникающие в работе с параллельными транзакциями
    */

    @Override
    public List<UserDTO> getAll() {
        return userRepositoryImpl.findAll()
                .stream()
                .map(userMapper::toObj)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO save(User user) {
        User res = userRepositoryImpl.save(user);
        return userMapper.toObj(res);
    }

    @Override
    @Transactional
    public User saveUser(UserDTO userDTO) {
        //System.out.println("\u001B[38;5;44m" + "SERVICE OUTPUT--userDTO(svc): "+userDTO+ "\u001B[0m");
        User user = userMapper.toModel(userDTO);
        User savedUser = userRepositoryImpl.save(user);
        user.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        return savedUser;
    }

/*    public void checkUsers() {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, 32, sortById);
        do {
            List<User> users = userRepository.findAll(page).getContent();
            // выполнение требуемых операций с полученными пользователями
            page = page.next();
        } while (page.getOffset() < userRepository.

                count());
    }*/
}
