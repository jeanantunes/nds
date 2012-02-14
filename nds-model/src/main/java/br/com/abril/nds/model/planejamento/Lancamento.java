package br.com.abril.nds.model.planejamento;
import java.util.Date;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
public class Lancamento {

	private Long id;
	private int reparte;
	/**
	 * Data do lan�amento em banca
	 */
	private Date dataLancamentoPrevista;
	/**
	 * Vira sugerida e podera ser editada
	 */
	private Date dataLancamentoDistribuidor;
	private Date dataRecolhimentoPrervista;
	private Date dataRecolhimentoDistribuidor;
	private Date dataExpedicao;
	public ProdutoEdicao produtoEdicao;
	public TipoLancamento tipoLancamento;

	public Lancamento(){

	}

}