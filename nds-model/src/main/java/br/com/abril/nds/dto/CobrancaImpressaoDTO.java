package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.util.CurrencyUtil;

public class CobrancaImpressaoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer numeroCota;
	private String nomeCota;
	private String box;
	private String rota;
	private String roteiro;
	private BigDecimal valor;
	private Date emissao;
	private Date vencimento;
	private String nomeBanco;
	private String agencia;
	private String conta;
	private String nomeFavorecido;
	private TipoCobranca tipoCobranca;
    private String valorExtenso;
	
	public String getValorExtenso() {
		
		if(this.valor!= null){
			return CurrencyUtil.valorExtenso(this.valor);
		}
		return valorExtenso;
	}

	public void setValorExtenso(String valorExtenso) {
		this.valorExtenso = valorExtenso;
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
	
	public String getBox() {
		return box;
	}
	
	public void setBox(String box) {
		this.box = box;
	}
	
	public String getRota() {
		return rota;
	}
	
	public void setRota(String rota) {
		this.rota = rota;
	}
	
	public String getRoteiro() {
		return roteiro;
	}
	
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public Date getEmissao() {
		return emissao;
	}

	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}

	public Date getVencimento() {
		return vencimento;
	}
	
	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}
	
	public String getNomeBanco() {
		return nomeBanco;
	}
	
	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}
	
	public String getAgencia() {
		return agencia;
	}
	
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	
	public String getConta() {
		return conta;
	}
	
	public void setConta(String conta) {
		this.conta = conta;
	}
	
	public String getNomeFavorecido() {
		return nomeFavorecido;
	}
	
	public void setNomeFavorecido(String nomeFavorecido) {
		this.nomeFavorecido = nomeFavorecido;
	}
	
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
}