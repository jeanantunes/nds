package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

public class FiltroRelatorioTiposProdutos implements Serializable {

	private static final long serialVersionUID = 104473624178466067L;
	
	private Long tipoProduto;
	private Date dataLancamentoDe;
	private Date dataLancamentoAte;
	private Date dataRecolhimentoDe;
	private Date dataRecolhimentoAte;
	
	
	public Long getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(Long tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	public Date getDataLancamentoDe() {
		return dataLancamentoDe;
	}
	public void setDataLancamentoDe(Date dataLancamentoDe) {
		this.dataLancamentoDe = dataLancamentoDe;
	}
	public Date getDataLancamentoAte() {
		return dataLancamentoAte;
	}
	public void setDataLancamentoAte(Date dataLancamentoAte) {
		this.dataLancamentoAte = dataLancamentoAte;
	}
	public Date getDataRecolhimentoDe() {
		return dataRecolhimentoDe;
	}
	public void setDataRecolhimentoDe(Date dataRecolhimentoDe) {
		this.dataRecolhimentoDe = dataRecolhimentoDe;
	}
	public Date getDataRecolhimentoAte() {
		return dataRecolhimentoAte;
	}
	public void setDataRecolhimentoAte(Date dataRecolhimentoAte) {
		this.dataRecolhimentoAte = dataRecolhimentoAte;
	}
}
