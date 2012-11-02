package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RegistroCurvaABCExportacaoDistribuidorVO {
	
	private RegistroCurvaABCDistribuidorVO registroCurvaABCDistribuidorVO;
	
	public RegistroCurvaABCExportacaoDistribuidorVO() {}
	
	public RegistroCurvaABCExportacaoDistribuidorVO(RegistroCurvaABCDistribuidorVO registroCurvaABCDistribuidorVO) {
		
		this.registroCurvaABCDistribuidorVO = registroCurvaABCDistribuidorVO;
	}
	
	@Export(label = "RK. Cota", exhibitionOrder = 2)
	public Long getRkCota(){
		return registroCurvaABCDistribuidorVO.getRkCota();
	}
	
	@Export(label = "Número", exhibitionOrder = 3)
	public String getNumeroCota(){
		return registroCurvaABCDistribuidorVO.getNumeroCotaString();
	}
	
	@Export(label = "Cota", exhibitionOrder = 4)
	public String getNomeCota(){
		return registroCurvaABCDistribuidorVO.getNomeCota();
	}

	@Export(label = "Municipio", exhibitionOrder = 5)
	public String getMunicipio(){
		return registroCurvaABCDistribuidorVO.getMunicipio();
	}

	@Export(label = "Quantidade de Pdvs", exhibitionOrder = 6)
	public String getQuantidadePdvs(){
		return registroCurvaABCDistribuidorVO.getQuantidadePdvsString();
	}
	
	@Export(label = "Venda de Exemplares", exhibitionOrder = 7)
	public String getVendaExemplaresFormatado(){
		return registroCurvaABCDistribuidorVO.getVendaExemplaresFormatado();
	}
	
	@Export(label = "Faturamento da Capa", exhibitionOrder = 8)
	public String getFaturamentoCapaFormatado(){
		return registroCurvaABCDistribuidorVO.getFaturamentoCapaFormatado();
	}

	@Export(label = "Participação", exhibitionOrder = 9)
	public String getParticipacao(){
		return registroCurvaABCDistribuidorVO.getParticipacaoFormatado();
	}

	@Export(label = "Participação Acumulada", exhibitionOrder = 10)
	public String getParticipacaoAcumulada(){
		return registroCurvaABCDistribuidorVO.getParticipacaoAcumuladaFormatado();
	}
}
