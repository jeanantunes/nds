package br.com.abril.nds.dto;

public class ColunaRelatorioInformeEncalhe {

	public ColunaRelatorioInformeEncalhe(String nome, int largura, String param){
		
		this.nome = nome;
		this.largura = largura;
		this.param = param;
	}
	
	public ColunaRelatorioInformeEncalhe(){}
	
	private String nome;
	
	private int largura;
	
	private String param;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getLargura() {
		return largura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
}