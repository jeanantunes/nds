package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
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

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;

		if (object == null)
			return false;

		if (this.getClass() != object.getClass())
			return false;

		FiltroSegmentoNaoRecebidoDTO other = (FiltroSegmentoNaoRecebidoDTO) object;

		String otherNomeCota = "";
		String nomeCota = "";

		if (other.getNomeCota() != null) {
			otherNomeCota = other.getNomeCota();
		}

		if (this.getNomeCota() != null) {
			nomeCota = this.getNomeCota();
		}

		if (this.getNumeroCota() == other.getNumeroCota()
				&& nomeCota.equals(otherNomeCota)
				&& this.getTipoSegmentoProdutoId() == other
						.getTipoSegmentoProdutoId()
				&& this.isCotasAtivas() == other.isCotasAtivas()) {
			return true;
		} else {
			return false;
		}
	}

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

}
