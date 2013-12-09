package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;

public class EdicoesFechadasRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private EdicoesFechadasRepositoryImpl edicoesFechadasRepositoryImpl;

	@Test
	public void testarObterResultadoTotalEdicoesFechadas() {

		BigInteger totalEdicoes;

		Calendar d = Calendar.getInstance();
		Date dataDe = d.getTime();
		Date dataAte = d.getTime();

		Long idFornecedor = 1L;

		totalEdicoes = edicoesFechadasRepositoryImpl.obterResultadoTotalEdicoesFechadas(dataDe, 
						                                                                dataAte,
						                                                                idFornecedor,
						                                                                this.obterGruposMovimentoEstoqueExtratoEdicao(),
						                                                                StatusAprovacao.APROVADO);

//		Assert.assertNull(totalEdicoes);

	}

	@Test
	public void testarObterResultadoEdicoesFechadas() {

		List<RegistroEdicoesFechadasVO> resultadosEdicoes;

		Calendar d = Calendar.getInstance();
		Date dataDe = d.getTime();
		Date dataAte = d.getTime();

		Long idFornecedor = 1L;
		String sortorder = "asc";
		String sortname = "produto.nome";
		Integer firstResult = 1;
		Integer maxResults = 2;

		resultadosEdicoes = edicoesFechadasRepositoryImpl.obterResultadoEdicoesFechadas(dataDe, 
				                                                                        dataAte, 
				                                                                        idFornecedor,
				                                                                        sortorder, 
				                                                                        sortname, 
				                                                                        firstResult, 
				                                                                        maxResults,
				                                                                        this.obterGruposMovimentoEstoqueExtratoEdicao(),
				                                                                        StatusAprovacao.APROVADO);
		
		Assert.assertNotNull(resultadosEdicoes);

	}

    private List<GrupoMovimentoEstoque> obterGruposMovimentoEstoqueExtratoEdicao() {
		
		List<GrupoMovimentoEstoque> grupos = new ArrayList<GrupoMovimentoEstoque>();

		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_FORNECEDOR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DEVOLUCAO_ENCALHE);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_LANCAMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DANIFICADOS);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_FORNECEDOR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_RECOLHIMENTO);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_SUPLEMENTAR);
		grupos.add(GrupoMovimentoEstoque.TRANSFERENCIA_SAIDA_PRODUTOS_DEVOLUCAO_ENCALHE);
		
		return grupos;
	} 
}
