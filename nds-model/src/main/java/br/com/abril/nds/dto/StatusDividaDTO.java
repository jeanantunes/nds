package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class StatusDividaDTO implements Serializable {
	
	private static final long serialVersionUID = -4132748025627433307L;

	private Long idDivida;
	
	private Long idCota;
	
	private Long idCobranca;
	
	private Long idNegociacao;
	
	private TipoCobranca tipoCobranca;
	
	private String descricaoTipoCobranca;
	
	private BigDecimal comissaoSaldoDivida;
	
	@Export(label = "Cota", alignment=Alignment.LEFT)
	private Integer numCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT)
	private String nome;
	
	@Export(label = "Status", alignment=Alignment.LEFT)
	private String status;
		
	@Export(label = "Consignado até Data", alignment=Alignment.CENTER)
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
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";

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
		this.status = SituacaoCadastro.valueOf(status).toString();
	}

	public String getConsignado() {
		return consignado;
	}

	public void setConsignado(BigDecimal consignado) {
		this.consignado = CurrencyUtil.formatarValor(consignado);
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		if(dataVencimento==null) {
			this.dataVencimento = "";
			return;
		}
		this.dataVencimento = DateUtil.formatarData(dataVencimento, FORMATO_DATA);
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		if(dataPagamento==null) {
			this.dataPagamento = "";
			return;
		}
		this.dataPagamento = DateUtil.formatarData(dataPagamento, FORMATO_DATA);
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = StatusDivida.valueOf(situacao).toString();
	}

	public String getDividaAcumulada() {
		return dividaAcumulada;
	}

	public void setDividaAcumulada(BigDecimal dividaAcumulada) {
		this.dividaAcumulada = CurrencyUtil.formatarValor(dividaAcumulada);
	}

	public Long getDiasAtraso() {
		return diasAtraso;
	}

	public void setDiasAtraso(Long diasAtraso) {
		this.diasAtraso = diasAtraso;
	}

	public Long getIdDivida() {
		return idDivida;
	}

	public void setIdDivida(Long idDivida) {
		this.idDivida = idDivida;
	}

	public Long getIdCobranca() {
		return idCobranca;
	}

	public void setIdCobranca(Long idCobranca) {
		this.idCobranca = idCobranca;
	}

	public Long getIdNegociacao() {
		return idNegociacao;
	}

	public void setIdNegociacao(Long idNegociacao) {
		if(idNegociacao == null)
			return;
		this.idNegociacao = idNegociacao;
	}

	public BigDecimal getComissaoSaldoDivida() {
		return comissaoSaldoDivida;
	}

	public void setComissaoSaldoDivida(BigDecimal comissaoSaldoDivida) {
		this.comissaoSaldoDivida = comissaoSaldoDivida;
	}

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	
	public String getDescricaoTipoCobranca() {
		return descricaoTipoCobranca;
	}

	public void setNameTipoCobranca(String nameTipoCobranca) {
		
		if(nameTipoCobranca == null || nameTipoCobranca.trim().isEmpty()) {
			this.tipoCobranca = null;
		} else {
			
			this.tipoCobranca = TipoCobranca.valueOf(nameTipoCobranca);
			
			this.descricaoTipoCobranca = this.tipoCobranca.getDescricao();
			
		}
		
	}
	
	

}
