package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.dto.RegistroCurvaABCDTO;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de curva ABC do distribuidor.
 * @author InfoA2
 */
@Exportable
public class RegistroCurvaABCDistribuidorVO extends RegistroCurvaABCDTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3849170873913801404L;

	private Integer numeroCota;

	@Export(label = "Cota", exhibitionOrder = 2)
	private String nomeCota;

	private Integer quantidadePdvs;

	@Export(label = "Municipio", exhibitionOrder = 4)
	private String municipio;

	private BigDecimal vendaExemplares;

	private BigDecimal faturamentoCapa;

	@Export(label = "Venda de Exemplares", exhibitionOrder = 5)
	private String vendaExemplaresFormatado;
	
	@Export(label = "Faturamento da Capa", exhibitionOrder = 6)
	private String faturamentoCapaFormatado;
	
	public RegistroCurvaABCDistribuidorVO() {
	}

	public RegistroCurvaABCDistribuidorVO(Integer numeroCota, String nomeCota,
			Integer quantidadePdvs, String municipio, BigDecimal vendaExemplares, BigDecimal faturamento) {
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.quantidadePdvs = quantidadePdvs;
		this.municipio = municipio;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamento;
		this.formatarCampos();
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public Integer getQuantidadePdvs() {
		return quantidadePdvs;
	}

	public void setQuantidadePdvs(Integer quantidadePdvs) {
		this.quantidadePdvs = quantidadePdvs;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public BigDecimal getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigDecimal vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
	}

	public BigDecimal getFaturamentoCapa() {
		return faturamentoCapa;
	}

	public void setFaturamentoCapa(BigDecimal faturamentoCapa) {
		this.faturamentoCapa = faturamentoCapa;
	}

	@Export(label = "Número", exhibitionOrder = 1)
	public String getNumeroCotaString() {
		return this.getNumeroCota().toString();
	}

	@Export(label = "Quantidade de Pdvs", exhibitionOrder = 3)
	public String getQuantidadePdvsString() {
		return this.getQuantidadePdvs().toString();
	}

	@Export(label = "Participação", exhibitionOrder = 7)
	public String getParticipacaoString() {
		return getParticipacaoFormatado();
	}

	@Export(label = "Participação Acumulada", exhibitionOrder = 8)
	public String getParticipacaoAcumuladaString() {
		return getParticipacaoAcumuladaFormatado();
	}

	public String getVendaExemplaresFormatado() {
		return vendaExemplaresFormatado;
	}

	public void setVendaExemplaresFormatado(String vendaExemplaresFormatado) {
		this.vendaExemplaresFormatado = vendaExemplaresFormatado;
	}

	public String getFaturamentoCapaFormatado() {
		return faturamentoCapaFormatado;
	}

	public void setFaturamentoCapaFormatado(String faturamentoCapaFormatado) {
		this.faturamentoCapaFormatado = faturamentoCapaFormatado;
	}

	private void formatarCampos() {
		this.vendaExemplaresFormatado = CurrencyUtil.formatarValor(vendaExemplares);
		this.faturamentoCapaFormatado = CurrencyUtil.formatarValor(faturamentoCapa);
	}

}
