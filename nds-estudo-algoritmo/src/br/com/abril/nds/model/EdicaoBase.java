package br.com.abril.nds.model;

public class EdicaoBase extends Produto {

	private Integer reparte;
	private Integer venda;
	
	public Integer getReparte() {
		return reparte;
	}
	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}
	public Integer getVenda() {
		return venda;
	}
	public void setVenda(Integer venda) {
		this.venda = venda;
	}
}
