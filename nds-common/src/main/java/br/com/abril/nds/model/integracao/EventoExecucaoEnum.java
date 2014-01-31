package br.com.abril.nds.model.integracao;

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
	INF_DADO_ALTERADO(5),
	REGISTRO_JA_EXISTENTE(6);
		
	private EventoExecucaoEnum(int codigo) {
		this.codigo = Long.valueOf(codigo);
	}
	
	private Long codigo;

	public Long getCodigo() {
		return this.codigo;
	}
}
