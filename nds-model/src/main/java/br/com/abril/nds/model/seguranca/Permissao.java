package br.com.abril.nds.model.seguranca;

/**
 * Enumerated de tipos de permissões
 * A ordem dos enums define a ordem que aparecerá nos menus.
 * As permissões pais devem sempre vir antes de qualquer outra permissão Filho. 
 * @author InfoA2
 */
public enum Permissao
{

 	ROLE_CADASTRO("Cadastro", 												 						  	   null),
	ROLE_CADASTRO_PRODUTO("Produto", 										 						  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_EDICAO("Edição", 											 						  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_COTA("Cota", 																		  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_FIADOR("Fiador", 																	  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_ENTREGADOR("Entregador", 															  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_TRANSPORTADOR("Transportador", 						                        	 	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_FORNECEDOR("Fornecedor", 															  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_ROTEIRIZACAO("Roteirização", 														  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_BOX("Box", 												 						  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_BANCO("Banco", 											 						  	   Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_ALTERACAO_COTA("Alteração / Cota",			          					 		       Permissao.ROLE_CADASTRO),
	ROLE_CADASTRO_HELP("Help",                                                                     	 	   Permissao.ROLE_CADASTRO),

	ROLE_LANCAMENTO("Lançamento",														  				   null),
	ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ("Balanceamento da Matriz",									  	   Permissao.ROLE_LANCAMENTO), 
	ROLE_LANCAMENTO_FURO_PRODUTO("Furo de Lançamento",												  	   Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_PARCIAIS("Parciais", 																   Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_RELATORIO_VENDAS("Relatório de Vendas",											       Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_VENDA_PRODUTO("Venda por Produto",												  	   Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS("Relatório Tipos de Produtos",							  	   Permissao.ROLE_LANCAMENTO),
	ROLE_LANCAMENTO_HELP("Help",																		   Permissao.ROLE_LANCAMENTO),
	
	ROLE_ESTOQUE("Estoque",																	  			   null),
	ROLE_ESTOQUE_RECEBIMENTO_FISICO("Recebimento Físico",												   Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS("Lançamento Faltas e Sobras",									   Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_CONSULTA_NOTAS("Consulta de Notas",													   Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS("Consulta Faltas e Sobras",										   Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_EXTRATO_EDICAO("Extrato de Edição",													   Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_VISAO_DO_ESTOQUE("Visão do Estoque",                                                      Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO("Edições Fechadas com Saldo",									   Permissao.ROLE_ESTOQUE),
	ROLE_ESTOQUE_HELP("Help",							                                                   Permissao.ROLE_ESTOQUE),
	
	ROLE_EXPEDICAO("Expedição",															  				   null), 
	ROLE_EXPEDICAO_MAPA_ABASTECIMENTO("Mapa de Abastecimento",										  	   Permissao.ROLE_EXPEDICAO), 
	ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO("Confirma Expedição",											  	   Permissao.ROLE_EXPEDICAO), 
	ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO("Nota de Envio",	 												   Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_COTA_AUSENTE("Cota Ausente", 														   Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_RESUMO_EXPEDICAO("Resumo de Expedição", 												   Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_ROMANEIOS("Romaneios", 															  	   Permissao.ROLE_EXPEDICAO),
	ROLE_EXPEDICAO_HELP("Help", 																		   Permissao.ROLE_EXPEDICAO),

	ROLE_RECOLHIMENTO("Recolhimento", 													  				   null),
	ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ("Balanceamento da Matriz", 							      	   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE("Informe Recolhimento", 						  		   	   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO("CE Antecipada - Produto", 							  		   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_EMISSAO_CE("Emissão CE",															   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA("Conferência de Encalhe", 						  		   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_VENDA_ENCALHE("Venda de Encalhe / Suplementar",									   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE("Fechamento Encalhe",											   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO("Fechamento CE - Integração",								   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_DIGICACAO_CONTAGEM_DEVOLUCAO("Devolução ao Fornecedor",	 						   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CHAMADAO("Chamadão", 														  		   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA("Consulta Encalhe",    								  	   	   Permissao.ROLE_RECOLHIMENTO),
	ROLE_RECOLHIMENTO_HELP("Help", 									   							 		   Permissao.ROLE_RECOLHIMENTO),

	ROLE_NFE("NF-e",																	  	   			   null),
	ROLE_NFE_RETORNO_NFE("Retorno NF-e",																   Permissao.ROLE_NFE),
	ROLE_NFE_GERACAO_NFE("Geração de NF-e", 													  	   	   Permissao.ROLE_NFE),
	ROLE_NFE_IMPRESSAO_NFE("Impressão NF-e",															   Permissao.ROLE_NFE),
	ROLE_NFE_CONSULTA_NFE_ENCALHE_TRATAMENTO("Consulta NF-e Encalhe Tratamento",						   Permissao.ROLE_NFE),
	ROLE_NFE_PAINEL_MONITOR_NFE("Painel Monitor NF-e",													   Permissao.ROLE_NFE),
	
	ROLE_FINANCEIRO("Financeiro", 					   				                         	  	  	   null),
	ROLE_FINANCEIRO_BAIXA_BANCARIA("Baixa Financeira", 					                         	  	   Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA("Negociação de Divida",			                         	  	   Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA("Débitos / Créditos Cota",                              	  	   Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_IMPRESSAO_BOLETOS("Impressão de Boletos",                                     	       Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA("Manutenção de Status Cota",                           	       Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_SUSPENSAO_COTA("Suspensão Cota",                                             	 	   Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA("Consulta Boletos por Cota",                         	  	   	   Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_CONTA_CORRENTE("Conta Corrente",                                              	  	   Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA("Inadimplência",                          	  	   			   Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_CONSIGNADO_COTA("Consulta Consignado",                                                 Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_TIPO_DESCONTO_COTA("Tipo de Desconto Cota",								  		   	   Permissao.ROLE_FINANCEIRO),
	ROLE_FINANCEIRO_PARAMETROS_COBRANCA("Parâmetros de Cobrança",                                	 	   Permissao.ROLE_FINANCEIRO), 
	ROLE_FINANCEIRO_HELP("Help",           															 	   Permissao.ROLE_FINANCEIRO),
	
	ROLE_ADMINISTRACAO("Administração",														  			   null),
	ROLE_ADMINISTRACAO_CONTROLE_APROVACAO("Controle Aprovação",									  		   Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO("Painel Processamento	",							  		   Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA("Follow Up do Sistema",									  	   Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_GRUPOS_ACESSO("Grupos de Acesso",											  	   Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_CALENDARIO("Calendario",													  		   Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_TIPO_NOTA("Tipos de NF-e",													  	   Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA("Parâmetros de Sistema",	          					 		   Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR("Parâmetros Distribuidor",	 						 	   Permissao.ROLE_ADMINISTRACAO),
	ROLE_ADMINISTRACAO_HELP("Help",									          					 		   Permissao.ROLE_ADMINISTRACAO);

	private String descricao;
	private Permissao permissaoPai;

	/**
	 * @param descricao - A descrição que aparecerá no menu
	 * @param classeExibicao - a class (css) definida no menu principal (null, caso não seja o menu principal)
	 * @param permissaoPai - a Permissao pai da Permissao (que será o menu pai do submenu)
	 */
	private Permissao(String descricao, Permissao permissaoPai) {
		this.descricao = descricao;
		this.permissaoPai = permissaoPai;
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

	public String getNome() {
		StringBuilder nome = new StringBuilder("");
		if (this.getPermissaoPai() != null) {
			nome.append(this.getPermissaoPai().getNome());
		}
		if (nome.length() != 0) {
			nome.append(" - ");
		}
		nome.append(this.getDescricao());
		return nome.toString();
	}

}
