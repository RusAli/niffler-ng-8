package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;

public interface AuthorityDao {

  void createAuthority(List<AuthorityEntity> authorityEntities);
}