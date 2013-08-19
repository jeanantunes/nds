package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ValidacaoLancamentoFaltaESobraFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = -747585147405542759L;

	@Export(label = "Código" , alignment = Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto" , alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Código" , alignment = Alignment.LEFT, exhibitionOrder = 3)
	private Long edicao;
	
	@Export(label = "Inconsistência" , alignment = Alignment.LEFT, exhibitionOrder = 4)
	private String inconsistencia;
	
	
	private TipoDiferenca tipoDiferenca;

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

	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public String getInconsistencia() {
		return inconsistencia;
	}

	public void setInconsistencia(TipoDiferenca inconsistencia) {
		this.inconsistencia = inconsistencia.getDescricao();
	}

	public TipoDiferenca getTipoDiferenca() {
		return tipoDiferenca;
	}

	public void setTipoDiferenca(TipoDiferenca tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	
	
}
