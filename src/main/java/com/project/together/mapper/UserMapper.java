package com.project.together.mapper;

import com.project.together.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface UserMapper {


    int idCheck(String username);

    int emailCheck(String userEmail);

    void joinUser(User user);

    User findByUsername(String username);

    List<K5_Player> getK5Players();

    List<K7_Player> getK7Players();

    List<W1_Player> getW1Players();

    void delete(User user);

    List<User> findUsersByEmail(String userInput);

    List<User> findUsersByPhoneNumber(String userInput);

    User findByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

    void updateUserPassword(User user);


}
