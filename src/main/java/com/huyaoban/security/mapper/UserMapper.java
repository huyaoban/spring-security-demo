package com.huyaoban.security.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.huyaoban.security.model.User;

@Component
public interface UserMapper {

	@Select("select * from users where username = #{username}")
	User findByUsername(@Param("username") String username);
}
