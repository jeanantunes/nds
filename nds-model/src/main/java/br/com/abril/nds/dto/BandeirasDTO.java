package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class BandeirasDTO implements Serializable{

	private static final long serialVersionUID = -7847418746083619011L;
	
	private BigInteger numeroNotaFiscal;
	
	private Integer serieNotaFiscal;
	
	private String chaveNFe;
		
	private BigInteger codigoEditor;
	
	private String nomeEditor;
	
	private Date dataSaida;
	
	private BigInteger volumes;
	
	
	
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
	
	private String semanaRecolhimento;
	
	public BandeirasDTO() {
		super();
	}
	
	public BandeirasDTO(BigInteger numeroNotaFiscal, Integer serieNotaFiscal, BigInteger codigoEditor, String nomeEditor, String chaveNFe, BigInteger volumes, String destino) {
		super();
		this.numeroNotaFiscal = numeroNotaFiscal;
		this.serieNotaFiscal = serieNotaFiscal;
		this.codigoEditor = codigoEditor;
		this.nomeEditor = nomeEditor;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_MONTH, 1);
		this.dataSaida = c.getTime();
		this.chaveNFe = chaveNFe;
		this.volumes = volumes;
		this.destino = destino;
	}
	
	public BigInteger getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(BigInteger numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public Integer getSerieNotaFiscal() {
		return serieNotaFiscal;
	}

	public void setSerieNotaFiscal(Integer serieNotaFiscal) {
		this.serieNotaFiscal = serieNotaFiscal;
	}

	public String getChaveNFe() {
		return chaveNFe;
	}

	public void setChaveNFe(String chaveNFe) {
		this.chaveNFe = chaveNFe;
	}

	public BigInteger getCodigoEditor() {
		return codigoEditor;
	}

	public void setCodigoEditor(BigInteger codigoEditor) {
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

	public BigInteger getVolumes() {
		return volumes;
	}

	public void setVolumes(BigInteger volume) {
		this.volumes = volume;
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

	public String getSemanaRecolhimento() {
		return semanaRecolhimento;
	}

	public void setSemanaRecolhimento(String semanaRecolhimento) {
		this.semanaRecolhimento = semanaRecolhimento;
	}
}