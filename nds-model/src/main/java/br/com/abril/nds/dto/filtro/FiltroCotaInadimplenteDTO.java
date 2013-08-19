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
	
	
}
