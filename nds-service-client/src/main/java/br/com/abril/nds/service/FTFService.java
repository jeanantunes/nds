package br.com.abril.nds.service;

import java.io.File;
import java.util.List;

import br.com.abril.nds.dto.FTFReportDTO;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.ftf.retorno.FTFRetTipoRegistro01;
import br.com.abril.nds.model.ftf.retorno.FTFRetornoRET;
import br.com.abril.nds.vo.ItemEncalheBandeiraVO;
import br.com.abril.nds.vo.NotaEncalheBandeiraVO;

public interface FTFService {
	
	List<FTFRetornoRET> processarArquivosRet(File...files);

	FTFReportDTO gerarFtf(List<NotaFiscal> notas);
	
	void atualizarRetornoFTF(List<FTFRetTipoRegistro01> list);
	
	public List<NotaEncalheBandeiraVO> obterNotasNaoEnviadas();
	
	public List<ItemEncalheBandeiraVO> obterItensNotasNaoEnviadas(Integer notaId) ;
	public void atualizaFlagInterfaceNotasEnviadas(Integer notaId,boolean flag);
	
}
