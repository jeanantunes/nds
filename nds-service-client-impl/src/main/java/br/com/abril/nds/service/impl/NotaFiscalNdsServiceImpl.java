package br.com.abril.nds.service.impl;

import java.util.List;

import br.com.abril.nds.service.NotaFiscalNdsService;
import br.com.abril.nfe.model.NotaFiscal;

public class NotaFiscalNdsServiceImpl implements NotaFiscalNdsService {

	@SuppressWarnings("unused")
	private List<NotaFiscal> obterDadosNotaFiscal(){
		
		StringBuffer sql = new StringBuffer("");

		sql.append(" SELECT ");	

		sql.append(" NOTA_FISCAL.ID as idNotaFiscal, 					"); 

		sql.append(" NOTA_FISCAL.NUMERO_DOCUMENTO_FISCAL as numero, 	"); 

		sql.append(" NOTA_FISCAL.SERIE as serie, 						"); 

		sql.append(" NOTA_FISCAL.DATA_EMISSAO as emissao, 				"); 

		sql.append(" 'NORMAL' as tipoEmissao, 		"); 	

		sql.append(" CASE WHEN PESSOA_DESTINATARIO.TIPO = 'J'  THEN NOTA_FISCAL.DOCUMENTO_DESTINATARIO 	ELSE NULL END AS  cnpjDestinatario,	");

		sql.append(" CASE WHEN PESSOA_REMETENTE.TIPO 	= 'J'  THEN NOTA_FISCAL.DOCUMENTO_EMITENTE 	 	ELSE NULL END AS  cnpjRemetente,	");

		sql.append(" CASE WHEN PESSOA_REMETENTE.TIPO 	= 'F'  THEN NOTA_FISCAL.DOCUMENTO_EMITENTE 		ELSE NULL END AS  cpfRemetente,		");

		sql.append(" NOTA_FISCAL.STATUS as statusNfe,  "); 

		sql.append(" CASE WHEN NOTA_FISCAL.TIPO_OPERACAO = 0 THEN 'ENTRADA' ELSE 'SAIDA' END AS tipoNfe,	");

		sql.append(" NOTA_FISCAL.MOTIVO as movimentoIntegracao ");

		sql.append(" from NOTA_FISCAL ");
		
		return null;
	}
	
	private void salvarNotaFiscal(br.com.abril.nds.model.fiscal.NotaFiscal notaFiscal){
		
	}
	
}
