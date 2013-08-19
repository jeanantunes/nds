package br.com.abril.nds.dto;

/**
 * 
 * @author InfoA2 - Samuel Mendes
 *
 * Classe utilizada para montar o grid <b>Edições do Produto</b> que fica no menu Histórico de Vendas.
 * Como o existia um DTO com a maioria das informações, reutilizei os campos com herança.
 *
 */
public class EdicaoProdutoDTO extends ProdutoEdicaoDTO {

	private static final long serialVersionUID = 6322570675452464938L;
	
	private Integer periodo;
	
	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}
	
}
