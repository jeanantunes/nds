package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.repository.RomaneioRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class RomaneioRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private RomaneioRepository romaneioRepository;

	@Test
	public void buscarRomaneios() {
		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();
		filtro.setPaginacao(new PaginacaoVO());

		boolean limitar = false;

		List<RomaneioDTO> lista = romaneioRepository.buscarRomaneios(filtro,
				limitar);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRomaneiosPorProdutos() {
		List<Long> listaProdutos = new ArrayList<Long>();
		listaProdutos.add(1L);
		listaProdutos.add(2L);

		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();

		filtro.setProdutos(listaProdutos);

		filtro.setPaginacao(new PaginacaoVO());

		boolean limitar = false;

		List<RomaneioDTO> lista = romaneioRepository.buscarRomaneios(filtro,
				limitar);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRomaneiosPorIdBox() {
		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();
		filtro.setIdBox(1L);

		filtro.setPaginacao(new PaginacaoVO());

		boolean limitar = false;

		List<RomaneioDTO> lista = romaneioRepository.buscarRomaneios(filtro,
				limitar);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRomaneiosPorIdRoteiro() {
		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();
		filtro.setIdRoteiro(1L);

		filtro.setPaginacao(new PaginacaoVO());

		boolean limitar = false;

		List<RomaneioDTO> lista = romaneioRepository.buscarRomaneios(filtro,
				limitar);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRomaneiosPorIdRota() {
		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();
		filtro.setIdRota(1L);

		filtro.setPaginacao(new PaginacaoVO());

		boolean limitar = false;

		List<RomaneioDTO> lista = romaneioRepository.buscarRomaneios(filtro,
				limitar);

		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarRomaneiosPorData() {
		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();
		filtro.setData(Fixture.criarData(10, Calendar.MARCH, 2012));

		filtro.setPaginacao(new PaginacaoVO());

		boolean limitar = false;

		List<RomaneioDTO> lista = romaneioRepository.buscarRomaneios(filtro,
				limitar);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRomaneiosTrue() {
		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();

		filtro.setPaginacao(new PaginacaoVO());

		boolean limitar = true;

		List<RomaneioDTO> lista = romaneioRepository.buscarRomaneios(filtro,
				limitar);

		Assert.assertNotNull(lista);
	}
	
	@Test
	public void buscarTotalTrue(){
		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();
		filtro.setPaginacao(new PaginacaoVO());
		
		Integer totalRegistros = romaneioRepository.buscarTotal(filtro, true);
	}
	
	@Test
	public void buscarTotalFalse(){
		FiltroRomaneioDTO filtro = new FiltroRomaneioDTO();
		filtro.setPaginacao(new PaginacaoVO());
		
		Integer totalRegistros = romaneioRepository.buscarTotal(filtro, false);
	}
	
}
