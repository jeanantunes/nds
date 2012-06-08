package br.com.abril.nds.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="LOG_LOCALIDADE")
public class LogLocalidade {
	
	@Id
	@Column(name = "LOC_NU") 
	private Long locNu;
	
	@Column(name = "UFE_SG") 
	private String ufeSg;
	
	@Column(name = "LOC_NO") 
	private String locNo;
	
	@Column(name = "CEP") 
	private String cep;
	
	@Column(name = "LOC_IN_SIT") 
	private String locInSit;
	
	@Column(name = "LOC_IN_TIPO_LOC") 
	private String locInTipoLoc;
	
	@Column(name = "LOC_NU_SUB") 
	private Integer locNuSub;
	
	@Column(name = "LOC_NO_ABREV") 
	private String locNoAbrev;
	
	@Column(name = "MUN_NU") 
	private Integer munNu;

	@OneToMany(mappedBy = "logLocalidade")
	private List<LogBairro> logBairros = new ArrayList<LogBairro>();

	public Long getLocNu() {
		return locNu;
	}

	public void setLocNu(Long locNu) {
		this.locNu = locNu;
	}

	public String getUfeSg() {
		return ufeSg;
	}

	public void setUfeSg(String ufeSg) {
		this.ufeSg = ufeSg;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLocInSit() {
		return locInSit;
	}

	public void setLocInSit(String locInSit) {
		this.locInSit = locInSit;
	}

	public String getLocInTipoLoc() {
		return locInTipoLoc;
	}

	public void setLocInTipoLoc(String locInTipoLoc) {
		this.locInTipoLoc = locInTipoLoc;
	}

	public Integer getLocNuSub() {
		return locNuSub;
	}

	public void setLocNuSub(Integer locNuSub) {
		this.locNuSub = locNuSub;
	}

	public String getLocNoAbrev() {
		return locNoAbrev;
	}

	public void setLocNoAbrev(String locNoAbrev) {
		this.locNoAbrev = locNoAbrev;
	}

	public Integer getMunNu() {
		return munNu;
	}

	public void setMunNu(Integer munNu) {
		this.munNu = munNu;
	}

	public List<LogBairro> getLogBairros() {
		return logBairros;
	}

	public void setLogBairros(List<LogBairro> logBairros) {
		this.logBairros = logBairros;
	}

	public String getLocNo() {
		return locNo;
	}

	public void setLocNo(String locNo) {
		this.locNo = locNo;
	}
	
	
}
