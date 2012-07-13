package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Calendar;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaFollowupCadastroDTO implements Serializable {

	private static final long serialVersionUID = -7544892338598259055L;

	@Export(label = "Cota", alignment=Alignment.CENTER, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String nomeJornaleiro;
	
	@Export(label = "Documento", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String tipo;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private Double valor;
	
	@Export(label = "Dt. Vencto.", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private String dataVencimento;
	
	public ConsultaFollowupCadastroDTO() {}
	
	public ConsultaFollowupCadastroDTO(Integer numeroCota,
			String nomeJornaleiro, String tipo, Double valor,
			String dataVencimento) {
		super();
		this.numeroCota = numeroCota;
		this.nomeJornaleiro = nomeJornaleiro;
		this.tipo = tipo;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
	}



	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeJornaleiro() {
		return nomeJornaleiro;
	}

	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Calendar dataVencimento) {
		this.dataVencimento =  DateUtil.formatarData(dataVencimento.getTime(), Constantes.DATE_PATTERN_PT_BR) ;	
	}

	
}
