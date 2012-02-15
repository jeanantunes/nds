package br.com.abril.nds.model.planejamento;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "LANCAMENTO")
public class Lancamento {

	@Id
	private Long id;
	private int reparte;
	private Date dataLancamentoPrevista;
	private Date dataLancamentoDistribuidor;
	private Date dataRecolhimentoPrervista;
	private Date dataRecolhimentoDistribuidor;
	private Date dataExpedicao;
	@ManyToOne
	private ProdutoEdicao produtoEdicao;
	@Enumerated(EnumType.STRING)
	private TipoLancamento tipoLancamento;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getReparte() {
		return reparte;
	}
	
	public void setReparte(int reparte) {
		this.reparte = reparte;
	}
	
	public Date getDataLancamentoPrevista() {
		return dataLancamentoPrevista;
	}
	
	public void setDataLancamentoPrevista(Date dataLancamentoPrevista) {
		this.dataLancamentoPrevista = dataLancamentoPrevista;
	}
	
	public Date getDataLancamentoDistribuidor() {
		return dataLancamentoDistribuidor;
	}
	
	public void setDataLancamentoDistribuidor(Date dataLancamentoDistribuidor) {
		this.dataLancamentoDistribuidor = dataLancamentoDistribuidor;
	}
	
	public Date getDataRecolhimentoPrervista() {
		return dataRecolhimentoPrervista;
	}
	
	public void setDataRecolhimentoPrervista(Date dataRecolhimentoPrervista) {
		this.dataRecolhimentoPrervista = dataRecolhimentoPrervista;
	}
	
	public Date getDataRecolhimentoDistribuidor() {
		return dataRecolhimentoDistribuidor;
	}
	
	public void setDataRecolhimentoDistribuidor(Date dataRecolhimentoDistribuidor) {
		this.dataRecolhimentoDistribuidor = dataRecolhimentoDistribuidor;
	}
	
	public Date getDataExpedicao() {
		return dataExpedicao;
	}
	
	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}
	
	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

}