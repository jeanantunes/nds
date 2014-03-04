package br.com.abril.nds.model.ftf.envio;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro04 extends FTFBaseDTO implements FTFCommons {

	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "4";
	
	@FTFfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codEstabelecimentoEmissor;

	@FTFfield(tamanho = 14, tipo = "char", ordem = 3)
	private String cnpjEstabelecimentoEmissor;

	@FTFfield(tamanho = 11, tipo = "char", ordem = 4)
	private String codLocal;

	@FTFfield(tamanho = 2, tipo = "char", ordem = 5)
	private String tipoPedido;

	@FTFfield(tamanho = 8, tipo = "char", ordem = 6)
	private String numeroDocOrigem;
	
	@FTFfield(tamanho=3, tipo="numeric", ordem=7)
	private String numItemPedido;
 
	@FTFfield(tamanho=6, tipo="char", ordem=8)
	private String codSetorialCRP;
		
	@FTFfield(tamanho=5, tipo="char", ordem=9)
	private String percentualRateio;
	
	@FTFfield(tamanho=5, tipo="char", ordem=10)
	private String codEvento;
	
	@FTFfield(tamanho=3, tipo="char", ordem=11)
	private String codPlanoFinanceiro;
	
	@FTFfield(tamanho=1, tipo="char", ordem=12)
	private String indicadorCirculacaoRevista;
	
	@FTFfield(tamanho=11, tipo="char", ordem=13)
	private String codUnidadeGerencialOuFiscal;
	
	@FTFfield(tamanho=2, tipo="char", ordem=14)
	private String codAnalitica;
	
	@FTFfield(tamanho=15, tipo="numeric", ordem=15)
	private String valorRateio;
	
	@FTFfield(tamanho=2, tipo="char", ordem=16)
	private String tipo;
	
	@FTFfield(tamanho=4, tipo="char", ordem=17)
	private String subTipo;
	
	@FTFfield(tamanho=4, tipo="char", ordem=18)
	private String codEmpresaIQ;
	
	@FTFfield(tamanho=11, tipo="char", ordem=19)
	private String centroCustoAntigoIQ;
	
	@FTFfield(tamanho=10, tipo="char", ordem=20)
	private String idCentroCustoCorporativo;
	
	@Override
	public void setCodEstabelecimentoEmissor(String codEstabelecimentoEmissor) {
		this.codEstabelecimentoEmissor = codEstabelecimentoEmissor;
	}

	@Override
	public void setCnpjEstabelecimentoEmissor(String cnpjEstabelecimentoEmissor) {
		this.cnpjEstabelecimentoEmissor = cnpjEstabelecimentoEmissor;
	}

	@Override
	public void setCodLocal(String codLocal) {
		this.codLocal = codLocal;
	}

	@Override
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	@Override
	public void setNumeroDocOrigem(String numeroDocOrigem) {
		this.numeroDocOrigem = numeroDocOrigem;
	}
	
}
