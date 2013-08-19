package br.com.abril.nds.model.planejamento.fornecedor;

/**
 * Dommínio do regime de recolhimento (Tipo de recolhimento) do item na chamda de encalhe do fornecedor
 *
 */
public enum RegimeRecolhimento {
    
    NORMAL("N"), 
    PARCIAL("P"), 
    FINAL("F");
    
    private String codigo;

    /**
     * @param codigo
     */
    private RegimeRecolhimento(String codigo) {
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public static RegimeRecolhimento getByCodigo(String codigo) {
        for (RegimeRecolhimento regime : RegimeRecolhimento.values()) {
            if (regime.getCodigo().equals(codigo)) {
                return regime;
            }
        }
        return null;
    }

}
