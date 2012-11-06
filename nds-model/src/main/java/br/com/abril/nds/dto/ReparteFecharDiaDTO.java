package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ReparteFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 2126291233836764519L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Preço Capa", alignment=Alignment.LEFT, exhibitionOrder = 4)
	private BigDecimal precoVenda;
	
	private BigDecimal sobras;
	
	private BigDecimal faltas;
	
	private BigDecimal transferencias;
	
	private BigDecimal distribuidos;
	
	private BigDecimal valorTotalReparte;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private Long qtdReparte;
	
	@Export(label = "Sobras", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private Long qtdSobras;
	
	@Export(label = "Faltas", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private Long qtdFaltas;
	
	@Export(label = "Transf.", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private Long qtdTransferido;
	
	@Export(label = "A Distr.", alignment=Alignment.CENTER, exhibitionOrder = 9)
	private Integer qtdADistribuir;
	
	@Export(label = "Distribuido", alignment=Alignment.CENTER, exhibitionOrder = 10)
	private Long qtdDistribuido;
	
	@Export(label = "Sobra Distr.", alignment=Alignment.LEFT, exhibitionOrder = 11)
	private Integer qtdSobraDiferenca;
	
	@Export(label = "Diferença", alignment=Alignment.CENTER, exhibitionOrder = 12)
	private Integer qtdDiferenca;
	
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

	public BigDecimal getValorTotalReparte() {
		return valorTotalReparte;
	}

	public void setValorTotalReparte(BigDecimal valorTotalReparte) {
		this.valorTotalReparte = valorTotalReparte;
	}
	
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public BigDecimal getSobras() {
		return sobras;
	}

	public void setSobras(BigDecimal sobras) {
		this.sobras = sobras;
	}

	public BigDecimal getFaltas() {
		return faltas;
	}

	public void setFaltas(BigDecimal faltas) {
		this.faltas = faltas;
	}

	public BigDecimal getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(BigDecimal transferencias) {
		this.transferencias = transferencias;
	}

	public BigDecimal getDistribuidos() {
		return distribuidos;
	}

	public void setDistribuidos(BigDecimal distribuidos) {
		this.distribuidos = distribuidos;
	}

	public Long getQtdReparte() {
		return qtdReparte;
	}

	public void setQtdReparte(Long qtdReparte) {
		this.qtdReparte = qtdReparte;
	}

	public Long getQtdSobras() {
		return qtdSobras;
	}

	public void setQtdSobras(Long qtdSobras) {
		this.qtdSobras = qtdSobras;
	}

	public Long getQtdFaltas() {
		return qtdFaltas;
	}

	public void setQtdFaltas(Long qtdFaltas) {
		this.qtdFaltas = qtdFaltas;
	}

	public Long getQtdTransferido() {
		return qtdTransferido;
	}

	public void setQtdTransferido(Long qtdTransferido) {
		this.qtdTransferido = qtdTransferido;
	}

	public Integer getQtdADistribuir() {
		return qtdADistribuir;
	}

	public void setQtdADistribuir(Integer qtdADistribuir) {
		this.qtdADistribuir = qtdADistribuir;
	}

	public Long getQtdDistribuido() {
		return qtdDistribuido;
	}

	public void setQtdDistribuido(Long qtdDistribuido) {
		this.qtdDistribuido = qtdDistribuido;
	}

	public Integer getQtdSobraDiferenca() {
		return qtdSobraDiferenca;
	}

	public void setQtdSobraDiferenca(Integer qtdSobraDiferenca) {
		this.qtdSobraDiferenca = qtdSobraDiferenca;
	}

	public Integer getQtdDiferenca() {
		return qtdDiferenca;
	}

	public void setQtdDiferenca(Integer qtdDiferenca) {
		this.qtdDiferenca = qtdDiferenca;
	}
	
}
