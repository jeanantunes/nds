package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroCotaInadimplenteDTO implements Serializable{

	private static final long serialVersionUID = 4974398934934768542L;
	
	@Export(label = "Cota")
	private Integer numCota;
	
	@Export(label = "Nome")
	private String nome;
	
	@Export(label = "Status")
	private String status;
	
	@Export(label = "Consignado até Data")
	private String consignado;
	
	@Export(label = "Data Vencimento")
	private String dataVencimento;
	
	@Export(label = "Data Pagamento")
	private String dataPagamento;
	
	@Export(label = "Situação da Dívida")
	private String situacao;
	
	@Export(label = "Dívida Acumulada")
	private String dividaAcumulada;
	
	@Export(label = "Dias em Atraso")
	private String diasAtraso;

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
	
	public Integer getNumCota() {
		return numCota;
	}

	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConsignado() {
		return consignado;
	}

	public void setConsignado(String consignado) {
		this.consignado = consignado;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getDividaAcumulada() {
		return dividaAcumulada;
	}

	public void setDividaAcumulada(String dividaAcumulada) {
		this.dividaAcumulada = dividaAcumulada;
	}

	public String getDiasAtraso() {
		return diasAtraso;
	}

	public void setDiasAtraso(String diasAtraso) {
		this.diasAtraso = diasAtraso;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
