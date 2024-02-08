package com.example.demo.servicelayer;

import java.util.List;
import java.util.Optional;
import com.example.demo.entity.User;

public interface Servicelayer {

	public User addUser(User u);

	public List<User> retieveAll();

	public Optional<User> singleUser(long id);

	public void delete(long id);

	public User updateUser(User u);

}
