package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
public enum SituacaoCadastro {
	
	PENDENTE("Pendente"),
	ATIVO("Ativo"),
	SUSPENSO("Suspenso"),
	INATIVO("Inativo");
	
	private String descricao;
	
	private SituacaoCadastro(String descricao) {
		
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {

		return this.descricao;
	}
	
}