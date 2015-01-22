package br.com.abril.nds.integracao.enums;

public enum RouteInterface {
	
	EMS0127(127, "EMS0127"),
	EMS0128(128, "EMS0128"),
	EMS0185(185, "EMS0185"),
	EMS2021(2021, "EMS2021"),
	EMS0140(140, "EMS0140"),
	EMS0137(137, "EMS0137"),
	EMS3100(3100, "EMS3100");
	
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