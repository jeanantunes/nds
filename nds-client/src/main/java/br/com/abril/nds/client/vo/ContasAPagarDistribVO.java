package br.com.abril.nds.client.vo;

import br.com.abril.nds.dto.ContasAPagarDistribDTO;
import br.com.abril.nds.util.CurrencyUtil;

public class ContasAPagarDistribVO {

	private String nome;
	private String total;
	
	public ContasAPagarDistribVO()
	{}
	
	
	public ContasAPagarDistribVO(ContasAPagarDistribDTO dto) {
	
		this.nome = dto.getNome();
		this.total = CurrencyUtil.formatarValor(dto.getTotal());
	}
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
}
