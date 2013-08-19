package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0126Input extends IntegracaoDocument implements Serializable{

	/**
	 * @author Jones.Costa
	 * @version 1.0
	 */
	private static final long serialVersionUID = 1L;
	
	private long codDistribuidor;
	private	Date dataGeracaoArquivo;
	private Date horaGeracaoArquivo;
	private String mnemonicoTabela;
	private int contextoProduto;
	private Long codFornecedorProduto;
	private String codigoProduto;
	private Long edicao;
	private String codigoBarras;
	
	
	/*@Field(offset = 1, length=7)*/
	public long getCodDistribuidor() {
		return codDistribuidor;
	}
	public void setCodDistribuidor(long codDistribuidor) {
		this.codDistribuidor = codDistribuidor;
	}
	
	/*@Field(offset = 8, length=8)
	@FixedFormatPattern("yyyyMMdd")  */
	public Date getDataGeracaoArquivo() {
		return dataGeracaoArquivo;
	}
	public void setDataGeracaoArquivo(Date dataGeracaoArquivo) {
		this.dataGeracaoArquivo = dataGeracaoArquivo;
	}
	
	/*@Field(offset = 16, length=6)
	@FixedFormatPattern("HHmmss")*/
	public Date getHoraGeracaoArquivo() {
		return horaGeracaoArquivo;
	}
	public void setHoraGeracaoArquivo(Date horaGeracaoArquivo) {
		this.horaGeracaoArquivo = horaGeracaoArquivo;
	}
	
	/*@Field(offset = 22, length=4)*/
	public String getMnemonicoTabela() {
		return mnemonicoTabela;
	}
	public void setMnemonicoTabela(String mnemonicoTabela) {
		this.mnemonicoTabela = mnemonicoTabela;
	}
	
	/*@Field(offset = 26, length=1)*/
	public int getContextoProduto() {
		return contextoProduto;
	}
	public void setContextoProduto(int contextoProduto) {
		this.contextoProduto = contextoProduto;
	}
	
	/*@Field(offset = 27, length=4)*/
	public Long getCodFornecedorProduto() {
		return codFornecedorProduto;
	}
	public void setCodFornecedorProduto(Long codFornecedorProduto) {
		this.codFornecedorProduto = codFornecedorProduto;
	}
	
	@Field(offset = 34, length=8)
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	@Field(offset = 42, length=4)
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset = 46, length=18)
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (codDistribuidor ^ (codDistribuidor >>> 32));
		result = prime
				* result
				+ ((codFornecedorProduto == null) ? 0 : codFornecedorProduto
						.hashCode());
		result = prime * result
				+ ((codigoBarras == null) ? 0 : codigoBarras.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result + contextoProduto;
		result = prime
				* result
				+ ((dataGeracaoArquivo == null) ? 0 : dataGeracaoArquivo
						.hashCode());
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
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
		EMS0126Input other = (EMS0126Input) obj;
		if (codDistribuidor != other.codDistribuidor)
			return false;
		if (codFornecedorProduto == null) {
			if (other.codFornecedorProduto != null)
				return false;
		} else if (!codFornecedorProduto.equals(other.codFornecedorProduto))
			return false;
		if (codigoBarras == null) {
			if (other.codigoBarras != null)
				return false;
		} else if (!codigoBarras.equals(other.codigoBarras))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (contextoProduto != other.contextoProduto)
			return false;
		if (dataGeracaoArquivo == null) {
			if (other.dataGeracaoArquivo != null)
				return false;
		} else if (!dataGeracaoArquivo.equals(other.dataGeracaoArquivo))
			return false;
		if (edicao == null) {
			if (other.edicao != null)
				return false;
		} else if (!edicao.equals(other.edicao))
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
	@Override
	public String toString() {
		return "EMS0126Input [codDistribuidor=" + codDistribuidor
				+ ", dataGeracaoArquivo=" + dataGeracaoArquivo
				+ ", horaGeracaoArquivo=" + horaGeracaoArquivo
				+ ", mnemonicoTabela=" + mnemonicoTabela + ", contextoProduto="
				+ contextoProduto + ", codFornecedorProduto="
				+ codFornecedorProduto + ", codigoProduto=" + codigoProduto
				+ ", edicao=" + edicao + ", codigoBarras=" + codigoBarras + "]";
	}
	
	
	
	
	
	
	
	
	
	

}
