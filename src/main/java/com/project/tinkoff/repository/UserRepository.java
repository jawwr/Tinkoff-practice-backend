package com.project.tinkoff.repository;

import com.project.tinkoff.repository.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String userLogin);

    @Query(value = """
            select count(*) <> 0
            from users
            where login = :#{#login}
            """, nativeQuery = true)
    boolean existUserByLogin(String login);

    User findUserById(long id);

    @Modifying
    @Query(value = """
            delete from users
            where id = :#{#id};
            """, nativeQuery = true)
    void deleteUserById(long id);
}
