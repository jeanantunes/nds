package br.com.abril.nds.integracao.model;

/**
 * 
 * Dados cadastrados na tabela evento_execucao.
 * 
 * @author Discover Technology
 *
 */
public enum EventoExecucaoEnum {

	// ERRO GENERICO NAO PODE SER UTILIZADO.
	
	ERRO_INFRA(0),
	SEM_DOMINIO(1),
	HIERARQUIA(2),
	RELACIONAMENTO(3),
	GERACAO_DE_ARQUIVO(4),
	INF_DADO_ALTERADO(5);
		
	private EventoExecucaoEnum(int codigo) {
		this.codigo = new Long(codigo);
	}
	
	private Long codigo;

	public Long getCodigo() {
		return this.codigo;
	}
}
