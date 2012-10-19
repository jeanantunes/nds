package br.com.abril.nds.client.vo;

import java.util.ArrayList;
import java.util.List;

import br.com.abril.nds.dto.ContasAPagarDistribDTO;
import br.com.abril.nds.dto.ContasAPagarTotalDistribDTO;

public class ContasAPagarTotalDistribVO<VO, DTO> {

	private List<VO> grid;
	private List<ContasAPagarDistribVO> totalDistrib;

	
	public ContasAPagarTotalDistribVO()
	{}
	
	
	public ContasAPagarTotalDistribVO(ContasAPagarTotalDistribDTO<DTO> dto) {
		
		this.grid = new ArrayList<VO>();
		this.totalDistrib = new ArrayList<ContasAPagarDistribVO>(); 
		for (ContasAPagarDistribDTO to : dto.getTotalDistrib()) {
			this.totalDistrib.add(new ContasAPagarDistribVO(to));
		}
	}
	
	
	public List<VO> getGrid() {
		return grid;
	}
	public void setGrid(List<VO> grid) {
		this.grid = grid;
	}
	public List<ContasAPagarDistribVO> getTotalDistrib() {
		return totalDistrib;
	}
	public void setTotalDistrib(List<ContasAPagarDistribVO> totalDistrib) {
		this.totalDistrib = totalDistrib;
	}
}
