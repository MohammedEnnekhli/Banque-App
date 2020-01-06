package com.banque.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.banque.entities.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query( "select o from Operation o where o.compte.codeCompte=?1 order by o.dateOperation desc" )
    public Page<Operation> listOperation( String codeCompte, PageRequest pageRequest );
}
