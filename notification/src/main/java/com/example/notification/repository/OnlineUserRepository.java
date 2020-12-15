package com.example.notification.repository;

import com.example.notification.entity.OnlineUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineUserRepository extends CrudRepository<OnlineUser, String> {}