package src.controller;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import src.dao.MovimentacaoDAO;
import src.model.Movimentacao;
import src.model.TipoMovimentacao;

public class RelatorioController {

    private final CarteiraDAO carteiraDAO;
    private final MovimentacaoDAO movimentacaoDAO;

    public RelatorioController(CarteiraDAO carteiraDAO, MovimentacaoDAO movimentacaoDAO) {
        this.carteiraDAO = carteiraDAO;
        this.movimentacaoDAO = movimentacaoDAO;
    }

    //requisito 1: listar carteiras ordenadas por ID
    public List<Carteira> listarOrdenadasPorId() {
        List<Carteira> lista = carteiraDAO.listarTodas();
        lista.sort(Comparator.comparingInt(Carteira::getIdentificador));
        return lista;
    }

    //requisito 2: listar carteiras ordenadas por Nome
    public List<Carteira> listarOrdenadasPorNome() {
        List<Carteira> lista = carteiraDAO.listarTodas();
        lista.sort(Comparator.comparing(Carteira::getNomeTitular, String.CASE_INSENSITIVE_ORDER));
        return lista;
    }

    //requisito 3: calcular saldo de criptomoedas de uma carteira
    public BigDecimal calcularSaldoAtual(int idCarteira) {
        if (carteiraDAO.consultar(idCarteira) == null) {
            throw new IllegalArgumentException("Carteira não existe.");
        }
        
        BigDecimal saldo = BigDecimal.ZERO;
        for (Movimentacao m : movimentacaoDAO.listarTodas()) {
            if (m.getIdCarteira() == idCarteira) {
                if (m.getTipoMovimentacao() == TipoMovimentacao.COMPRA) {
                    saldo = saldo.add(m.getQuantidade());
                } else {
                    saldo = saldo.subtract(m.getQuantidade());
                }
            }
        }
        return saldo;
    }

    //requisito 4: obter histórico ordenado por data
    public List<Movimentacao> obterHistoricoOrdenado(int idCarteira) {
        if (carteiraDAO.consultar(idCarteira) == null) {
            throw new IllegalArgumentException("Carteira não existe.");
        }
        return movimentacaoDAO.listarTodas().stream()
                .filter(m -> m.getIdCarteira() == idCarteira)
                .sorted(Comparator.comparing(Movimentacao::getDataOperacao))
                .collect(Collectors.toList());
    }

    //requisito 5: calcular ganho ou perda total (usando cotação das movimentações)
    public BigDecimal calcularGanhoOuPerda(int idCarteira) {
        if (carteiraDAO.consultar(idCarteira) == null) {
            throw new IllegalArgumentException("Carteira não existe.");
        }

        BigDecimal totalGasto = BigDecimal.ZERO;
        BigDecimal totalRecebido = BigDecimal.ZERO;
        BigDecimal saldoTokens = BigDecimal.ZERO;

        for (Movimentacao m : movimentacaoDAO.listarTodas()) {
            if (m.getIdCarteira() == idCarteira) {
                BigDecimal valorDaOperacao = m.getQuantidade().multiply(m.getCotacaoNaData());
                if (m.getTipoMovimentacao() == TipoMovimentacao.COMPRA) {
                    totalGasto = totalGasto.add(valorDaOperacao);
                    saldoTokens = saldoTokens.add(m.getQuantidade());
                } else {
                    totalRecebido = totalRecebido.add(valorDaOperacao);
                    saldoTokens = saldoTokens.subtract(m.getQuantidade());
                }
            }
        }

        //usa a cotação da última movimentação como cotação atual (aproximação)
        BigDecimal cotacaoAtual = buscarUltimaCotacaoDaCarteira(idCarteira);
        BigDecimal patrimonioHoje = saldoTokens.multiply(cotacaoAtual);

        return patrimonioHoje.add(totalRecebido).subtract(totalGasto);
    }

    //método auxiliar: pega a cotação da movimentação mais recente da carteira
    private BigDecimal buscarUltimaCotacaoDaCarteira(int idCarteira) {
        return movimentacaoDAO.listarTodas().stream()
                .filter(m -> m.getIdCarteira() == idCarteira)
                .max(Comparator.comparing(Movimentacao::getDataOperacao))
                .map(Movimentacao::getCotacaoNaData)
                .orElse(BigDecimal.ZERO);
    }
}