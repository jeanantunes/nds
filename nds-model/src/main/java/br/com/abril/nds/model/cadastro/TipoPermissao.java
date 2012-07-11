package br.com.abril.nds.model.cadastro;

/**
 * Enumerated de tipos de permissões
 * @author InfoA2
 */
public enum TipoPermissao {

	ROLE_CADASTRO_PRODUTO("Permissão para acessar a funcionalidade Cadastro de Produto"),
	ROLE_CADASTRO_EDICAO("Permissão para acessar a funcionalidade Cadastro de Edição"),
	ROLE_CADASTRO_COTA("Permissão para acessar a funcionalidade Cadastro de Cota"),
	ROLE_CADASTRO_FIADOR("Permissão para acessar a funcionalidade Cadastro de Fiador"),
	ROLE_CADASTRO_ENTREGADOR("Permissão para acessar a funcionalidade Cadastro de Entregador"),
	ROLE_CADASTRO_TRANSPORTADOR("Permissão para acessar a funcionalidade Cadastro de Transportador"),
	ROLE_CADASTRO_FORNECEDOR("Permissão para acessar a funcionalidade Cadastro de Fornecedor"),
	ROLE_CADASTRO_ROTEIRIZACAO("Permissão para acessar a funcionalidade Cadastro de Roteirização"),
	ROLE_CADASTRO_PARCIAIS("Permissão para acessar a funcionalidade Cadastro de Parciais"),
	ROLE_CADASTRO_BOX("Permissão para acessar a funcionalidade Cadastro de Box"),
	ROLE_CADASTRO_BANCO("Permissão para acessar a funcionalidade Cadastro de Banco"),
	ROLE_CADASTRO_HELP("Permissão para acessar a funcionalidade Cadastro Help"),
	ROLE_FINANCEIRO_BAIXA_BANCARIA("Permissão para acessar a funcionalidade Financeiro Baixa Bancária"),
	ROLE_FINANCEIRO_BAIXA_BANCARIA_MANUAL("Permissão para acessar a funcionalidade Financeiro Baixa Bancária Manual"), 
	ROLE_FINANCEIRO_BAIXA_MANUAL_DIVIDAS("Permissão para acessar a funcionalidade Financeiro Baixa Manual de Dívidas"), 
	ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA("Permissão para acessar a funcionalidade Financeiro de Consulta Boletos por Cota"), 
	ROLE_FINANCEIRO_CONTA_CORRENTE("Permissão para acessar a funcionalidade Financeiro Conta Corrente"), 
	ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA("Permissão para acessar a funcionalidade Financeiro Débitos / Créditos Cota"), 
	ROLE_FINANCEIRO_GERACAO_COBRANCA("Permissão para acessar a funcionalidade Financeiro Geração Cobrança"), 
	ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA("Permissão para acessar a funcionalidade Financeiro Histórico de Inadimplência"),
	ROLE_FINANCEIRO_IMPRESSAO_BOLETOS("Permissão para acessar a funcionalidade Financeiro Impressão de Boletos"), 
	ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA("Permissão para acessar a funcionalidade Financeiro Manutenção de Status Cota"), 
	ROLE_FINANCEIRO_PARAMETROS_COBRANCA("Permissão para acessar a funcionalidade Financeiro Parâmetros de Cobrança"), 
	ROLE_FINANCEIRO_SUSPENSAO_COTA("Permissão para acessar a funcionalidade Financeiro Suspensão Cota"),
	ROLE_FINANCEIRO_WORKFLOW_APROVACAO("Permissão para acessar a funcionalidade Financeiro Work flow de Aprovação"),
	ROLE_FINANCEIRO_CONSIGNADO_COTA("Permissão para acessar a funcionalidade Financeiro Consignado Cota"),
	ROLE_FINANCEIRO_HELP("Permissão para acessar a funcionalidade Financeiro Help"),
	ROLE_ESTOQUE_CONSULTA_NOTAS("Permissão para acessar a funcionalidade Estoque Consulta de Notas"),
	ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS("Permissão para acessar a funcionalidade Estoque Consulta Faltas e Sobras"),
	ROLE_ESTOQUE_EXTRATO_EDICAO("Permissão para acessar a funcionalidade Estoque Extrato de Edição"),
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS("Permissão para acessar a funcionalidade Estoque Lançamento Faltas e Sobras"),
	ROLE_ESTOQUE_RECEBIMENTO_FISICO("Permissão para acessar a funcionalidade Estoque Recebimento Físico"),
	ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO("Permissão para acessar a funcionalidade Estoque Edições Fechadas com Saldo"),
	ROLE_ESTOQUE_HELP("Permissão para acessar a funcionalidade Estoque Help"),
	ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ("Permissão para acessar a funcionalidade Lançamento Balanceamento da Matriz"), 
	ROLE_LANCAMENTO_CONSULTA_REPARTE_COTA("Permissão para acessar a funcionalidade Lançamento Consulta Reparte Cota"), 
	ROLE_LANCAMENTO_FURO_PRODUTO("Permissão para acessar a funcionalidade Lançamento Furo de Produto"),
	ROLE_LANCAMENTO_RELATORIO_VENDAS("Permissão para acessar a funcionalidade Lançamento Relatório de Vendas"),
	ROLE_LANCAMENTO_VENDA_PRODUTO("Permissão para acessar a funcionalidade Lançamento Venda por Produto"),
	ROLE_LANCAMENTO_HELP("Permissão para acessar a funcionalidade Lançamento Help"),
	ROLE_EXPEDICAO_MAPA_ABASTECIMENTO("Permissão para acessar a funcionalidade Expedição Mapa de Abastecimento"), 
	ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO("Permissão para acessar a funcionalidade Expedição Confirma Expedição"), 
	ROLE_EXPEDICAO_CONSULTA_RESUMOS_NFE_GERADAS_RETORNADAS("Permissão para acessar a funcionalidade Consulta Resumos NF-e Geradas e Retornadas"), 
	ROLE_EXPEDICAO_GERACAO_ARQUIVOS_NFE("Permissão para acessar a funcionalidade Expedição Geração de Arquivos NF-e"),
	ROLE_EXPEDICAO_GERACAO_NFE("Permissão para acessar a funcionalidade Expedição Geração de NF-e"),
	ROLE_EXPEDICAO_COTA_AUSENTE("Permissão para acessar a funcionalidade Expedição Cota Ausente"),
	ROLE_EXPEDICAO_PAINEL_MONITOR_NFE("Permissão para acessar a funcionalidade Expedição Painel Monitor NF-e"),
	ROLE_EXPEDICAO_RESUMO_EXPEDICAO("Permissão para acessar a funcionalidade Resumo de Expedição"),
	ROLE_EXPEDICAO_ROMANEIOS("Permissão para acessar a funcionalidade Expedição Romaneios"),
	ROLE_EXPEDICAO_INTEGRACAO_ARQUIVO_RETORNO_NFE("Permissão para acessar a funcionalidade Expedição Integração Arquivo Retorno NFE"),
	ROLE_EXPEDICAO_HELP("Permissão para acessar a funcionalidade Expedição Help"),
	ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ("Permissão para acessar a funcionalidade Recolhimento Balanceamento da Matriz"),
	ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO("Permissão para acessar a funcionalidade Recolhimento CE Antecipada - Produto"),
	ROLE_RECOLHIMENTO_CHAMADAO("Permissão para acessar a funcionalidade Recolhimento Chamadão"),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA("Permissão para acessar a funcionalidade Recolhimento Conferência Encalhe Cota"),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_CONTINGENCIA("Permissão para acessar a funcionalidade Recolhimento Conferência Encalhe Cota Contingência"),
	ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA("Permissão para acessar a funcionalidade Recolhimento Consulta Encalhe Cota"),
	ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE("Permissão para acessar a funcionalidade Recolhimento Consulta Informe Encalhe"),
	ROLE_RECOLHIMENTO_CONSULTA_CE("Permissão para acessar a funcionalidade Recolhimento Consulta CE"),
	ROLE_RECOLHIMENTO_LIBERACAO_ENCALHE_CONFERIDO("Permissão para acessar a funcionalidade Recolhimento Liberação Encalhe Conferido"),
	ROLE_RECOLHIMENTO_VENDA_ENCALHE("Permissão para acessar a funcionalidade Recolhimento Venda Encalhe"),
	ROLE_RECOLHIMENTO_HELP("Permissão para acessar a funcionalidade Recolhimento Help"),
	ROLE_DEVOLUCAO_EMISSAO_CE("Permissão para acessar a funcionalidade Devolução Emissão CE"),
	ROLE_DEVOLUCAO_DIGICACAO_CONTAGEM_DEVOLUCAO("Permissão para acessar a funcionalidade Devolução Digitação Contagem Devolução"),
	ROLE_DEVOLUCAO_FECHAMENTO_ENCALHE("Permissão para acessar a funcionalidade Devolução Fechamento Encalhe"),
	ROLE_DEVOLUCAO_HELP("Permissão para acessar a funcionalidade Devolução Help"),
	ROLE_NFE_RETORNO_NFE("Permissão para acessar a funcionalidade NF-e Retorno NF-e"),
	ROLE_NFE_CONSULTA_NFE_ENCALHE_TRATAMENTO("Permissão para acessar a funcionalidade NFe Consulta NFe Encalhe Tratamento"),
	ROLE_NFE_PAINEL_MONITOR_NFE("Permissão para acessar a funcionalidade NFe Painel Monitor NFe"),
	ROLE_ADMINISTRACAO_CONTROLE_APROVACAO("Permissão para acessar a funcionalidade Administração Controle Aprovação"),
	ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO("Permissão para acessar a funcionalidade Administração Painel de Processamento"),
	ROLE_ADMINISTRACAO_GRUPOS_ACESSO("Permissão para acessar a funcionalidade Administração Grupos de Acesso"),
	ROLE_ADMINISTRACAO_CALENDARIO("Permissão para acessar a funcionalidade Administração Calendario"),
	ROLE_ADMINISTRACAO_TIPO_DESCONTO_COTA("Permissão para acessar a funcionalidade Administração Tipo Desconto da Cota"),
	ROLE_ADMINISTRACAO_TIPO_MOVIMENTO("Permissão para acessar a funcionalidade Administração Tipo de Movimento"),
	ROLE_ADMINISTRACAO_TIPO_NOTA("Permissão para acessar a funcionalidade Administração Tipo da Nota"),
	ROLE_ADMINISTRACAO_INICIAR_DIA("Permissão para acessar a funcionalidade Administração Iniciar o Dia"),
	ROLE_ADMINISTRACAO_FECHAR_DIA("Permissão para acessar a funcionalidade Administração Fechar o Dia"),
	ROLE_ADMINISTRACAO_SERVICO_ENTREGA("Permissão para acessar a funcionalidade Administração Serviço de Entrega"),
	ROLE_ADMINISTRACAO_TIPO_PRODUTO("Permissão para acessar a funcionalidade Administração Tipo de Produto"),
	ROLE_ADMINISTRACAO_HELP("Permissão para acessar a funcionalidade Administração Help"),
	ROLE_ADMINISTRACAO_PARAMETROS_COBRANCA("Permissão para acessar a funcionalidade Administração Parêmetros de Cobrança"),
	ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA("Permissão para acessar a funcionalidade Administração Parêmetros do Sistema"),
	ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR("Permissão para acessar a funcionalidade Administração Parêmetros do Distribuidor");

	private String descricao;
	
	private TipoPermissao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return this.descricao;
	}

}
