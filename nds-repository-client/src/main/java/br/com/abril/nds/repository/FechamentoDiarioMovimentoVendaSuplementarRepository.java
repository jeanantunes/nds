package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaSuplementar;
import br.com.abril.nds.vo.PaginacaoVO;

public interface FechamentoDiarioMovimentoVendaSuplementarRepository extends Repository<FechamentoDiarioMovimentoVendaSuplementar, Long>{
	
	List<VendaFechamentoDiaDTO> obterDadosVendaSuplementar(Date dataFechamento, PaginacaoVO paginacao);
	
	Long countDadosVendaSuplementar(Date dataFechamento);
}
