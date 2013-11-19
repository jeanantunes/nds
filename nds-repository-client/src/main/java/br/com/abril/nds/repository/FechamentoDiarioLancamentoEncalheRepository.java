package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoEncalhe;
import br.com.abril.nds.vo.PaginacaoVO;

public interface FechamentoDiarioLancamentoEncalheRepository extends Repository<FechamentoDiarioLancamentoEncalhe, Long> {
	
	List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date dataFechamento,PaginacaoVO paginacao);
	
	Long countDadosGridEncalhe(Date dataFechamento);
}
