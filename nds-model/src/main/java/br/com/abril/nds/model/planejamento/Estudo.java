package br.com.abril.nds.model.planejamento;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
public class Estudo {

	private Long id;
	private double qtdeReparte;
	private Date data;
	public Lancamento lancamento;
	public ProdutoEdicao produtoEdicao;

	public Estudo(){

	}

}