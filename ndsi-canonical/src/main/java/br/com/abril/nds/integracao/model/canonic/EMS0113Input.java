package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0113Input extends IntegracaoDocument implements Serializable{

	/**
	 * @author Jones.Costa
	 * @version 1.0
	 */
	private static final long serialVersionUID = 1L;
	

	
	private Long codigoDistribuidor;
	private	Date dataGeracaoArquivo;
	private Date horaGeracaoArquivo;
	private String mnemonicoTabela;
	private String tipoOperacao;
	private int contextoDistribuidor;
	private Long codigoFornecedor;
	private int tipoDesconto;
	private BigDecimal percentDesconto;
	private BigDecimal percentPrestServico;
	private Date dataInicioDesconto;
	
	@Field(offset = 1, length=7)
	public Long getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	public void setCodigoDistribuidor(Long codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	
	
	public Date getDataGeracaoArquivo() {
		return dataGeracaoArquivo;
	}
	public void setDataGeracaoArquivo(Date dataGeracaoArquivo) {
		this.dataGeracaoArquivo = dataGeracaoArquivo;
	}
	
	
	public Date getHoraGeracaoArquivo() {
		return horaGeracaoArquivo;
	}
	public void setHoraGeracaoArquivo(Date horaGeracaoArquivo) {
		this.horaGeracaoArquivo = horaGeracaoArquivo;
	}
	
	
	public String getMnemonicoTabela() {
		return mnemonicoTabela;
	}
	public void setMnemonicoTabela(String mnemonicoTabela) {
		this.mnemonicoTabela = mnemonicoTabela;
	}
	
	
	public String getTipoOperacao() {
		return tipoOperacao;
	}
	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}
	
	
	public int getContextoDistribuidor() {
		return contextoDistribuidor;
	}
	public void setContextoDistribuidor(int contextoDistribuidor) {
		this.contextoDistribuidor = contextoDistribuidor;
	}
	
	
	public Long getCodigoFornecedor() {
		return codigoFornecedor;
	}
	public void setCodigoFornecedor(Long codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}
	
	@Field(offset = 35, length=2)
	public int getTipoDesconto() {
		return tipoDesconto;
	}
	public void setTipoDesconto(int tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}
	
	@Field(offset = 37, length=7)
	public BigDecimal getPercentDesconto() {
		return percentDesconto;
	}
	public void setPercentDesconto(BigDecimal percentDesconto) {
		this.percentDesconto = percentDesconto;
	}
	
	@Field(offset = 44, length=7)
	public BigDecimal getPercentPrestServico() {
		return percentPrestServico;
	}
	public void setPercentPrestServico(BigDecimal percentPrestServico) {
		this.percentPrestServico = percentPrestServico;
	}
	
	@Field(offset = 51, length=8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataInicioDesconto() {
		return dataInicioDesconto;
	}
	public void setDataInicioDesconto(Date dataInicioDesconto) {
		this.dataInicioDesconto = dataInicioDesconto;
	}
	
	
}
