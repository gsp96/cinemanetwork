package koseungpyo.movie.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	private int userNum;
	private String userId;
	private String pw;
	private String userName;
	private LocalDate birthDate;
	private LocalDate regDate;
	private String email;
	private int phoneNumber;
}