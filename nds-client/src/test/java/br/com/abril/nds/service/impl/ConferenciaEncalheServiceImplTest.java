package br.com.abril.nds.service.impl;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.abril.nds.dto.ProdutoEdicaoSlipDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;


@RunWith( MockitoJUnitRunner.class )
public class ConferenciaEncalheServiceImplTest {
	
	@Mock
	private ConferenciaEncalheServiceImpl conferenciaEncalheServiceImpl;
	
	@Mock
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	@Mock
	private DistribuidorService distribuidorService;
	
	@Mock
	private  ConferenciaEncalheRepository conferenciaEncalheRepository;
	
	@Mock
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	private String ondeGerarSlip;
	
	
	
	
	@Before
	public void setUp() throws URISyntaxException {
		
		Date dataOperacao = new Date();
		
		Whitebox.setInternalState(conferenciaEncalheServiceImpl, "controleConferenciaEncalheCotaRepository", controleConferenciaEncalheCotaRepository);
		
		Whitebox.setInternalState(conferenciaEncalheServiceImpl, "distribuidorService", distribuidorService);
		
		Whitebox.setInternalState(conferenciaEncalheServiceImpl, "conferenciaEncalheRepository", conferenciaEncalheRepository);
		
		Whitebox.setInternalState(conferenciaEncalheServiceImpl, "chamadaEncalheCotaRepository", chamadaEncalheCotaRepository);
		
		when(this.controleConferenciaEncalheCotaRepository.buscarPorId(anyLong())).thenReturn(obterControleConferenciaEncalheCota());
		
		when(this.distribuidorService.obterDataOperacaoDistribuidor()).thenReturn(dataOperacao);
		
		when(this.conferenciaEncalheRepository.obterDadosSlipConferenciaEncalhe(anyLong())).thenReturn(obterListaProdutosSlip());
		
		when(this.chamadaEncalheCotaRepository.obterReparteDaChamaEncalheCota(123, dataOperacao, false, false)).thenReturn(BigDecimal.ZERO);
		
		when(this.conferenciaEncalheServiceImpl.obterValorTotalDebitoCreditoCota(123, dataOperacao)).thenReturn(BigDecimal.TEN);
		
		when(this.conferenciaEncalheServiceImpl.gerarSlip(1L, false)).thenCallRealMethod();
		
		when(this.conferenciaEncalheServiceImpl.obterSlipReportPath())
		.thenReturn(Thread.currentThread().getContextClassLoader().getResource("reports/slip.jasper").toURI().getPath());

		when(this.conferenciaEncalheServiceImpl.obterSlipSubReportPath())
		.thenReturn(Thread.currentThread().getContextClassLoader().getResource("reports/").toURI().getPath());
		
		ondeGerarSlip = Thread.currentThread().getContextClassLoader().getResource("reports/").toURI().getPath();
		
	}

	private ControleConferenciaEncalheCota obterControleConferenciaEncalheCota(){
		
		Pessoa pessoa = new PessoaJuridica();
		((PessoaJuridica)pessoa).setNomeFantasia("JOSE DA SILVA");
		((PessoaJuridica)pessoa).setRazaoSocial("JOSE DA SILVA");
		
		ControleConferenciaEncalheCota controleConfEncCota = new ControleConferenciaEncalheCota();
		
		controleConfEncCota.setNumeroSlip(1L);
		
		controleConfEncCota.setDataFim(new Date());
		
		controleConfEncCota.setBox(new Box());
		controleConfEncCota.getBox().setCodigo(1);

		controleConfEncCota.setCota(new Cota());
		controleConfEncCota.getCota().setPessoa(new PessoaJuridica());
		
		controleConfEncCota.getCota().setNumeroCota(123);
		controleConfEncCota.getCota().setId(1L);
		controleConfEncCota.getCota().setPessoa(pessoa);
		
		return controleConfEncCota;
		
	}
	
	private List<ProdutoEdicaoSlipDTO> obterListaProdutosSlip() {
		
		List<ProdutoEdicaoSlipDTO> listaProdutoEdicaoSlipDTO = new ArrayList<ProdutoEdicaoSlipDTO>();
		
		ProdutoEdicaoSlipDTO p = null;
		
		p = new ProdutoEdicaoSlipDTO();
		p.setIdChamadaEncalhe(1L);
		p.setNomeProduto("VAIVAIVAIVAI");
		p.setEncalhe(new BigInteger("54"));
		p.setReparte(new BigInteger("35"));
		
		p.setNumeroEdicao(1L);
		p.setPrecoVenda(new BigDecimal(24.45));
		p.setValorTotal(new BigDecimal(52.65));
		
		listaProdutoEdicaoSlipDTO.add(p);

		p = new ProdutoEdicaoSlipDTO();
		p.setIdChamadaEncalhe(1L);
		p.setNomeProduto("JIKKE KITRTO");
		p.setEncalhe(new BigInteger("43"));
		p.setReparte(new BigInteger("763"));
		
		p.setNumeroEdicao(2L);
		p.setPrecoVenda(new BigDecimal(57.45));
		p.setValorTotal(new BigDecimal(87.98));

		
		listaProdutoEdicaoSlipDTO.add(p);

		p = new ProdutoEdicaoSlipDTO();
		p.setIdChamadaEncalhe(1L);
		p.setNomeProduto("NAIIE OIIKE");
		p.setEncalhe(new BigInteger("32"));
		p.setReparte(new BigInteger("65"));
		
		p.setNumeroEdicao(3L);
		p.setPrecoVenda(new BigDecimal(84.87));
		p.setValorTotal(new BigDecimal(12.92));

		
		listaProdutoEdicaoSlipDTO.add(p);
		
		return listaProdutoEdicaoSlipDTO;
		
	}
	
	
	/*@Ignore(" É necessario adicionar os arquivos de slip.jasper e " +
			"slip_subreport.jasper para execução do teste com sucesso. ")*/
	@Test
	public void test() throws IOException {
		
		byte[] relatorioBytes = this.conferenciaEncalheServiceImpl.gerarSlip(1L, false);
		
		//File relatorioSlip = new File(ondeGerarSlip + "relatorioSlip.pdf");
		File relatorioSlip = new File(ondeGerarSlip + "relatorioSlip.txt");
		
		System.out.println(ondeGerarSlip);
		
		FileUtils.writeByteArrayToFile(relatorioSlip, relatorioBytes);
		
		org.junit.Assert.assertNotNull(relatorioBytes);
		
	}
	
}
