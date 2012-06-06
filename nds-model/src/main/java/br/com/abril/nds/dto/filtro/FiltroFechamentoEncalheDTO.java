package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

public class FiltroFechamentoEncalheDTO implements Serializable {

	private static final long serialVersionUID = -7826124115727106592L;
	
	private Date dataEncalhe;
	private Long fornecedorId;
	private Long boxId;
	
	
	public Date getDataEncalhe() {
		return dataEncalhe;
	}
	public void setDataEncalhe(Date dataEncalhe) {
		this.dataEncalhe = dataEncalhe;
	}
	public Long getFornecedorId() {
		return fornecedorId;
	}
	public void setFornecedorId(Long fornecedorId) {
		this.fornecedorId = fornecedorId;
	}
	public Long getBoxId() {
		return boxId;
	}
	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}
}
