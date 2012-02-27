package br.com.abril.nds.model.movimentacao;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "TIPO_MOVIMENTO")
public class TipoMovimento {

	@Id
	private Long id;
	private String descricao;
	private boolean aprovacaoAutomatica;
	private boolean incideDivida;
	
	@Enumerated(EnumType.STRING)
	private TipoMovimentoEstoque tipoMovimentoEstoque;
	
	@Enumerated(EnumType.STRING)
	private TipoOperacao tipoOperacao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public boolean isAprovacaoAutomatica() {
		return aprovacaoAutomatica;
	}
	
	public void setAprovacaoAutomatica(boolean aprovacaoAutomatica) {
		this.aprovacaoAutomatica = aprovacaoAutomatica;
	}
	
	public boolean isIncideDivida() {
		return incideDivida;
	}
	
	public void setIncideDivida(boolean incideDivida) {
		this.incideDivida = incideDivida;
	}
	
	public TipoMovimentoEstoque getTipoMovimentoEstoque() {
		return tipoMovimentoEstoque;
	}
	
	public void setTipoMovimentoEstoque(TipoMovimentoEstoque tipoMovimentoEstoque) {
		
		if(tipoMovimentoEstoque != null){
			this.tipoOperacao = tipoMovimentoEstoque.getTipoOperacao();
		}
		
		this.tipoMovimentoEstoque = tipoMovimentoEstoque;
		
	}

	public TipoOperacao getTipoOperacao() {
		
		if(this.tipoMovimentoEstoque == null){
			return null;
		}
		
		return tipoMovimentoEstoque.getTipoOperacao();
	}

	
}