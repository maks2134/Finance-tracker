package com.myfinance.financetracker.dao.impl;

import com.myfinance.financetracker.dao.UserDao;
import com.myfinance.financetracker.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

	private final ConcurrentHashMap<Long, User> userStore = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(1);

	@Override
	public Optional<User> findById(Long id) {
		return Optional.ofNullable(userStore.get(id));
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(userStore.values());
	}

	@Override
	public User save(User user) {
		if (user.getId() == null) {
			user.setId(idGenerator.getAndIncrement());
		}
		userStore.put(user.getId(), user);
		return user;
	}

	@Override
	public void deleteById(Long id) {
		userStore.remove(id);
	}
}