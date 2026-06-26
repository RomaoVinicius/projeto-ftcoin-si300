package src.controller;

import java.time.LocalDate;

import src.dao.CotacaoDAO;
import src.model.Cotacao;

public class OraculoController {

    private final CotacaoDAO cotacaoDAO;

    public OraculoController(CotacaoDAO cotacaoDAO) {
        this.cotacaoDAO = cotacaoDAO;
    }

    public Cotacao consultar(LocalDate data) {
        return cotacaoDAO.consultar(data);
    }
}