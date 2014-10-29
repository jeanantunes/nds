package br.com.abril.nds.model.seguranca;

/**
 * Enumerated de tipos de permissões
 * A ordem dos enums define a ordem que aparecerão nos menus.
 * As permissões pais devem sempre vir antes de qualquer outra permissão Filho. 
 * @author InfoA2
 */
public enum Permissao {
	
	//ROLE_ESPECIFICA_ALTERACAO("Permisão Específica",														null, false),
	//ROLE_ESPECIFICA("Permisão Específica",																	null, Permissao.ROLE_ESPECIFICA_ALTERACAO, false),
	
	//ROLE_ALTERACAO_ESPECIFICA("Permissão de alteração especifica",											Permissao.ROLE_ESPECIFICA, false),
	
	//ROLE_ESPECIFICA_VIZUALIZACAO_TESTE("Permissão de vizsualização especifica",						  	   	Permissao.ROLE_ESPECIFICA, null, false),
	
	
	ROLE_CADASTRO_ALTERACAO("Cadastro", 													   				null, true),
	ROLE_CADASTRO("Cadastro", 												 						  	   	null, ROLE_CADASTRO_ALTERACAO, true, null),
	
	ROLE_CADASTRO_PRODUTO_ALTERACAO("Produto", 										 		   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_PRODUTO("Produto", 										 						  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_PRODUTO_ALTERACAO, true, null),
	
	ROLE_CADASTRO_EDICAO_ALTERACAO("Edição", 											 	   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_EDICAO("Edição", 											 						  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_EDICAO_ALTERACAO, true, null),
	
	ROLE_CADASTRO_COTA_ALTERACAO("Cota", 													   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_COTA("Cota", 																		  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_COTA_ALTERACAO, true, null),
	
	ROLE_CADASTRO_EDITOR_ALTERACAO("Editor",												   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_EDITOR("Editor", 																	  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_EDITOR_ALTERACAO, true, null),
	
	ROLE_CADASTRO_COTA_BASE_ALTERACAO("Cota Base",											   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_COTA_BASE("Cota Base",															  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_COTA_BASE_ALTERACAO, true, null),
	
	ROLE_CADASTRO_FIADOR_ALTERACAO("Fiador", 												   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_FIADOR("Fiador", 																	  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_FIADOR_ALTERACAO, true, null),
	
	ROLE_CADASTRO_ENTREGADOR_ALTERACAO("Entregador", 										   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_ENTREGADOR("Entregador", 															  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_ENTREGADOR_ALTERACAO, true, null),
	
	ROLE_CADASTRO_TRANSPORTADOR_ALTERACAO("Transportador", 						               				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_TRANSPORTADOR("Transportador", 						                        	 	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_TRANSPORTADOR_ALTERACAO, true, null),
	
	ROLE_CADASTRO_FORNECEDOR_ALTERACAO("Fornecedor", 										   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_FORNECEDOR("Fornecedor", 															  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_FORNECEDOR_ALTERACAO, true, null),
	
	ROLE_CADASTRO_ROTEIRIZACAO_ALTERACAO("Roteirização", 								       				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_ROTEIRIZACAO("Roteirização", 														       	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_ROTEIRIZACAO_ALTERACAO, true, null),
	
	ROLE_CADASTRO_BOX_ALTERACAO("Box", 												 		   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_BOX("Box", 												 						  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_BOX_ALTERACAO, true, null),
	
	ROLE_CADASTRO_BANCO_ALTERACAO("Banco", 											 		   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_BANCO("Banco", 											 						  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_BANCO_ALTERACAO, true, null),
	
	ROLE_CADASTRO_ALTERACAO_COTA_ALTERACAO("Alteração / Cota",			          			   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_ALTERACAO_COTA("Alteração / Cota",			          					 		       	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_ALTERACAO_COTA_ALTERACAO, true, null),
	
	ROLE_CADASTRO_VALE_DESCONTO_ALTERACAO("Vale Desconto",			          			   					Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_VALE_DESCONTO("Vale Desconto",			          					 		       		Permissao.ROLE_CADASTRO, ROLE_CADASTRO_VALE_DESCONTO_ALTERACAO, true, null),
	
	ROLE_CADASTRO_HELP_ALTERACAO("Help",                                                       				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_HELP("Help",                                                                     	 	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_HELP_ALTERACAO, true, null),
	
	
	ROLE_LANCAMENTO_ALTERACAO("Lançamento",													   				null, true),	
	ROLE_LANCAMENTO("Lançamento",														  				   	null, ROLE_LANCAMENTO_ALTERACAO, true, null),
	
	ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO("Matriz de Lançamento", 				   					Permissao.ROLE_LANCAMENTO, true), 
	ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ("Matriz de Lançamento",							   			   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO, true, null), 
	
	ROLE_LANCAMENTO_FURO_PRODUTO_ALTERACAO("Furo de Lançamento",							   				Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_FURO_PRODUTO("Furo de Lançamento",												  	   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_FURO_PRODUTO_ALTERACAO, true, null),
	
	ROLE_LANCAMENTO_PARCIAIS_ALTERACAO("Parciais", 											   				Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_PARCIAIS("Parciais", 																   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_PARCIAIS_ALTERACAO, true, null),
	
	ROLE_LANCAMENTO_RELATORIO_VENDAS_ALTERACAO("Relatório de Vendas",						   				Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_RELATORIO_VENDAS("Relatório de Vendas",											       	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_RELATORIO_VENDAS_ALTERACAO, true, null),
	
	ROLE_LANCAMENTO_VENDA_PRODUTO_ALTERACAO("Venda por Produto",						  	   			   	Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_VENDA_PRODUTO("Venda por Produto",												  	   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_VENDA_PRODUTO_ALTERACAO, true, null),
	
	ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS_ALTERACAO("Relatório Tipos de Produtos",	  	   				Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS("Relatório Tipos de Produtos",							  	   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS_ALTERACAO, true, null),
	
	ROLE_LANCAMENTO_HELP_ALTERACAO("Help",													  		 		Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_HELP("Help",																		   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_HELP_ALTERACAO, true, null),
	
	
	ROLE_DISTRIBUICAO_ALTERACAO("Distribuição",												   				null, true),
	ROLE_DISTRIBUICAO("Distribuição",																	   	null, ROLE_DISTRIBUICAO_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO_ALTERACAO("Matriz Distribuição"				 ,					Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO("Matriz Distribuição"				 ,					 	   	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO_ALTERACAO, true, null),
	
	ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO("Analise de Estudos", 				  					Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS("Analise de Estudos", 				  							   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_MIX_COTA_PRODUTO_ALTERACAO("Mix por Cota/Publicação", 				   				Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_MIX_COTA_PRODUTO("Mix por Cota/Publicação", 				   						   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_MIX_COTA_PRODUTO_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_FIXACAO_REPARTE_ALTERACAO("Fixação de Reparte", 				   						Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_FIXACAO_REPARTE("Fixação de Reparte", 				   							   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_FIXACAO_REPARTE_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA_ALTERACAO("Classificação Não Recebida",					Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA("Classificação Não Recebida",						 	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO_ALTERACAO("Segmento Não Recebido",					 			Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO("Segmento Não Recebido",					 				   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS_ALTERACAO("Exceção de Segmentos e Parciais",				Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS("Exceção de Segmentos e Parciais",					 	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO("Ajuste de reparte",	 									Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE("Ajuste de reparte",	 										   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO, true, null),
	
	ROLE_DISTRIBUICAO_DESENGLOBACAO_ALTERACAO("Desenglobação",												Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_DESENGLOBACAO("Desenglobação",												 	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_DESENGLOBACAO_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_HISTOGRAMA_VENDAS_ALTERACAO("Histograma de Vendas", 				   					Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_HISTOGRAMA_VENDAS("Histograma de Vendas", 				   						   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_HISTOGRAMA_VENDAS_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_HISTORICO_VENDA_ALTERACAO("Histórico de Vendas",										Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_HISTORICO_VENDA("Histórico de Vendas",										 	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_HISTORICO_VENDA_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_REGIAO_ALTERACAO("Região", 				  											Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_REGIAO("Região", 				  													   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_REGIAO_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_AREAINFLUENCIA_GERADORFLUXO_ALTERACAO("Área de influência / Gerador de Fluxo", 		Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_AREAINFLUENCIA_GERADORFLUXO("Área de influência / Gerador de Fluxo", 				   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_AREAINFLUENCIA_GERADORFLUXO_ALTERACAO, true, null),
	
	ROLE_DISTRIBUICAO_CARACTERISTICA_DISTRIBUICAO_ALTERACAO("Características de Distribuição",              Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_CARACTERISTICA_DISTRIBUICAO("Características de Distribuição",                        Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_CARACTERISTICA_DISTRIBUICAO_ALTERACAO, true, null),

	ROLE_DISTRIBUICAO_INFORMACOES_PRODUTO_ALTERACAO("Informações do Produto",	 							Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_INFORMACOES_PRODUTO("Informações do Produto",	 									   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_INFORMACOES_PRODUTO_ALTERACAO, true, null),
	
	ROLE_DISTRIBUICAO_HISTOGRAMA_POS_ESTUDO_ALTERACAO("Histograma Pré Análise",                             Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_HISTOGRAMA_POS_ESTUDO("Histograma Pré Análise",                                       Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_HISTOGRAMA_POS_ESTUDO_ALTERACAO, true, null),

	ROLE_ESTOQUE_ALTERACAO("Estoque",																	  	null, true),
	ROLE_ESTOQUE("Estoque",																	  			   	null, ROLE_ESTOQUE_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_RECEBIMENTO_FISICO_ALTERACAO("Recebimento Físico",											Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_RECEBIMENTO_FISICO("Recebimento Físico",												   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_RECEBIMENTO_FISICO_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_RECEBIMENTO_FISICO_CONF_CEGA("Recebimento Físico (Conferência Cega)", 						Permissao.ROLE_ESTOQUE, null, false,
		"As colunas 'Reparte Previsto', 'Diferença', 'Valor Total' e 'Valor Total com Desconto' e o botões 'Confirmar' e 'Replicar' não estarão visíveis.", RestricoesAcesso.CONF_CEGA_RECEBIMENTO_FISICO),
	
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO("Lançamento Faltas e Sobras",							Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS("Lançamento Faltas e Sobras",									   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_BOTAO_CONFIRMACAO_ALTERACAO("Lançamento Faltas e Sobras, botão Confirmar", null, false),
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_BOTAO_CONFIRMACAO("Lançamento Faltas e Sobras, botão Confirmar",	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_BOTAO_CONFIRMACAO_ALTERACAO, false),
	
	ROLE_ESTOQUE_CONSULTA_NOTAS_ALTERACAO("Consulta de Notas",												Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_CONSULTA_NOTAS("Consulta de Notas",													   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_CONSULTA_NOTAS_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS_ALTERACAO("Consulta Faltas e Sobras",								Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS("Consulta Faltas e Sobras",										   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_EXTRATO_EDICAO_ALTERACAO("Extrato de Edição",												Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_EXTRATO_EDICAO("Extrato de Edição",													   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_EXTRATO_EDICAO_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_VISAO_DO_ESTOQUE_ALTERACAO("Visão do Estoque",                                             Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_VISAO_DO_ESTOQUE("Visão do Estoque",                                                      	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_VISAO_DO_ESTOQUE_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO_ALTERACAO("Edições Fechadas com Saldo",								Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO("Edições Fechadas com Saldo",									   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_HELP_ALTERACAO("Help",							                                            Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_HELP("Help",							                                                   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_HELP_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_TRANSFERENCIA_PARCIAL_ALTERACAO("Transferência de Estoque de Parciais",					Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_TRANSFERENCIA_PARCIAL("Transferência de Estoque de Parciais",								Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_TRANSFERENCIA_PARCIAL_ALTERACAO, true, null),
	
	ROLE_EXPEDICAO_ALTERACAO("Expedição",															  		null, true), 
	ROLE_EXPEDICAO("Expedição",															  				   	null, ROLE_EXPEDICAO_ALTERACAO, true, null), 
	
	ROLE_EXPEDICAO_MAPA_ABASTECIMENTO_ALTERACAO("Mapa de Abastecimento",									Permissao.ROLE_EXPEDICAO, true), 
	ROLE_EXPEDICAO_MAPA_ABASTECIMENTO("Mapa de Abastecimento",										  	   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_MAPA_ABASTECIMENTO_ALTERACAO, true, null), 
	
	ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO_ALTERACAO("Confirma Expedição",						   				Permissao.ROLE_EXPEDICAO, true), 
	ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO("Confirma Expedição",											  	   	Permissao.ROLE_EXPEDICAO, Permissao.ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO_ALTERACAO, true, null), 
	
	ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO_ALTERACAO("Nota de Envio",	 										Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO("Nota de Envio",	 												   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO_ALTERACAO, true, null),
	
	ROLE_EXPEDICAO_COTA_AUSENTE_ALTERACAO("Cota Ausente", 													Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_COTA_AUSENTE("Cota Ausente", 														   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_COTA_AUSENTE_ALTERACAO, true, null),
	
	ROLE_EXPEDICAO_RESUMO_EXPEDICAO_ALTERACAO("Resumo de Expedição", 										Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_RESUMO_EXPEDICAO("Resumo de Expedição", 												   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_RESUMO_EXPEDICAO_ALTERACAO, true, null),
	
	ROLE_EXPEDICAO_ROMANEIOS_ALTERACAO("Romaneios", 														Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_ROMANEIOS("Romaneios", 															  	   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_ROMANEIOS_ALTERACAO, true, null),
	
	ROLE_EXPEDICAO_HELP_ALTERACAO("Help", 																	Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_HELP("Help", 																		   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_HELP_ALTERACAO, true, null),

	
	ROLE_RECOLHIMENTO_ALTERACAO("Recolhimento", 													  		null, true),
	ROLE_RECOLHIMENTO("Recolhimento", 													  				   	null, ROLE_RECOLHIMENTO_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO("Bal. Matriz Recolhimento", 						   	Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ("Bal. Matriz Recolhimento", 						   			   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE_ALTERACAO("Informe Recolhimento", 						  	Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE("Informe Recolhimento", 						  		   	   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO("CE Antecipada - Produto", 							Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO("CE Antecipada - Produto", 							  		   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_EMISSAO_CE_ALTERACAO("Emissão CE",													Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_EMISSAO_CE("Emissão CE",															   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_EMISSAO_CE_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO("Conferência de Encalhe", 						  	Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA("Conferência de Encalhe", 						  		   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO, true, null),

	ROLE_RECOLHIMENTO_SEMAFORO_ALTERACAO("Status Processos de Encalhe", 														Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_SEMAFORO("Status Processos de Encalhe", 						  		   									Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_SEMAFORO_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_VENDA_ENCALHE_ALTERACAO("Venda de Encalhe / Suplementar",								Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_VENDA_ENCALHE("Venda de Encalhe / Suplementar",									   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_VENDA_ENCALHE_ALTERACAO, true, null),
	
	ROLE_ESTOQUE_PRODUTOS_EM_RECOLHIMENTO_ALTERACAO("Estoque Produtos em Recolhimento",					   	Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_ESTOQUE_PRODUTOS_EM_RECOLHIMENTO("Estoque Produtos em Recolhimento",					   			Permissao.ROLE_RECOLHIMENTO, ROLE_ESTOQUE_PRODUTOS_EM_RECOLHIMENTO_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO("Fechamento Encalhe",									Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE("Fechamento Encalhe",											   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_CONF_CEGA("Fechamento Encalhe (Conferência Cega)", 				Permissao.ROLE_RECOLHIMENTO, null, false,
		"As colunas 'Exemplar Devolução', 'Total R$', 'Diferença' e os botões 'Confirmar' e 'Cotas Ausentes' não estarão visíveis.", RestricoesAcesso.CONF_CEGA_FECHAMENTO_ENCALHE),
	
	ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO("Fechamento CE - Integração",							Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO("Fechamento CE - Integração",								   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_DIGICACAO_CONTAGEM_DEVOLUCAO_ALTERACAO("Devolução ao Fornecedor",	 					Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_DIGICACAO_CONTAGEM_DEVOLUCAO("Devolução ao Fornecedor",	 						   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_DIGICACAO_CONTAGEM_DEVOLUCAO_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_CHAMADAO_ALTERACAO("Chamadão", 														Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CHAMADAO("Chamadão", 														  		   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CHAMADAO_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA_ALTERACAO("Consulta Encalhe",    								Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA("Consulta Encalhe",    								  	   	   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA_ALTERACAO("Emissão das Bandeiras",									Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA("Emissão das Bandeiras",											   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA_ALTERACAO, true, null),
	
	ROLE_RECOLHIMENTO_HELP_ALTERACAO("Help", 									   							Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_HELP("Help", 									   							 		   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_HELP_ALTERACAO, true, null),

	
	ROLE_NFE_ALTERACAO("NF-e",																	  	   		null, true),
	ROLE_NFE("NF-e",																	  	   			   	null, ROLE_NFE_ALTERACAO, true, null),
	
	ROLE_NFE_RETORNO_NFE_ALTERACAO("Retorno NF-e",															Permissao.ROLE_NFE, true),
	ROLE_NFE_RETORNO_NFE("Retorno NF-e",																   	Permissao.ROLE_NFE, ROLE_NFE_RETORNO_NFE_ALTERACAO, true, null),
	
	ROLE_NFE_ENTRADA_NFE_TERCEIROS_ALTERACAO("Entrada de NF-e Terceiros",									Permissao.ROLE_NFE, true),
	ROLE_NFE_ENTRADA_NFE_TERCEIROS("Entrada de NF-e Terceiros",											   	Permissao.ROLE_NFE, ROLE_NFE_ENTRADA_NFE_TERCEIROS_ALTERACAO, true, null),
	
	ROLE_NFE_GERACAO_NFE_ALTERACAO("Geração de NF-e", 													  	Permissao.ROLE_NFE, true),
	ROLE_NFE_GERACAO_NFE("Geração de NF-e", 													  	   	   	Permissao.ROLE_NFE, ROLE_NFE_GERACAO_NFE_ALTERACAO, true, null),
	
	ROLE_NFE_IMPRESSAO_NFE_ALTERACAO("Impressão NF-e",														Permissao.ROLE_NFE, true),
	ROLE_NFE_IMPRESSAO_NFE("Impressão NF-e",															   	Permissao.ROLE_NFE, ROLE_NFE_IMPRESSAO_NFE_ALTERACAO, true, null),
	
	ROLE_NFE_PAINEL_MONITOR_NFE_ALTERACAO("Painel Monitor NF-e",											Permissao.ROLE_NFE, true),
	ROLE_NFE_PAINEL_MONITOR_NFE("Painel Monitor NF-e",													   	Permissao.ROLE_NFE, ROLE_NFE_PAINEL_MONITOR_NFE_ALTERACAO, true, null),
	
	
	ROLE_FINANCEIRO_ALTERACAO("Financeiro", 					   				                         	null, true),
	ROLE_FINANCEIRO("Financeiro", 					   				                         	  	  	   	null, ROLE_FINANCEIRO_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO("Baixa Financeira", 					                        Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_BAIXA_BANCARIA("Baixa Financeira", 					                         	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_INTEGRACAO_FISCAL_P7_ALTERACAO("Integração Fiscal P7", 					                Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_INTEGRACAO_FISCAL_P7("Integração Fiscal P7", 					                        Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_INTEGRACAO_FISCAL_P7_ALTERACAO, true, null),
		
	ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA_ALTERACAO("Negociação de Divida",			                         	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA("Negociação de Divida",			                         	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA_ALTERACAO("Débitos / Créditos Cota",                              Permissao.ROLE_FINANCEIRO, true), 
	ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA("Débitos / Créditos Cota",                              	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA_ALTERACAO, true, null), 
	
	ROLE_FINANCEIRO_IMPRESSAO_BOLETOS_ALTERACAO("Impressão de Boletos",                                     Permissao.ROLE_FINANCEIRO, true), 
	ROLE_FINANCEIRO_IMPRESSAO_BOLETOS("Impressão de Boletos",                                     	       	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_IMPRESSAO_BOLETOS_ALTERACAO, true, null), 
	
	ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA_ALTERACAO("Manutenção de Status Cota",                           Permissao.ROLE_FINANCEIRO, true), 
	ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA("Manutenção de Status Cota",                           	       	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA_ALTERACAO, true, null), 
	
	ROLE_FINANCEIRO_SUSPENSAO_COTA_ALTERACAO("Suspensão Cota",                                             	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_SUSPENSAO_COTA("Suspensão Cota",                                             	 	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_SUSPENSAO_COTA_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA_ALTERACAO("Consulta Boletos por Cota",                         	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA("Consulta Boletos por Cota",                         	  	   	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_CONTA_CORRENTE_ALTERACAO("Conta Corrente",                                              Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_CONTA_CORRENTE("Conta Corrente",                                              	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_CONTA_CORRENTE_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_CONTAS_A_PAGAR_ALTERACAO("Contas a Pagar",                                              Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_CONTAS_A_PAGAR("Contas a Pagar",                                              	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_CONTAS_A_PAGAR_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA_ALTERACAO("Inadimplência",                          	  	   	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA("Inadimplência",                          	  	   			   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_CONSIGNADO_COTA_ALTERACAO("Consulta Consignado",                                        Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_CONSIGNADO_COTA("Consulta Consignado",                                                 	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_CONSIGNADO_COTA_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_TIPO_DESCONTO_COTA_ALTERACAO("Tipo de Desconto Cota",								  	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_TIPO_DESCONTO_COTA("Tipo de Desconto Cota",								  		   	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_TIPO_DESCONTO_COTA_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_RELATORIO_DE_GARANTIAS_ALTERACAO("Relatório de Garantias",								Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_RELATORIO_DE_GARANTIAS("Relatório de Garantias",									   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_RELATORIO_DE_GARANTIAS_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_PARAMETROS_COBRANCA_ALTERACAO("Parâmetros de Cobrança",                                	Permissao.ROLE_FINANCEIRO, true), 
	ROLE_FINANCEIRO_PARAMETROS_COBRANCA("Parâmetros de Cobrança",                                	 	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_PARAMETROS_COBRANCA_ALTERACAO, true, null), 
	
	ROLE_MOVIMENTO_FINANCEIRO_COTA_ALTERACAO("Processamento Financeiro",                                	Permissao.ROLE_FINANCEIRO, true), 
	ROLE_MOVIMENTO_FINANCEIRO_COTA("Processamento Financeiro",                                	 	   	    Permissao.ROLE_FINANCEIRO, ROLE_MOVIMENTO_FINANCEIRO_COTA_ALTERACAO, true, null),
	
	ROLE_FINANCEIRO_P3_ALTERACAO("Extração relatório P3", 													Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_P3("Extração relatório P3",																Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_P3_ALTERACAO, true, null),

	ROLE_FINANCEIRO_MANUTENCAO_PUBLICACAO_ALTERACAO("Manutenção de Preço de Publicação Expedida", 			Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_MANUTENCAO_PUBLICACAO("Manutenção de Preço de Publicação Expedida",						Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_MANUTENCAO_PUBLICACAO_ALTERACAO, true, null),

	
	ROLE_FINANCEIRO_HELP_ALTERACAO("Help",           														Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_HELP("Help",           															 	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_HELP_ALTERACAO, true, null),
	
	
	ROLE_ADMINISTRACAO_ALTERACAO("Administração",														  	null, true),
	ROLE_ADMINISTRACAO("Administração",														  			   	null, ROLE_ADMINISTRACAO_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_FECHAR_DIA_ALTERACAO("Fechamento Diário",											Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_FECHAR_DIA("Fechamento Diário",												  	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_FECHAR_DIA_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_CONTROLE_APROVACAO_ALTERACAO("Controle Aprovação",									Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_CONTROLE_APROVACAO("Controle Aprovação",									  		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_CONTROLE_APROVACAO_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO("Painel Processamento	",							  	Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO("Painel Processamento	",							  		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA_ALTERACAO("Follow Up do Sistema",									Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA("Follow Up do Sistema",									  	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO("Grupos de Acesso",						   	   				Permissao.ROLE_ADMINISTRACAO, true), 
	ROLE_ADMINISTRACAO_GRUPOS_ACESSO("Grupos de Acesso",											  	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_CALENDARIO_ALTERACAO("Calendario",													Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_CALENDARIO("Calendario",													  		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_CALENDARIO_ALTERACAO, true, null),
	
	
	ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA_ALTERACAO("Relatórios de Serviço de Entrega",				Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA("Relatórios de Serviço de Entrega",					   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO("Geracao De Arquivos",									    Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_GERACAO_ARQUIVO("Geracao De Arquivos",									           	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_TIPO_NOTA_ALTERACAO("Tipos de NF-e",													Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_TIPO_NOTA("Tipos de NF-e",													  	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_TIPO_NOTA_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA_ALTERACAO("Parâmetros de Sistema",	          					Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA("Parâmetros de Sistema",	          					 		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO("Parâmetros Distribuidor",	 						Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR("Parâmetros Distribuidor",	 						 	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO, true, null),
	
	ROLE_ADMINISTRACAO_HELP_ALTERACAO("Help",									          					Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_HELP("Help",									          					 		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_HELP_ALTERACAO, false, null);

	private String descricao;
	private Permissao permissaoPai;
	private Permissao permissaoAlteracao;
	private boolean isPermissaoAlteracao;
	private boolean isPermissaoMenu;
	private String observacao;
	private RestricoesAcesso restricoesAcesso;
	
	/**
	 * Define permissão de vizualização
	 * 
	 * @param descricao
	 * @param permissaoPai - define  a permissão "Pai" para fins de agrupamento
	 * @param permissaoAlteracao - caso seja uma permissão de tela é necessário definir a permissão de "Alteração" da tela
	 * @param isPermissaoMenu - define se a permissão será apresentada no menu
	 * @param observacao - será exibido em um tooltip ao parar o mouse sobre a regra
	 */
	private Permissao(String descricao, Permissao permissaoPai, Permissao permissaoAlteracao, boolean isPermissaoMenu,
			String observacao) {
		this.descricao = descricao;
		this.permissaoPai = permissaoPai;
		this.permissaoAlteracao = permissaoAlteracao;
		this.isPermissaoMenu = isPermissaoMenu;
		this.observacao = observacao;
	}
	
	/**
	 * Define permissão de vizualização
	 * 
	 * @param descricao
	 * @param permissaoPai - define  a permissão "Pai" para fins de agrupamento
	 * @param permissaoAlteracao - caso seja uma permissão de tela é necessário definir a permissão de "Alteração" da tela
	 * @param isPermissaoMenu - define se a permissão será apresentada no menu
	 * @param observacao - será exibido em um tooltip ao parar o mouse sobre a regra
	 */
	private Permissao(String descricao, Permissao permissaoPai, Permissao permissaoAlteracao, boolean isPermissaoMenu,
			String observacao, RestricoesAcesso restricoesAcesso) {
		this.descricao = descricao;
		this.permissaoPai = permissaoPai;
		this.permissaoAlteracao = permissaoAlteracao;
		this.isPermissaoMenu = isPermissaoMenu;
		this.observacao = observacao;
		this.restricoesAcesso = restricoesAcesso;
	}

	/**
	 * Define permissão de alteração
	 * 
	 * @param descricao
	 * @param permissaoPai - define  a permissão "Pai" para fins de agrupamento
	 * @param isPermissaoMenu - define se a permissão será apresentada no menu
	 */
	private Permissao(String descricao, Permissao permissaoPai, boolean isPermissaoMenu) {
				
		this.descricao = descricao;
		this.permissaoPai = permissaoPai;
		this.isPermissaoAlteracao = true;
		this.isPermissaoMenu = isPermissaoMenu;
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
	
	public Permissao getPermissaoAlteracao() {
		return permissaoAlteracao;
	}

	public boolean isPermissaoAlteracao() {
		return isPermissaoAlteracao;
	}

	public void setPermissaoAlteracao(boolean isPermissaoAlteracao) {
		this.isPermissaoAlteracao = isPermissaoAlteracao;
	}

	public boolean isPermissaoMenu() {
		return isPermissaoMenu;
	}

	public void setPermissaoMenu(boolean isPermissaoMenu) {
		this.isPermissaoMenu = isPermissaoMenu;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	/**
	 * @return the restricoesAcesso
	 */
	public RestricoesAcesso getRestricoesAcesso() {
		return restricoesAcesso;
	}
}
