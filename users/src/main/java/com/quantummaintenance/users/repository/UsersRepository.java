package com.quantummaintenance.users.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.users.entity.Users;

	public interface UsersRepository extends MongoRepository<Users,String> {
		public List<Users> findByCompanyId(String id);
}
