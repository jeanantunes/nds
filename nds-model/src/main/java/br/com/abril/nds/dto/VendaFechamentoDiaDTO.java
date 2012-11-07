package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class VendaFechamentoDiaDTO implements Serializable {

	private static final long serialVersionUID = 2126291233836764519L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Qtde", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private BigInteger qtde;
	
	@Export(label = "Valor R$", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private BigDecimal valor;
	
	@Export(label = "Dt. Rclto", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private String dataRecolhimento;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarData(dataRecolhimento, Constantes.DATE_PATTERN_PT_BR);
	}
	
}
