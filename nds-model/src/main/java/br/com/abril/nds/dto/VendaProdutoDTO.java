package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class VendaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 2903790306949832310L;
	
	@Export(label = "Edição", alignment=Alignment.LEFT)
	private Integer numEdicao;
	
	@Export(label = "Data Lançamento", alignment=Alignment.CENTER)
	private String dataLancamento;
	
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER)
	private String dataRecolhimento;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER)
	private Long reparte;
	
	@Export(label = "Venda", alignment=Alignment.CENTER)
	private Long venda;
	
	@Export(label = "% Venda", alignment=Alignment.CENTER)
	private Long percentagemVenda;
	
	@Export(label = "Preço Capa", alignment=Alignment.CENTER)
	private Long precoCapa;
	
	@Export(label = "Total", alignment=Alignment.CENTER)
	private Long total;
	
	
	public VendaProdutoDTO() {
		
	}
	
	public VendaProdutoDTO(Integer numEdicao, String dataLancamento, String dataRecolhimento, Long reparte, Long venda,  Long percentagemVenda, Long precoCapa, Long total) {
		super();
		this.numEdicao = numEdicao;
		this.dataLancamento = dataLancamento;
		this.dataRecolhimento = dataRecolhimento;
		this.reparte = reparte;
		this.venda = venda;
		this.percentagemVenda = percentagemVenda;
		this.precoCapa = precoCapa;
		this.total = total;		
	}
	
	

	public Integer getNumEdicao() {
		return numEdicao;
	}

	public void setNumEdicao(Integer numEdicao) {
		this.numEdicao = numEdicao;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	public Long getReparte() {
		return reparte;
	}

	public void setReparte(Long reparte) {
		this.reparte = reparte;
	}

	public Long getVenda() {
		return venda;
	}

	public void setVenda(Long venda) {
		this.venda = venda;
	}

	public Long getPercentagemVenda() {
		return percentagemVenda;
	}

	public void setPercentagemVenda(Long percentagemVenda) {
		this.percentagemVenda = percentagemVenda;
	}

	public Long getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(Long precoCapa) {
		this.precoCapa = precoCapa;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
	
	
}
