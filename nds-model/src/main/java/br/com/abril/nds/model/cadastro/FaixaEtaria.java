package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;

public enum FaixaEtaria {
	ZERO_A_QUATRO(1,"De 0 a 4 anos", new BigDecimal(5)),
	CINCO_A_NOVE(2,"De 5 a 9 anos", new BigDecimal(5)),
	DEZ_A_QUATORZE(3,"De 10 a 14 anos", new BigDecimal(5)),
	QUINZE_A_DEZENOVE(4,"De 15 à 19 anos", new BigDecimal(5)),
	VINTE_A_VINTE_E_NOVE(5,"De 20 à 29 anos", new BigDecimal(20)),
	TRINTA_A_QUARENTA_E_NOVE(6,"De 30 à 49 anos", new BigDecimal(60)),
	MAIS_DE_CINQUENTA(7,"50 anos ou mais", new BigDecimal(15));

	private Integer codigo;
	
	private String descricao;
	
	private BigDecimal porcentagemParticipacao;
	
	private FaixaEtaria(Integer codigo,String descricao,BigDecimal porcentagemParticipacao) {
		
		this.codigo = codigo; 
		this.descricao = descricao;
		this.porcentagemParticipacao = porcentagemParticipacao;
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	
	public String getDescFaixaEtaria(){
		return this.descricao;
	}

	public BigDecimal getPorcentagemParticipacao() {
		return porcentagemParticipacao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
	
}
