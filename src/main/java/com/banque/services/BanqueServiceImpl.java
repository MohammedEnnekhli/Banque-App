package com.banque.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banque.dao.CompteRepository;
import com.banque.dao.OperationRepository;
import com.banque.entities.Compte;
import com.banque.entities.CompteCourant;
import com.banque.entities.Operation;
import com.banque.entities.Retrait;
import com.banque.entities.Versement;

@Service
@Transactional
public class BanqueServiceImpl implements IBanqueService {
    @Autowired
    private CompteRepository    compteRepository;
    @Autowired
    private OperationRepository operationRepository;

    @Override
    public Compte consulterCompte( String codeCompte ) {
        Compte compte = compteRepository.findById( codeCompte ).orElse( null );
        if ( compte == null )
            throw new RuntimeException( "Compte introuvable" );
        return compte;
    }

    @Override
    public void verser( String codeCompte, double montant ) {
        Compte compte = consulterCompte( codeCompte );
        Versement versement = new Versement( new Date(), montant, compte );
        operationRepository.save( versement );
        compte.setSolde( compte.getSolde() + montant );
        compteRepository.save( compte );
    }

    @Override
    public void retirer( String codeCompte, double montant ) {
        Compte compte = consulterCompte( codeCompte );
        double facilitesCaisse = 0;
        if ( compte instanceof CompteCourant )
            facilitesCaisse = ( (CompteCourant) compte ).getDecouvert();
        if ( compte.getSolde() + facilitesCaisse < montant )
            throw new RuntimeException( "Solde insuffisant" );
        Retrait retrait = new Retrait( new Date(), montant, compte );
        operationRepository.save( retrait );
        compte.setSolde( compte.getSolde() - montant );
        compteRepository.save( compte );

    }

    @Override
    public void virement( String codeCompte1, String CodeCompte2, double montant ) {
        if ( codeCompte1.equals( CodeCompte2 ) )
            throw new RuntimeException( "Impossible d'effectuer un virement sur le meme compte" );
        retirer( codeCompte1, montant );
        verser( CodeCompte2, montant );

    }

    @Override
    public Page<Operation> listOpertaion( String codeCompte, int page, int size ) {

        return operationRepository.listOperation( codeCompte, PageRequest.of( page, size ) );

    }
}