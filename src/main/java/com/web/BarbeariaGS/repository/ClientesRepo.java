package com.web.BarbeariaGS.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.BarbeariaGS.models.Cliente;

public interface ClientesRepo extends CrudRepository<Cliente, Integer>{

    @Query(value= "select CASE WHEN count(1)>0 THEN 'true' ELSE 'false' END from clientes where id = :id", nativeQuery = true)
    public boolean exist(int id);

    @Query(value= "select CASE WHEN count(1)>0 THEN 'true' ELSE 'false' END from clientes where email = :email", nativeQuery = true)
    public boolean existsByEmail(String email);

    @Query(value = "select * from clientes where email = :email", nativeQuery = true)
    Cliente findByEmail(String email);

    @Query(value = "select senha from clientes where email = :email", nativeQuery = true)
    String findSenhaByEmail(String email);

    @Query(value= "select * from clientes where email = :email and senha = :senha", nativeQuery = true)
    public Cliente login(String email, String senha);

}