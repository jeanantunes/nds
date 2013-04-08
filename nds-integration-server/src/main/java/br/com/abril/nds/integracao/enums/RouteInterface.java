package br.com.abril.nds.integracao.enums;

public enum RouteInterface {
	
	EMS0127(127, "EMS0127"),
	EMS0185(185, "EMS0185"),
	EMS2021(2021, "EMS2021");
	
	private Integer id;
	
	private String name;
	
	private RouteInterface(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
}