package src.controller;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import src.dao.CotacaoDAO;
import src.dao.MovimentacaoDAO;
import src.model.Cotacao;
import src.model.Movimentacao;
import src.model.TipoMovimentacao;

public class MovimentacaoController {

    private MovimentacaoDAO movimentacaoDAO;
    private CotacaoDAO cotacaoDAO;

    public MovimentacaoController(MovimentacaoDAO movimentacaoDAO, CotacaoDAO cotacaoDAO) {
        this.movimentacaoDAO = movimentacaoDAO;
        this.cotacaoDAO = cotacaoDAO;
    }

    // Registra uma COMPRA
    public boolean registrarCompra(int idCarteira, LocalDate data, BigDecimal quantidade) {
        Cotacao cotacao = cotacaoDAO.consultar(data);
        if (cotacao == null) {
            System.out.println("Erro: cotação não encontrada para a data " + data);
            return false;
        }

        Movimentacao m = new Movimentacao();
        m.setIdCarteira(idCarteira);
        m.setDataOperacao(data);
        m.setTipoMovimentacao(TipoMovimentacao.COMPRA);
        m.setQuantidade(quantidade);
        m.setCotacaoNaData(cotacao.getValor());

        movimentacaoDAO.inserir(m);
        return true;
    }

    // Registra uma VENDA (valida saldo antes)
    public boolean registrarVenda(int idCarteira, LocalDate data, BigDecimal quantidade) {
        BigDecimal saldo = calcularSaldo(idCarteira);
        if (quantidade.compareTo(saldo) > 0) {
            System.out.println("Erro: saldo insuficiente. Saldo atual: " + saldo);
            return false;
        }

        Cotacao cotacao = cotacaoDAO.consultar(data);
        if (cotacao == null) {
            System.out.println("Erro: cotação não encontrada para a data " + data);
            return false;
        }

        Movimentacao m = new Movimentacao();
        m.setIdCarteira(idCarteira);
        m.setDataOperacao(data);
        m.setTipoMovimentacao(TipoMovimentacao.VENDA);
        m.setQuantidade(quantidade);
        m.setCotacaoNaData(cotacao.getValor());

        movimentacaoDAO.inserir(m);
        return true;
    }

    // Calcula o saldo de moedas da carteira (compras - vendas)
    public BigDecimal calcularSaldo(int idCarteira) {
        List<Movimentacao> lista = movimentacaoDAO.listarPorCarteira(idCarteira);
        BigDecimal saldo = BigDecimal.ZERO;
        for (Movimentacao m : lista) {
            if (m.getTipoMovimentacao() == TipoMovimentacao.COMPRA) {
                saldo = saldo.add(m.getQuantidade());
            } else {
                saldo = saldo.subtract(m.getQuantidade());
            }
        }
        return saldo;
    }

    // Lista todas as movimentações de uma carteira
    public List<Movimentacao> listarPorCarteira(int idCarteira) {
        return movimentacaoDAO.listarPorCarteira(idCarteira);
    }

    // Lista todas as movimentações
    public List<Movimentacao> listarTodos() {
        return movimentacaoDAO.listarTodas();
    }

    // Busca movimentação por ID
    public Movimentacao buscarPorId(int id) {
        return movimentacaoDAO.consultar(id);
    }

    // Remove uma movimentação
    public void deletar(int id) {
        movimentacaoDAO.excluir(id);
    }
}