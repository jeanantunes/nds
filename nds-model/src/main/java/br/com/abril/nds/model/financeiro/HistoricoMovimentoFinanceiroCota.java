package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.HistoricoEdicao;
import br.com.abril.nds.model.cadastro.Cota;

@Entity
@Table(name = "HISTORICO_MOVTO_FINANCEIRO_COTA")
@SequenceGenerator(name = "HIST_MOVTO_FIN_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoMovimentoFinanceiroCota extends HistoricoEdicao {
	
	@Id
	@GeneratedValue(generator = "HIST_MOVTO_FIN_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_MOVTO_ID")
	private TipoMovimentoFinanceiro tipoMovimento;
	
	@Column(name = "VALOR", nullable = false)
	private BigDecimal valor;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Date data;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
    private Cota cota;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "MOVTO_FINANCEIRO_COTA_ID")
	private MovimentoFinanceiroCota movimentoFinanceiroCota;
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public TipoMovimentoFinanceiro getTipoMovimento() {
		return tipoMovimento;
	}
	
	public void setTipoMovimento(TipoMovimentoFinanceiro tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Cota getCota() {
		return cota;
	}
	
	public void setCota(Cota cota) {
		this.cota = cota;
	}
	
	public MovimentoFinanceiroCota getMovimentoFinanceiroCota() {
		return movimentoFinanceiroCota;
	}
	
	public void setMovimentoFinanceiroCota(
			MovimentoFinanceiroCota movimentoFinanceiroCota) {
		this.movimentoFinanceiroCota = movimentoFinanceiroCota;
	}

}
