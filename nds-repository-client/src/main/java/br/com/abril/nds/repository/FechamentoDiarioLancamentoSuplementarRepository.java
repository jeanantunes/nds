package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoSuplementar;
import br.com.abril.nds.vo.PaginacaoVO;

public interface FechamentoDiarioLancamentoSuplementarRepository extends Repository<FechamentoDiarioLancamentoSuplementar, Long> {
	
	Long countDadosGridSuplementar(Date dataFechamento);
	
	List<SuplementarFecharDiaDTO> obterDadosGridSuplementar(Date dataFechamento, PaginacaoVO paginacao);
}
