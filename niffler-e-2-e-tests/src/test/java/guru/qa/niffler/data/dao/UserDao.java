package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {

  UserEntity createUser(UserEntity userEntity);

  Optional<UserEntity> findById(UUID id);

  Optional<UserEntity> findByUsername(String username);

  void delete(UserEntity userEntity);
}