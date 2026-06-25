public enum TipoMovimentacao {
    COMPRA('C'), VENDA('V');

    private final char codigo;

    TipoMovimentacao(char codigo) {  
        this.codigo = codigo;
    }

    public char getCodigo() {
        return codigo;
    }

    public static TipoMovimentacao fromCodigo(char codigo) {  
        for (TipoMovimentacao tipo : values()) {              
            if (tipo.codigo == codigo) return tipo;
        }
        throw new IllegalArgumentException("Tipo inválido: " + codigo);
    }
}