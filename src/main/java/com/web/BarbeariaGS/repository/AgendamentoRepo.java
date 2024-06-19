package com.web.BarbeariaGS.repository;

import java.sql.Date;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.web.BarbeariaGS.models.Agendamento;
import com.web.BarbeariaGS.models.Cliente;
import com.web.BarbeariaGS.models.Funcionario;


public interface AgendamentoRepo extends CrudRepository<Agendamento, Integer>{
  
  @Query("SELECT a FROM Agendamento a WHERE a.cliente = :cliente AND a.status = false")
List<Agendamento> findByCliente(Cliente cliente);

@Query("SELECT a FROM Agendamento a WHERE a.data >= :data ORDER BY a.data")
List<Agendamento> findByDataGreaterThanEqualOrderByData(@Param("data") LocalDate data);

    @Query("SELECT a FROM Agendamento a WHERE a.funcionario = :funcionario AND a.status = false ORDER BY a.data")
    List<Agendamento> findByFuncionarioOrderByData(Funcionario funcionario);
    
      @Query(value= "select CASE WHEN count(1)>0 THEN 'true' ELSE 'false' END from agendamentos where id = :id", nativeQuery = true)
    public boolean exist(int id);

    @Query(value= "select CASE WHEN count(1)>0 THEN 'true' ELSE 'false' END from agendamentos where email = :email", nativeQuery = true)
    public boolean existsByEmail(String email);

    @Query(value= "select * from agendamentos where email = :email and senha = :senha", nativeQuery = true)
    public Funcionario login(String email, String senha);

    @Query(value = "select * from agendamentos where email = :email", nativeQuery = true)
    Funcionario findByEmail(String email);

    @Query(value = "select senha from agendamentos where email = :email", nativeQuery = true)
    String findSenhaByEmail(String email);

    @Query(value = "select * from agendamentos where email = :email and id <> :id", nativeQuery = true)
    Funcionario findByEmailAndIdNot(String email, int id);

    @Query("SELECT a FROM Agendamento a WHERE a.data = :data AND a.funcionario = :funcionario AND a.status = false ORDER BY a.data")
List<Agendamento> findByDataAndFuncionarioOrderByData(LocalDate data, Funcionario funcionario);


@Query("SELECT a FROM Agendamento a WHERE a.data < :data AND a.status != true")
List<Agendamento> findAgendamentosAntesDe(@Param("data") LocalDate data);

@Query(value = "SELECT h.id, h.horario " +
"FROM horarios h " +
"LEFT JOIN agendamentos a " +
"ON h.id = a.horario_id AND a.data = :dataAgendamento AND a.funcionario_id = :funcionarioId AND a.status = false " +
"WHERE a.id IS NULL " +
"ORDER BY h.horario", nativeQuery = true)
List<Object[]> findHorariosVagosByFuncionarioAndData(int funcionarioId, Date dataAgendamento);

// Querys de relatorio financeiro para funcionario

@Query("SELECT a FROM Agendamento a WHERE a.data = :data AND a.funcionario = :funcionario AND a.status = true ORDER BY a.data")
List<Agendamento> findByDataAndFuncionarioOrderByDataWithStatus1(LocalDate data, Funcionario funcionario);
@Query(value = "SELECT SUM(s.preco) " +
                 "FROM agendamentos a " +
                 "JOIN servicos s ON a.servico_id = s.id " +
                 "WHERE a.status = 1 " +
                 "AND a.funcionario_id = :funcionarioId " +
                 "AND a.data = :data", nativeQuery = true)
  Double findTotalValueByFuncionarioAndData(@Param("funcionarioId") int funcionarioId, @Param("data") LocalDate data);

    // Query to get appointments within a date range
    @Query(value = "SELECT * FROM agendamentos " +
    "WHERE funcionario_id = :funcionarioId " +
    "AND data BETWEEN :startDate AND :endDate " +
    "AND status = 1", nativeQuery = true)
List<Agendamento> findByFuncionarioAndDateRange(@Param("funcionarioId") int funcionarioId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

// Query to sum the service prices of completed appointments within a date range
@Query(value = "SELECT SUM(s.preco) " +
    "FROM agendamentos a " +
    "JOIN servicos s ON a.servico_id = s.id " +
    "WHERE a.status = 1 " +
    "AND a.funcionario_id = :funcionarioId " +
    "AND a.data BETWEEN :startDate AND :endDate", nativeQuery = true)
Double findTotalValueByFuncionarioAndDateRange(@Param("funcionarioId") int funcionarioId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

// Querys de relatorio financeiro para cliente

@Query("SELECT a FROM Agendamento a WHERE a.data = :data AND a.cliente = :cliente AND a.status = true ORDER BY a.data")
List<Agendamento> findByDataAndClienteOrderByDataWithStatus1(LocalDate data, Cliente cliente);

@Query(value = "SELECT SUM(s.preco) " +
                 "FROM agendamentos a " +
                 "JOIN servicos s ON a.servico_id = s.id " +
                 "WHERE a.status = 1 " +
                 "AND a.cliente_id = :clienteId " +
                 "AND a.data = :data", nativeQuery = true)
Double findTotalValueByClienteAndData(@Param("clienteId") int clienteId, @Param("data") LocalDate data);

// Query to get appointments within a date range for a client
@Query(value = "SELECT * FROM agendamentos " +
    "WHERE cliente_id = :clienteId " +
    "AND data BETWEEN :startDate AND :endDate " +
    "AND status = 1", nativeQuery = true)
List<Agendamento> findByClienteAndDateRange(@Param("clienteId") int clienteId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

// Query to sum the service prices of completed appointments within a date range for a client
@Query(value = "SELECT SUM(s.preco) " +
    "FROM agendamentos a " +
    "JOIN servicos s ON a.servico_id = s.id " +
    "WHERE a.status = 1 " +
    "AND a.cliente_id = :clienteId " +
    "AND a.data BETWEEN :startDate AND :endDate", nativeQuery = true)
Double findTotalValueByClienteAndDateRange(@Param("clienteId") int clienteId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);

}
