package com.example.demo.servicelayer;

import java.util.List;
import java.util.Optional;
import com.example.demo.entity.User;

public interface Servicelayer {

	 User addUser(User u);

	 List<User> retieveAll();

	 Optional<User> singleUser(long id);

	 void delete(long id);

	public User updateUser(User u);

}
