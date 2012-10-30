package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaFollowupCadastroParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupCadastroParcialDTO;
import br.com.abril.nds.repository.FollowupCadastroParcialRepository;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.vo.PaginacaoVO;

public class FollowupCadastroParcialImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FollowupCadastroParcialRepository followupCadastroParcial;
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void obterConsignadosParaChamadao() {
		
		PaginacaoVO paginacao = new PaginacaoVO();
		
		paginacao.setPaginaAtual(1);
		
		paginacao.setQtdResultadosPorPagina(10);
		
		paginacao.setOrdenacao(PaginacaoVO.Ordenacao.ASC);

		paginacao.setSortColumn("numeroEdicao");
		
		FiltroFollowupCadastroParcialDTO filtro = new FiltroFollowupCadastroParcialDTO();
		
		filtro.setDataOperacao(DateUtil.formatarDataPTBR(new Date()));
		
		filtro.setPaginacao(paginacao);

		List<ConsultaFollowupCadastroParcialDTO> resultFollowUp = 
				this.followupCadastroParcial.obterConsignadosParaChamadao(filtro);
		
		Assert.assertNotNull(resultFollowUp);
	}
}
