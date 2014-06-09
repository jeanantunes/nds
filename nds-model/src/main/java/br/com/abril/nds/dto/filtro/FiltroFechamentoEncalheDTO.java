package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FiltroFechamentoEncalheDTO implements Serializable {

	private static final long serialVersionUID = -7826124115727106592L;
	
	private Date dataEncalhe;
	private Long fornecedorId;
	private Long boxId;
	private List<Long> fisico;
	private boolean indAgrupaPorCota;
	private boolean checkAll;
	
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
	public List<Long> getFisico() {
		return fisico;
	}
	public void setFisico(List<Long> fisico) {
		this.fisico = fisico;
	}
	public boolean isIndAgrupaPorCota() {
		return indAgrupaPorCota;
	}
	public void setIndAgrupaPorCota(boolean indAgrupaPorCota) {
		this.indAgrupaPorCota = indAgrupaPorCota;
	}
	/**
	 * @return the checkAll
	 */
	public boolean isCheckAll() {
		return checkAll;
	}
	/**
	 * @param checkAll the checkAll to set
	 */
	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}
	
}