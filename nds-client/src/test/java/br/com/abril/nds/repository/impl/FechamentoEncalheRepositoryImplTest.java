package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.util.DateUtil;

public class FechamentoEncalheRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Test
	public void testarBuscarConferenciaEncalhe() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 9, 6);
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(dataEncalhe.getTime());
		
		List<FechamentoFisicoLogicoDTO> resultado = this.fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, "asc", "codigo", 2, 20);
		
		Assert.assertNotNull(resultado);
	}
	
//	getBoxId
	@Test
	public void testarBuscarConferenciaEncalheGetBoxId() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 9, 6);
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(dataEncalhe.getTime());
		filtro.setBoxId(1L);
		
		List<FechamentoFisicoLogicoDTO> resultado = this.fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, "asc", "codigo", 2, 20);
		
		Assert.assertNotNull(resultado);
		
	}
	
//	getForncedorId
	@Test
	public void testarBuscarConferenciaEncalheGetFornecedorId() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 9, 6);
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(dataEncalhe.getTime());
		filtro.setFornecedorId(1L);
		
		List<FechamentoFisicoLogicoDTO> resultado = this.fechamentoEncalheRepository.buscarConferenciaEncalhe(filtro, "asc", "codigo", 2, 20);
		
		Assert.assertNotNull(resultado);
		
	}
	
	@Test
	public void testarBuscaControleFechamentoEncalhe() {
		
		Boolean controleFechamento;
		
		Calendar d = Calendar.getInstance();
		Date dataEncalhe = d.getTime();
		
		controleFechamento = fechamentoEncalheRepository.buscaControleFechamentoEncalhe(dataEncalhe);
		
		Assert.assertFalse(controleFechamento);
		
	}
	
	@Test
	public void testarBuscarFechamentoEncalhe() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		List<FechamentoEncalhe> resultado = this.fechamentoEncalheRepository.buscarFechamentoEncalhe(dataEncalhe.getTime());
		
		Assert.assertNotNull(resultado);
	}
	
	@Test
	public void testarBuscarCotasAusentesSucesso() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		
		dataEncalhe.set(2012, 5, 20);
		
		List<CotaAusenteEncalheDTO> listaCotasAusentes = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(
				dataEncalhe.getTime(), "asc", "numeroCota", 0, 20);
		
		Assert.assertNotNull(listaCotasAusentes);
		
		listaCotasAusentes = 
			this.fechamentoEncalheRepository.buscarCotasAusentes(
				dataEncalhe.getTime(), "desc", "numeroCota", 20, 20);

		Assert.assertNotNull(listaCotasAusentes);
	}

	@Test
	public void testarBuscarTotalCotasAusentes() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		
		dataEncalhe.set(2012, 5, 20);
		
		int total = 
			this.fechamentoEncalheRepository.buscarTotalCotasAusentes(dataEncalhe.getTime());
		
		Assert.assertNotNull(total);
		
	}
	
	@Test
	public void testarBuscarValorTotalEncalhe() {

		Calendar dataEncalhe = Calendar.getInstance();
		
		dataEncalhe.set(2012, 1, 28);
		
		List<FechamentoFisicoLogicoDTO> listaFechamentoFisicoLogicoDTO =
			this.fechamentoEncalheRepository.buscarValorTotalEncalhe(dataEncalhe.getTime(), 13L);
		
		Assert.assertNotNull(listaFechamentoFisicoLogicoDTO);
	}
	
	@Ignore
	@Test
	public void testarSalvarControleFechamentoEncalhe() {
		
		ControleFechamentoEncalhe controleFechamentoEncalhe= new ControleFechamentoEncalhe();
		
		fechamentoEncalheRepository.salvarControleFechamentoEncalhe(controleFechamentoEncalhe);
		
	}
	
	@Test
	public void testarBuscarChamadaEncalheCota() {
		
		List<ChamadaEncalheCota> listaChamadaEncalhe;
		
		Calendar d = Calendar.getInstance();
		Date dataEncalhe = d.getTime();
		
		Long idCota = 1L;
		
		listaChamadaEncalhe = fechamentoEncalheRepository.buscarChamadaEncalheCota(dataEncalhe, idCota);
		
		Assert.assertNotNull(listaChamadaEncalhe);
	}
	
	@Test
	public void testarBuscaQuantidadeConferencia() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		int count = this.fechamentoEncalheRepository.buscaQuantidadeConferencia(dataEncalhe.getTime(), true);
		
		System.out.println(count);
		
		assert count > 0;
	}
	
	@Test
	public void testarObterChamdasEncalhePostergadas() {

		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		Date data = 
			this.fechamentoEncalheRepository.obterChamdasEncalhePostergadas(1L, dataEncalhe.getTime());
	
//		Assert.assertNotNull(data);
	}
	
	@Test
	public void testarBuscaControleFechamentoEncalhePorData() {
		
		ControleFechamentoEncalhe controleFechamentoEncalhe;
		
		Calendar d = Calendar.getInstance();
		Date dataFechamentoEncalhe = d.getTime();
		
		controleFechamentoEncalhe = fechamentoEncalheRepository.buscaControleFechamentoEncalhePorData(dataFechamentoEncalhe);
		
//		Assert.assertNotNull(controleFechamentoEncalhe);
		
	}
	
	@Test
	public void testarBuscaDataUltimoControleFechamentoEncalhe() {
		
		Date dataUltimoControle;
		
		dataUltimoControle = fechamentoEncalheRepository.buscaDataUltimoControleFechamentoEncalhe();
		
		Assert.assertNull(dataUltimoControle);
		
	}
	
	@Test
	public void testarBuscarUltimoFechamentoEncalheDia() {
		
		Date dataUltimoFechamento;
		
		Calendar d = Calendar.getInstance();
		Date dataFechamentoEncalhe = d.getTime();
		
		dataUltimoFechamento = fechamentoEncalheRepository.buscarUltimoFechamentoEncalheDia(dataFechamentoEncalhe);
		
		Assert.assertNull(dataUltimoFechamento);
		
	}	
	
	
	@Test
	public void testarBuscarAnaliticoEncalhe() {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR("28/02/2012"));

		List<AnaliticoEncalheDTO> list = fechamentoEncalheRepository.buscarAnaliticoEncalhe(filtro, "ASC", "numeroCota", 0, 15);
		Assert.assertNotNull(list);

	}
	
//	getIdBox
	@Test
	public void testarBuscarAnaliticoEncalheGetIdBox() {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR("28/02/2012"));
		filtro.setBoxId(1L);

		List<AnaliticoEncalheDTO> list = fechamentoEncalheRepository.buscarAnaliticoEncalhe(filtro, "ASC", "numeroCota", 0, 15);
		Assert.assertNotNull(list);

	}
	
//	getIdFornecedor
	@Test
	public void testarBuscarAnaliticoEncalheGetIdFornecedor() {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR("28/02/2012"));
		filtro.setFornecedorId(1L);

		List<AnaliticoEncalheDTO> list = fechamentoEncalheRepository.buscarAnaliticoEncalhe(filtro, "ASC", "numeroCota", 0, 15);
		Assert.assertNotNull(list);

	}
	
	@Test
	public void testarBuscarTotalAnaliticoEncalhe() {
		
		Integer totalAnalitico;
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR("28/02/2012"));

		totalAnalitico = fechamentoEncalheRepository.buscarTotalAnaliticoEncalhe(filtro);
		Assert.assertNotNull(totalAnalitico);
		
	}
	
//	getIdBox
	@Test
	public void testarBuscarTotalAnaliticoEncalheGetIdBox() {
		
		Integer totalAnalitico;
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR("28/02/2012"));
		filtro.setBoxId(1L);

		totalAnalitico = fechamentoEncalheRepository.buscarTotalAnaliticoEncalhe(filtro);
		Assert.assertNotNull(totalAnalitico);
		
	}
	
//	getIdFornecedor
	@Test
	public void testarBuscarTotalAnaliticoEncalheGetIdFornecedor() {
		
		Integer totalAnalitico;
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR("28/02/2012"));
		filtro.setFornecedorId(1L);

		totalAnalitico = fechamentoEncalheRepository.buscarTotalAnaliticoEncalhe(filtro);
		Assert.assertNotNull(totalAnalitico);
		
	}
	
	@Test
	public void testarBuscarCotaChamadaEncalhe() {
		
		Calendar dataEncalhe = Calendar.getInstance();
		dataEncalhe.set(2012, 1, 28);
		
		List<Cota> resultado = this.fechamentoEncalheRepository.buscarCotaChamadaEncalhe(dataEncalhe.getTime());
		
		Assert.assertNotNull(resultado);
	}

}
