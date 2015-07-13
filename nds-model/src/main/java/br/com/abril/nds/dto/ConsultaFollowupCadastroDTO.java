package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.ColumnType;
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
	
	@Export(label = "Responsável", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String responsavel;
	
	@Export(label = "Documento", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private String tipo;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 5, columnType=ColumnType.MOEDA)
	private BigDecimal valor = BigDecimal.ZERO;
	
	@Export(label = "Dt. Vencto.", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String dataVencimento;
	
	public ConsultaFollowupCadastroDTO() {}
	
	public ConsultaFollowupCadastroDTO(Integer numeroCota,
			String nomeJornaleiro, String tipo, BigDecimal valor,
			String dataVencimento, String responsavel) {
		super();
		this.numeroCota = numeroCota;
		this.nomeJornaleiro = nomeJornaleiro;
		this.tipo = tipo;
		this.valor = valor;
		this.dataVencimento = dataVencimento;
		this.responsavel = responsavel;
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento =  DateUtil.formatarData(dataVencimento, Constantes.DATE_PATTERN_PT_BR) ;	
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}
	
}
