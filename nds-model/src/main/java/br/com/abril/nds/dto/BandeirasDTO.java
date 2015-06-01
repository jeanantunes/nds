package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class BandeirasDTO implements Serializable{

	private static final long serialVersionUID = -7847418746083619011L;
	
	private String numeroNotaFiscal;
	
	private String serieNotaFiscal;
	
	private String codigoEditor;
	
	private String nomeEditor;
	
	private Date dataSaida;
	
	private Long volume;
	
	@Export(label="Cód. Produto", alignment=Export.Alignment.LEFT, exhibitionOrder=2)
	private String codProduto;
	
	@Export(label="Produto", alignment=Export.Alignment.LEFT, exhibitionOrder=4)
	private String nomeProduto;
	
	@Export(label="Edição", alignment=Export.Alignment.LEFT, exhibitionOrder=3)
	private Long edProduto;
	private Integer pctPadrao;
	
	@Export(label="Destino", alignment=Export.Alignment.LEFT, exhibitionOrder=5)
	private String destino;
	
	@Export(label="Prioridade", alignment=Export.Alignment.CENTER, exhibitionOrder=6)
	private Integer prioridade;
	
	@Export(label="Qtde. Solicitada", alignment=Export.Alignment.CENTER, exhibitionOrder=7)
	private BigInteger qtde;
	
	@Export(label="Data", alignment=Export.Alignment.LEFT, exhibitionOrder=1)
	private Date data;
	
	public BandeirasDTO() {
		
	}
	
	public BandeirasDTO(String codProduto, String nomeProduto,
			Long edProduto, Integer pctPadrao) {
		super();
		this.codProduto = codProduto;
		this.nomeProduto = nomeProduto;
		this.edProduto = edProduto;
		this.pctPadrao = pctPadrao;
	}

	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public String getSerieNotaFiscal() {
		return serieNotaFiscal;
	}

	public void setSerieNotaFiscal(String serieNotaFiscal) {
		this.serieNotaFiscal = serieNotaFiscal;
	}

	public String getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(String codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public Integer getPctPadrao() {
		return pctPadrao;
	}
	public void setPctPadrao(Integer pctPadrao) {
		this.pctPadrao = pctPadrao;
	}

	public Integer getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Integer prioridade) {
		this.prioridade = prioridade;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = (qtde== null) ? BigInteger.ZERO : qtde;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Long getEdProduto() {
		return edProduto;
	}

	public void setEdProduto(Long edProduto) {
		this.edProduto = edProduto;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}	
}
