package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;

public class EdicoesFechadasRepositoryImplTest extends
		AbstractRepositoryImplTest {

	@Autowired
	private EdicoesFechadasRepositoryImpl edicoesFechadasRepositoryImpl;

	@Test
	public void testarObterResultadoTotalEdicoesFechadas() {

		BigDecimal totalEdicoes;

		Calendar d = Calendar.getInstance();
		Date dataDe = d.getTime();
		Date dataAte = d.getTime();

		Long idFornecedor = 1L;

		totalEdicoes = edicoesFechadasRepositoryImpl
				.obterResultadoTotalEdicoesFechadas(dataDe, dataAte,
						idFornecedor);

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

		resultadosEdicoes = edicoesFechadasRepositoryImpl
				.obterResultadoEdicoesFechadas(dataDe, dataAte, idFornecedor,
						sortorder, sortname, firstResult, maxResults);
		
		Assert.assertNotNull(resultadosEdicoes);

	}

}
