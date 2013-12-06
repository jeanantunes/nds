package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioMovimentoVendaEncalhe;
import br.com.abril.nds.vo.PaginacaoVO;

public interface FechamentoDiarioMovimentoVendaEncalheRepository extends Repository<FechamentoDiarioMovimentoVendaEncalhe, Long>	{
	
	Long countDadosVendaEncalhe(Date dataFechamento);
	
	List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataFechamento, PaginacaoVO paginacao);
}
