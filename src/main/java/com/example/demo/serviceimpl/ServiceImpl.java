package com.example.demo.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.servicelayer.Servicelayer;

@Service
public class ServiceImpl implements Servicelayer {

	private final UserRepository userrepository;

	private static final Logger log = LoggerFactory.getLogger(ServiceImpl.class);

	public ServiceImpl(UserRepository userrepository) {
		this.userrepository = userrepository;
	}

	@Override
	public User addUser(User u) {

		return userrepository.save(u);
	}

	@Override
	public List<User> retieveAll() {
		List<User> user = userrepository.findAll();
		log.error("user not found");
		return user;
	}

	@Override
	public void delete(long id) {
		userrepository.deleteById(id);

	}

	@Override
	public User updateUser(User u) {
		if (u.getUsername() == null) {
			throw new UsernameNotFoundException("username not found here ");
		}

		return userrepository.save(u);
	}

	@Override
	public Optional<User> singleUser(long id) {

		return userrepository.findById(id);
	}

}
