package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.repository.AlteracaoCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class AlteracaoCotaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private AlteracaoCotaRepository alteracaoCotaRepository;
	
	@Test
	public void testarobterCobrancasPorIDS() {
		
		PaginacaoVO paginacao = new PaginacaoVO(1, 1, "cota.numeroCota");
		paginacao.setOrdenacao(paginacao.getOrdenacao().ASC);
		
		FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO = new FiltroAlteracaoCotaDTO();
		filtroAlteracaoCotaDTO.setIdFornecedor(1l);
		filtroAlteracaoCotaDTO.setPaginacao(paginacao);
		
		List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota = this.alteracaoCotaRepository.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);
		
		Assert.assertNotNull(pesquisarAlteracaoCota);
	}
	
}
