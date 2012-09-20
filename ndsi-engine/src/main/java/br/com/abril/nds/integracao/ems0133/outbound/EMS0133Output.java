package br.com.abril.nds.integracao.ems0133.outbound;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0133Output implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoDistribuidor;
	private Date dataGeracaoArquivo;
	private Date horaGeracaoArquivo;
	private String mnemonicoTabela;
	private Integer contextoProduto;
	private Integer codigoFornecedorProduto;
	private String codigoProduto;
	private Long numeroEdicao;
	private Date dataRecolhimento;
	
	@Field(offset = 1, length = 7)
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	
	@FixedFormatPattern("ddMMyyyy")
	@Field(offset = 8, length = 8)
	public Date getDataGeracaoArquivo() {
		return dataGeracaoArquivo;
	}
	public void setDataGeracaoArquivo(Date dataGeracaoArquivo) {
		this.dataGeracaoArquivo = dataGeracaoArquivo;
	}
			
	@FixedFormatPattern("HHmmss")
	@Field(offset = 16, length = 6)
	public Date getHoraGeracaoArquivo() {
		return horaGeracaoArquivo;
	}
	public void setHoraGeracaoArquivo(Date horaGeracaoArquivo) {
		this.horaGeracaoArquivo = horaGeracaoArquivo;
	}
	
	@Field(offset = 22, length = 4)
	public String getMnemonicoTabela() {
		return "RCPR";
	}
	
	public void setMnemonicoTabela(String mnemonicoTabela){
		this.mnemonicoTabela = mnemonicoTabela;
	}
	
	
	@Field(offset = 26, length = 1)
	public Integer getContextoProduto() {
		return contextoProduto;
	}
	public void setContextoProduto(Integer contextoProduto) {
		this.contextoProduto = contextoProduto;
	}
	
	@Field(offset = 27, length = 7)
	public Integer getCodigoFornecedorProduto() {
		return codigoFornecedorProduto;
	}
	public void setCodigoFornecedorProduto(Integer codigoFornecedorProduto) {
		this.codigoFornecedorProduto = codigoFornecedorProduto;
	}
	
	@Field(offset = 34, length = 8)
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	/**
	 * @return the numeroEdicao
	 */
	@Field(offset=42, length=4, paddingChar='0', align=Align.RIGHT)
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	@FixedFormatPattern("ddMMyyyy")
	@Field(offset = 46, length = 8)
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codigoDistribuidor == null) ? 0 : codigoDistribuidor
						.hashCode());
		result = prime
				* result
				+ ((codigoFornecedorProduto == null) ? 0
						: codigoFornecedorProduto.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());						
		result = prime * result
				+ ((contextoProduto == null) ? 0 : contextoProduto.hashCode());
		result = prime
				* result
				+ ((dataGeracaoArquivo == null) ? 0 : dataGeracaoArquivo
						.hashCode());
		result = prime
				* result
				+ ((dataRecolhimento == null) ? 0 : dataRecolhimento.hashCode());
		result = prime
				* result
				+ ((horaGeracaoArquivo == null) ? 0 : horaGeracaoArquivo
						.hashCode());
		result = prime * result
				+ ((mnemonicoTabela == null) ? 0 : mnemonicoTabela.hashCode());
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
		EMS0133Output other = (EMS0133Output) obj;
		if (codigoDistribuidor == null) {
			if (other.codigoDistribuidor != null)
				return false;
		} else if (!codigoDistribuidor.equals(other.codigoDistribuidor))
			return false;
		if (codigoFornecedorProduto == null) {
			if (other.codigoFornecedorProduto != null)
				return false;
		} else if (!codigoFornecedorProduto
				.equals(other.codigoFornecedorProduto))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		if (contextoProduto == null) {
			if (other.contextoProduto != null)
				return false;
		} else if (!contextoProduto.equals(other.contextoProduto))
			return false;
		if (dataGeracaoArquivo == null) {
			if (other.dataGeracaoArquivo != null)
				return false;
		} else if (!dataGeracaoArquivo.equals(other.dataGeracaoArquivo))
			return false;
		if (dataRecolhimento == null) {
			if (other.dataRecolhimento != null)
				return false;
		} else if (!dataRecolhimento.equals(other.dataRecolhimento))
			return false;
		if (horaGeracaoArquivo == null) {
			if (other.horaGeracaoArquivo != null)
				return false;
		} else if (!horaGeracaoArquivo.equals(other.horaGeracaoArquivo))
			return false;
		if (mnemonicoTabela == null) {
			if (other.mnemonicoTabela != null)
				return false;
		} else if (!mnemonicoTabela.equals(other.mnemonicoTabela))
			return false;
		return true;
	}
	
	
	

}
