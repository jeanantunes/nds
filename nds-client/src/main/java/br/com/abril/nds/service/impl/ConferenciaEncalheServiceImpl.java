package br.com.abril.nds.service.impl;

public class ConferenciaEncalheServiceImpl {
	
	/**
	 * Metodos a serem implementados na conferencia de encalhe EMS 005
	 */
	
	// obterListaBoxEncalhe()
	
	// obterBoxPadraoUsuario()
	
	// verificarCotaProcessada() - 			 Verifica se o encalhe para esta cota ja foi conferido.
	//										 Caso positivo retorna true.
	//                                       Caso não haja chamada de encalhe prevista ou não pocessa reabrir
	//									     conferencia devido a data de operacao lancara uma exception.
	
	// verificarCotaEmiteNFe() - 			 Verifica cota emite NFe.
	//										 Caso positivo retorna true

	// inserirDadosNotaFiscalCota()
	
	//
	// obterDadosEncalheCota() - obtem os dados sumarizados de encalhe da cota, e se esta estiver
	//						  	 com sua conferencia reaberta retorna tambem a lista do que ja foi
	//						  	 conferido.
	//	
	//	   Dados sumarizado de encalhe:
	//
	//		->  Reparte,  
	//		->  ( - ) Encalhe, 
	//		->  Valor Venda Dia, 
	//		->  ( + )Outros valores, (Carregar tmb lista de outros valores)
	//		->  ( = )Valor a pagar R$
	
	//	verificarProdutoExistenciaChamadaEncalhe() - cada produto que é adicionado na conferencia de encalhe dever ser
	//												 verificado se existe uma chamada de encalhe para o mesmo.
	//
	//												 Retorna dados do produto caso o mesmo esteja na chamada de encalhe em andamento.
	
	
	
	// salvarDadosConferenciaEncalhe()
	
	// finalizarConferenciaEncalhe() - (caso valor nota esteja diferente do encalhe requisitar correcao)
	
	// - gerarDiferencas() - caso existam diferencas
	
	//    					Se diferenca maior (valor nota maior que valor fisico do encalhe)
	//					    	Disparar a EMS NFe de Venda
	//
	//						Se diferenca menor (valor nota menor que valor fisico do encalhe).
	//							Disparar workflow administrativo NFe devolucao de remessa em consignacao pelo PDV
	//							apenas da diferenca...
	
	
	// - gerarDivida() - 
	
	//   - gerarDividaChamadaEncalheAntecipada()

	
	// - gerarDocumentosConferenciaEncalhe()- Apos finalizar conferencia de encalhe sera verificado
	//									     quais documentos serao gerados e se os mesmos serao impressos
	//										 ou enviados por email.
	//		- gerarSlip()
	
	//		- gerarBoleto()
	
	//		- gerarRecibo()
	
	
	
}
