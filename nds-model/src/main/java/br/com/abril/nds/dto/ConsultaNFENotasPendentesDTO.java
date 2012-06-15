package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaNFENotasPendentesDTO implements Serializable {

	private static final long serialVersionUID = 8366815250237375585L;
	
	@Export(label = "Cota", alignment=Alignment.LEFT)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT)
	private String nome;
	
	@Export(label = "Data Encalhe", alignment=Alignment.LEFT)
	private String dataEncalhe;
	
	@Export(label = "Data Encalhe", alignment=Alignment.LEFT)
	private TipoOperacao tipoNota;
	
	@Export(label = "Valor Nota R$", alignment=Alignment.LEFT)
	private BigDecimal vlrNota;
	
	@Export(label = "Valor Real R$", alignment=Alignment.LEFT)
	private BigDecimal vlrReal;
	
	@Export(label = "Diferença", alignment=Alignment.LEFT)
	private BigDecimal diferenca;
	
	@Export(label = "Status", alignment=Alignment.LEFT)
	private String status;
	
	private Long numeroNfe;
	
	private String serie;
	
	private String chaveAcesso;
	
	private Long idNotaFiscalEntrada;
	
	public ConsultaNFENotasPendentesDTO() {}
	
	public ConsultaNFENotasPendentesDTO(Integer numeroCota, String nome,
			String dataEncalhe, TipoOperacao tipoNota, BigDecimal vlrNota,
			BigDecimal vlrReal, BigDecimal diferenca, String status,
			Long numeroNfe, String serie, String chaveAcesso, Long idNotaFiscalEntrada) {
		super();
		this.numeroCota = numeroCota;
		this.nome = nome;
		this.dataEncalhe = dataEncalhe;
		this.tipoNota = tipoNota;
		this.vlrNota = vlrNota;
		this.vlrReal = vlrReal;
		this.diferenca = diferenca;
		this.status = status;
		this.numeroNfe = numeroNfe;
		this.serie = serie;
		this.chaveAcesso = chaveAcesso;
		this.idNotaFiscalEntrada = idNotaFiscalEntrada;
	}

	public Long getNumeroNfe() {
		return numeroNfe;
	}

	public void setNumeroNfe(Long numeroNfe) {
		this.numeroNfe = numeroNfe;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getChaveAcesso() {
		return chaveAcesso;
	}

	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDataEncalhe() {
		return dataEncalhe;
	}

	public void setDataEncalhe(Date dataEncalhe) {
		this.dataEncalhe = DateUtil.formatarData(dataEncalhe, Constantes.DATE_PATTERN_PT_BR);;
	}

	public TipoOperacao getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(TipoOperacao tipoNota) {
		this.tipoNota = tipoNota;
	}

	public BigDecimal getVlrNota() {
		return vlrNota;
	}

	public void setVlrNota(BigDecimal vlrNota) {
		this.vlrNota = vlrNota;
	}

	public BigDecimal getVlrReal() {
		return vlrReal;
	}

	public void setVlrReal(BigDecimal vlrReal) {
		this.vlrReal = vlrReal;
	}

	public BigDecimal getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigDecimal diferenca) {
		this.diferenca = diferenca;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getIdNotaFiscalEntrada() {
		return idNotaFiscalEntrada;
	}

	public void setIdNotaFiscalEntrada(Long idNotaFiscalEntrada) {
		this.idNotaFiscalEntrada = idNotaFiscalEntrada;
	}
	
}
