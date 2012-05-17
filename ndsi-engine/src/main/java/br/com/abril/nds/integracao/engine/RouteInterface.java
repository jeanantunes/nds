package br.com.abril.nds.integracao.engine;

public enum RouteInterface {
	
	EMS0106(106, "EMS0106"),
	EMS0107(107, "EMS0107"),
	EMS0108(108, "EMS0108"),
	EMS0109(109, "EMS0109"),
	EMS0110(110, "EMS0110"),
	EMS0114(114, "EMS0114"),
	EMS0116(116, "EMS0116"),
	EMS0117(117, "EMS0117"),
	EMS0118(118, "EMS0118"),
	EMS0119(119, "EMS0119"),
	EMS0120(120, "EMS0120"),
	EMS0130(130, "EMS0130"),
	EMS0131(131, "EMS0131"), 
	EMS0132(132, "EMS0132"),
	EMS0133(133, "EMS0133");
	
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