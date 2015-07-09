package br.com.abril.nds.model.planejamento;

public enum TipoGeracaoEstudo {
	
	GERACAO_AUTOMATICA("Geração automática"),
	VENDA_MEDIA("Distribuição Venda Média"),
    MANUAL("Distribuição Manual"),
    SOMA("Soma de Estudos"),
    DIVISAO("Divisão de Estudos"),
    ESTUDO_COMPLEMENTAR("Estudo Complementar"),
    COPIA_PROPORCIONAL("Cópia Proporcional de Estudo");
    
    private String descricao;
    
    private TipoGeracaoEstudo(String descricao) {
        
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
}