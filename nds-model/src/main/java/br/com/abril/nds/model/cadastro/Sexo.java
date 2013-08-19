package br.com.abril.nds.model.cadastro;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public enum Sexo {
	MASCULINO(1,"Masculino"),
	FEMININO(2,"Feminino"),
	GLS(4,"Gls"),
	TODOS(5,"Todos");
	
	private Integer codigo;
	
	private String descricao;
	
	Sexo(Integer codigo, String descricao){
		
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

}