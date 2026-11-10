package koseungpyo.movie.dao;

import java.util.List;

import koseungpyo.movie.domain.User;


public interface UserDao {
	List<User> selectUsers();
	User selectUser(String userId, String pw);
	void insertUser(User user);
	void updateUser(User user);
	void deleteUser(int userNum);
}
