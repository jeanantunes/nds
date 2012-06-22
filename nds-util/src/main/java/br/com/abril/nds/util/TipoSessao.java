package br.com.abril.nds.util;

public enum TipoSessao {
	
	A(2), 
	B(18), 
	B13(1), 
	C(6), 
	C02(1), 
	C02a(1), 
	C05(11),
	D(11), 
	E(3), E02(1), E03(1), E05(11), 
	H(2), 
	I(18), 
	M(0), 
	N(0), 
	N02(6), N03(12), N04(7), N05(8), N06(2), N07(7), N08(4), N09(13), N10(13), 
	O(5), O07(2), O08(1), O10(2), O11(2), 
	Q(0), Q02(4), Q03(4), Q04(1), Q05(2), Q07(2), Q10(2),
	R(1), R02(2), R04(2),
	S(0), S02(4), S03(4), S04(1), S05(2), S07(2), S09(2),
	T(1), T02(2), T04(2),
	U(5),
	W(0), W02(14), W17(5), W23(7),
	X(1), X03(5), X04(1), X05(1), X11(6), X18(3), X26(6),
	Z(2), Z04(2);
	
	private Integer tamanhoMaximo;
	
	private TipoSessao (Integer tamanhoMaximo) {
		
		this.tamanhoMaximo = tamanhoMaximo;
		
	}
	
	/**
	 * Retorna o tamanho maximo de parametros que uma sessão pode ter
	 * @return
	 */
	public Integer getTamanhoMaximo() {
		return this.tamanhoMaximo;
	}
}
