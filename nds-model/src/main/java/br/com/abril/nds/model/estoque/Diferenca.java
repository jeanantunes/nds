package br.com.abril.nds.model.estoque;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
public class Diferenca {

	private Long id;
	private BigDecimal qtde;
	public Usuario usuario;
	public ItemRecebimentoFisico itemRecebimentoFisico;
	public ProdutoEdicao produtoEdicao;
	public TipoDiferenca tipoDiferenca;

	public Diferenca(){

	}

}