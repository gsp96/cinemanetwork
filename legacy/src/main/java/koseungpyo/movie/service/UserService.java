package koseungpyo.movie.service;

import java.util.List;

import koseungpyo.movie.domain.User;

public interface UserService {
	List<User> getUsers();
	User getUser(String userId, String pw);
	void addUser(User user);
	void fixUser(User user);
	void delUser(int userNum);
}
