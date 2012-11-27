package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO.ColunaOrdenacao;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class PdvRepositoryTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private PdvRepository pdvRepository;
	
	private Box box1;
	
	private Cota cotaManoel;
	
	private HistoricoTitularidadeCota historicoTitularidadeCotaManoel;
	
	@Before
	public void setup() {
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
						"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(cotaManoel);
		
		TipoLicencaMunicipal tipoLicencaMunicipal = Fixture.criarTipoLicencaMunicipal(10L,"TipoLicenca");
		save(tipoLicencaMunicipal);
		
		AreaInfluenciaPDV areaInfluenciaPDV = Fixture.criarAreaInfluenciaPDV(10L, "Area influencia");
		save(areaInfluenciaPDV);
			
		TipoPontoPDV tipoPontoPDV = Fixture.criarTipoPontoPDV(10L, "Tipo Ponto");
		save(tipoPontoPDV);
		
		TipoClusterPDV tipoClusterPDV = Fixture.criarTipoClusterPDV(10L, "Tipo Cluster");
		save(tipoClusterPDV);
		
		SegmentacaoPDV segmentacao = Fixture.criarSegmentacaoPdv(areaInfluenciaPDV, 
																TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, 
																tipoPontoPDV, tipoClusterPDV);
		
		CaracteristicasPDV caracteristicas = Fixture.criarCaracteristicaPDV(true, true, true, true, "Teste");
		
		LicencaMunicipal licencaMunicipal = Fixture.criarLicencaMunicipal("Nome Licença", "1000", tipoLicencaMunicipal );
		
		PDV pdv  = Fixture.criarPDV("PDv Teste", new BigDecimal(10), TamanhoPDV.G, cotaManoel, true, StatusPDV.ATIVO, 
									caracteristicas, licencaMunicipal, segmentacao);
		save(pdv);
		
		Endereco endereco = Fixture.criarEndereco(TipoEndereco.RESIDENCIAL, "13720-000", "logradouro", "10", "Bairro", "Mococa", "SP",1);
		save(endereco);
		
		Telefone telefone = Fixture.telefone("001", "369222", "10");
		save(telefone);
		
		EnderecoPDV enderecoPdv = Fixture.criarEnderecoPDV(endereco, pdv, true, TipoEndereco.RESIDENCIAL);
		save(enderecoPdv);
		
		TelefonePDV telefonePDV = Fixture.criarTelefonePDV(telefone, pdv, true, TipoTelefone.COMERCIAL);
		save(telefonePDV);
		
		Endereco endereco1 = Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13720-000", "logradouro", "10", "Bairro", "Mococa", "SP",1);
		save(endereco1);
		
		Telefone telefone1 = Fixture.telefone("001", "369222", "10");
		save(telefone1);
		
		EnderecoPDV enderecoPdv1 = Fixture.criarEnderecoPDV(endereco, pdv, false, TipoEndereco.COMERCIAL);
		save(enderecoPdv1);
		
		TelefonePDV telefonePDV1 = Fixture.criarTelefonePDV(telefone, pdv, false, TipoTelefone.COMERCIAL);
		save(telefonePDV1);
		
		historicoTitularidadeCotaManoel = Fixture.historicoTitularidade(cotaManoel);
		save(cotaManoel);
		
	}
	
	
	
	@Test
	public void obterQntPDVIdCota(){
		Long idCota = 1L;
		
		pdvRepository.obterQntPDV(idCota, null);
		
	}
	
	@Test
	public void obterQntPDVIdPdvIgnorar(){
		Long idPdvIgnorar = 1L;
		
		pdvRepository.obterQntPDV(null, idPdvIgnorar);
		
	}
	
	@Test
	public void existePDVPrincipalIdcota(){
		Long idCota = 1L;
		
		pdvRepository.existePDVPrincipal(idCota, null);
		
	}
	
	@Test
	public void existePDVPrincipalidPdvIgnorar(){
		Long idPdvIgnorar = 1L;
		
		pdvRepository.existePDVPrincipal(null, idPdvIgnorar);
		
	}
	
	@Test
	public void obterPDVsPorCota(){
		
		FiltroPdvDTO filtro = new FiltroPdvDTO();
		filtro.setIdCota(cotaManoel.getId());
		
		filtro.setColunaOrdenacao(ColunaOrdenacao.CONTATO);
		
		List<PdvDTO> lista = pdvRepository.obterPDVsPorCota(filtro);
		
		Assert.assertNotNull(lista);
		

	}
	
	@Test
	public void obterPDVsPorCotaPaginacao(){
		
		FiltroPdvDTO filtro = new FiltroPdvDTO();
		filtro.setIdCota(cotaManoel.getId());
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		filtro.setColunaOrdenacao(ColunaOrdenacao.CONTATO);
		
		List<PdvDTO> lista = pdvRepository.obterPDVsPorCota(filtro);
		
		Assert.assertNotNull(lista);
		
	}
	
	@Test
	public void obterPDVIdCota(){
		Long idCota = 1L;
		
		PDV pdv = pdvRepository.obterPDV(idCota, null);
		
	}
	
	@Test
	public void obterPDVIdPDV(){
		Long idPDV = 1L;
		
		PDV pdv = pdvRepository.obterPDV(null, idPDV);
		
	}
	
	@Test
	public void setarPDVPrincipalPrincipal(){
		boolean principal = true; 
		
		pdvRepository.setarPDVPrincipal(principal, null);
		
	}
	
	@Test
	public void setarPDVPrincipalIdCota(){
		boolean principal = false; 
		Long idCota = 1L; 
		
		pdvRepository.setarPDVPrincipal(principal, idCota);
		
	}
	
	
	@Test
	public void obterPDVPrincipal(){
		PDV pdv = this.pdvRepository.obterPDVPrincipal(cotaManoel.getId());

		Assert.assertNotNull(pdv);
		
		Assert.assertEquals(cotaManoel, pdv.getCota());
		
		Assert.assertTrue(pdv.getCaracteristicas().isPontoPrincipal());
		
	}
	
	@Test
	public void obterPDVPorRota(){
		
		Long idRota = 1L; 
		
		List<PDV> pdv =  pdvRepository.obterPDVPorRota(idRota);
		
		Assert.assertNotNull(pdv);
		
	}
	
	private FiltroPdvDTO criarParametrosPesquisaHistoricoTitularidade() {
	    Long idCota = cotaManoel.getId();
	    Long idHistorico = historicoTitularidadeCotaManoel.getId();
	    
	    FiltroPdvDTO filtro = new FiltroPdvDTO();
	    filtro.setIdCota(idCota);
	    filtro.setIdHistorico(idHistorico);
	    PaginacaoVO paginacao = new PaginacaoVO(1, 10, "asc");
	    filtro.setPaginacao(paginacao);
	    return filtro;
	}
	
	@Test
	public void testObterPDVsHistoricoTitularidadeOrdernacaoNome() {
	    FiltroPdvDTO filtro = criarParametrosPesquisaHistoricoTitularidade();
	    HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
	    
	    filtro.setColunaOrdenacao(ColunaOrdenacao.NOME_PDV);
	    List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
	    Assert.assertNotNull(pdvs);
	    Assert.assertTrue(pdvs.size() == 1);
	    Assert.assertEquals(expected, pdvs.get(0));
	}
	
	@Test
    public void testObterPDVsHistoricoTitularidadeOrdernacaoContato() {
        FiltroPdvDTO filtro = criarParametrosPesquisaHistoricoTitularidade();
        HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
    
        filtro.setColunaOrdenacao(ColunaOrdenacao.CONTATO);
        List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
        Assert.assertNotNull(pdvs);
        Assert.assertTrue(pdvs.size() == 1);
        Assert.assertEquals(expected, pdvs.get(0));
    }
	
	@Test
    public void testObterPDVsHistoricoTitularidadeOrdernacaoEndereco() {
        FiltroPdvDTO filtro = criarParametrosPesquisaHistoricoTitularidade();
        HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
    
        filtro.setColunaOrdenacao(ColunaOrdenacao.ENDERECO);
        List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
        Assert.assertNotNull(pdvs);
        Assert.assertTrue(pdvs.size() == 1);
        Assert.assertEquals(expected, pdvs.get(0));
    }
	
	@Test
    public void testObterPDVsHistoricoTitularidadeOrdernacaoTelefone() {
        FiltroPdvDTO filtro = criarParametrosPesquisaHistoricoTitularidade();
        HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
    
        filtro.setColunaOrdenacao(ColunaOrdenacao.TELEFONE);
        List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
        Assert.assertNotNull(pdvs);
        Assert.assertTrue(pdvs.size() == 1);
        Assert.assertEquals(expected, pdvs.get(0));
    }
	
	@Test
    public void testObterPDVsHistoricoTitularidadeOrdernacaoFaturamento() {
        FiltroPdvDTO filtro = criarParametrosPesquisaHistoricoTitularidade();
        HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
    
        filtro.setColunaOrdenacao(ColunaOrdenacao.FATURAMENTO);
        List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
        Assert.assertNotNull(pdvs);
        Assert.assertTrue(pdvs.size() == 1);
        Assert.assertEquals(expected, pdvs.get(0));
    }
	
	@Test
    public void testObterPDVsHistoricoTitularidadeOrdernacaoStatus() {
        FiltroPdvDTO filtro = criarParametrosPesquisaHistoricoTitularidade();
        HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
    
        filtro.setColunaOrdenacao(ColunaOrdenacao.STATUS);
        List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
        Assert.assertNotNull(pdvs);
        Assert.assertTrue(pdvs.size() == 1);
        Assert.assertEquals(expected, pdvs.get(0));
    }
	
	@Test
    public void testObterPDVsHistoricoTitularidadeOrdernacaoTipoPonto() {
        FiltroPdvDTO filtro = criarParametrosPesquisaHistoricoTitularidade();
        HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
    
        filtro.setColunaOrdenacao(ColunaOrdenacao.TIPO_PONTO);
        List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
        Assert.assertNotNull(pdvs);
        Assert.assertTrue(pdvs.size() == 1);
        Assert.assertEquals(expected, pdvs.get(0));
    }
	
	@Test
    public void testObterPDVsHistoricoTitularidadeOrdernacaoPrincipal() {
        FiltroPdvDTO filtro = criarParametrosPesquisaHistoricoTitularidade();
        HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
    
        filtro.setColunaOrdenacao(ColunaOrdenacao.PRINCIPAL);
        List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
        Assert.assertNotNull(pdvs);
        Assert.assertTrue(pdvs.size() == 1);
        Assert.assertEquals(expected, pdvs.get(0));
    }
	
	
	@Test
    public void testObterPDVHistoricoTitularidade() {
	    HistoricoTitularidadeCotaPDV expected = historicoTitularidadeCotaManoel.getPdvs().iterator().next();
	    
        HistoricoTitularidadeCotaPDV pdv = pdvRepository.obterPDVHistoricoTitularidade(expected.getId());
        Assert.assertNotNull(pdv);
        Assert.assertEquals(expected, pdv);
    }
	
	@Test
	public void obterPDVPorCotaEEnderecoNumCota(){
		
		Integer numCota = 1; 
		
		List<PDV> pdv =  pdvRepository.obterPDVPorCotaEEndereco(numCota, null, null, null, null);
		
		Assert.assertNotNull(pdv);
		
	}

	@Test
	public void obterPDVPorCotaEEnderecoCidade(){
		
		String cidade = "cidadeTeste"; 
		
		List<PDV> pdv =  pdvRepository.obterPDVPorCotaEEndereco(null, cidade, null, null, null);
		
		Assert.assertNotNull(pdv);
		
	}
	
	@Test
	public void obterPDVPorCotaEEnderecoUf(){
		
		String uf = "ufTeste"; 
		
		List<PDV> pdv =  pdvRepository.obterPDVPorCotaEEndereco(null, null, uf, null, null);
		
		Assert.assertNotNull(pdv);
		
	}
	
	@Test
	public void obterPDVPorCotaEEnderecoBairro(){
		
		String bairro = "bairroTeste"; 
		
		List<PDV> pdv =  pdvRepository.obterPDVPorCotaEEndereco(null, null, null, bairro, null);
		
		Assert.assertNotNull(pdv);
		
	}
	
	@Test
	public void obterPDVPorCotaEEnderecoCep(){
		
		String cep = "cepTeste"; 
		
		List<PDV> pdv =  pdvRepository.obterPDVPorCotaEEndereco(null, null, null, null, cep);
		
		Assert.assertNotNull(pdv);
		
	}
}
