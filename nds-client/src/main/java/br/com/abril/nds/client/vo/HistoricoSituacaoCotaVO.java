package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;

/**
 * Value Object de histórico de situação da cota.
 * 
 * @author Discover Technology
 *
 */
public class HistoricoSituacaoCotaVO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4573189935810707316L;
	
	private Date data;

	private SituacaoCadastro statusAnterior;
	
	private SituacaoCadastro statusAtualizado;
	
	private String usuario;
	
	private MotivoAlteracaoSituacao motivo;
	
	private String descricao;
	
	/**
	 * Construtor padrão.
	 */
	public HistoricoSituacaoCotaVO() {
		
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return the statusAnterior
	 */
	public SituacaoCadastro getStatusAnterior() {
		return statusAnterior;
	}

	/**
	 * @param statusAnterior the statusAnterior to set
	 */
	public void setStatusAnterior(SituacaoCadastro statusAnterior) {
		this.statusAnterior = statusAnterior;
	}

	/**
	 * @return the statusAtualizado
	 */
	public SituacaoCadastro getStatusAtualizado() {
		return statusAtualizado;
	}

	/**
	 * @param statusAtualizado the statusAtualizado to set
	 */
	public void setStatusAtualizado(SituacaoCadastro statusAtualizado) {
		this.statusAtualizado = statusAtualizado;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the motivo
	 */
	public MotivoAlteracaoSituacao getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(MotivoAlteracaoSituacao motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((motivo == null) ? 0 : motivo.hashCode());
		result = prime * result
				+ ((statusAnterior == null) ? 0 : statusAnterior.hashCode());
		result = prime
				* result
				+ ((statusAtualizado == null) ? 0 : statusAtualizado.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		HistoricoSituacaoCotaVO other = (HistoricoSituacaoCotaVO) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (motivo != other.motivo)
			return false;
		if (statusAnterior != other.statusAnterior)
			return false;
		if (statusAtualizado != other.statusAtualizado)
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
}
