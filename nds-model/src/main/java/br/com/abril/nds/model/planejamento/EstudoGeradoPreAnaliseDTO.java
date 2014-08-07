package br.com.abril.nds.model.planejamento;

import br.com.abril.nds.dto.TipoSegmentoProdutoDTO;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;

public class EstudoGeradoPreAnaliseDTO {
	
	private boolean parcial;
	
	private PeriodicidadeProduto periodicidade;
	
	private Long idTipoSegmentoProduto;
	
	private String descricaoTipoSegmentoProduto;
	
	private boolean liberado;
	
	private Long idProdutoEdicao;
	
	public boolean isParcial() {
		return parcial;
	}

	public void setParcial(Boolean parcial) {
		this.parcial = parcial == null ? false : parcial;
	}

	public PeriodicidadeProduto getPeriodicidade() {
		return periodicidade;
	}

	public void setPeriodicidade(PeriodicidadeProduto periodicidade) {
		this.periodicidade = periodicidade;
	}

	public Long getIdTipoSegmentoProduto() {
		return idTipoSegmentoProduto;
	}

	public void setIdTipoSegmentoProduto(Long idTipoSegmentoProduto) {
		this.idTipoSegmentoProduto = idTipoSegmentoProduto;
	}

	public String getDescricaoTipoSegmentoProduto() {
		return descricaoTipoSegmentoProduto;
	}

	public void setDescricaoTipoSegmentoProduto(String descricaoTipoSegmentoProduto) {
		this.descricaoTipoSegmentoProduto = descricaoTipoSegmentoProduto;
	}

	public boolean isLiberado() {
		return liberado;
	}

	public void setLiberado(Boolean liberado) {
		this.liberado = liberado == null ? false : liberado;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public TipoSegmentoProdutoDTO getTipoSegmentoProduto() {

		TipoSegmentoProdutoDTO segmento = new TipoSegmentoProdutoDTO();
		
		segmento.setIdSegmento(this.idTipoSegmentoProduto);
		segmento.setDescricao(this.descricaoTipoSegmentoProduto);
		
		return segmento;
	}
}
