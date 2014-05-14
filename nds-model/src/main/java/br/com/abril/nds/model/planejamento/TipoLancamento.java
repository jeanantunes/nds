package br.com.abril.nds.model.planejamento;

import java.text.Normalizer;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum TipoLancamento {
	
	LANCAMENTO(1,"Lançamento"),
	REDISTRIBUICAO(2,"Redistribuição");
	
	private Integer codigo;
	
	private String descricao;
	
	private TipoLancamento(Integer codigo, String descricao) {
		
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}	
	
	public static TipoLancamento getByDescricao(String descricao) {
		
		if (descricao == null) {
			
			return null;
		}
		
		descricao = Normalizer.normalize(descricao, Normalizer.Form.NFD);
		descricao = descricao.replaceAll("[^\\p{ASCII}]", "");

		for (TipoLancamento tipoLancamento : values()) {

			String descricaoAtual = tipoLancamento.getDescricao();
			descricaoAtual = Normalizer.normalize(descricaoAtual, Normalizer.Form.NFD);
			descricaoAtual = descricaoAtual.replaceAll("[^\\p{ASCII}]", "");

			if (descricao.equalsIgnoreCase(descricaoAtual)) {
				return tipoLancamento;
			}
		}
		
		return null;
	}
}