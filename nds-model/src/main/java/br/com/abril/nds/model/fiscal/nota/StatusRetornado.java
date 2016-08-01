package br.com.abril.nds.model.fiscal.nota;

public enum StatusRetornado {

	AUTORIZADO(100, "Autorizado o uso da NF-e"),
	CANCELAMENTO_HOMOLOGADO(101, "Cancelamento de NF-e homologado"),
	NUMERO_HOMOLOGADO_INUTILIZADO(102, "Inutilização de número homologado"),
	LOTE_RECEBIDO_SUCESSO(103, "Lote recebido com sucesso"),
	LOTE_PROCESSADO(104, "Lote processado"),
	LOTE_EM_PROCESSAMENTO(105, "Lote em processamento"),
	LOTE_NAO_LOCALIZADO(106, "Lote não localizado"),
	SERVICO_EM_OPERACAO(107, "Serviço em Operação"),
	SERVICO_PARALISADO_MOMENTANEAMENTE(108, "Serviço Paralisado Momentaneamente (curto prazo)"),
	SERVICO_PARALISADO(109, "Serviço Paralisado sem Previsão"),
	USO_DENEGADO(110, "Uso Denegado"),
	CONSULTA_CADASTRO_UMA_OCORRENCIA(111, "Consulta cadastro com uma ocorrência"),
	CONSULTA_CADASTRO_MUITAS_OCORRENCIAS(112, "Consulta cadastro com mais de uma ocorrência"),
	DENEGADO(302, "Uso Denegado : Irregularidade fiscal do destinatario");
	
	private Integer codigo;
	
	private String descricao;
	
	/**
	 * Construtor.
	 * 
	 * @param descricao - descrição do status
	 */
	private StatusRetornado(Integer codigo, String descricao) {
		
		this.codigo = codigo;
		this.descricao = descricao;
	}

	/**
	 * Obtém descricao
	 *
	 * @return String
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}
	
	/**
	 * Obtém um Status pelo seu código.
	 * 
	 * @param codigo - código do status
	 * 
	 * @return {@link StatusRetornado}
	 */
	public static StatusRetornado obterPeloCodigo(Integer codigo) {
		
		for (StatusRetornado status : StatusRetornado.values()) {
			
			if (status.getCodigo().equals(codigo)) {
				
				return status;
			}
		}
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		
		return this.getDescricao();
	}
	
}
