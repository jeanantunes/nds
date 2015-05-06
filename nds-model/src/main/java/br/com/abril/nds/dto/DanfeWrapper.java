package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DanfeWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<DanfeDTO> danfes;
	
	public DanfeWrapper() {}

	public DanfeWrapper(DanfeDTO danfe) {
		
		danfes = new ArrayList<DanfeDTO>();
		
		danfes.add(danfe);
		
	}

	/**
	 * Obtém danfes
	 *
	 * @return List<DanfeDTO>
	 */
	public List<DanfeDTO> getDanfes() {
		return danfes;
	}

	/**
	 * Atribuí danfes
	 * @param danfes 
	 */
	public void setDanfes(List<DanfeDTO> danfes) {
		this.danfes = danfes;
	}
}