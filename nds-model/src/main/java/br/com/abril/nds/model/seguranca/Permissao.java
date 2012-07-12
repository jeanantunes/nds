package br.com.abril.nds.model.seguranca;

/**
 * Enumerated de tipos de permissões
 * @author InfoA2
 */
public enum Permissao {

 	ROLE_CADASTRO("Cadastro", 												 						  	 0, null),
	ROLE_CADASTRO_PRODUTO("Produto", 										 						  	 0, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_EDICAO("Edição", 											 						  	 1, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_BANCO("Banco", 											 						  	 2, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_BOX("Box", 												 						  	 3, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_COTA("Cota", 																		  	 4, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_ENTREGADOR("Entregador", 															  	 5, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_FIADOR("Fiador", 																	  	 6, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_FORNECEDOR("Fornecedor", 															  	 7, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_PARCIAIS("Parciais", 																  	 8, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_ROTEIRIZACAO("Roteirização", 														  	 9, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_TRANSPORTADOR("Transportador", 						                        	 	10, Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_HELP("Help",                                                                     	 	11, Permissao.ROLE_CADASTRO),
	ROLE_FINANCEIRO("Financeiro", 					   				                         	  	  	 1, null),
	ROLE_FINANCEIRO_BAIXA_BANCARIA("Baixa Bancária", 					                         	  	 0, Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_BAIXA_BANCARIA_MANUAL("Baixa Bancária Manual",                                	  	 1, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_BAIXA_MANUAL_DIVIDAS("Baixa Manual de Dívidas",                               	  	 2, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA("de Consulta Boletos por Cota",                         	  	 3, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_CONTA_CORRENTE("Conta Corrente",                                              	  	 4, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA("Débitos / Créditos Cota",                              	  	 5, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_GERACAO_COBRANCA("Geração Cobrança",                                          	  	 6, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA("Histórico de Inadimplência",                          	  	 7, Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_IMPRESSAO_BOLETOS("Impressão de Boletos",                                     	     8, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA("Manutenção de Status Cota",                           	     9, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_PARAMETROS_COBRANCA("Parâmetros de Cobrança",                                	 	10, Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_SUSPENSAO_COTA("Suspensão Cota",                                             	 	11, Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_WORKFLOW_APROVACAO("Work flow de Aprovação",                                      	12, Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_CONSIGNADO_COTA("Consignado Cota",                                                	13, Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_HELP("Help",           															 	14, Permissao.ROLE_FINANCEIRO),
	ROLE_ESTOQUE("Estoque",																	  			 2, null),
	ROLE_ESTOQUE_CONSULTA_NOTAS("Consulta de Notas",													 0, Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS("Consulta Faltas e Sobras",										 1, Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_EXTRATO_EDICAO("Extrato de Edição",													 2, Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS("Lançamento Faltas e Sobras",									 3, Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_RECEBIMENTO_FISICO("Recebimento Físico",												 4, Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO("Edições Fechadas com Saldo",									 5, Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_HELP("Help",							                                                 6, Permissao.ROLE_ESTOQUE),
	ROLE_LANCAMENTO("Lançamento",														  				 3, null), 
	ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ("Balanceamento da Matriz",									  	 0, Permissao.ROLE_LANCAMENTO), 
	ROLE_LANCAMENTO_CONSULTA_REPARTE_COTA("Consulta Reparte Cota",									  	 1, Permissao.ROLE_LANCAMENTO), 
	ROLE_LANCAMENTO_FURO_PRODUTO("Furo de Produto",													  	 2, Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_RELATORIO_VENDAS("Relatório de Vendas",											     3, Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_VENDA_PRODUTO("Venda por Produto",												  	 4, Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_HELP("Help",																		 5, Permissao.ROLE_LANCAMENTO),
	ROLE_EXPEDICAO("Expedição",															  				 4, null), 
	ROLE_EXPEDICAO_MAPA_ABASTECIMENTO("Mapa de Abastecimento",										  	 0, Permissao.ROLE_EXPEDICAO), 
	ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO("Confirma Expedição",											  	 1, Permissao.ROLE_EXPEDICAO), 
	ROLE_EXPEDICAO_CONSULTA_RESUMOS_NFE_GERADAS_RETORNADAS("Consulta Resumos NF-e Geradas e Retornadas", 2, Permissao.ROLE_EXPEDICAO), 
	ROLE_EXPEDICAO_GERACAO_ARQUIVOS_NFE("Geração de Arquivos NF-e", 									 3, Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_GERACAO_NFE("Geração de NF-e", 													  	 4, Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_COTA_AUSENTE("Cota Ausente", 														 5, Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_PAINEL_MONITOR_NFE("Painel Monitor NF-e", 											 6, Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_RESUMO_EXPEDICAO("Resumo de Expedição", 												 7, Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_ROMANEIOS("Romaneios", 															  	 8, Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_INTEGRACAO_ARQUIVO_RETORNO_NFE("Integração Arquivo Retorno NFE", 					 9, Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_HELP("Help", 																		10, Permissao.ROLE_EXPEDICAO),
	ROLE_RECOLHIMENTO("Recolhimento", 													  				 5, null),
	ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ("Balanceamento da Matriz", 							      	 0, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO("CE Antecipada - Produto", 							  		 1, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CHAMADAO("Chamadão", 														  		 2, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA("Conferência Encalhe Cota", 						  		 3, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_CONTINGENCIA("Conferência Encalhe Cota Contingência", 	 4, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA("Consulta Encalhe Cota", 								  	 5, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE("Consulta Informe Encalhe", 						  		 6, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONSULTA_CE("Consulta CE", 													  	 7, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_LIBERACAO_ENCALHE_CONFERIDO("Liberação Encalhe Conferido", 					  	 8, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_VENDA_ENCALHE("Venda Encalhe", 												  	 9, Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_HELP("Help", 									   							 		10, Permissao.ROLE_RECOLHIMENTO),
	ROLE_DEVOLUCAO("Devolução",																		  	 6, null),
	ROLE_DEVOLUCAO_EMISSAO_CE("Emissão CE",															  	 0, Permissao.ROLE_DEVOLUCAO),
	ROLE_DEVOLUCAO_DIGICACAO_CONTAGEM_DEVOLUCAO("Digitação Contagem Devolução", 						 1, Permissao.ROLE_DEVOLUCAO),
	ROLE_DEVOLUCAO_FECHAMENTO_ENCALHE("Fechamento Encalhe",											  	 2, Permissao.ROLE_DEVOLUCAO),
	ROLE_DEVOLUCAO_HELP("Help",																		  	 3, Permissao.ROLE_DEVOLUCAO),
	ROLE_NFE_RETORNO_NFE("NF-e",																	  	 4, Permissao.ROLE_DEVOLUCAO),
	ROLE_NFE_CONSULTA_NFE_ENCALHE_TRATAMENTO("Consulta NF-e Encalhe Tratamento",						 5, Permissao.ROLE_DEVOLUCAO),
	ROLE_NFE_PAINEL_MONITOR_NFE("Painel Monitor NF-e",													 6, Permissao.ROLE_DEVOLUCAO),
	ROLE_ADMINISTRACAO("Administração",														  			 7, null),
	ROLE_ADMINISTRACAO_CONTROLE_APROVACAO("Controle Aprovação",									  		 0, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO("Painel de Processamento",							  		 1, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_GRUPOS_ACESSO("Grupos de Acesso",											  	 2, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_CALENDARIO("Calendario",													  		 3, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_DESCONTO_COTA("Tipo Desconto da Cota",								  		 4, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_MOVIMENTO("Tipo de Movimento",										  		 5, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_NOTA("Tipo da Nota",													  	 6, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_INICIAR_DIA("Iniciar o Dia",												  		 7, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_FECHAR_DIA("Fechar o Dia",													  	 8, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_SERVICO_ENTREGA("Serviço de Entrega",										  	 9, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_PRODUTO("Tipo de Produto",				          					 		10, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_HELP("Help",									          					 		11, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PARAMETROS_COBRANCA("Parêmetros de Cobrança",	          					 	12, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA("Parêmetros do Sistema",	          					 		13, Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR("Parêmetros do Distribuidor", 						 	14, Permissao.ROLE_ADMINISTRACAO);

	private String descricao;
	private int ordem;
	private Permissao permissaoPai;
	
	/**
	 * @param descricao - A descrição que aparecerá no menu
	 * @param ordem - a ordem que deverá aparecer no menu
	 * @param permissaoPai - a Permissao pai da Permissao
	 */
	private Permissao(String descricao, int ordem, Permissao permissaoPai) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.permissaoPai = permissaoPai; 
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getOrdem() {
		return ordem;
	}

	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}

	public Permissao getPermissaoPai() {
		return permissaoPai;
	}

	public void setPermissaoPai(Permissao permissaoPai) {
		this.permissaoPai = permissaoPai;
	}
	
}
