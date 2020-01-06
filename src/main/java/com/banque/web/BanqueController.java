package com.banque.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banque.entities.Compte;
import com.banque.entities.Operation;
import com.banque.services.IBanqueService;

@Controller
public class BanqueController {
    @Autowired
    IBanqueService banqueService;

    @GetMapping( value = "/operations" )
    public String index() {
        return "comptes";

    }

    @GetMapping( value = "/" )
    public String home() {
        return "comptes";

    }

    @GetMapping( value = "/login" )
    public String login() {
        return "login";

    }

    @GetMapping( value = "/403" )
    public String accessDenied() {
        return "403";

    }

    @GetMapping( value = "/consulterCompte" )
    public String consulter( String codeCompte, Model model, @RequestParam( name = "page", defaultValue = "0" ) int p,
            @RequestParam( name = "size", defaultValue = "5" ) int s ) {
        try {
            Compte compte = banqueService.consulterCompte( codeCompte );
            Page<Operation> page = banqueService.listOpertaion( codeCompte, p, s );
            int[] pages = new int[page.getTotalPages()];
            model.addAttribute( "listOperations", page.getContent() );
            model.addAttribute( "pages", pages );
            model.addAttribute( "currentPage", p );
            model.addAttribute( "size", s );
            model.addAttribute( "compte", compte );
        } catch ( Exception e ) {
            model.addAttribute( "exception", e );
        }

        return "comptes";

    }

    @PostMapping( value = "/saveOperation" )
    public String saveOperation( Model model, String typeOperation,
            String codeCompte, double montant,
            String codeCompte2 ) {

        try {
            if ( typeOperation.equals( "VERS" ) ) {
                banqueService.verser( codeCompte, montant );
            } else if ( typeOperation.equals( "RETR" ) ) {
                banqueService.retirer( codeCompte, montant );
            }
            if ( typeOperation.equals( "VIR" ) ) {
                banqueService.virement( codeCompte, codeCompte2, montant );
            }

        } catch ( Exception e ) {
            model.addAttribute( "error", e );
            return "redirect:/consulterCompte?codeCompte=" + codeCompte + "&error=" + e.getMessage();
        }
        return "redirect:/consulterCompte?codeCompte=" + codeCompte;

    }
}
