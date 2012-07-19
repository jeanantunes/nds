package br.com.abril.nds.dto.filtro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroFollowupChamadaoDTO extends FiltroDTO implements Serializable  {

	private static final long serialVersionUID = -1446823473193145083L;

	private String numeroCota;
	private String nomeJornaleiro;
	private BigDecimal valorConsignadoLimite;
	private Date dataProgramado;
	private String dataOperacao;
	private int quantidadeDiasSuspenso;
	
	private PaginacaoVO paginacao;
	
	private FollowupOrdenacaoChamadao followupordenacaoColuna;
	
	public FiltroFollowupChamadaoDTO() {}
	
	public FiltroFollowupChamadaoDTO(Date dt, int qtd,BigDecimal valor) {
		setDataOperacao(DateUtil.formatarData(dt, Constantes.DATA_FMT_PESQUISA_MYSQL));
		setQuantidadeDiasSuspenso(qtd);
		setValorConsignadoLimite(valor);
	}
	
	public enum FollowupOrdenacaoChamadao {

		COTA("cota");
		
		private String nomeColuna;
		
		private FollowupOrdenacaoChamadao(String nomeColuna) {
			this.nomeColuna = nomeColuna;
		}
		
		@Override
		public String toString() {
			return this.nomeColuna;
		}
		
		public static FollowupOrdenacaoChamadao getPorDescricao(String descricao) {
			for(FollowupOrdenacaoChamadao coluna: FollowupOrdenacaoChamadao.values()) {
				if(coluna.toString().equals(descricao))
					return coluna;
			}
			return null;
		}
	}
	
	public String getNumeroCota() {
		return this.numeroCota;
	}

	public void setNumeroCota(String numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeJornaleiro() {
		return this.nomeJornaleiro;
	}

	public void setNomeJornaleiro(String nomeJornaleiro) {
		this.nomeJornaleiro = nomeJornaleiro;
	}



	public Date getDataProgramado() {
		return this.dataProgramado;
	}

	public void setDataProgramado(Date dataProgramado) {
		this.dataProgramado = dataProgramado;
	}

	public String getDataOperacao() {
		return this.dataOperacao;
	}

	public void setDataOperacao(String dataOper ) {
		this.dataOperacao = dataOper;
	}

	public int getQuantidadeDiasSuspenso() {
		return this.quantidadeDiasSuspenso;
	}

	public void setQuantidadeDiasSuspenso(int quantidadeDiasSuspenso) {
		this.quantidadeDiasSuspenso = quantidadeDiasSuspenso;
	}


	public PaginacaoVO getPaginacao() {
		return this.paginacao;
	}

	public void setPaginacao(PaginacaoVO paginacao) {
		this.paginacao = paginacao;
	}

	public FollowupOrdenacaoChamadao getFollowupordenacaoColuna() {
		return this.followupordenacaoColuna;
	}

	public void setFollowupordenacaoColuna(
			FollowupOrdenacaoChamadao followupordenacaoColuna) {
		this.followupordenacaoColuna = followupordenacaoColuna;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public BigDecimal getValorConsignadoLimite() {
		return valorConsignadoLimite;
	}

	public void setValorConsignadoLimite(BigDecimal valorConsignadoLimite) {
		this.valorConsignadoLimite = valorConsignadoLimite;
	}
}
