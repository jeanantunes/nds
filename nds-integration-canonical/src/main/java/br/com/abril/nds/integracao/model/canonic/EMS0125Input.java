package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * @author ariel.maldonado
 * @version 1.0
 */
@Record
public class EMS0125Input extends IntegracaoDocument implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codDistrib;
	private String dataGeracaoArq;
	private String horaGeracaoArq;
	private String mnemonicoTab;
	private Integer contextoProd;
	private Long codForncProd;
	private String codProd;
	private Long edicao;
	private String chamadaCapa;
	
	@Field(offset = 1, length = 7)
	public String getCodDistrib() {
		return codDistrib;
	}
	
	public void setCodDistrib(String codDistrib) {
		this.codDistrib = codDistrib;
	}
	
	@Field(offset = 8, length = 8)
	public String getDataGeracaoArq() {
		return dataGeracaoArq;
	}
	
	public void setDataGeracaoArq(String dataGeracaoArq) {
		this.dataGeracaoArq = dataGeracaoArq;
	}
	
	@Field(offset = 16, length = 6)
	public String getHoraGeracaoArq() {
		return horaGeracaoArq;
	}
	
	public void setHoraGeracaoArq(String horaGeracaoArq) {
		this.horaGeracaoArq = horaGeracaoArq;
	}
	
	@Field(offset = 22, length = 4)
	public String getMnemonicoTab() {
		return mnemonicoTab;
	}
	
	public void setMnemonicoTab(String mnemonicoTab) {
		this.mnemonicoTab = mnemonicoTab;
	}
	
	@Field(offset = 26, length = 1)
	public Integer getContextoProd() {
		return contextoProd;
	}
	
	public void setContextoProd(Integer contextoProd) {
		this.contextoProd = contextoProd;
	}
	
	@Field(offset = 27, length = 7)
	public Long getCodForncProd() {
		return codForncProd;
	}
	
	public void setCodForncProd(Long codForncProd) {
		this.codForncProd = codForncProd;
	}
	
	@Field(offset = 34, length = 8)
	public String getCodProd() {
		return codProd;
	}
	
	public void setCodProd(String codProd) {
		this.codProd = codProd;
	}
	
	@Field(offset = 42, length = 4)
	public Long getEdicao() {
		return edicao;
	}
	
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
	@Field(offset = 46, length = 15)
	public String getChamadaCapa() { 
		return chamadaCapa;
	}
	
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = chamadaCapa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chamadaCapa == null) ? 0 : chamadaCapa.hashCode());
		result = prime * result
				+ ((codDistrib == null) ? 0 : codDistrib.hashCode());
		result = prime * result
				+ ((codForncProd == null) ? 0 : codForncProd.hashCode());
		result = prime * result + ((codProd == null) ? 0 : codProd.hashCode());
		result = prime * result
				+ ((contextoProd == null) ? 0 : contextoProd.hashCode());
		result = prime * result
				+ ((dataGeracaoArq == null) ? 0 : dataGeracaoArq.hashCode());
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
		result = prime * result
				+ ((horaGeracaoArq == null) ? 0 : horaGeracaoArq.hashCode());
		result = prime * result
				+ ((mnemonicoTab == null) ? 0 : mnemonicoTab.hashCode());
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
		EMS0125Input other = (EMS0125Input) obj;
		if (chamadaCapa == null) {
			if (other.chamadaCapa != null)
				return false;
		} else if (!chamadaCapa.equals(other.chamadaCapa))
			return false;
		if (codDistrib == null) {
			if (other.codDistrib != null)
				return false;
		} else if (!codDistrib.equals(other.codDistrib))
			return false;
		if (codForncProd == null) {
			if (other.codForncProd != null)
				return false;
		} else if (!codForncProd.equals(other.codForncProd))
			return false;
		if (codProd == null) {
			if (other.codProd != null)
				return false;
		} else if (!codProd.equals(other.codProd))
			return false;
		if (contextoProd == null) {
			if (other.contextoProd != null)
				return false;
		} else if (!contextoProd.equals(other.contextoProd))
			return false;
		if (dataGeracaoArq == null) {
			if (other.dataGeracaoArq != null)
				return false;
		} else if (!dataGeracaoArq.equals(other.dataGeracaoArq))
			return false;
		if (edicao == null) {
			if (other.edicao != null)
				return false;
		} else if (!edicao.equals(other.edicao))
			return false;
		if (horaGeracaoArq == null) {
			if (other.horaGeracaoArq != null)
				return false;
		} else if (!horaGeracaoArq.equals(other.horaGeracaoArq))
			return false;
		if (mnemonicoTab == null) {
			if (other.mnemonicoTab != null)
				return false;
		} else if (!mnemonicoTab.equals(other.mnemonicoTab))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EMS0125Input [codDistrib=" + codDistrib + ", dataGeracaoArq="
				+ dataGeracaoArq + ", horaGeracaoArq=" + horaGeracaoArq
				+ ", mnemonicoTab=" + mnemonicoTab + ", contextoProd="
				+ contextoProd + ", codForncProd=" + codForncProd
				+ ", codProd=" + codProd + ", edicao=" + edicao + ", chamadaCapa="
				+ chamadaCapa + "]";
	}


}