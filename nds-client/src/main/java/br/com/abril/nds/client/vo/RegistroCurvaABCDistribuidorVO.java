package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

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

	@Export(label = "Cota", exhibitionOrder = 4)
	private String nomeCota;

	private Integer quantidadePdvs;

	@Export(label = "Municipio", exhibitionOrder = 5)
	private String municipio;

	private BigInteger vendaExemplares;

	private BigDecimal faturamentoCapa;

	@Export(label = "Venda de Exemplares", exhibitionOrder = 7)
	private String vendaExemplaresFormatado;
	
	@Export(label = "Faturamento da Capa", exhibitionOrder = 8)
	private String faturamentoCapaFormatado;
	
	@Export(label = "RK.Produto", exhibitionOrder = 1)
	private Long rkProduto;
	
	@Export(label = "RK. Cota", exhibitionOrder = 2)
	private Long rkCota;
	
	private Long idProduto;
	
	private Long idCota;
	
	@Export(label = "Número", exhibitionOrder = 3)
	public String getNumeroCotaString() {
		return this.getNumeroCota().toString();
	}

	@Export(label = "Quantidade de Pdvs", exhibitionOrder = 6)
	public String getQuantidadePdvsString() {
		return this.getQuantidadePdvs().toString();
	}

	@Export(label = "Participação", exhibitionOrder = 9)
	public String getParticipacaoString() {
		return getParticipacaoFormatado();
	}

	@Export(label = "Participação Acumulada", exhibitionOrder = 10)
	public String getParticipacaoAcumuladaString() {
		return getParticipacaoAcumuladaFormatado();
	}
	
	public RegistroCurvaABCDistribuidorVO() {
	}

	public RegistroCurvaABCDistribuidorVO(Integer numeroCota, String nomeCota,
			Integer quantidadePdvs, String municipio, BigInteger vendaExemplares, BigDecimal faturamento,Long idProduto,Long idCota) {
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.quantidadePdvs = quantidadePdvs;
		this.municipio = municipio;
		this.vendaExemplares = vendaExemplares;
		this.faturamentoCapa = faturamento;
		this.idProduto = idProduto;
		this.idCota = idCota;
		this.formatarCampos();
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public Long getRkProduto() {
		return rkProduto;
	}

	public void setRkProduto(Long rkProduto) {
		this.rkProduto = rkProduto;
	}

	public Long getRkCota() {
		return rkCota;
	}

	public void setRkCota(Long rkCota) {
		this.rkCota = rkCota;
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

	public BigInteger getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigInteger vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
	}

	public BigDecimal getFaturamentoCapa() {
		return faturamentoCapa;
	}

	public void setFaturamentoCapa(BigDecimal faturamentoCapa) {
		this.faturamentoCapa = faturamentoCapa;
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
