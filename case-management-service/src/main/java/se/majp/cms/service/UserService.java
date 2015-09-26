package se.majp.cms.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import se.majp.cms.model.User;

public interface UserService
{
	User addOrUpdateUser(User user);

	void removeUser(String userId);

	List<User> findByFirstNameOrLastNameOrEmail(String value);

	User findByEmail(String email);

	User findByUserId(String userId);

	List<User> findByProject(String projectId);

	List<User> findAllUsers(Pageable page);
}
