package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroTipoDescontoCotaDTO extends FiltroDTO implements Serializable {
	
	private static final long serialVersionUID = 2281254940257591061L;
	
	private Long desconto;
	
	private Date dataAlteracao;
	
	private String usuario;
	
	private Long idCota;
	
	private String nomeEspecifico;
	
	private Long idProduto;
	
	private Long numeroEdicao;
	
	private PaginacaoVO paginacao;
	
	
	private OrdenacaoColunaConsulta ordenacaoColuna;
	
	
	public enum OrdenacaoColunaConsulta {
		
		SEQUENCIAL("sequencial"),COTA("cota"),CODIGO("codigo");
		
		private String nomeColuna;
		
		private OrdenacaoColunaConsulta(String nomeColuna) {
			
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			
			return this.nomeColuna;
		}
	}
	
	public FiltroTipoDescontoCotaDTO() {
		
	}
	
	public FiltroTipoDescontoCotaDTO(Long desconto, Date dataAlteracao, String usuario) {
		super();
		this.desconto = desconto;
		this.dataAlteracao = dataAlteracao;
		this.usuario = usuario;		
	}

	public Long getDesconto() {
		return desconto;
	}

	public void setDesconto(Long desconto) {
		this.desconto = desconto;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public PaginacaoVO getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public OrdenacaoColunaConsulta getOrdenacaoColuna() {
		return ordenacaoColuna;
	}

	public void setOrdenacaoColuna(OrdenacaoColunaConsulta ordenacaoColuna) {
		this.ordenacaoColuna = ordenacaoColuna;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getNomeEspecifico() {
		return nomeEspecifico;
	}

	public void setNomeEspecifico(String nomeEspecifico) {
		this.nomeEspecifico = nomeEspecifico;
	}
	
}
