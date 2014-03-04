package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalReferenciada;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro00;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro01;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro02;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro06;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro08;
import br.com.abril.nds.model.ftf.envio.FTFEnvTipoRegistro09;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01;

public interface FTFRepository {

	List<FTFEnvTipoRegistro01> obterResgistroTipo01(List<NotaFiscal> notas, long idTipoNotaFiscal);
	
	List<FTFEnvTipoRegistro02> obterResgistroTipo02(Long idNF, long idTipoNotaFiscal);
	
	FTFEnvTipoRegistro00 obterRegistroTipo00(long idTipoNotaFiscal);
	
	void atualizarRetornoFTF(List<FTFRetTipoRegistro01> list);

	FTFEnvTipoRegistro09 obterRegistroTipo09(long idTipoNotaFiscal);
	
	FTFEnvTipoRegistro06 obterRegistroTipo06(NotaFiscalReferenciada nota);
	
	FTFEnvTipoRegistro06 obterRegistroTipo06(Long idNF);
	
	FTFEnvTipoRegistro08 obterRegistroTipo08(long idTipoNotaFiscal);
}
