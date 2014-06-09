package br.com.abril.nds.client.vo;

import br.com.abril.nds.model.EficienciaEnum;

public class DistribuidorPercentualExcedenteVO {

	private Long id;
	private EficienciaEnum eficiencia;
	private Integer venda;
	private Integer pdv;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EficienciaEnum getEficiencia() {
		return eficiencia;
	}

	public void setEficiencia(EficienciaEnum eficiencia) {
		this.eficiencia = eficiencia;
	}

	public Integer getVenda() {
		return venda;
	}

	public void setVenda(Integer venda) {
		this.venda = venda;
	}

	public Integer getPdv() {
		return pdv;
	}

	public void setPdv(Integer pdv) {
		this.pdv = pdv;
	}

}
