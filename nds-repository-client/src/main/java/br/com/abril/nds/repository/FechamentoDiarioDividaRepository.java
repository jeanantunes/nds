package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.fechamentodiario.DividaDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.model.fechar.dia.FechamentoDiarioDivida;
import br.com.abril.nds.vo.PaginacaoVO;

public interface FechamentoDiarioDividaRepository extends Repository<FechamentoDiarioDivida, Long>{
	
	List<DividaDTO> obterDividas(Date dataFechamento, TipoDivida tipoDivida, PaginacaoVO paginacao);
	
	Long countDividas(Date dataFechamento, TipoDivida tipoDivida);
}
