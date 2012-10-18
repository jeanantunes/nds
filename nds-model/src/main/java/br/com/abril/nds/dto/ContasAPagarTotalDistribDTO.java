package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class ContasAPagarTotalDistribDTO<T> implements Serializable {

	private static final long serialVersionUID = -625595265589550325L;
	
	private List<T> grid;
	private List<ContasAPagarDistribDTO> totalDistrib;
	
	
	public List<T> getGrid() {
		return grid;
	}
	public void setGrid(List<T> grid) {
		this.grid = grid;
	}
	public List<ContasAPagarDistribDTO> getTotalDistrib() {
		return totalDistrib;
	}
	public void setTotalDistrib(List<ContasAPagarDistribDTO> totalDistrib) {
		this.totalDistrib = totalDistrib;
	}
}
