package br.com.abril.nds.model.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.AbstractMovimentoFinanceiro;

@Entity
@Table(name = "MOVIMENTO_FINANCEIRO_COTA")
public class MovimentoFinanceiroCota extends AbstractMovimentoFinanceiro {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@OneToMany
	@JoinTable(name = "MVTO_FINANCEIRO_ESTOQUE_COTA", joinColumns = {@JoinColumn(name = "MVTO_FINANCEIRO_COTA_ID")}, 
	inverseJoinColumns = {@JoinColumn(name = "MVTO_ESTOQUE_COTA_ID")})
	private List<MovimentoEstoqueCota> movimentos = new ArrayList<MovimentoEstoqueCota>();
	
	@Column(name = "OBSERVACAO")
	private String observacao;
	
	/**
	 * Indica se o lançamento foi gerado manualmente ou pelo
	 * sistema
	 */
	@Column(name = "LANCAMENTO_MANUAL", nullable = false)
	private boolean lancamentoManual;
	
	@OneToMany(mappedBy = "movimentoFinanceiroCota", cascade=CascadeType.REMOVE)
	private List<HistoricoMovimentoFinanceiroCota> historicos = new ArrayList<HistoricoMovimentoFinanceiroCota>();
	
	@OneToOne(mappedBy="movimentoFinanceiroCota")
	private ParcelaNegociacao parcelaNegociacao;
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public List<MovimentoEstoqueCota> getMovimentos() {
		return movimentos;
	}
	
	public void setMovimentos(List<MovimentoEstoqueCota> movimentos) {
		this.movimentos = movimentos;
	}
	
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public boolean isLancamentoManual() {
		return lancamentoManual;
	}
	
	public void setLancamentoManual(boolean lancamentoManual) {
		this.lancamentoManual = lancamentoManual;
	}
	
	public List<HistoricoMovimentoFinanceiroCota> getHistoricos() {
		return historicos;
	}
	
	public void setHistoricos(List<HistoricoMovimentoFinanceiroCota> historicos) {
		this.historicos = historicos;
	}

	/**
	 * @return the parcelaNegociacao
	 */
	public ParcelaNegociacao getParcelaNegociacao() {
		return parcelaNegociacao;
	}

	/**
	 * @param parcelaNegociacao the parcelaNegociacao to set
	 */
	public void setParcelaNegociacao(ParcelaNegociacao parcelaNegociacao) {
		this.parcelaNegociacao = parcelaNegociacao;
	}
	
	
}
