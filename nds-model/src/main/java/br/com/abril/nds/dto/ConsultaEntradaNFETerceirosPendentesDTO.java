package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaEntradaNFETerceirosPendentesDTO implements Serializable {

	private static final long serialVersionUID = 8366815250237375585L;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nome;
	
	@Export(label = "Data Encalhe", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String dataEncalhe;
	
	@Export(label = "Tipo de Nota", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private String tipoNotaFiscal;
	
	private BigDecimal valorNota;
	
	private BigDecimal valorReal;
	
	private BigDecimal diferenca;
	
	@Export(label = "Status", alignment=Alignment.LEFT, exhibitionOrder = 8)
	private String status;
	
	private Long idNotaFiscalEntrada;
	
	private Long numeroNfe;
	
	private String serie;
	
	private String chaveAcesso;
	
	private Long idControleConferenciaEncalheCota;
	
	private String valorNotaFormatado;
	private String valorRealFormatado;
	private String diferencaFormatado;
	
	
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
		this.dataEncalhe = DateUtil.formatarData(dataEncalhe, Constantes.DATE_PATTERN_PT_BR);
	}

	public BigDecimal getValorNota() {
		return valorNota;		
	}
	
	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = CurrencyUtil.arredondarValorParaDuasCasas(valorNota);
		if(valorNota != null){
			valorNotaFormatado = CurrencyUtil.formatarValor(valorNota);
		}
	}
	
	@Export(label = "Valor Nota R$", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	public String getValorNotaFormatado() {
		return valorNotaFormatado;
	}

	public BigDecimal getValorReal() {
		return valorReal;
	}

	public void setValorReal(BigDecimal valorReal) {
		this.valorReal = CurrencyUtil.arredondarValorParaQuatroCasas(valorReal);
		if(valorReal != null){
			valorRealFormatado = CurrencyUtil.formatarValor(valorReal);
		}
	}
	
	@Export(label = "Valor Real R$", alignment=Alignment.RIGHT, exhibitionOrder = 6)
	public String getValorRealFormatado() {
		return valorRealFormatado;
	}

	public BigDecimal getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigDecimal diferenca) {
		this.diferenca = CurrencyUtil.arredondarValorParaDuasCasas(diferenca);
		if(diferenca != null){
			diferencaFormatado = CurrencyUtil.formatarValor(diferenca);
		}
	}
	
	@Export(label = "Diferença", alignment=Alignment.RIGHT, exhibitionOrder = 7)
	public String getDiferencaFormatado() {
		return diferencaFormatado;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the idControleConferenciaEncalheCota
	 */
	public Long getIdControleConferenciaEncalheCota() {
		return idControleConferenciaEncalheCota;
	}

	/**
	 * @param idControleConferenciaEncalheCota the idControleConferenciaEncalheCota to set
	 */
	public void setIdControleConferenciaEncalheCota(Long idControleConferenciaEncalheCota) {
		this.idControleConferenciaEncalheCota = idControleConferenciaEncalheCota;
	}

	/**
	 * @return the tipoNotaFiscal
	 */
	public String getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	/**
	 * @param tipoNotaFiscal the tipoNotaFiscal to set
	 */
	public void setTipoNotaFiscal(String tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
	}

	public Long getIdNotaFiscalEntrada() {
		return idNotaFiscalEntrada;
	}

	public void setIdNotaFiscalEntrada(Long idNotaFiscalEntrada) {
		this.idNotaFiscalEntrada = idNotaFiscalEntrada;
	}
}
