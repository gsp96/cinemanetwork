package koseungpyo.movie.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import koseungpyo.movie.dao.map.UserMap;
import koseungpyo.movie.domain.User;

@Repository
public class UserDaoImpl implements UserDao{
	@Autowired private UserMap userMap;

	@Override
	public List<User> selectUsers() {
		return userMap.selectUsers();
	}

	@Override
	public void insertUser(User user) {
		userMap.insertUser(user);
	}

	@Override
	public void updateUser(User user) {
		userMap.updateUser(user);
	}

	@Override
	public void deleteUser(int userNum) {
		userMap.deleteUser(userNum);
	}

	@Override
	public User selectUser(String userId, String pw) {
		return userMap.selectUser(userId, pw);
	}
}