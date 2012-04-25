package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class StatusDividaDTO implements Serializable {
	
	private static final long serialVersionUID = -4132748025627433307L;

	
	private Long idCota;
	
	@Export(label = "Cota", alignment=Alignment.LEFT)
	private Integer numCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT)
	private String nome;
	
	@Export(label = "Status", alignment=Alignment.LEFT)
	private String status;
		
	@Export(label = "Consifnado até Data", alignment=Alignment.CENTER)
	private String consignado;
	
	@Export(label = "Data Vencimento", alignment=Alignment.CENTER)
	private String dataVencimento;
	
	@Export(label = "Data pagamento", alignment=Alignment.CENTER)
	private String dataPagamento;
	
	@Export(label = "Situação da Divida", alignment=Alignment.LEFT)
	private String situacao;
	
	@Export(label = "Divida Acumulada", alignment=Alignment.RIGHT)
	private String dividaAcumulada;
		
	@Export(label = "Dias em Atraso", alignment=Alignment.CENTER)
	private Long diasAtraso;

	public StatusDividaDTO() {
		
	}
	
	public StatusDividaDTO(Long idCota, Integer numCota, String nome,
			String status, String consignado, String dataVencimento,
			String dataPagamento, String situacao, String dividaAcumulada,
			Long diasAtraso) {
		super();
		this.idCota = idCota;
		this.numCota = numCota;
		this.nome = nome;
		this.status = status;
		this.consignado = consignado;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
		this.situacao = situacao;
		this.dividaAcumulada = dividaAcumulada;
		this.diasAtraso = diasAtraso;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
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

	public Long getDiasAtraso() {
		return diasAtraso;
	}

	public void setDiasAtraso(Long diasAtraso) {
		this.diasAtraso = diasAtraso;
	}
}
