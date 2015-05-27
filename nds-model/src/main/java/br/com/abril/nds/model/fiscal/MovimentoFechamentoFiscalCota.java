package br.com.abril.nds.model.fiscal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;

@Entity
@Table(name = "MOVIMENTO_FECHAMENTO_FISCAL_COTA")
@DiscriminatorValue(value="COTA")
public class MovimentoFechamentoFiscalCota extends MovimentoFechamentoFiscal {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "NOTA_FISCAL_VENDA_EMITIDA")
	private boolean notaFiscalVendaEmitida;
	
	@Column(name = "DESOBRIGA_NOTA_FISCAL_VENDA")
	private boolean desobrigaNotaFiscalVenda;
	
	@OneToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@OneToOne
	@JoinColumn(name = "CHAMADA_ENCALHE_COTA_ID")
	private ChamadaEncalheCota chamadaEncalheCota;
	
	public boolean isNotaFiscalVendaEmitida() {
		return notaFiscalVendaEmitida;
	}

	public void setNotaFiscalVendaEmitida(boolean notaFiscalVendaEmitida) {
		this.notaFiscalVendaEmitida = notaFiscalVendaEmitida;
	}
	
	public boolean isDesobrigaNotaFiscalVenda() {
		return desobrigaNotaFiscalVenda;
	}

	public void setDesobrigaNotaFiscalVenda(boolean desobrigaNotaFiscalVenda) {
		this.desobrigaNotaFiscalVenda = desobrigaNotaFiscalVenda;
	}
	
	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public ChamadaEncalheCota getChamadaEncalheCota() {
		return chamadaEncalheCota;
	}

	public void setChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota) {
		this.chamadaEncalheCota = chamadaEncalheCota;
	}

}