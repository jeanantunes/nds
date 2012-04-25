package br.com.abril.nds.dto;

import java.io.Serializable;

public class MapDTO implements Serializable {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	private Long map;

	/**
	 * @return the map
	 */
	public Long getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Long map) {
		this.map = map;
	}
	
	
}
