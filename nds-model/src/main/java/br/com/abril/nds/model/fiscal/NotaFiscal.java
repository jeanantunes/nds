package br.com.abril.nds.model.fiscal;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.model.cadastro.Juridica;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public abstract class NotaFiscal {

	private Long id;
	private Date dataEmissao;
	private Date dataExpedicao;
	private String numero;
	private String serie;
	private String chaveAcesso;
	private StatusNotaFiscal statusNotaFiscal;
	public Usuario usuario;
	public CFOP cFOP;
	public OrigemNota origemNota;
	public Juridica juridica;
	public TipoNotaFiscal tipoNotaFiscal;

	public NotaFiscal(){

	}

}