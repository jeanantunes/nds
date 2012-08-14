package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.client.vo.ResultadoGrupoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaGrupoDTO;
import br.com.abril.nds.model.seguranca.GrupoPermissao;

public interface GrupoPermissaoRepository extends Repository<GrupoPermissao,Long>{
	public List<ResultadoGrupoVO> buscaFiltrada(FiltroConsultaGrupoDTO filtro);
}