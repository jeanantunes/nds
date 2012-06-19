package br.com.abril.nds.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="LOG_BAIRRO")
public class LogBairro {
	
	@Id
	@Column(name = "BAI_NU") 
	private Long baiNu;
	
	@Column(name = "UFE_SG") 
	private String ufeSg;
	
	@ManyToOne
	@JoinColumn(name = "LOC_NU")
	private LogLocalidade logLocalidade;
	
	@Column(name = "BAI_NO") 
	private String baiNo;
	
	@Column(name = "BAI_NO_ABREV") 
	private String baiNoAbrev;

	public Long getBaiNu() {
		return baiNu;
	}

	public void setBaiNu(Long baiNu) {
		this.baiNu = baiNu;
	}

	public String getUfeSg() {
		return ufeSg;
	}

	public void setUfeSg(String ufeSg) {
		this.ufeSg = ufeSg;
	}

	public LogLocalidade getLogLocalidade() {
		return logLocalidade;
	}

	public void setLogLocalidade(LogLocalidade logLocalidade) {
		this.logLocalidade = logLocalidade;
	}

	public String getBaiNo() {
		return baiNo;
	}

	public void setBaiNo(String baiNo) {
		this.baiNo = baiNo;
	}

	public String getBaiNoAbrev() {
		return baiNoAbrev;
	}

	public void setBaiNoAbrev(String baiNoAbrev) {
		this.baiNoAbrev = baiNoAbrev;
	}

	
	

	

}
