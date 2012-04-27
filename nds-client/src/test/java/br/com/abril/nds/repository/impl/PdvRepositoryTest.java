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
import br.com.abril.nds.model.cadastro.pdv.ClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.PdvRepository;

public class PdvRepositoryTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private PdvRepository pdvRepository;
	
	private Box box1;
	
	private Cota cotaManoel;
	
	@Before
	public void setup() {
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
						"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(cotaManoel);
		
		TipoLicencaMunicipal tipoLicencaMunicipal = Fixture.criarTipoLicencaMunicipal(10L,"TipoLicenca");
		save(tipoLicencaMunicipal);
		
		AreaInfluenciaPDV areaInfluenciaPDV = Fixture.criarAreaInfluenciaPDV(10L, "Area influencia");
		save(areaInfluenciaPDV);
		
		ClusterPDV clusterPDV = Fixture.criarClusterPDV(10L, "Cluster X");
		save(clusterPDV);
		
		TipoPontoPDV tipoPontoPDV = Fixture.criarTipoPontoPDV(10L, "Tipo Ponto");
		save(tipoPontoPDV);
		
		TipoClusterPDV tipoClusterPDV = Fixture.criarTipoClusterPDV(10L, "Tipo Cluster");
		save(tipoClusterPDV);
		
		SegmentacaoPDV segmentacao = Fixture.criarSegmentacaoPdv(areaInfluenciaPDV, clusterPDV, 
																TipoCaracteristicaSegmentacaoPDV.ALTERNATIVO, 
																tipoPontoPDV, tipoClusterPDV);
		
		CaracteristicasPDV caracteristicas = Fixture.criarCaracteristicaPDV(true, true, true, true, "Teste");
		
		LicencaMunicipal licencaMunicipal = Fixture.criarLicencaMunicipal("Nome Licença", "1000", tipoLicencaMunicipal );
		
		PDV pdv  = Fixture.criarPDV("PDv Teste", new BigDecimal(10), TamanhoPDV.G, cotaManoel, true, StatusPDV.ATIVO, 
									caracteristicas, licencaMunicipal, segmentacao);
		save(pdv);
		
		Endereco endereco = Fixture.criarEndereco(TipoEndereco.RESIDENCIAL, "13720-000", "logradouro", 10, "Bairro", "Mococa", "SP");
		save(endereco);
		
		Telefone telefone = Fixture.telefone("001", "369222", "10");
		save(telefone);
		
		EnderecoPDV enderecoPdv = Fixture.criarEnderecoPDV(endereco, pdv, true, TipoEndereco.RESIDENCIAL);
		save(enderecoPdv);
		
		TelefonePDV telefonePDV = Fixture.criarTelefonePDV(telefone, pdv, true, TipoTelefone.COMERCIAL);
		save(telefonePDV);
		
		Endereco endereco1 = Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13720-000", "logradouro", 10, "Bairro", "Mococa", "SP");
		save(endereco1);
		
		Telefone telefone1 = Fixture.telefone("001", "369222", "10");
		save(telefone1);
		
		EnderecoPDV enderecoPdv1 = Fixture.criarEnderecoPDV(endereco, pdv, false, TipoEndereco.COMERCIAL);
		save(enderecoPdv1);
		
		TelefonePDV telefonePDV1 = Fixture.criarTelefonePDV(telefone, pdv, false, TipoTelefone.COMERCIAL);
		save(telefonePDV1);
		
	}
	
	@Test
	public void obterPDVsPorCota(){
		
		FiltroPdvDTO filtro = new FiltroPdvDTO();
		filtro.setIdCota(cotaManoel.getId());
		
		filtro.setColunaOrdenacao(ColunaOrdenacao.CONTATO);
		
		List<PdvDTO> lista = pdvRepository.obterPDVsPorCota(filtro);
		
		Assert.assertNotNull(lista);
		
		Assert.assertTrue(!lista.isEmpty());

	}
	
	
}
