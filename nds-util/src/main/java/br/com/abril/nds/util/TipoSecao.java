package br.com.abril.nds.util;

public enum TipoSecao {
	
	A(2, "A"), 
	B(18, "B"), 
	B13(1, "B13"), B14(6, "B14"), 
	C(6, "C"), 
	C02(1, "C02"), 
	C02a(1, "C02a"), 
	C05(11, "C05"),
	D(11, "D"), 
	E(3, "E"), E02(1, "E02"), E03(1, "E03"), E05(11, "E05"), 
	H(2, "H"), 
	I(18, "I"), 
	M(0, "M"), 
	N(0, "N"), 
	N02(6, "N02"), N03(12, "N03"), N04(7, "N04"), N05(8, "N05"), N06(2, "N06"), N07(7, "N07"), N08(4, "N08"), N09(13, "N09"), N10(13, "N10"), 
	O(5, "O"), O07(2, "O07"), O08(1, "O08"), O10(2, "O10"), O11(2, "O11"), 
	Q(0, "Q"), Q02(4, "Q02"), Q03(4, "Q03"), Q04(1, "Q04"), Q05(2, "Q05"), Q07(2, "Q07"), Q10(2, "Q10"),
	R(1, "R"), R02(2, "R02"), R04(2, "R04"),
	S(0, "S"), S02(4, "S02"), S03(4, "S03"), S04(1, "S04"), S05(2, "S05"), S07(2, "S07"), S09(2, "S09"),
	T(1, "T"), T02(2, "T02"), T04(2, "T04"),
	U(5, "U"),
	W(0, "W"), W02(14, "W02"), W17(5, "W17"), W23(7, "W23"),
	X(1, "X"), X03(5, "X03"), X04(1, "X04"), X05(1, "X05"), X11(6, "X11"), X18(3, "X18"), X26(6, "X26"),
	Z(2, "Z"), Z04(2, "Z04"),
	EMPTY(0, "");
	
	private Integer tamanhoMaximo;
	private String sigla;
	
	private TipoSecao (Integer tamanhoMaximo, String sigla) {
		this.sigla = sigla;
		this.tamanhoMaximo = tamanhoMaximo;
	}
	
	/**
	 * Retorna o tamanho maximo de parametros que uma sessão pode ter
	 * @return
	 */
	public Integer getTamanhoMaximo() {
		return this.tamanhoMaximo;
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return this.sigla;
	}
	
	
}
