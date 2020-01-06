package com.banque;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.banque.dao.ClientRepository;
import com.banque.dao.CompteRepository;
import com.banque.dao.OperationRepository;
import com.banque.entities.Client;
import com.banque.entities.Compte;
import com.banque.entities.CompteCourant;
import com.banque.entities.CompteEpargne;
import com.banque.entities.Retrait;
import com.banque.entities.Versement;
import com.banque.services.IBanqueService;

@SpringBootApplication
public class BanqueApplication implements CommandLineRunner {
    @Autowired
    private ClientRepository    clientRepository;
    @Autowired
    private CompteRepository    compteRepository;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private IBanqueService      banqueService;

    public static void main( String[] args ) {
        SpringApplication.run( BanqueApplication.class, args );

    }

    @Override
    public void run( String... args ) throws Exception {
        Client c1 = clientRepository.save( new Client( "Hassan", "hassan@gmail.com" ) );
        Client c2 = clientRepository.save( new Client( "Rachid", "rachid@gmail.com" ) );

        Compte cp1 = compteRepository.save( new CompteCourant( "c1", new Date(), 90000, c1, 6000 ) );
        Compte cp2 = compteRepository.save( new CompteEpargne( "c2", new Date(), 60000, c2, 5.5 ) );

        operationRepository.save( new Versement( new Date(), 9000, cp1 ) );
        operationRepository.save( new Versement( new Date(), 6000, cp1 ) );
        operationRepository.save( new Versement( new Date(), 2300, cp1 ) );
        operationRepository.save( new Retrait( new Date(), 9000, cp1 ) );

        operationRepository.save( new Versement( new Date(), 4000, cp2 ) );
        operationRepository.save( new Versement( new Date(), 2500, cp2 ) );
        operationRepository.save( new Versement( new Date(), 1000, cp2 ) );
        operationRepository.save( new Retrait( new Date(), 1500, cp2 ) );

        banqueService.verser( "c1", 111111 );

    }

}
