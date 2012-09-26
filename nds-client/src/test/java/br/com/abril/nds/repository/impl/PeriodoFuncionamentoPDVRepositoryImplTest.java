package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoClusterPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.repository.PeriodoFuncionamentoPDVRepository;

public class PeriodoFuncionamentoPDVRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private PeriodoFuncionamentoPDVRepository funcionamentoPDVRepository;
	
	private PeriodoFuncionamentoPDV periodoFuncionamento;
	private PDV pdv;

	@Before
	public void setup() {
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
				"sys.discover@gmail.com", "Manoel da Silva");
		save(manoel);
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
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
		
		pdv  = Fixture.criarPDV("PDv Teste", new BigDecimal(10), TamanhoPDV.G, cotaManoel, true, StatusPDV.ATIVO, 
									caracteristicas, licencaMunicipal, segmentacao, 1);
		save(pdv);
		
		periodoFuncionamento = Fixture.gerarPeriodoFuncionamentoPDV(new Date(),new Date(),pdv, TipoPeriodoFuncionamentoPDV.DIARIA);
		
		save(periodoFuncionamento);
		
	}
	
	@Test
	public void obterPeriodosFuncionamentoPDV(){
		
		List<PeriodoFuncionamentoPDV> periodos = funcionamentoPDVRepository.obterPeriodoFuncionamentoPDV(pdv.getId());
		
		Set<PeriodoFuncionamentoPDV> set = new HashSet<PeriodoFuncionamentoPDV>();
		set.addAll(periodos);
		
		Assert.assertTrue(!set.isEmpty());

	}
}
