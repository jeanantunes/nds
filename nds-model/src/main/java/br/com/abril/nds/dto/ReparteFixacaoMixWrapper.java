package br.com.abril.nds.dto;

import java.util.Date;

import br.com.abril.nds.model.estudo.ClassificacaoCota;

public class ReparteFixacaoMixWrapper {

	private Long id;
	
	private String legenda;
	
	private Integer numeroCota;
	
	private Date dataAtualizacao;
	
	private Integer reparte;

	public ReparteFixacaoMixWrapper(Long id, String legenda, Integer numeroCota, Date dataAtualizacao, Integer reparte) {
		this.id=id;
		this.legenda=legenda;
		this.numeroCota=numeroCota;
		this.dataAtualizacao=dataAtualizacao;
		this.reparte=reparte;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLegenda() {
		return legenda;
	}

	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Integer getReparte() {
		return reparte;
	}

	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}
	
	public boolean isFixacao() {
		return ClassificacaoCota.ReparteFixado.getCodigo().equals(this.legenda);
	}
	
	public boolean isMix() {
		return ClassificacaoCota.CotaMix.getCodigo().equals(this.legenda);
	}
}
