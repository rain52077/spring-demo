package com.spring.demo.mapper;

import com.spring.demo.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    @Select("select * from users where username=#{username}")
    User getUserByName(@Param("username") String username);
}
