package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * @author erick.dzadotz
 * @version 1.0
 */
@Record
public class EMS0114Input extends IntegracaoDocument implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codDistrib;
	private Date dataGeracaoArq;
	private String horaGeracaoArq;
	private String mnemonicoTab;
	private String contextoProd;
	private String codForncProd;
	private String codProd;
	private Long edicao;
	private Date dataRecolhimento;
	private String descBrinde;
	private String regimeRecolhimento;
	private String tipoDesconto;
	private String statusRecolhimento;
	
	@Field(offset = 1, length = 7)
	public String getCodDistrib() {
		return codDistrib;
	}
	public void setCodDistrib(String codDistrib) {
		this.codDistrib = codDistrib;
	}
	
	@Field(offset = 8, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataGeracaoArq() {
		return dataGeracaoArq;
	}
	public void setDataGeracaoArq(Date dataGeracaoArq) {
		this.dataGeracaoArq = dataGeracaoArq;
	}
	
	public String getHoraGeracaoArq() {
		return horaGeracaoArq;
	}
	
	public void setHoraGeracaoArq(String horaGeracaoArq) {
		this.horaGeracaoArq = horaGeracaoArq;
	}
	
	public String getMnemonicoTab() {
		return mnemonicoTab;
	}
	
	public void setMnemonicoTab(String mnemonicoTab) {
		this.mnemonicoTab = mnemonicoTab;
	}
	
	public String getContextoProd() {
		return contextoProd;
	}
	
	public void setContextoProd(String contextoProd) {
		this.contextoProd = contextoProd;
	}
	
	public String getCodForncProd() {
		return codForncProd;
	}
	
	public void setCodForncProd(String codForncProd) {
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
	
	@Field(offset = 46, length = 8)
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}
	
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
	public String getDescBrinde() {
		return descBrinde;
	}
	
	public void setDescBrinde(String descBrinde) {
		this.descBrinde = descBrinde;
	}
	
	public String getRegimeRecolhimento() {
		return regimeRecolhimento;
	}
	
	public void setRegimeRecolhimento(String regimeRecolhimento) {
		this.regimeRecolhimento = regimeRecolhimento;
	}
	
	public String getTipoDesconto() {
		return tipoDesconto;
	}
	
	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}
	
	public String getStatusRecolhimento() {
		return statusRecolhimento;
	}
	
	public void setStatusRecolhimento(String statusRecolhimento) {
		this.statusRecolhimento = statusRecolhimento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codDistrib == null) ? 0 : codDistrib.hashCode());
		result = prime * result
				+ ((codForncProd == null) ? 0 : codForncProd.hashCode());
		result = prime * result + ((codProd == null) ? 0 : codProd.hashCode());
		result = prime * result
				+ ((contextoProd == null) ? 0 : contextoProd.hashCode());
		result = prime * result
				+ ((dataGeracaoArq == null) ? 0 : dataGeracaoArq.hashCode());
		result = prime
				* result
				+ ((dataRecolhimento == null) ? 0 : dataRecolhimento.hashCode());
		result = prime * result
				+ ((descBrinde == null) ? 0 : descBrinde.hashCode());
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
		result = prime * result
				+ ((horaGeracaoArq == null) ? 0 : horaGeracaoArq.hashCode());
		result = prime * result
				+ ((mnemonicoTab == null) ? 0 : mnemonicoTab.hashCode());
		result = prime
				* result
				+ ((regimeRecolhimento == null) ? 0 : regimeRecolhimento
						.hashCode());
		result = prime
				* result
				+ ((statusRecolhimento == null) ? 0 : statusRecolhimento
						.hashCode());
		result = prime * result
				+ ((tipoDesconto == null) ? 0 : tipoDesconto.hashCode());
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
		EMS0114Input other = (EMS0114Input) obj;
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
		if (dataRecolhimento == null) {
			if (other.dataRecolhimento != null)
				return false;
		} else if (!dataRecolhimento.equals(other.dataRecolhimento))
			return false;
		if (descBrinde == null) {
			if (other.descBrinde != null)
				return false;
		} else if (!descBrinde.equals(other.descBrinde))
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
		if (regimeRecolhimento == null) {
			if (other.regimeRecolhimento != null)
				return false;
		} else if (!regimeRecolhimento.equals(other.regimeRecolhimento))
			return false;
		if (statusRecolhimento == null) {
			if (other.statusRecolhimento != null)
				return false;
		} else if (!statusRecolhimento.equals(other.statusRecolhimento))
			return false;
		if (tipoDesconto == null) {
			if (other.tipoDesconto != null)
				return false;
		} else if (!tipoDesconto.equals(other.tipoDesconto))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "EMS0114Input [codDistrib=" + codDistrib + ", dataGeracaoArq="
				+ dataGeracaoArq + ", horaGeracaoArq=" + horaGeracaoArq
				+ ", mnemonicoTab=" + mnemonicoTab + ", contextoProd="
				+ contextoProd + ", codForncProd=" + codForncProd
				+ ", codProd=" + codProd + ", edicao=" + edicao
				+ ", dataRecolhimento=" + dataRecolhimento + ", descBrinde="
				+ descBrinde + ", regimeRecolhimento=" + regimeRecolhimento
				+ ", tipoDesconto=" + tipoDesconto + ", statusRecolhimento="
				+ statusRecolhimento + "]";
	}	
}
