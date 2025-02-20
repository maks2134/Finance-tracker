package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.dao.UserDao;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private final UserDao userDao;

	@Autowired
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public Optional<User> getUserById(Long id) {
		return userDao.findById(id);
	}

    @Override
	public List<User> getAllUsers() {
		return userDao.findAll();
	}

	@Override
	public User createOrUpdateUser(User user) {
		return userDao.save(user);
	}

	@Override
	public void deleteUser(Long id) {
		userDao.deleteById(id);
	}
}