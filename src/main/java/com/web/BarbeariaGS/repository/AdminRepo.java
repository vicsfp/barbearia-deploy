package com.web.BarbeariaGS.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.BarbeariaGS.models.Admin;
import com.web.BarbeariaGS.models.Funcionario;

public interface AdminRepo extends CrudRepository<Admin, Integer>{
    
    @Query(value= "select CASE WHEN count(1)>0 THEN 'true' ELSE 'false' END from administradores where id = :id", nativeQuery = true)
    public boolean exist(int id);

    @Query(value= "select CASE WHEN count(1)>0 THEN 'true' ELSE 'false' END from administradores where email = :email", nativeQuery = true)
    public boolean existsByEmail(String email);

    @Query(value = "select * from administradores where email = :email", nativeQuery = true)
    Admin findByEmail(String email);

    @Query(value = "select senha from administradores where email = :email", nativeQuery = true)
    String findSenhaByEmail(String email);

    @Query(value= "select * from administradores where email = :email and senha = :senha", nativeQuery = true)
    public Admin login(String email, String senha);

      @Query(value = "select * from administradores where email = :email and id <> :id", nativeQuery = true)
    Admin findByEmailAndIdNot(String email, int id);
}
