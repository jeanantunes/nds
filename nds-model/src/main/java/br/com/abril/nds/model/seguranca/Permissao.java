package br.com.abril.nds.model.seguranca;

/**
 * Enumerated de tipos de permissões
 * A ordem dos enums define a ordem que aparecerá nos menus.
 * As permissões pais devem sempre vir antes de qualquer outra permissão Filho. 
 * @author InfoA2
 */
public enum Permissao
{

 	ROLE_CADASTRO("Cadastro", 												 						  	 "classCadastros", null),
	ROLE_CADASTRO_PRODUTO("Produto", 										 						  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_EDICAO("Edição", 											 						  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_BANCO("Banco", 											 						  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_BOX("Box", 												 						  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_COTA("Cota", 																		  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_ENTREGADOR("Entregador", 															  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_FIADOR("Fiador", 																	  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_FORNECEDOR("Fornecedor", 															  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_PARCIAIS("Parciais", 																  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_ROTEIRIZACAO("Roteirização", 														  	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_TRANSPORTADOR("Transportador", 						                        	 	 null,             Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_HELP("Help",                                                                     	 	 null,             Permissao.ROLE_CADASTRO),
	ROLE_FINANCEIRO("Financeiro", 					   				                         	  	  	 "classFinanceiro", null),
	ROLE_FINANCEIRO_BAIXA_BANCARIA("Baixa Bancária", 					                         	  	 null,             Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_BAIXA_BANCARIA_MANUAL("Baixa Bancária Manual",                                	  	 null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_BAIXA_MANUAL_DIVIDAS("Baixa Manual de Dívidas",                               	  	 null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA("de Consulta Boletos por Cota",                         	  	 null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_CONTA_CORRENTE("Conta Corrente",                                              	  	 null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA("Débitos / Créditos Cota",                              	  	 null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_GERACAO_COBRANCA("Geração Cobrança",                                          	  	 null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA("Histórico de Inadimplência",                          	  	 null,             Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_IMPRESSAO_BOLETOS("Impressão de Boletos",                                     	     null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA("Manutenção de Status Cota",                           	     null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_PARAMETROS_COBRANCA("Parâmetros de Cobrança",                                	 	 null,             Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_SUSPENSAO_COTA("Suspensão Cota",                                             	 	 null,             Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_WORKFLOW_APROVACAO("Work flow de Aprovação",                                      	 null,             Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_CONSIGNADO_COTA("Consignado Cota",                                                	 null,             Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_HELP("Help",           															 	 null,             Permissao.ROLE_FINANCEIRO),
	ROLE_ESTOQUE("Estoque",																	  			 "classEstoque", null),
	ROLE_ESTOQUE_CONSULTA_NOTAS("Consulta de Notas",													 null,             Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS("Consulta Faltas e Sobras",										 null,             Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_EXTRATO_EDICAO("Extrato de Edição",													 null,             Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS("Lançamento Faltas e Sobras",									 null,             Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_RECEBIMENTO_FISICO("Recebimento Físico",												 null,             Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO("Edições Fechadas com Saldo",									 null,             Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_HELP("Help",							                                                 null,             Permissao.ROLE_ESTOQUE),
	ROLE_LANCAMENTO("Lançamento",														  				 "classLancamento", null), 
	ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ("Balanceamento da Matriz",									  	 null,             Permissao.ROLE_LANCAMENTO), 
	ROLE_LANCAMENTO_CONSULTA_REPARTE_COTA("Consulta Reparte Cota",									  	 null,             Permissao.ROLE_LANCAMENTO), 
	ROLE_LANCAMENTO_FURO_PRODUTO("Furo de Produto",													  	 null,             Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_RELATORIO_VENDAS("Relatório de Vendas",											     null,             Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_VENDA_PRODUTO("Venda por Produto",												  	 null,             Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_HELP("Help",																		 null,             Permissao.ROLE_LANCAMENTO),
	ROLE_EXPEDICAO("Expedição",															  				 "classExpedicao", null), 
	ROLE_EXPEDICAO_MAPA_ABASTECIMENTO("Mapa de Abastecimento",										  	 null,             Permissao.ROLE_EXPEDICAO), 
	ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO("Confirma Expedição",											  	 null,             Permissao.ROLE_EXPEDICAO), 
	ROLE_EXPEDICAO_CONSULTA_RESUMOS_NFE_GERADAS_RETORNADAS("Consulta Resumos NF-e Geradas e Retornadas", null,             Permissao.ROLE_EXPEDICAO), 
	ROLE_EXPEDICAO_GERACAO_ARQUIVOS_NFE("Geração de Arquivos NF-e", 									 null,             Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_GERACAO_NFE("Geração de NF-e", 													  	 null,             Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_COTA_AUSENTE("Cota Ausente", 														 null,             Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_PAINEL_MONITOR_NFE("Painel Monitor NF-e", 											 null,             Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_RESUMO_EXPEDICAO("Resumo de Expedição", 												 null,             Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_ROMANEIOS("Romaneios", 															  	 null,             Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_INTEGRACAO_ARQUIVO_RETORNO_NFE("Integração Arquivo Retorno NFE", 					 null,             Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_HELP("Help", 																		 null,             Permissao.ROLE_EXPEDICAO),
	ROLE_RECOLHIMENTO("Recolhimento", 													  				 "classRecolhimento", null),
	ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ("Balanceamento da Matriz", 							      	 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO("CE Antecipada - Produto", 							  		 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CHAMADAO("Chamadão", 														  		 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA("Conferência Encalhe Cota", 						  		 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_CONTINGENCIA("Conferência Encalhe Cota Contingência", 	 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA("Consulta Encalhe Cota", 								  	 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE("Consulta Informe Encalhe", 						  		 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONSULTA_CE("Consulta CE", 													  	 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_LIBERACAO_ENCALHE_CONFERIDO("Liberação Encalhe Conferido", 					  	 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_VENDA_ENCALHE("Venda Encalhe", 												  	 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_HELP("Help", 									   							 		 null,             Permissao.ROLE_RECOLHIMENTO),
	ROLE_DEVOLUCAO("Devolução",																		  	 "classDevolucao", null),
	ROLE_DEVOLUCAO_EMISSAO_CE("Emissão CE",															  	 null,             Permissao.ROLE_DEVOLUCAO),
	ROLE_DEVOLUCAO_DIGICACAO_CONTAGEM_DEVOLUCAO("Digitação Contagem Devolução", 						 null,             Permissao.ROLE_DEVOLUCAO),
	ROLE_DEVOLUCAO_FECHAMENTO_ENCALHE("Fechamento Encalhe",											  	 null,             Permissao.ROLE_DEVOLUCAO),
	ROLE_DEVOLUCAO_HELP("Help",																		  	 null,             Permissao.ROLE_DEVOLUCAO),
	ROLE_NFE_RETORNO_NFE("NF-e",																	  	 null,             Permissao.ROLE_DEVOLUCAO),
	ROLE_NFE_CONSULTA_NFE_ENCALHE_TRATAMENTO("Consulta NF-e Encalhe Tratamento",						 null,             Permissao.ROLE_DEVOLUCAO),
	ROLE_NFE_PAINEL_MONITOR_NFE("Painel Monitor NF-e",													 null,             Permissao.ROLE_DEVOLUCAO),
	ROLE_ADMINISTRACAO("Administração",														  			 "classAdministracao", null),
	ROLE_ADMINISTRACAO_CONTROLE_APROVACAO("Controle Aprovação",									  		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO("Painel de Processamento",							  		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_GRUPOS_ACESSO("Grupos de Acesso",											  	 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_CALENDARIO("Calendario",													  		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_DESCONTO_COTA("Tipo Desconto da Cota",								  		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_MOVIMENTO("Tipo de Movimento",										  		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_NOTA("Tipo da Nota",													  	 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_INICIAR_DIA("Iniciar o Dia",												  		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_FECHAR_DIA("Fechar o Dia",													  	 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_SERVICO_ENTREGA("Serviço de Entrega",										  	 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_PRODUTO("Tipo de Produto",				          					 		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_HELP("Help",									          					 		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PARAMETROS_COBRANCA("Parêmetros de Cobrança",	          					 	 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA("Parêmetros do Sistema",	          					 		 null,             Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR("Parêmetros do Distribuidor", 						 	 null,             Permissao.ROLE_ADMINISTRACAO);

	private String descricao;
	private Permissao permissaoPai;
	private String classeExibicao;

	/**
	 * @param descricao - A descrição que aparecerá no menu
	 * @param classeExibicao - a class (css) definida no menu principal (null, caso não seja o menu principal)
	 * @param permissaoPai - a Permissao pai da Permissao (que será o menu pai do submenu)
	 */
	private Permissao(String descricao, String classeExibicao, Permissao permissaoPai) {
		this.descricao = descricao;
		this.permissaoPai = permissaoPai;
		this.classeExibicao = classeExibicao;
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

	public Permissao getPermissaoPai() {
		return permissaoPai;
	}

	public void setPermissaoPai(Permissao permissaoPai) {
		this.permissaoPai = permissaoPai;
	}

	public String getClasseExibicao() {
		return classeExibicao;
	}

	public void setClasseExibicao(String classeExibicao) {
		this.classeExibicao = classeExibicao;
	}
	
}
