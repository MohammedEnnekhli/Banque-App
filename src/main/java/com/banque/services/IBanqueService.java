package com.banque.services;

import org.springframework.data.domain.Page;

import com.banque.entities.Compte;
import com.banque.entities.Operation;

public interface IBanqueService {
    public Compte consulterCompte( String codeCompte );

    public void verser( String codeCompte, double montant );

    public void retirer( String codeCompte, double montant );

    public void virement( String codeCompte1, String CodeCompte2, double montant );

    public Page<Operation> listOpertaion( String codeCompte, int page, int size );

}
