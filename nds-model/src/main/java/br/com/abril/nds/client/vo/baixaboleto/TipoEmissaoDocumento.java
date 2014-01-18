package br.com.abril.nds.client.vo.baixaboleto;

import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;

public enum TipoEmissaoDocumento {

	IMPRESSAO_SLIP(TipoParametrosDistribuidorEmissaoDocumento.SLIP),
	IMPRESSAO_BOLETO_RECIBO(TipoParametrosDistribuidorEmissaoDocumento.BOLETO),
	IMPRESSAO_BOLETO_SLIP(TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP),
	IMPRESSAO_NOTA_ENVIO(TipoParametrosDistribuidorEmissaoDocumento.NOTA_ENVIO),
	IMPRESSAO_CHAMADA_ENCALHE(TipoParametrosDistribuidorEmissaoDocumento.CHAMADA_ENCALHE),
	
	EMAIL_SLIP(TipoParametrosDistribuidorEmissaoDocumento.SLIP),
	EMAIL_BOLETO_RECIBO(TipoParametrosDistribuidorEmissaoDocumento.BOLETO),
	EMAIL_BOLETO_SLIP(TipoParametrosDistribuidorEmissaoDocumento.BOLETO_SLIP),
	EMAIL_NOTA_ENVIO(TipoParametrosDistribuidorEmissaoDocumento.NOTA_ENVIO),
	EMAIL_CHAMADA_ENCALHE(TipoParametrosDistribuidorEmissaoDocumento.CHAMADA_ENCALHE);
	
	private TipoParametrosDistribuidorEmissaoDocumento parametroEmissao;
	
	TipoEmissaoDocumento(TipoParametrosDistribuidorEmissaoDocumento parametroEmissao) {
		
		this.parametroEmissao = parametroEmissao;
	}
	
	public TipoParametrosDistribuidorEmissaoDocumento getParametroEmissao() {
		return this.parametroEmissao;
	}
}
