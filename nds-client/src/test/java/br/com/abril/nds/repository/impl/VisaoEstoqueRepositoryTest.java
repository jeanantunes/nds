package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.VisaoEstoqueDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.repository.VisaoEstoqueRepository;

public class VisaoEstoqueRepositoryTest extends AbstractRepositoryImplTest {

	@Autowired
	private VisaoEstoqueRepository visaoEstoqueRepository;
	
	@Test
	public void obterDetalhe() {
		
		FiltroConsultaVisaoEstoque filtro = new FiltroConsultaVisaoEstoque();
		filtro.setTipoEstoque(TipoEstoque.LANCAMENTO.toString());
		filtro.setIdFornecedor(1L);
		//filtro.setDataMovimentacao(new Date());
		
		List<VisaoEstoqueDetalheDTO> list = visaoEstoqueRepository.obterVisaoEstoqueDetalhe(filtro);
		
		Assert.assertTrue(list != null);
	}
}
