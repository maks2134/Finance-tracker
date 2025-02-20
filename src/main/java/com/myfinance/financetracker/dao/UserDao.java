package com.myfinance.financetracker.dao;

import com.myfinance.financetracker.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
	Optional<User> findById(Long id);

	List<User> findAll();

	User save(User user);

	void deleteById(Long id);
}