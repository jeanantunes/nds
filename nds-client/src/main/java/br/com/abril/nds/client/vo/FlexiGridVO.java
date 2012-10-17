package br.com.abril.nds.client.vo;

import java.util.List;

public class FlexiGridVO<T> {

	protected List<T> grid;
	protected Integer totalGrid;
	
	
	public FlexiGridVO()
	{}
	
	
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
