package br.com.abril.nds.service;

import java.io.File;
import java.util.List;

import br.com.abril.nds.dto.FTFReportDTO;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01;
import br.com.abril.nds.model.ftf.retorno.FTFRetornoRET;

public interface FTFService {
	
	List<FTFRetornoRET> processarArquivosRet(File...files);

	FTFReportDTO gerarFtf(List<NotaFiscal> notas);
	
	void atualizarRetornoFTF(List<FTFRetTipoRegistro01> list);
	
}
