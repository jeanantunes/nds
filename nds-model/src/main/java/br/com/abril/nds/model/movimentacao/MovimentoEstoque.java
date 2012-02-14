package br.com.abril.nds.model.movimentacao;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public class MovimentoEstoque {

	private Long id;
	private Date dataInclusao;
	private BigDecimal qtde;
	public TipoMovimento tipoMovimento;
	public Usuario usuario;
	public Diferenca diferenca;
	public ProdutoEdicao produtoEdicao;
	public ItemRecebimentoFisico itemRecebimentoFisico;

	public MovimentoEstoque(){

	}

}