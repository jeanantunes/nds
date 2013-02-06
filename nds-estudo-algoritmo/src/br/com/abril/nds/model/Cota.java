package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.List;

public class Cota {

	private Integer id;
	private String nome;
	private ClassificacaoCota classificacao;
	private BigDecimal reparteCalculado;
	private boolean vendaMediaMaisN;
	private BigDecimal ajusteReparte; // parametro VendaMedia + n na tela de Ajuste de Reparte
	private BigDecimal vendaMediaFinal;
	private BigDecimal reparteMinimo; // parametro ReparteMinimo na tela de bonificações ou na tela Mix de Produto
	private BigDecimal vendaMediaNominalCota; // VendaMediaNominalCota = SomatoriaVendasCota / QtdeEdicoesRecebidasCota;
	private List<EdicaoBase> edicoesBase;

	public void calculate() {
		
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<EdicaoBase> getEdicoesBase() {
		return edicoesBase;
	}
	public void setEdicoesBase(List<EdicaoBase> edicoesBase) {
		this.edicoesBase = edicoesBase;
	}
	public ClassificacaoCota getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(ClassificacaoCota classificacao) {
		this.classificacao = classificacao;
	}
	public BigDecimal getReparteCalculado() {
		return reparteCalculado;
	}
	public void setReparteCalculado(BigDecimal reparteCalculado) {
		this.reparteCalculado = reparteCalculado;
	}
	public boolean isVendaMediaMaisN() {
		return vendaMediaMaisN;
	}
	public void setVendaMediaMaisN(boolean vendaMediaMaisN) {
		this.vendaMediaMaisN = vendaMediaMaisN;
	}
	public BigDecimal getAjusteReparte() {
		return ajusteReparte;
	}
	public void setAjusteReparte(BigDecimal ajusteReparte) {
		this.ajusteReparte = ajusteReparte;
	}
	public BigDecimal getVendaMediaFinal() {
		return vendaMediaFinal;
	}
	public void setVendaMediaFinal(BigDecimal vendaMediaFinal) {
		this.vendaMediaFinal = vendaMediaFinal;
	}
	public BigDecimal getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(BigDecimal reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
	public BigDecimal getVendaMediaNominalCota() {
		return vendaMediaNominalCota;
	}
	public void setVendaMediaNominalCota(BigDecimal vendaMediaNominalCota) {
		this.vendaMediaNominalCota = vendaMediaNominalCota;
	}
}
