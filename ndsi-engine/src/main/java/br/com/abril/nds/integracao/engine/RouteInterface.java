package br.com.abril.nds.integracao.engine;

public enum RouteInterface {
	
	EMS0106(106, "EMS0106"),
	EMS0107(107, "EMS0107"),
	EMS0108(108, "EMS0108"),
	EMS0109(109, "EMS0109"),
	EMS0110(110, "EMS0110"),
	EMS0111(111, "EMS0111"),
	EMS0112(112, "EMS0112"),
	EMS0113(113, "EMS0113"),
	EMS0114(114, "EMS0114"),
	EMS0116(116, "EMS0116"),
	EMS0117(117, "EMS0117"),
	EMS0118(118, "EMS0118"),
	EMS0119(119, "EMS0119"),
	EMS0120(120, "EMS0120"),
	EMS0121(121, "EMS0121"),
	EMS0122(122, "EMS0122"),
	EMS0123(121, "EMS0123"),
	EMS0124(124, "EMS0124"),
	EMS0125(125, "EMS0125"),
	EMS0126(126, "EMS0126"),
	EMS0127(127, "EMS0127"),
	EMS0129(129, "EMS0129"),
	EMS0130(130, "EMS0130"),
	EMS0131(131, "EMS0131"), 
	EMS0132(132, "EMS0132"),
	EMS0133(133, "EMS0133"),
	EMS0185(185, "EMS0185"),
	EMS0197(197, "EMS0197"),
	EMS0135(135, "EMS0135");
	
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