package br.com.abril.nds.nfe;

import java.util.List;

import br.com.abril.nfe.model.NotaFiscal;

public class NotaFiscalNdsServiceImpl {

	@SuppressWarnings("unused")
	private List<NotaFiscal> obterDadosNotaFiscal(){
		
		StringBuffer sql = new StringBuffer("");

		sql.append(" SELECT ");	

		sql.append(" NOTA_FISCAL_NOVO.ID as idNotaFiscal, 					"); 

		sql.append(" NOTA_FISCAL_NOVO.NUMERO_DOCUMENTO_FISCAL as numero, 	"); 

		sql.append(" NOTA_FISCAL_NOVO.SERIE as serie, 						"); 

		sql.append(" NOTA_FISCAL_NOVO.DATA_EMISSAO as emissao, 				"); 

		sql.append(" 'NORMAL' as tipoEmissao, 		"); 	

		sql.append(" CASE WHEN PESSOA_DESTINATARIO.TIPO = 'J'  THEN NOTA_FISCAL_NOVO.DOCUMENTO_DESTINATARIO 	ELSE NULL END AS  cnpjDestinatario,	");

		sql.append(" CASE WHEN PESSOA_REMETENTE.TIPO 	= 'J'  THEN NOTA_FISCAL_NOVO.DOCUMENTO_EMITENTE 	 	ELSE NULL END AS  cnpjRemetente,	");

		sql.append(" CASE WHEN PESSOA_REMETENTE.TIPO 	= 'F'  THEN NOTA_FISCAL_NOVO.DOCUMENTO_EMITENTE 		ELSE NULL END AS  cpfRemetente,		");

		sql.append(" NOTA_FISCAL_NOVO.STATUS as statusNfe,  "); 

		sql.append(" CASE WHEN NOTA_FISCAL_NOVO.TIPO_OPERACAO = 0 THEN 'ENTRADA' ELSE 'SAIDA' END AS tipoNfe,	");

		sql.append(" NOTA_FISCAL_NOVO.MOTIVO as movimentoIntegracao ");

		sql.append(" from NOTA_FISCAL ");
		
		return null;
	}
	
	private void salvarNotaFiscal(){
		
	}
	
}
