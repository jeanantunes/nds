package br.com.abril.nds.strategy.importacao.input;

import java.io.Serializable;
import java.util.StringTokenizer;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ 
	"codigoAgente"
	, "codigoContexto"
	, "numeroCota" 
	, "codigoProduto"
	, "indiceDesvio"
	, "qtdDanificadoAceito"
	, "qtdDanificadoRegeitado"
	, "qtdEncalhe"
	, "qtdNivelamento"
	, "qtdRechacado"
	, "qtdReparteBaseCalculo"
	, "qtdReparte"
	, "qtdSuplementacao"
	, "qtdVendaBaseCalculo"
	, "qtdVendaEstimada"
	, "qtdVenda"
	, "qtdVendaTratada"
	, "tipoEncalhe"
	, "tipoHistorico"
	, "tipoOrigiemHistorico"
	, "tipoOrigiemReparte"
	, "tipoVenda"
	, "valorVenda"
	})
public class HistoricoVendaInput implements Serializable{
		
	private static final long serialVersionUID = 1L;

	private Integer codigoAgente;
	private Integer codigoContexto;
	private Integer numeroCota; 
	private String codigoProduto;
	private double indiceDesvio;
	private Integer qtdDanificadoAceito;
	private Integer qtdDanificadoRegeitado;
	private Integer qtdEncalhe;
	private Integer qtdNivelamento;
	private Integer qtdRechacado;
	private Integer qtdReparteBaseCalculo;
	private Integer qtdReparte;
	private Integer qtdSuplementacao;
	private Integer qtdVendaBaseCalculo;
	private Integer qtdVendaEstimada;
	private Integer qtdVenda;
	private Integer qtdVendaTratada;
	private String tipoEncalhe;
	private String tipoHistorico;
	private String tipoOrigiemHistorico;
	private String tipoOrigiemReparte;
	private String tipoVenda;
	private double valorVenda;
	private Long idUsuario;
	
	
	/**
	 * @return the idUsuario
	 */
	public Long getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto.substring(0,8);
	}

	/**
	 * @return the numeroEdicao
	 */
	public Integer getNumeroEdicao() {
		return Integer.valueOf( codigoProduto.substring(8) );
	}

	/**
	 * @return the codigoAgente
	 */
	public Integer getCodigoAgente() {
		return codigoAgente;
	}

	/**
	 * @param codigoAgente the codigoAgente to set
	 */
	public void setCodigoAgente(Integer codigoAgente) {
		this.codigoAgente = codigoAgente;
	}

	/**
	 * @return the codigoContexto
	 */
	public Integer getCodigoContexto() {
		return codigoContexto;
	}

	/**
	 * @param codigoContexto the codigoContexto to set
	 */
	public void setCodigoContexto(Integer codigoContexto) {
		this.codigoContexto = codigoContexto;
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the indiceDesvio
	 */
	public double getIndiceDesvio() {
		return indiceDesvio;
	}

	/**
	 * @param indiceDesvio the indiceDesvio to set
	 */
	public void setIndiceDesvio(double indiceDesvio) {
		this.indiceDesvio = indiceDesvio;
	}

	/**
	 * @return the qtdDanificadoAceito
	 */
	public Integer getQtdDanificadoAceito() {
		return qtdDanificadoAceito;
	}

	/**
	 * @param qtdDanificadoAceito the qtdDanificadoAceito to set
	 */
	public void setQtdDanificadoAceito(Integer qtdDanificadoAceito) {
		this.qtdDanificadoAceito = qtdDanificadoAceito;
	}

	/**
	 * @return the qtdDanificadoRegeitado
	 */
	public Integer getQtdDanificadoRegeitado() {
		return qtdDanificadoRegeitado;
	}

	/**
	 * @param qtdDanificadoRegeitado the qtdDanificadoRegeitado to set
	 */
	public void setQtdDanificadoRegeitado(Integer qtdDanificadoRegeitado) {
		this.qtdDanificadoRegeitado = qtdDanificadoRegeitado;
	}

	/**
	 * @return the qtdEncalhe
	 */
	public Integer getQtdEncalhe() {
		return qtdEncalhe;
	}

	/**
	 * @param qtdEncalhe the qtdEncalhe to set
	 */
	public void setQtdEncalhe(Integer qtdEncalhe) {
		this.qtdEncalhe = qtdEncalhe;
	}

	/**
	 * @return the qtdNivelamento
	 */
	public Integer getQtdNivelamento() {
		return qtdNivelamento;
	}

	/**
	 * @param qtdNivelamento the qtdNivelamento to set
	 */
	public void setQtdNivelamento(Integer qtdNivelamento) {
		this.qtdNivelamento = qtdNivelamento;
	}

	/**
	 * @return the qtdRechacado
	 */
	public Integer getQtdRechacado() {
		return qtdRechacado;
	}

	/**
	 * @param qtdRechacado the qtdRechacado to set
	 */
	public void setQtdRechacado(Integer qtdRechacado) {
		this.qtdRechacado = qtdRechacado;
	}

	/**
	 * @return the qtdReparteBaseCalculo
	 */
	public Integer getQtdReparteBaseCalculo() {
		return qtdReparteBaseCalculo;
	}

	/**
	 * @param qtdReparteBaseCalculo the qtdReparteBaseCalculo to set
	 */
	public void setQtdReparteBaseCalculo(Integer qtdReparteBaseCalculo) {
		this.qtdReparteBaseCalculo = qtdReparteBaseCalculo;
	}

	/**
	 * @return the qtdReparte
	 */
	public Integer getQtdReparte() {
		return qtdReparte;
	}

	/**
	 * @param qtdReparte the qtdReparte to set
	 */
	public void setQtdReparte(Integer qtdReparte) {
		this.qtdReparte = qtdReparte;
	}

	/**
	 * @return the qtdSuplementacao
	 */
	public Integer getQtdSuplementacao() {
		return qtdSuplementacao;
	}

	/**
	 * @param qtdSuplementacao the qtdSuplementacao to set
	 */
	public void setQtdSuplementacao(Integer qtdSuplementacao) {
		this.qtdSuplementacao = qtdSuplementacao;
	}

	/**
	 * @return the qtdVendaBaseCalculo
	 */
	public Integer getQtdVendaBaseCalculo() {
		return qtdVendaBaseCalculo;
	}

	/**
	 * @param qtdVendaBaseCalculo the qtdVendaBaseCalculo to set
	 */
	public void setQtdVendaBaseCalculo(Integer qtdVendaBaseCalculo) {
		this.qtdVendaBaseCalculo = qtdVendaBaseCalculo;
	}

	/**
	 * @return the qtdVendaEstimada
	 */
	public Integer getQtdVendaEstimada() {
		return qtdVendaEstimada;
	}

	/**
	 * @param qtdVendaEstimada the qtdVendaEstimada to set
	 */
	public void setQtdVendaEstimada(Integer qtdVendaEstimada) {
		this.qtdVendaEstimada = qtdVendaEstimada;
	}

	/**
	 * @return the qtdVenda
	 */
	public Integer getQtdVenda() {
		return qtdVenda;
	}

	/**
	 * @param qtdVenda the qtdVenda to set
	 */
	public void setQtdVenda(Integer qtdVenda) {
		this.qtdVenda = qtdVenda;
	}

	/**
	 * @return the qtdVendaTratada
	 */
	public Integer getQtdVendaTratada() {
		return qtdVendaTratada;
	}

	/**
	 * @param qtdVendaTratada the qtdVendaTratada to set
	 */
	public void setQtdVendaTratada(Integer qtdVendaTratada) {
		this.qtdVendaTratada = qtdVendaTratada;
	}

	/**
	 * @return the tipoEncalhe
	 */
	public String getTipoEncalhe() {
		return tipoEncalhe;
	}

	/**
	 * @param tipoEncalhe the tipoEncalhe to set
	 */
	public void setTipoEncalhe(String tipoEncalhe) {
		this.tipoEncalhe = tipoEncalhe;
	}

	/**
	 * @return the tipoHistorico
	 */
	public String getTipoHistorico() {
		return tipoHistorico;
	}

	/**
	 * @param tipoHistorico the tipoHistorico to set
	 */
	public void setTipoHistorico(String tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}

	/**
	 * @return the tipoOrigiemHistorico
	 */
	public String getTipoOrigiemHistorico() {
		return tipoOrigiemHistorico;
	}

	/**
	 * @param tipoOrigiemHistorico the tipoOrigiemHistorico to set
	 */
	public void setTipoOrigiemHistorico(String tipoOrigiemHistorico) {
		this.tipoOrigiemHistorico = tipoOrigiemHistorico;
	}

	/**
	 * @return the tipoOrigiemReparte
	 */
	public String getTipoOrigiemReparte() {
		return tipoOrigiemReparte;
	}

	/**
	 * @param tipoOrigiemReparte the tipoOrigiemReparte to set
	 */
	public void setTipoOrigiemReparte(String tipoOrigiemReparte) {
		this.tipoOrigiemReparte = tipoOrigiemReparte;
	}

	/**
	 * @return the tipoVenda
	 */
	public String getTipoVenda() {
		return tipoVenda;
	}

	/**
	 * @param tipoVenda the tipoVenda to set
	 */
	public void setTipoVenda(String tipoVenda) {
		this.tipoVenda = tipoVenda;
	}

	/**
	 * @return the valorVenda
	 */
	public double getValorVenda() {
		return valorVenda;
	}

	/**
	 * @param valorVenda the valorVenda to set
	 */
	public void setValorVenda(double valorVenda) {
		this.valorVenda = valorVenda;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
}
