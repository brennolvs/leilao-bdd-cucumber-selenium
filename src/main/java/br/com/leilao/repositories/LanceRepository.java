package br.com.leilao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.leilao.model.Lance;

@Repository
public interface LanceRepository extends JpaRepository<Lance, Long> {

}
