package br.com.abril.nds.model.movimentacao;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
public class FuroProduto {

	private Long id;
	private Date data;
	public List<ProdutoEdicao> produtoEdicao;
	public Usuario usuario;
	public Lancamento lancamento;

	public FuroProduto(){

	}

}