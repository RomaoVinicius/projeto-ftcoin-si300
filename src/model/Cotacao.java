package src.model;
import java.math.BigDecimal;
import java.time.LocalDate;

//Representa a cotação da moeda virtual em uma determinada data (Oráculo)

public class Cotacao {
    
    private LocalDate data;
    private BigDecimal valor;

    public Cotacao(LocalDate data, BigDecimal valor) {
        this.data = data;
        setValor(valor); // A validação é feita no setter
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Erro: A cotação não pode ter valor negativo.");
        }
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Cotação [Data: " + data + ", Valor: R$ " + valor + "]";
    }
}