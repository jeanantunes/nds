package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class TipoDescontoCotaDTO implements Serializable {

	private static final long serialVersionUID = -792066273850207153L;
	
	@Export(label = "Cota", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Integer numeroCota;
	
	@Export(label = "Nome", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeCota;
	
	@Export(label = "Desconto", alignment=Alignment.RIGHT, exhibitionOrder = 3)
	private Float desconto;
	
	@Export(label = "Data Alteração", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private String dataAlteracao;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 5)
	private String nomeUsuario;
	
	public TipoDescontoCotaDTO() {}

	public TipoDescontoCotaDTO(Integer numeroCota, String nomeCota,
			Float desconto, String dataAlteracao, String nomeUsuario) {
		super();
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.desconto = desconto;
		this.dataAlteracao = dataAlteracao;
		this.nomeUsuario = nomeUsuario;
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

	public Float getDesconto() {
		return desconto;
	}

	public void setDesconto(Float desconto) {
		this.desconto = desconto;
	}

	public String getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = DateUtil.formatarData(dataAlteracao, Constantes.DATE_PATTERN_PT_BR);;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
	

}
