package br.com.abril.nds.model.ftf.envio;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.ftfutil.FTFBaseDTO;
import br.com.abril.nds.ftfutil.FTFfield;
import br.com.abril.nds.model.ftf.FTFCommons;

public class FTFEnvTipoRegistro03 extends FTFBaseDTO implements FTFCommons {

	@FTFfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "3";
	
	@FTFfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codigoEstabelecimentoEmissor;

	@FTFfield(tamanho = 14, tipo = "char", ordem = 3)
	private String cnpjEmpresaEmissora;

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
	
	@FTFfield(tamanho=3, tipo="char", ordem=18)
	private String codEmpresaIQ;
	
	@FTFfield(tamanho=11, tipo="char", ordem=19)
	private String centroCustoAntigoIQ;
	
	@FTFfield(tamanho=10, tipo="char", ordem=20)
	private String idCentroCustoCorporativo;

	public String getCodigoEstabelecimentoEmissor() {
		return codigoEstabelecimentoEmissor;
	}

	public void setCodigoEstabelecimentoEmissor(String codigoEstabelecimentoEmissor) {
		this.codigoEstabelecimentoEmissor = codigoEstabelecimentoEmissor;
	}

	@Override
	public void setCnpjEmpresaEmissora(String cnpjEmpresaEmissora) {
		this.cnpjEmpresaEmissora = cnpjEmpresaEmissora != null ? cnpjEmpresaEmissora.replaceAll("\\D+","") : null;
	}

	@Override
	public void setCodLocal(String codLocal) {
		this.codLocal = codLocal != null ? StringUtils.rightPad(codLocal, 11, ' ') : StringUtils.leftPad("", 11, ' ');
	}

	@Override
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido != null ? StringUtils.leftPad(tipoPedido, 2, '0') : StringUtils.leftPad("", 2, '0');
	}

	@Override
	public void setNumeroDocOrigem(String numeroDocOrigem) {
		this.numeroDocOrigem = numeroDocOrigem;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getNumItemPedido() {
		return numItemPedido;
	}

	public void setNumItemPedido(String numItemPedido) {
		this.numItemPedido = numItemPedido;
	}

	public String getCodSetorialCRP() {
		return codSetorialCRP;
	}

	public void setCodSetorialCRP(String codSetorialCRP) {
		this.codSetorialCRP = codSetorialCRP;
	}

	public String getPercentualRateio() {
		return percentualRateio;
	}

	public void setPercentualRateio(String percentualRateio) {
		this.percentualRateio = percentualRateio;
	}

	public String getCodEvento() {
		return codEvento;
	}

	public void setCodEvento(String codEvento) {
		this.codEvento = codEvento;
	}

	public String getCodPlanoFinanceiro() {
		return codPlanoFinanceiro;
	}

	public void setCodPlanoFinanceiro(String codPlanoFinanceiro) {
		this.codPlanoFinanceiro = codPlanoFinanceiro;
	}

	public String getIndicadorCirculacaoRevista() {
		return indicadorCirculacaoRevista;
	}

	public void setIndicadorCirculacaoRevista(String indicadorCirculacaoRevista) {
		this.indicadorCirculacaoRevista = indicadorCirculacaoRevista;
	}

	public String getCodUnidadeGerencialOuFiscal() {
		return codUnidadeGerencialOuFiscal;
	}

	public void setCodUnidadeGerencialOuFiscal(String codUnidadeGerencialOuFiscal) {
		this.codUnidadeGerencialOuFiscal = codUnidadeGerencialOuFiscal;
	}

	public String getCodAnalitica() {
		return codAnalitica;
	}

	public void setCodAnalitica(String codAnalitica) {
		this.codAnalitica = codAnalitica;
	}

	public String getValorRateio() {
		return valorRateio;
	}

	public void setValorRateio(String valorRateio) {
		this.valorRateio = valorRateio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(String subTipo) {
		this.subTipo = subTipo;
	}

	public String getCodEmpresaIQ() {
		return codEmpresaIQ;
	}

	public void setCodEmpresaIQ(String codEmpresaIQ) {
		this.codEmpresaIQ = codEmpresaIQ;
	}

	public String getCentroCustoAntigoIQ() {
		return centroCustoAntigoIQ;
	}

	public void setCentroCustoAntigoIQ(String centroCustoAntigoIQ) {
		this.centroCustoAntigoIQ = centroCustoAntigoIQ;
	}

	public String getIdCentroCustoCorporativo() {
		return idCentroCustoCorporativo;
	}

	public void setIdCentroCustoCorporativo(String idCentroCustoCorporativo) {
		this.idCentroCustoCorporativo = idCentroCustoCorporativo;
	}

	public String getCnpjEmpresaEmissora() {
		return cnpjEmpresaEmissora;
	}

	public String getCodLocal() {
		return codLocal;
	}

	public String getTipoPedido() {
		return tipoPedido;
	}

	public String getNumeroDocOrigem() {
		return numeroDocOrigem;
	}
	
}