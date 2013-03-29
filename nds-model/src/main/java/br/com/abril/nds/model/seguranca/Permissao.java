package br.com.abril.nds.model.seguranca;

/**
 * Enumerated de tipos de permissões
 * A ordem dos enums define a ordem que aparecerão nos menus.
 * As permissões pais devem sempre vir antes de qualquer outra permissão Filho. 
 * @author InfoA2
 */
public enum Permissao
{

	ROLE_CADASTRO_ALTERACAO("Cadastro", 													   				null, true),
	ROLE_CADASTRO("Cadastro", 												 						  	   	null, ROLE_CADASTRO_ALTERACAO),
	
	ROLE_CADASTRO_PRODUTO_ALTERACAO("Produto", 										 		   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_PRODUTO("Produto", 										 						  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_PRODUTO_ALTERACAO),
	
	ROLE_CADASTRO_EDICAO_ALTERACAO("Edição", 											 	   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_EDICAO("Edição", 											 						  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_EDICAO_ALTERACAO),
	
	ROLE_CADASTRO_COTA_ALTERACAO("Cota", 													   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_COTA("Cota", 																		  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_COTA_ALTERACAO),
	
	ROLE_CADASTRO_COTA_BASE_ALTERACAO("Cota Base",											   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_COTA_BASE("Cota Base",															  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_COTA_BASE_ALTERACAO),
	
	ROLE_CADASTRO_FIADOR_ALTERACAO("Fiador", 												   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_FIADOR("Fiador", 																	  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_FIADOR_ALTERACAO),
	
	ROLE_CADASTRO_ENTREGADOR_ALTERACAO("Entregador", 										   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_ENTREGADOR("Entregador", 															  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_ENTREGADOR_ALTERACAO),
	
	ROLE_CADASTRO_TRANSPORTADOR_ALTERACAO("Transportador", 						               				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_TRANSPORTADOR("Transportador", 						                        	 	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_TRANSPORTADOR_ALTERACAO),
	
	ROLE_CADASTRO_FORNECEDOR_ALTERACAO("Fornecedor", 										   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_FORNECEDOR("Fornecedor", 															  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_FORNECEDOR_ALTERACAO),
	
	ROLE_CADASTRO_ROTEIRIZACAO_ALTERACAO("Roteirização", 								       				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_ROTEIRIZACAO("Roteirização", 														       	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_ROTEIRIZACAO_ALTERACAO),
	
	ROLE_CADASTRO_BOX_ALTERACAO("Box", 												 		   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_BOX("Box", 												 						  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_BOX_ALTERACAO),
	
	ROLE_CADASTRO_BANCO_ALTERACAO("Banco", 											 		   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_BANCO("Banco", 											 						  	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_BANCO_ALTERACAO),
	
	ROLE_CADASTRO_ALTERACAO_COTA_ALTERACAO("Alteração / Cota",			          			   				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_ALTERACAO_COTA("Alteração / Cota",			          					 		       	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_ALTERACAO_COTA_ALTERACAO),
	
	ROLE_CADASTRO_HELP_ALTERACAO("Help",                                                       				Permissao.ROLE_CADASTRO, true),
	ROLE_CADASTRO_HELP("Help",                                                                     	 	   	Permissao.ROLE_CADASTRO, ROLE_CADASTRO_HELP_ALTERACAO),
	
	
	ROLE_LANCAMENTO_ALTERACAO("Lançamento",													   				null, true),	
	ROLE_LANCAMENTO("Lançamento",														  				   	null, ROLE_LANCAMENTO_ALTERACAO),
	
	ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO("Bal. Matriz Lançamento",				   				Permissao.ROLE_LANCAMENTO, true), 
	ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ("Bal. Matriz Lançamento",							   			   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO), 
	
	ROLE_LANCAMENTO_FURO_PRODUTO_ALTERACAO("Furo de Lançamento",							   				Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_FURO_PRODUTO("Furo de Lançamento",												  	   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_FURO_PRODUTO_ALTERACAO),
	
	ROLE_LANCAMENTO_PARCIAIS_ALTERACAO("Parciais", 											   				Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_PARCIAIS("Parciais", 																   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_PARCIAIS_ALTERACAO),
	
	ROLE_LANCAMENTO_RELATORIO_VENDAS_ALTERACAO("Relatório de Vendas",						   				Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_RELATORIO_VENDAS("Relatório de Vendas",											       	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_RELATORIO_VENDAS_ALTERACAO),
	
	ROLE_LANCAMENTO_VENDA_PRODUTO_ALTERACAO("Venda por Produto",						  	   			   	Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_VENDA_PRODUTO("Venda por Produto",												  	   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_VENDA_PRODUTO_ALTERACAO),
	
	ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS_ALTERACAO("Relatório Tipos de Produtos",	  	   				Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS("Relatório Tipos de Produtos",							  	   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_RELATORIO_TIPOS_PRODUTOS_ALTERACAO),
	
	ROLE_LANCAMENTO_HELP_ALTERACAO("Help",													  		 		Permissao.ROLE_LANCAMENTO, true),
	ROLE_LANCAMENTO_HELP("Help",																		   	Permissao.ROLE_LANCAMENTO, ROLE_LANCAMENTO_HELP_ALTERACAO),
	
	
	ROLE_DISTRIBUICAO_ALTERACAO("Distribuição",												   				null, true),
	ROLE_DISTRIBUICAO("Distribuição",																	   	null, ROLE_DISTRIBUICAO_ALTERACAO),
	
	ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO("Ajuste de reparte",	 									Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE("Ajuste de reparte",	 										   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_AJUSTE_DE_REPARTE_ALTERACAO),
	
	ROLE_DISTRIBUICAO_INFORMACOES_PRODUTO_ALTERACAO("Informações do Produto",	 							Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_INFORMACOES_PRODUTO("Informações do Produto",	 									   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_INFORMACOES_PRODUTO_ALTERACAO),
	
	ROLE_DISTRIBUICAO_AREAINFLUENCIA_GERADORFLUXO_ALTERACAO("Área de influência / Gerador de Fluxo", 		Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_AREAINFLUENCIA_GERADORFLUXO("Área de influência / Gerador de Fluxo", 				   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_AREAINFLUENCIA_GERADORFLUXO_ALTERACAO),

	ROLE_DISTRIBUICAO_REGIAO_ALTERACAO("Região", 				  											Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_REGIAO("Região", 				  													   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_REGIAO_ALTERACAO),
	
	ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO_ALTERACAO("Segmento Não Recebido",					 			Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO("Segmento Não Recebido",					 				   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO_ALTERACAO),
	
	ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS_ALTERACAO("Exceção de Segmentos e Parciais",				Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS("Exceção de Segmentos e Parciais",					 	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS_ALTERACAO),
	
	ROLE_DISTRIBUICAO_HISTOGRAMA_VENDAS_ALTERACAO("Histograma de Vendas", 				   					Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_HISTOGRAMA_VENDAS("Histograma de Vendas", 				   						   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_HISTOGRAMA_VENDAS_ALTERACAO),

	ROLE_DISTRIBUICAO_FIXACAO_REPARTE_ALTERACAO("Fixação de Reparte", 				   						Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_FIXACAO_REPARTE("Fixação de Reparte", 				   							   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_FIXACAO_REPARTE_ALTERACAO),
	
	ROLE_DISTRIBUICAO_MIX_COTA_PRODUTO_ALTERACAO("Mix por Cota/Publicação", 				   				Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_MIX_COTA_PRODUTO("Mix por Cota/Publicação", 				   						   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_MIX_COTA_PRODUTO_ALTERACAO),
	
	ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA_ALTERACAO("Classificação Não Recebida",					Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA("Classificação Não Recebida",						 	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_CLASSIFICACAO_NAO_RECEBIDA_ALTERACAO),

	ROLE_DISTRIBUICAO_DESENGLOBACAO_ALTERACAO("Desenglobação",												Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_DESENGLOBACAO("Desenglobação",												 	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_DESENGLOBACAO_ALTERACAO),
	
	ROLE_DISTRIBUICAO_HISTORICO_VENDA_ALTERACAO("Histórico de Vendas",										Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_HISTORICO_VENDA("Histórico de Vendas",										 	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_HISTORICO_VENDA_ALTERACAO),
	
	ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO_ALTERACAO("Matriz Distribuição"				 ,					Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO("Matriz Distribuição"				 ,					 	   	   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO_ALTERACAO),
	
	ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO("Analise de Estudos", 				  					Permissao.ROLE_DISTRIBUICAO, true),
	ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS("Analise de Estudos", 				  							   	Permissao.ROLE_DISTRIBUICAO, ROLE_DISTRIBUICAO_ANALISE_DE_ESTUDOS_ALTERACAO),
	
	
	ROLE_ESTOQUE_ALTERACAO("Estoque",																	  	null, true),
	ROLE_ESTOQUE("Estoque",																	  			   	null, ROLE_ESTOQUE_ALTERACAO),
	
	ROLE_ESTOQUE_RECEBIMENTO_FISICO_ALTERACAO("Recebimento Físico",											Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_RECEBIMENTO_FISICO("Recebimento Físico",												   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_RECEBIMENTO_FISICO_ALTERACAO),
	
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO("Lançamento Faltas e Sobras",							Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS("Lançamento Faltas e Sobras",									   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_LANCAMENTO_FALTAS_SOBRAS_ALTERACAO),
	
	ROLE_ESTOQUE_CONSULTA_NOTAS_ALTERACAO("Consulta de Notas",												Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_CONSULTA_NOTAS("Consulta de Notas",													   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_CONSULTA_NOTAS_ALTERACAO),
	
	ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS_ALTERACAO("Consulta Faltas e Sobras",								Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS("Consulta Faltas e Sobras",										   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_CONSULTA_FALTAS_SOBRAS_ALTERACAO),
	
	ROLE_ESTOQUE_EXTRATO_EDICAO_ALTERACAO("Extrato de Edição",												Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_EXTRATO_EDICAO("Extrato de Edição",													   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_EXTRATO_EDICAO_ALTERACAO),
	
	ROLE_ESTOQUE_VISAO_DO_ESTOQUE_ALTERACAO("Visão do Estoque",                                             Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_VISAO_DO_ESTOQUE("Visão do Estoque",                                                      	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_VISAO_DO_ESTOQUE_ALTERACAO),
	
	ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO_ALTERACAO("Edições Fechadas com Saldo",								Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO("Edições Fechadas com Saldo",									   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_EDICOES_FECHADAS_SALDO_ALTERACAO),
	
	ROLE_ESTOQUE_HELP_ALTERACAO("Help",							                                            Permissao.ROLE_ESTOQUE, true),
	ROLE_ESTOQUE_HELP("Help",							                                                   	Permissao.ROLE_ESTOQUE, ROLE_ESTOQUE_HELP_ALTERACAO),
	
	
	ROLE_EXPEDICAO_ALTERACAO("Expedição",															  		null, true), 
	ROLE_EXPEDICAO("Expedição",															  				   	null, ROLE_EXPEDICAO_ALTERACAO), 
	
	ROLE_EXPEDICAO_MAPA_ABASTECIMENTO_ALTERACAO("Mapa de Abastecimento",									Permissao.ROLE_EXPEDICAO, true), 
	ROLE_EXPEDICAO_MAPA_ABASTECIMENTO("Mapa de Abastecimento",										  	   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_MAPA_ABASTECIMENTO_ALTERACAO), 
	
	ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO_ALTERACAO("Confirma Expedição",						   				Permissao.ROLE_EXPEDICAO, true), 
	ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO("Confirma Expedição",											  	   	Permissao.ROLE_EXPEDICAO, Permissao.ROLE_EXPEDICAO_CONFIRMA_EXPEDICAO_ALTERACAO), 
	
	ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO_ALTERACAO("Nota de Envio",	 										Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO("Nota de Envio",	 												   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_GERACAO_NOTA_ENVIO_ALTERACAO),
	
	ROLE_EXPEDICAO_COTA_AUSENTE_ALTERACAO("Cota Ausente", 													Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_COTA_AUSENTE("Cota Ausente", 														   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_COTA_AUSENTE_ALTERACAO),
	
	ROLE_EXPEDICAO_RESUMO_EXPEDICAO_ALTERACAO("Resumo de Expedição", 										Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_RESUMO_EXPEDICAO("Resumo de Expedição", 												   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_RESUMO_EXPEDICAO_ALTERACAO),
	
	ROLE_EXPEDICAO_ROMANEIOS_ALTERACAO("Romaneios", 														Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_ROMANEIOS("Romaneios", 															  	   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_ROMANEIOS_ALTERACAO),
	
	ROLE_EXPEDICAO_HELP_ALTERACAO("Help", 																	Permissao.ROLE_EXPEDICAO, true),
	ROLE_EXPEDICAO_HELP("Help", 																		   	Permissao.ROLE_EXPEDICAO, ROLE_EXPEDICAO_HELP_ALTERACAO),

	
	ROLE_RECOLHIMENTO_ALTERACAO("Recolhimento", 													  		null, true),
	ROLE_RECOLHIMENTO("Recolhimento", 													  				   	null, ROLE_RECOLHIMENTO_ALTERACAO),
	
	ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO("Bal. Matriz Recolhimento", 						   	Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ("Bal. Matriz Recolhimento", 						   			   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO),
	
	ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE_ALTERACAO("Informe Recolhimento", 						  	Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE("Informe Recolhimento", 						  		   	   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CONSULTA_INFORME_ENCALHE_ALTERACAO),
	
	ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO("CE Antecipada - Produto", 							Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO("CE Antecipada - Produto", 							  		   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CE_ANTECIPADA_PRODUTO_ALTERACAO),
	
	ROLE_RECOLHIMENTO_EMISSAO_CE_ALTERACAO("Emissão CE",													Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_EMISSAO_CE("Emissão CE",															   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_EMISSAO_CE_ALTERACAO),
	
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO("Conferência de Encalhe", 						  	Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA("Conferência de Encalhe", 						  		   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO),
	
	ROLE_RECOLHIMENTO_VENDA_ENCALHE_ALTERACAO("Venda de Encalhe / Suplementar",								Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_VENDA_ENCALHE("Venda de Encalhe / Suplementar",									   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_VENDA_ENCALHE_ALTERACAO),
	
	ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO("Fechamento Encalhe",									Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE("Fechamento Encalhe",											   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO),
	
	ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO("Fechamento CE - Integração",							Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO("Fechamento CE - Integração",								   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO),
	
	ROLE_RECOLHIMENTO_DIGICACAO_CONTAGEM_DEVOLUCAO_ALTERACAO("Devolução ao Fornecedor",	 					Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_DIGICACAO_CONTAGEM_DEVOLUCAO("Devolução ao Fornecedor",	 						   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_DIGICACAO_CONTAGEM_DEVOLUCAO_ALTERACAO),
	
	ROLE_RECOLHIMENTO_CHAMADAO_ALTERACAO("Chamadão", 														Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CHAMADAO("Chamadão", 														  		   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CHAMADAO_ALTERACAO),
	
	ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA_ALTERACAO("Consulta Encalhe",    								Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA("Consulta Encalhe",    								  	   	   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA_ALTERACAO),
	
	ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA_ALTERACAO("Emissão das Bandeiras",									Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA("Emissão das Bandeiras",											   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA_ALTERACAO),
	
	ROLE_RECOLHIMENTO_HELP_ALTERACAO("Help", 									   							Permissao.ROLE_RECOLHIMENTO, true),
	ROLE_RECOLHIMENTO_HELP("Help", 									   							 		   	Permissao.ROLE_RECOLHIMENTO, ROLE_RECOLHIMENTO_HELP_ALTERACAO),

	
	ROLE_NFE_ALTERACAO("NF-e",																	  	   		null, true),
	ROLE_NFE("NF-e",																	  	   			   	null, ROLE_NFE_ALTERACAO),
	
	ROLE_NFE_RETORNO_NFE_ALTERACAO("Retorno NF-e",															Permissao.ROLE_NFE, true),
	ROLE_NFE_RETORNO_NFE("Retorno NF-e",																   	Permissao.ROLE_NFE, ROLE_NFE_RETORNO_NFE_ALTERACAO),
	
	ROLE_NFE_ENTRADA_NFE_TERCEIROS_ALTERACAO("Entrada de NF-e Terceiros",									Permissao.ROLE_NFE, true),
	ROLE_NFE_ENTRADA_NFE_TERCEIROS("Entrada de NF-e Terceiros",											   	Permissao.ROLE_NFE, ROLE_NFE_ENTRADA_NFE_TERCEIROS_ALTERACAO),
	
	ROLE_NFE_GERACAO_NFE_ALTERACAO("Geração de NF-e", 													  	Permissao.ROLE_NFE, true),
	ROLE_NFE_GERACAO_NFE("Geração de NF-e", 													  	   	   	Permissao.ROLE_NFE, ROLE_NFE_GERACAO_NFE_ALTERACAO),
	
	ROLE_NFE_IMPRESSAO_NFE_ALTERACAO("Impressão NF-e",														Permissao.ROLE_NFE, true),
	ROLE_NFE_IMPRESSAO_NFE("Impressão NF-e",															   	Permissao.ROLE_NFE, ROLE_NFE_IMPRESSAO_NFE_ALTERACAO),
	
	ROLE_NFE_PAINEL_MONITOR_NFE_ALTERACAO("Painel Monitor NF-e",											Permissao.ROLE_NFE, true),
	ROLE_NFE_PAINEL_MONITOR_NFE("Painel Monitor NF-e",													   	Permissao.ROLE_NFE, ROLE_NFE_PAINEL_MONITOR_NFE_ALTERACAO),
	
	
	ROLE_FINANCEIRO_ALTERACAO("Financeiro", 					   				                         	null, true),
	ROLE_FINANCEIRO("Financeiro", 					   				                         	  	  	   	null, ROLE_FINANCEIRO_ALTERACAO),
	
	ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO("Baixa Financeira", 					                        Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_BAIXA_BANCARIA("Baixa Financeira", 					                         	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO),
	
	ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA_ALTERACAO("Negociação de Divida",			                         	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA("Negociação de Divida",			                         	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA_ALTERACAO),
	
	ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA_ALTERACAO("Débitos / Créditos Cota",                              Permissao.ROLE_FINANCEIRO, true), 
	ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA("Débitos / Créditos Cota",                              	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA_ALTERACAO), 
	
	ROLE_FINANCEIRO_IMPRESSAO_BOLETOS_ALTERACAO("Impressão de Boletos",                                     Permissao.ROLE_FINANCEIRO, true), 
	ROLE_FINANCEIRO_IMPRESSAO_BOLETOS("Impressão de Boletos",                                     	       	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_IMPRESSAO_BOLETOS_ALTERACAO), 
	
	ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA_ALTERACAO("Manutenção de Status Cota",                           Permissao.ROLE_FINANCEIRO, true), 
	ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA("Manutenção de Status Cota",                           	       	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_MANUTENCAO_STATUS_COTA_ALTERACAO), 
	
	ROLE_FINANCEIRO_SUSPENSAO_COTA_ALTERACAO("Suspensão Cota",                                             	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_SUSPENSAO_COTA("Suspensão Cota",                                             	 	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_SUSPENSAO_COTA_ALTERACAO),
	
	ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA_ALTERACAO("Consulta Boletos por Cota",                         	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA("Consulta Boletos por Cota",                         	  	   	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_CONSULTA_BOLETOS_COTA_ALTERACAO),
	
	ROLE_FINANCEIRO_CONTA_CORRENTE_ALTERACAO("Conta Corrente",                                              Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_CONTA_CORRENTE("Conta Corrente",                                              	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_CONTA_CORRENTE_ALTERACAO),
	
	ROLE_FINANCEIRO_CONTAS_A_PAGAR_ALTERACAO("Contas a Pagar",                                              Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_CONTAS_A_PAGAR("Contas a Pagar",                                              	  	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_CONTAS_A_PAGAR_ALTERACAO),
	
	ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA_ALTERACAO("Inadimplência",                          	  	   	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA("Inadimplência",                          	  	   			   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_HISTORICO_INADIMPLENCIA_ALTERACAO),
	
	ROLE_FINANCEIRO_CONSIGNADO_COTA_ALTERACAO("Consulta Consignado",                                        Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_CONSIGNADO_COTA("Consulta Consignado",                                                 	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_CONSIGNADO_COTA_ALTERACAO),
	
	ROLE_FINANCEIRO_TIPO_DESCONTO_COTA_ALTERACAO("Tipo de Desconto Cota",								  	Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_TIPO_DESCONTO_COTA("Tipo de Desconto Cota",								  		   	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_TIPO_DESCONTO_COTA_ALTERACAO),
	
	ROLE_FINANCEIRO_RELATORIO_DE_GARANTIAS_ALTERACAO("Relatório de Garantias",								Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_RELATORIO_DE_GARANTIAS("Relatório de Garantias",									   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_RELATORIO_DE_GARANTIAS_ALTERACAO),
	
	ROLE_FINANCEIRO_PARAMETROS_COBRANCA_ALTERACAO("Parâmetros de Cobrança",                                	Permissao.ROLE_FINANCEIRO, true), 
	ROLE_FINANCEIRO_PARAMETROS_COBRANCA("Parâmetros de Cobrança",                                	 	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_PARAMETROS_COBRANCA_ALTERACAO), 
	
	ROLE_FINANCEIRO_HELP_ALTERACAO("Help",           														Permissao.ROLE_FINANCEIRO, true),
	ROLE_FINANCEIRO_HELP("Help",           															 	   	Permissao.ROLE_FINANCEIRO, ROLE_FINANCEIRO_HELP_ALTERACAO),
	
	
	ROLE_ADMINISTRACAO_ALTERACAO("Administração",														  	null, true),
	ROLE_ADMINISTRACAO("Administração",														  			   	null, ROLE_ADMINISTRACAO_ALTERACAO),
	
	ROLE_ADMINISTRACAO_FECHAR_DIA_ALTERACAO("Fechamento Diário",											Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_FECHAR_DIA("Fechamento Diário",												  	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_FECHAR_DIA_ALTERACAO),
	
	ROLE_ADMINISTRACAO_CONTROLE_APROVACAO_ALTERACAO("Controle Aprovação",									Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_CONTROLE_APROVACAO("Controle Aprovação",									  		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_CONTROLE_APROVACAO_ALTERACAO),
	
	ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO("Painel Processamento	",							  	Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO("Painel Processamento	",							  		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_PAINEL_PROCESSAMENTO_ALTERACAO),
	
	ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA_ALTERACAO("Follow Up do Sistema",									Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA("Follow Up do Sistema",									  	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_FOLLOW_UP_SISTEMA_ALTERACAO),
	
	ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO("Grupos de Acesso",						   	   				Permissao.ROLE_ADMINISTRACAO, true), 
	ROLE_ADMINISTRACAO_GRUPOS_ACESSO("Grupos de Acesso",											  	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_GRUPOS_ACESSO_ALTERACAO),
	
	ROLE_ADMINISTRACAO_CALENDARIO_ALTERACAO("Calendario",													Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_CALENDARIO("Calendario",													  		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_CALENDARIO_ALTERACAO),
	
	
	ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA_ALTERACAO("Relatórios de Serviço de Entrega",				Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA("Relatórios de Serviço de Entrega",					   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_RELATORIO_SERVICO_ENTREGA_ALTERACAO),
	
	ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO("Geracao De Arquivos",									    Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_GERACAO_ARQUIVO("Geracao De Arquivos",									           	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_GERACAO_ARQUIVO_ALTERACAO),
	
	ROLE_ADMINISTRACAO_TIPO_NOTA_ALTERACAO("Tipos de NF-e",													Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_TIPO_NOTA("Tipos de NF-e",													  	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_TIPO_NOTA_ALTERACAO),
	
	ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA_ALTERACAO("Parâmetros de Sistema",	          					Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA("Parâmetros de Sistema",	          					 		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA_ALTERACAO),
	
	ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO("Parâmetros Distribuidor",	 						Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR("Parâmetros Distribuidor",	 						 	   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_PARAMETROS_DISTRIBUIDOR_ALTERACAO),
	
	ROLE_ADMINISTRACAO_HELP_ALTERACAO("Help",									          					Permissao.ROLE_ADMINISTRACAO, true),
	ROLE_ADMINISTRACAO_HELP("Help",									          					 		   	Permissao.ROLE_ADMINISTRACAO, ROLE_ADMINISTRACAO_HELP_ALTERACAO);

	private String descricao;
	private Permissao permissaoPai;
	private Permissao permissaoAlteracao;
	private boolean isPermissaoAlteracao;

	/**
	 * @param descricao - A descriÃ§Ã£o que aparecerÃ¡ no menu
	 * @param classeExibicao - a class (css) definida no menu principal (null, caso nÃ£o seja o menu principal)
	 * @param permissaoPai - a Permissao pai da Permissao (que serÃ¡ o menu pai do submenu)
	 */
	private Permissao(String descricao, Permissao permissaoPai) {
		this.descricao = descricao;
		this.permissaoPai = permissaoPai;
	}
	
	private Permissao(String descricao, Permissao permissaoPai, Permissao permissaoAlteracao) {
		this.descricao = descricao;
		this.permissaoPai = permissaoPai;
		this.permissaoAlteracao = permissaoAlteracao;
	}
	
	private Permissao(String descricao, Permissao permissaoPai, boolean isPermissaoAlteracao) {
		this.descricao = descricao;
		this.permissaoPai = permissaoPai;
		this.isPermissaoAlteracao = isPermissaoAlteracao;
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
}
