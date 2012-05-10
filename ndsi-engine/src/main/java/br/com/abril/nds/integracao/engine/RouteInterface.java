package br.com.abril.nds.integracao.engine;

public enum RouteInterface {
	
	EMS0106(106, "EMS0106"),
	EMS0107(107, "EMS0107"),
	EMS0108(108, "EMS0108"),
	EMS0109(109, "EMS0109"),
	EMS0116(116, "EMS0116"),
	EMS0117(106, "EMS0117"),
	EMS0120(120, "EMS0120"),
	EMS0133(133, "EMS0133"),
	EMS0118(118, "EMS0118"),
	EMS0130(130, "EMS0130")
	;
	
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