package br.com.abril.nds.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.service.CalendarioService.TipoPesquisaFeriado;
import br.com.abril.nds.util.export.FileExporter.FileType;

public class CalendarioServiceImplTest {

	private List<CalendarioFeriadoDTO> getListaCalendarioFeriado() {
		
		List<CalendarioFeriadoDTO> lista = new ArrayList<CalendarioFeriadoDTO>();
		
		CalendarioFeriadoDTO calendarioFeriado = null;
		
		int contador = 1;
		
		Calendar novaData = Calendar.getInstance();
		
		while(contador++<10) {
			calendarioFeriado = new CalendarioFeriadoDTO();
			novaData.clear();
			novaData.set(Calendar.DATE, contador);
			novaData.set(Calendar.MONTH, 1);
			novaData.set(Calendar.YEAR, 2012);
			calendarioFeriado.setDescricaoFeriado("feriado_" + contador);
			calendarioFeriado.setDataFeriado(novaData.getTime());
			lista.add(calendarioFeriado);
		}
		
		contador = 1;

		while(contador++<10) {
			calendarioFeriado = new CalendarioFeriadoDTO();
			novaData.clear();
			novaData.set(Calendar.DATE, contador);
			novaData.set(Calendar.MONTH, 2);
			novaData.set(Calendar.YEAR, 2012);
			calendarioFeriado.setDescricaoFeriado("feriado_" + contador);
			calendarioFeriado.setDataFeriado(novaData.getTime());
			lista.add(calendarioFeriado);
		}

		contador = 1;
		
		while(contador++<10) {
			calendarioFeriado = new CalendarioFeriadoDTO();
			novaData.clear();
			novaData.set(Calendar.DATE, contador);
			novaData.set(Calendar.MONTH, 3);
			novaData.set(Calendar.YEAR, 2012);
			calendarioFeriado.setDescricaoFeriado("feriado_" + contador);
			calendarioFeriado.setDataFeriado(novaData.getTime());
			lista.add(calendarioFeriado);
		}

		
		return lista;
	}
	
	@Test
	public void teste_obter_calendario_feriado_anual() throws IOException {
		
		Calendar c = Calendar.getInstance();
		
		c.clear();
		
		c.set(Calendar.DATE, 1);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.YEAR, 2012);
		Date dataInicial = c.getTime();
		
		c.set(Calendar.DATE, 31);
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.YEAR, 2012);
		Date dataFinal = c.getTime();

		
		CalendarioServiceImpl calendarioServiceImpl = mock(CalendarioServiceImpl.class);
		
		FeriadoRepository feriadoRepository = mock(FeriadoRepository.class);
		
		calendarioServiceImpl.feriadoRepository = feriadoRepository;
		
		when(feriadoRepository.obterListaCalendarioFeriadoPeriodo(dataInicial, dataFinal)).thenReturn(getListaCalendarioFeriado());
		
		URL urlReport = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		when(calendarioServiceImpl.obterDiretorioReports()).thenReturn(urlReport);
		
 		when(calendarioServiceImpl.obterRelatorioCalendarioFeriado(FileType.PDF, TipoPesquisaFeriado.FERIADO_ANUAL, 0, 2012)).thenCallRealMethod();
		
		byte[] relatorio = calendarioServiceImpl.obterRelatorioCalendarioFeriado(FileType.PDF, TipoPesquisaFeriado.FERIADO_ANUAL, 0, 2012);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		File arquivoDanfe = new File(url.getPath() + "/relatorio_cadastro_feriado_anual.pdf");
		
		FileOutputStream fos = new FileOutputStream(arquivoDanfe);
		
		fos.write(relatorio);
	}

		
	
	@Test
	public void teste_obter_calendario_feriado_mensal() throws IOException {
		
		CalendarioServiceImpl calendarioServiceImpl = mock(CalendarioServiceImpl.class);
		
		FeriadoRepository feriadoRepository = mock(FeriadoRepository.class);
		
		calendarioServiceImpl.feriadoRepository = feriadoRepository;
		
		when(feriadoRepository.obterListaCalendarioFeriadoMensal(1, 2012)).thenReturn(getListaCalendarioFeriado());
		
		URL urlReport = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		when(calendarioServiceImpl.obterDiretorioReports()).thenReturn(urlReport);
		
		when(calendarioServiceImpl.obterRelatorioCalendarioFeriado(FileType.PDF, TipoPesquisaFeriado.FERIADO_MENSAL, 1, 2012)).thenCallRealMethod();
		
		byte[] relatorio = calendarioServiceImpl.obterRelatorioCalendarioFeriado(FileType.PDF, TipoPesquisaFeriado.FERIADO_MENSAL, 1, 2012);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		File arquivoDanfe = new File(url.getPath() + "/relatorio_cadastro_feriado_mensal.pdf");
		
		FileOutputStream fos = new FileOutputStream(arquivoDanfe);
		
		fos.write(relatorio);
		
	}
	
	
	@Test
	public void teste_obter_calendario_feriado_anual_excel() throws IOException {
		
		Calendar c = Calendar.getInstance();
		
		c.clear();
		
		c.set(Calendar.DATE, 1);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.YEAR, 2012);
		Date dataInicial = c.getTime();
		
		c.set(Calendar.DATE, 31);
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.YEAR, 2012);
		Date dataFinal = c.getTime();

		
		CalendarioServiceImpl calendarioServiceImpl = mock(CalendarioServiceImpl.class);
		
		FeriadoRepository feriadoRepository = mock(FeriadoRepository.class);
		
		calendarioServiceImpl.feriadoRepository = feriadoRepository;
		
		when(feriadoRepository.obterListaCalendarioFeriadoPeriodo(dataInicial, dataFinal)).thenReturn(getListaCalendarioFeriado());
		
		URL urlReport = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		when(calendarioServiceImpl.obterDiretorioReports()).thenReturn(urlReport);
		
 		when(calendarioServiceImpl.obterRelatorioCalendarioFeriado(FileType.XLS, TipoPesquisaFeriado.FERIADO_ANUAL, 0, 2012)).thenCallRealMethod();
		
		byte[] relatorio = calendarioServiceImpl.obterRelatorioCalendarioFeriado(FileType.XLS, TipoPesquisaFeriado.FERIADO_ANUAL, 0, 2012);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		File arquivoDanfe = new File(url.getPath() + "/relatorio_cadastro_feriado_anual.xls");
		
		FileOutputStream fos = new FileOutputStream(arquivoDanfe);
		
		fos.write(relatorio);
	}

		
	
	@Test
	public void teste_obter_calendario_feriado_mensal_excel() throws IOException {
		
		CalendarioServiceImpl calendarioServiceImpl = mock(CalendarioServiceImpl.class);
		
		FeriadoRepository feriadoRepository = mock(FeriadoRepository.class);
		
		calendarioServiceImpl.feriadoRepository = feriadoRepository;
		
		when(feriadoRepository.obterListaCalendarioFeriadoMensal(1, 2012)).thenReturn(getListaCalendarioFeriado());
		
		URL urlReport = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		when(calendarioServiceImpl.obterDiretorioReports()).thenReturn(urlReport);
		
		when(calendarioServiceImpl.obterRelatorioCalendarioFeriado(FileType.XLS, TipoPesquisaFeriado.FERIADO_MENSAL, 1, 2012)).thenCallRealMethod();
		
		byte[] relatorio = calendarioServiceImpl.obterRelatorioCalendarioFeriado(FileType.XLS, TipoPesquisaFeriado.FERIADO_MENSAL, 1, 2012);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("reports/");
		
		File arquivoDanfe = new File(url.getPath() + "/relatorio_cadastro_feriado_mensal.xls");
		
		FileOutputStream fos = new FileOutputStream(arquivoDanfe);
		
		fos.write(relatorio);
		
	}
}
