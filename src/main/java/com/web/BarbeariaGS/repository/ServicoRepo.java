package com.web.BarbeariaGS.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.BarbeariaGS.models.Servico;


public interface ServicoRepo extends CrudRepository<Servico, Integer> {
      @Query(value= "select CASE WHEN count(1)>0 THEN 'true' ELSE 'false' END from servicos where id = :id", nativeQuery = true)
    public boolean exist(int id);

    @Query(value= "select CASE WHEN count(1)>0 THEN 'true' ELSE 'false' END from servicos where email = :email", nativeQuery = true)
    public boolean existsByEmail(String email);

    @Query(value= "select * from servicos where email = :email and senha = :senha", nativeQuery = true)
    public Servico login(String email, String senha);

    @Query(value = "select * from servicos where email = :email", nativeQuery = true)
    Servico findByEmail(String email);

    @Query(value = "select senha from servicos where email = :email", nativeQuery = true)
    String findSenhaByEmail(String email);

    @Query(value = "select * from servicos where email = :email and id <> :id", nativeQuery = true)
    Servico findByEmailAndIdNot(String email, int id);
}
