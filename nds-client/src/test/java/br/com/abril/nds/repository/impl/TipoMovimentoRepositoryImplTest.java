package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.repository.TipoMovimentoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class TipoMovimentoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private TipoMovimentoRepository tipoMovimentoRepository;
	
	@Before
	public void setUp() {

		TipoMovimentoEstoque movEstoque = Fixture.tipoMovimentoEnvioEncalhe();
		TipoMovimentoFinanceiro movFinanceiro = Fixture.tipoMovimentoFinanceiroCompraEncalhe();
		
		save(movEstoque, movFinanceiro);
	}
	
	@Test
	public void obterTipoMovimentoPorDescricao() {
		
		FiltroTipoMovimento filtro = new FiltroTipoMovimento();
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc", "codigo"));
		filtro.setDescricao("Envio Encalhe");
		
		List<TipoMovimentoDTO> tipos =  tipoMovimentoRepository.obterTipoMovimento(filtro);
		Assert.assertTrue(tipos.size() == 1);
		
	}
		
}
