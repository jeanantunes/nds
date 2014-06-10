package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioLancamentoReparte;
import br.com.abril.nds.vo.PaginacaoVO;

public interface FechamentoDiarioLancamentoReparteRepository extends Repository<FechamentoDiarioLancamentoReparte, Long>{
	
	Long countLancametosReparte(Date data);
	
	List<ReparteFecharDiaDTO> obterLancametosReparte(Date data, PaginacaoVO paginacao);
	
}
