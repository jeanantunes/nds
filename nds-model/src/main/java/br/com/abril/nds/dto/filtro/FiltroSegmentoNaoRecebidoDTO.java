package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * @author Infoa2 - Samuel Mendes
 * 
 *         <h1>Classe que contém todos os filtros do menu Distribuição >
 *         Segmento Não Recebido</h1>
 * 
 */

@Exportable
public class FiltroSegmentoNaoRecebidoDTO implements Serializable {

	private static final long serialVersionUID = 7114342659677545114L;

	private Long tipoSegmentoProdutoId;
	private Integer numeroCota;
	private String nomeCota;
	private String nomeSegmento;
	private Long usuarioId;
	private boolean cotasAtivas;
	private boolean autoComplete;
	
	private PaginacaoVO paginacao;

	
	public Long getTipoSegmentoProdutoId() {
		return tipoSegmentoProdutoId;
	}

	public void setTipoSegmentoProdutoId(Long tipoSegmentoProdutoId) {
		this.tipoSegmentoProdutoId = tipoSegmentoProdutoId;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public boolean isCotasAtivas() {
		return cotasAtivas;
	}

	public void setCotasAtivas(boolean cotasAtivas) {
		this.cotasAtivas = cotasAtivas;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public String getNomeSegmento() {
		return nomeSegmento;
	}

	public void setNomeSegmento(String nomeSegmento) {
		this.nomeSegmento = nomeSegmento;
	}

	public boolean isAutoComplete() {
		return autoComplete;
	}

	public void setAutoComplete(boolean autoComplete) {
		this.autoComplete = autoComplete;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (cotasAtivas ? 1231 : 1237);
		result = prime * result
				+ ((nomeCota == null) ? 0 : nomeCota.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime
				* result
				+ ((tipoSegmentoProdutoId == null) ? 0 : tipoSegmentoProdutoId
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FiltroSegmentoNaoRecebidoDTO other = (FiltroSegmentoNaoRecebidoDTO) obj;
		if (cotasAtivas != other.cotasAtivas)
			return false;
		if (nomeCota == null) {
			if (other.nomeCota != null)
				return false;
		} else if (!nomeCota.equals(other.nomeCota))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (tipoSegmentoProdutoId == null) {
			if (other.tipoSegmentoProdutoId != null)
				return false;
		} else if (!tipoSegmentoProdutoId.equals(other.tipoSegmentoProdutoId))
			return false;
		return true;
	}

}
