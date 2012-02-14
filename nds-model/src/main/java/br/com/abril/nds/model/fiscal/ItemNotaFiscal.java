package br.com.abril.nds.model.fiscal;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public class ItemNotaFiscal {

	private Long id;
	private BigDecimal quantidade;
	private List<NotaFiscal> notaFiscal;
	private ProdutoEdicao produtoEdicao;
	public Usuario usuario;
	public OrigemNota origemNota;

	public ItemNotaFiscal(){

	}

}