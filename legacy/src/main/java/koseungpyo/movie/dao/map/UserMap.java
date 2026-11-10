package koseungpyo.movie.dao.map;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import koseungpyo.movie.domain.User;

public interface UserMap {
	List<User> selectUsers();
	User selectUser(@Param("userId") String userId, @Param("pw")String pw);
	void insertUser(User user);
	void updateUser(User user);
	void deleteUser(int userNum);
}