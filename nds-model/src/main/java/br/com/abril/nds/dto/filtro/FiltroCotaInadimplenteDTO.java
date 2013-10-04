package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroCotaInadimplenteDTO implements Serializable{

	private static final long serialVersionUID = 4974398934934768542L;
	
	@Export(label = "Período de")
	private String periodoDe;
	
	@Export(label = "Período até")
	private String periodoAte;
	
	@Export(label = "Cota")
	private Integer numCota;
	
	@Export(label = "Nome")
	private String nomeCota;
	
	@Export(label = "Status")
	private String statusCota;
	
	@Export(label = "Dividas Abertas")
	private Boolean situacaoEmAberto; 
	
	@Export(label = "Dividas Pagas")
	private Boolean situacaoPaga;
	
	@Export(label = "Dívidas Negociadas")
	private Boolean situacaoNegociada;
	
	private Date dataOperacaoDistribuidor;
	
	
	private PaginacaoVO paginacao;
	
	private	ColunaOrdenacao colunaOrdenacao;

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public ColunaOrdenacao getColunaOrdenacao() {
		return colunaOrdenacao;
	}

	public void setColunaOrdenacao(ColunaOrdenacao colunaOrdenacao) {
		this.colunaOrdenacao = colunaOrdenacao;
	}

	public Date getPeriodoDe() {
		if(periodoDe == null) return null;
		return DateUtil.parseDataPTBR(periodoDe);
	}

	public void setPeriodoDe(String periodoDe) {
		if(periodoDe != null && periodoDe.trim().isEmpty()) 
			return;
		this.periodoDe = periodoDe;
	}

	public Date getPeriodoAte() {
		if(periodoAte == null) return null;
		return DateUtil.parseDataPTBR(periodoAte);
	}

	public void setPeriodoAte(String periodoAte) {
		if(periodoAte != null && periodoAte.trim().isEmpty()) 
			return;
		this.periodoAte = periodoAte;
	}

	public Integer getNumCota() {
		return numCota;
	}

	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		if(nomeCota != null && nomeCota.trim().isEmpty()) 
			return;
		this.nomeCota = nomeCota;
	}

	public String getStatusCota() {
		return statusCota;
	}

	public void setStatusCota(String statusCota) {
		if(statusCota != null && statusCota.trim().isEmpty()) 
			return;
		this.statusCota = statusCota;
	}

	public Boolean getSituacaoEmAberto() {
		return situacaoEmAberto;
	}

	public void setSituacaoEmAberto(Boolean situacaoEmAberto) {
		this.situacaoEmAberto = situacaoEmAberto;
	}

	public Boolean getSituacaoPaga() {
		return situacaoPaga;
	}

	public void setSituacaoPaga(Boolean situacaoPaga) {
		this.situacaoPaga = situacaoPaga;
	}

	public Boolean getSituacaoNegociada() {
		return situacaoNegociada;
	}

	public void setSituacaoNegociada(Boolean situacaoNegociada) {
		this.situacaoNegociada = situacaoNegociada;
	}

	/**
	 * Enum para ordenação das colunas do filtro.
	 * 
	 * @author Discover Technology
	 *
	 */
	public enum ColunaOrdenacao{
		
		NUM_COTA("numCota"),
		NOME("nome"),
		STATUS("status"),
		CONSIGNADO("consignado"),
		DATA_VENCIMENTO("dataVencimento"),
		VALOR("valor"),
		DATA_PAGAMENTO("dataPagamento"),
		SITUACAO("situacao"),
		DIVIDA_ACUMULADA("dividaAcumulada"),
		ATRASO("diasAtraso");
		
		private String nomeColuna;
		
		private ColunaOrdenacao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
	}

	public Date getDataOperacaoDistribuidor() {
		return dataOperacaoDistribuidor;
	}

	public void setDataOperacaoDistribuidor(Date dataOperacaoDistribuidor) {
		this.dataOperacaoDistribuidor = dataOperacaoDistribuidor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((colunaOrdenacao == null) ? 0 : colunaOrdenacao.hashCode());
		result = prime
				* result
				+ ((dataOperacaoDistribuidor == null) ? 0
						: dataOperacaoDistribuidor.hashCode());
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result + ((numCota == null) ? 0 : numCota.hashCode());
		result = prime * result
				+ ((paginacao == null) ? 0 : paginacao.hashCode());
		result = prime * result
				+ ((periodoAte == null) ? 0 : periodoAte.hashCode());
		result = prime * result
				+ ((periodoDe == null) ? 0 : periodoDe.hashCode());
		result = prime
				* result
				+ ((situacaoEmAberto == null) ? 0 : situacaoEmAberto.hashCode());
		result = prime
				* result
				+ ((situacaoNegociada == null) ? 0 : situacaoNegociada
						.hashCode());
		result = prime * result
				+ ((situacaoPaga == null) ? 0 : situacaoPaga.hashCode());
		result = prime * result
				+ ((statusCota == null) ? 0 : statusCota.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroCotaInadimplenteDTO other = (FiltroCotaInadimplenteDTO) obj;
		if (colunaOrdenacao != other.colunaOrdenacao)
			return false;
		if (dataOperacaoDistribuidor == null) {
			if (other.dataOperacaoDistribuidor != null)
				return false;
		} else if (!dataOperacaoDistribuidor
				.equals(other.dataOperacaoDistribuidor))
			return false;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (numCota == null) {
			if (other.numCota != null)
				return false;
		} else if (!numCota.equals(other.numCota))
			return false;
		if (paginacao == null) {
			if (other.paginacao != null)
				return false;
		} else if (!paginacao.equals(other.paginacao))
			return false;
		if (periodoAte == null) {
			if (other.periodoAte != null)
				return false;
		} else if (!periodoAte.equals(other.periodoAte))
			return false;
		if (periodoDe == null) {
			if (other.periodoDe != null)
				return false;
		} else if (!periodoDe.equals(other.periodoDe))
			return false;
		if (situacaoEmAberto == null) {
			if (other.situacaoEmAberto != null)
				return false;
		} else if (!situacaoEmAberto.equals(other.situacaoEmAberto))
			return false;
		if (situacaoNegociada == null) {
			if (other.situacaoNegociada != null)
				return false;
		} else if (!situacaoNegociada.equals(other.situacaoNegociada))
			return false;
		if (situacaoPaga == null) {
			if (other.situacaoPaga != null)
				return false;
		} else if (!situacaoPaga.equals(other.situacaoPaga))
			return false;
		if (statusCota == null) {
			if (other.statusCota != null)
				return false;
		} else if (!statusCota.equals(other.statusCota))
			return false;
		return true;
	}
	
	
	
	
	
	
}
