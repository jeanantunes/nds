package br.com.abril.nds.model.estoque;
import java.util.Date;

import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public class RecebimentoFisico {

	private Long id;
	private Date data;
	private Date dataConfirmacao;
	public NotaFiscal notaFiscal;
	public Usuario usuario;
	public StatusConfirmacao statusConfirmacao;

	public RecebimentoFisico(){

	}

}