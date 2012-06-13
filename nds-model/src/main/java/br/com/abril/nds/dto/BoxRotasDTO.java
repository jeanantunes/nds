package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.HashMap;

public class BoxRotasDTO implements Serializable{

	private static final long serialVersionUID = -1179186550475184202L;

	private Integer qtdeTotal;
	private HashMap<String, Integer> rotasQtde;
	
	public BoxRotasDTO() {
		
	}
	
	public BoxRotasDTO(Integer qtdeTotal, HashMap<String, Integer> rotasQtde) {
		super();
		this.qtdeTotal = qtdeTotal;
		this.rotasQtde = rotasQtde;
	}

	public Integer getQtdeTotal() {
		return qtdeTotal;
	}
	public void setQtdeTotal(Integer qtdeTotal) {
		this.qtdeTotal = qtdeTotal;
	}
	public HashMap<String, Integer> getRotasQtde() {
		return rotasQtde;
	}
	public void setRotasQtde(HashMap<String, Integer> rotasQtde) {
		this.rotasQtde = rotasQtde;
	}
}
