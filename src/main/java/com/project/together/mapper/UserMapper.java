package com.project.together.mapper;

import com.project.together.domain.*;
import org.apache.ibatis.annotations.Mapper;

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

    User findIDByPhoneNum(String phone);

    User findIDByEmail(String email);

    User findPWByEmail(String userInput);

    User findPWByPhoneNum(String userInput);
}
