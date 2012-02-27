package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.vo.estoque.DetalheNotaFiscalVO;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalDTO;

public interface NotaFiscalRepository extends Repository<NotaFiscal, Long> {

	Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
		
	List<NotaFiscal> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalDTO filtroConsultaNotaFiscal);
	
	List<DetalheNotaFiscalVO> obterDetalhesNotaFical(Long idNotaFiscal);
}
