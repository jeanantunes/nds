package br.com.abril.nds.model.estoque;
import java.math.BigDecimal;

import br.com.abril.nds.model.fiscal.ItemNotaFiscal;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public class ItemRecebimentoFisico {

	private Long id;
	private BigDecimal qtdeFisico;
	public RecebimentoFisico recebimentoFisico;
	public ItemNotaFiscal itemNotaFiscal;

	public ItemRecebimentoFisico(){

	}

}