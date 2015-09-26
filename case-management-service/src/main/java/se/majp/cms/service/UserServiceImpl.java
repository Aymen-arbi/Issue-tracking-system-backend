package se.majp.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import se.majp.cms.exception.EntityNotFoundException;
import se.majp.cms.exception.UniqueConstraintException;
import se.majp.cms.model.Project;
import se.majp.cms.model.Story;
import se.majp.cms.model.User;
import se.majp.cms.repository.ProjectRepository;
import se.majp.cms.repository.StoryRepository;
import se.majp.cms.repository.UserRepository;
import se.majp.cms.util.IdGenerator;

public class UserServiceImpl implements UserService
{
	@Autowired
	private StoryRepository storyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProjectRepository projectRepository;

	private final IdGenerator idGenerator = IdGenerator.getBuilder().length(8).characters('0', 'z').build();

	@Override
	public User addOrUpdateUser(User user)
	{
		if (user.getUserId() == null)
		{
			if (userRepository.findByEmail(user.getEmail()) != null)
			{
				throw new UniqueConstraintException("User with that email already exists");
			}

			user = new User(idGenerator.getNextId(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName());
			return userRepository.save(user);
		}

		User userFromDb = userRepository.findByUserId(user.getUserId());

		if (userFromDb == null)
		{
			throw new EntityNotFoundException("No user with that userId");
		}

		userFromDb.setEmail(user.getEmail());
		userFromDb.setPassword(user.getPassword());
		userFromDb.setFirstName(user.getFirstName());
		userFromDb.setLastName(user.getLastName());
		
		return userRepository.save(userFromDb);
	}

	@Override
	public void removeUser(String userId)
	{
		User user = userRepository.findByUserId(userId);

		if (user == null)
		{
			throw new EntityNotFoundException("No user found with userId: " + userId);
		}

		projectRepository.findAllProjectsForUser(user).forEach(project -> {
			project.getTeam().removeUser(user);
			projectRepository.save(project);
		});

		user.getStories().forEach(story -> {
			removeStoryFromUser(story);
		});

		userRepository.delete(user.getId());
	}

	@Override
	public List<User> findByFirstNameOrLastNameOrEmail(String value)
	{
		List<User> users = userRepository.findByFirstNameOrLastNameOrEmail(value);

		if (users.size() > 0)
		{
			return users;
		}

		throw new EntityNotFoundException("No user matching that value");
	}

	@Override
	public User findByEmail(String email)
	{
		User user = userRepository.findByEmail(email);

		if (user == null)
		{
			throw new EntityNotFoundException("User with that email does not exist");
		}

		return user;
	}

	@Override
	public User findByUserId(String userId)
	{
		User user = userRepository.findByUserId(userId);

		if (user == null)
		{
			throw new EntityNotFoundException("user not found");
		}

		return user;
	}

	@Override
	public List<User> findByProject(String projectId)
	{
		Project project = projectRepository.findByProjectId(projectId);

		if (project == null)
		{
			throw new EntityNotFoundException("project not found");
		}

		return userRepository.findByProject(projectId);
	}

	@Override
	public List<User> findAllUsers(Pageable page)
	{
		List<User> users = new ArrayList<>();
		users.addAll(userRepository.findAll(page).getContent());
		return users;
	}

	private void removeStoryFromUser(Story story)
	{
		if (story.getUser() == null)
		{
			throw new IllegalArgumentException("this story is not asigned to any user");
		}

		story.setUser(null);
		storyRepository.save(story);
	}

}
