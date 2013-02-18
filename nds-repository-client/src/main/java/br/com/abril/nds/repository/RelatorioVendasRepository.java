package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCDistribuidor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;

public interface RelatorioVendasRepository {

	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO);

	public ResultadoCurvaABCDistribuidor obterCurvaABCDistribuidorTotal(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO);

}
