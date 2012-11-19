package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.EnderecoPDVRepository;

public class EnderecoPdvRepositoryTest extends AbstractRepositoryImplTest {

	@Autowired
	private EnderecoPDVRepository enderecoPDVRepository;
	
	private Box box1;
	
	private Cota cotaManoel;
	
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
		
		EnderecoPDV enderecoPdv = Fixture.criarEnderecoPDV(endereco, pdv, true, TipoEndereco.RESIDENCIAL);
		save(enderecoPdv);
		
		Endereco endereco1 = Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13720-000", "logradouro", "10", "Bairro", "Mococa", "SP",1);
		save(endereco1);
		
		EnderecoPDV enderecoPdv1 = Fixture.criarEnderecoPDV(endereco, pdv, false, TipoEndereco.COMERCIAL);
		save(enderecoPdv1);
		
		Endereco endereco2 = Fixture.criarEndereco(TipoEndereco.COMERCIAL, "13720-000", "logradouro", "10", "Bairro", "São Paulo", "SP",1);
		save(endereco2);
		
		EnderecoPDV enderecoPdv2 = Fixture.criarEnderecoPDV(endereco, pdv, false, TipoEndereco.COMERCIAL);
		save(enderecoPdv2);
	}
		
	@Test
	public void obterMunicipiosPdvPrincipal(){
		
		List<ItemDTO<Integer, String>> endereco = enderecoPDVRepository.buscarMunicipioPdvPrincipal();
		
		Assert.assertNotNull(endereco);
	}
	
	
}
