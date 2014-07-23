package br.com.abril.nds.util;

public enum ComponentesPDV {

	AREA_DE_INFLUENCIA("Área de Influência"),
	BAIRRO("Bairro"),     
	COTAS_A_VISTA("Cota À Vista"),
	COTAS_NOVAS_RETIVADAS("Cotas Novas/Reativadas"),
	DISTRITO("Distrito"),   
	GERADOR_DE_FLUXO("Gerador de Fluxo"),
	REGIAO("Região"),       
    TIPO_PONTO_DE_VENDA("Tipo Ponto de Venda");

    private String descricao;

    private ComponentesPDV(String descricao) {
	this.descricao = descricao;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    @Override
    public String toString() {
	return this.descricao;
    }
}
