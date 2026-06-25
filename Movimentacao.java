import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa uma movimentação (compra ou venda) em uma carteira de moeda virtual.
 */
public class Movimentacao {
    private int idMovimento;
    private int idCarteira;
    private LocalDate dataOperacao;
    private TipoMovimentacao tipoMovimentacao;
    private BigDecimal quantidade;
    private BigDecimal cotacaoNaData; // cotação do oráculo na data da operação

    public Movimentacao() {}

    public Movimentacao(int idMovimento, int idCarteira, LocalDate dataOperacao,TipoMovimentacao tipoOperacao, BigDecimal quantidade, BigDecimal cotacaoNaData) {
        this.idMovimento = idMovimento;
        this.idCarteira = idCarteira;
        this.dataOperacao = dataOperacao;
        this.tipoMovimentacao = tipoOperacao;
        this.quantidade = quantidade;
        this.cotacaoNaData = cotacaoNaData;
    }

    // Calcula o valor financeiro da operação (quantidade × cotação)
    public BigDecimal getValorFinanceiro() {
        if (cotacaoNaData == null || quantidade == null) return BigDecimal.ZERO;
        return quantidade.multiply(cotacaoNaData);
    }

    // Getters e Setters
    public int getIdMovimento() { return idMovimento; }
    public void setIdMovimento(int idMovimento) { this.idMovimento = idMovimento; }

    public int getIdCarteira() { return idCarteira; }
    public void setIdCarteira(int idCarteira) { this.idCarteira = idCarteira; }

    public LocalDate getDataOperacao() { return dataOperacao; }
    public void setDataOperacao(LocalDate dataOperacao) { this.dataOperacao = dataOperacao; }

    public TipoMovimentacao getTipoMovimentacao() { return tipoMovimentacao; }
    public void setTipoMovimentacao(TipoMovimentacao tipoOperacao) { this.tipoMovimentacao = tipoOperacao; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public BigDecimal getCotacaoNaData() { return cotacaoNaData; }
    public void setCotacaoNaData(BigDecimal cotacaoNaData) { this.cotacaoNaData = cotacaoNaData; }

    @Override
    public String toString() {
        return String.format("Movimentacao{id=%d, carteira=%d, data=%s, tipo=%s, qtd=%s, cotacao=%s}",
                idMovimento, idCarteira, dataOperacao, tipoMovimentacao, quantidade, cotacaoNaData);
    }
}
