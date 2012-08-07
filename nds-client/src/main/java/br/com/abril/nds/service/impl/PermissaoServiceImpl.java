package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.client.vo.ResultadoPermissaoVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaPermissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.PermissaoService;

/**
 * @author InfoA2
 */
@Service
public class PermissaoServiceImpl implements PermissaoService {

	@Override
	public List<ResultadoPermissaoVO> buscarResultado(FiltroConsultaPermissaoDTO filtro) {
		List<Permissao> permissoes = Arrays.asList(Permissao.values());

		List<ResultadoPermissaoVO> resultado = new ArrayList<ResultadoPermissaoVO>();
		
		ResultadoPermissaoVO permissaoVO = null;
		for (Permissao p : permissoes) {
			
			if ( ( filtro.getNome().isEmpty() || p.name().toLowerCase().contains(filtro.getNome().toLowerCase()) ) &&
				   (filtro.getDescricao().isEmpty() || p.getDescricao().toLowerCase().contains(filtro.getDescricao().toLowerCase()) ) ) {
					permissaoVO = new ResultadoPermissaoVO();
					permissaoVO.setDescricao(p.getDescricao());
					permissaoVO.setNome(p.name());
					resultado.add(permissaoVO);
			}
			
		}
		
		return resultado;
	}

	@Override
	public List<Permissao> buscar() {
		return new LinkedList<Permissao>(Arrays.asList(Permissao.values()));
	}
	
}
