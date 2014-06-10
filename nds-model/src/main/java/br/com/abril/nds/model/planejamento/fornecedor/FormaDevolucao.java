package br.com.abril.nds.model.planejamento.fornecedor;

/**
 * Domínio das formas de devolução do item na chamda de encalhe do fornecedor
 * 
 */
public enum FormaDevolucao {
    
    INTEIRO(1, "INTEIRO"), 
    CAPA(2, "CAPA"), 
    INT_BR(3, "INT+BR"),
    CAP_BR(4, "CAP+BR"),
    INT_VD(5, "INT+VD"),
    CAP_VD(6, "CAP+VD"),
    I_BR_VD(7, "I+BR+VD"),
    C_BR_VD(8, "C+BR+VD");
    
    private Integer codigo;
    
    private String descricao;
    
    private FormaDevolucao(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public static FormaDevolucao getByCodigo(Integer codigo) {
        for (FormaDevolucao forma : FormaDevolucao.values()) {
            if (forma.getCodigo().equals(codigo)) {
                return forma;
            }
        }
        return null;
    }

}
