package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class FlexiGridDTO<T> implements Serializable {

	private static final long serialVersionUID = 7846329432674490281L;
	
	protected List<T> grid;
	protected Integer totalGrid;
	
	public List<T> getGrid() {
		return grid;
	}
	public void setGrid(List<T> grid) {
		this.grid = grid;
	}
	public Integer getTotalGrid() {
		return totalGrid;
	}
	public void setTotalGrid(Integer totalGrid) {
		this.totalGrid = totalGrid;
	}
}
