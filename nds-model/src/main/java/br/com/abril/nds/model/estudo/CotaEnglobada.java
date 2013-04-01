package br.com.abril.nds.model.estudo;

import java.util.Date;

public class CotaEnglobada {

	private Long id;
	private Integer porcentualEnglobacao;
	private Date dataInclusao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getPorcentualEnglobacao() {
		return porcentualEnglobacao;
	}
	public void setPorcentualEnglobacao(Integer porcentualEnglobacao) {
		this.porcentualEnglobacao = porcentualEnglobacao;
	}
	public Date getDataInclusao() {
		return dataInclusao;
	}
	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}
	
}
