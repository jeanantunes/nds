package br.com.abril.nds.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.UnidadeFederacao;
import br.com.abril.nds.repository.FeriadoRepository;
import br.com.abril.nds.repository.LocalidadeRepository;
import br.com.abril.nds.repository.UnidadeFederacaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes
 * funcionalidades de calendário.
 * 
 * @author Discover Technology
 */
@Service
public class CalendarioServiceImpl implements CalendarioService {

	@Autowired
	private FeriadoRepository feriadoRepository;
	
	@Autowired
	private LocalidadeRepository localidadeRepository;
	
	@Autowired
	private UnidadeFederacaoRepository unidadeFederacaoRepository;
	
	
	@Override
	@Transactional(readOnly=true)
	public Date adicionarDiasUteis(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		if (numDias == 0) {
			
			// Verifica se o dia informado é util.
			// Caso não seja, incrementa até encontrar o primeiro dia útil.
			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
			}
			
		} else {
			
			// Adiciona o número de dias úteis informado.
			for (int i = 0; i < numDias; i++) {
				
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
				
				while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
					cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
				}
			}
		}
		
		return cal.getTime();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Date adicionarDiasRetornarDiaUtil(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		if (numDias == 0) {
			
			// Verifica se o dia informado é util.
			// Caso não seja, incrementa até encontrar o primeiro dia útil.
			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));
			}
			
		} else {
			
			// Adiciona o número de dias úteis informado.
			for (int i = 0; i < numDias; i++) {
				
				cal.setTime(DateUtil.adicionarDias(cal.getTime(), 1));								
			}
		}
		
		return cal.getTime();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Date subtrairDiasUteis(Date data, int numDias) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		for (int i = 0; i < numDias; i++) {
			
			cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
			
			while (DateUtil.isSabadoDomingo(cal) || isFeriado(cal)) {
				cal.setTime(DateUtil.subtrairDias(cal.getTime(), 1));
			}
		}
		
		return cal.getTime();
	}
	
	@Transactional
	public boolean isDiaUtil(Date data) {
		
		if (data == null) {
			
			return false;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		return !(DateUtil.isSabadoDomingo(cal) || isFeriado(cal));
	}
	
	@Override
	public Date adicionarDiasUteis(Date data, int numDias, List<Integer> diasSemanaConcentracaoCobranca, Integer diaMesConcentracaoCobranca) {
		
		if (diasSemanaConcentracaoCobranca == null || diasSemanaConcentracaoCobranca.isEmpty() && (diasSemanaConcentracaoCobranca == null)){
			
			return this.adicionarDiasUteis(data, numDias);
		}
		
		if (diasSemanaConcentracaoCobranca != null && !diasSemanaConcentracaoCobranca.isEmpty()){
			
			Calendar dataBase = Calendar.getInstance();
			dataBase.setTime(data);
			dataBase.add(Calendar.DAY_OF_MONTH, numDias);
			
			boolean dataValida = false;
			
			while (!dataValida){
				while (!diasSemanaConcentracaoCobranca.contains(dataBase.get(Calendar.DAY_OF_WEEK))){
					dataBase.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				dataBase.setTime(this.adicionarDiasUteis(dataBase.getTime(), 0));
				
				dataValida = diasSemanaConcentracaoCobranca.contains(dataBase.get(Calendar.DAY_OF_WEEK));
			}
			
			return dataBase.getTime();
		} else if (diaMesConcentracaoCobranca != null){
			
			if (Calendar.getInstance().getLeastMaximum(Calendar.DAY_OF_MONTH) > diaMesConcentracaoCobranca){
				
				diaMesConcentracaoCobranca = Calendar.getInstance().getLeastMaximum(Calendar.DAY_OF_MONTH);
			}
			
			Calendar dataVencimento = Calendar.getInstance();
			
			while (dataVencimento.get(Calendar.DAY_OF_MONTH) < diaMesConcentracaoCobranca){
				
				dataVencimento.setTime(this.adicionarDiasUteis(dataVencimento.getTime(), 1));
			}
			
			return dataVencimento.getTime();
		}
		
		return Calendar.getInstance().getTime();
	}
	
	private boolean isFeriado(Calendar cal) {
		
		Feriado feriado = null;
		
		if (cal != null) {
			
			List<Feriado> feriados = feriadoRepository.obterFeriados(cal.getTime(), TipoFeriado.FEDERAL , null, null);
			
			if(feriados == null || feriados.isEmpty()) {
				return false;
			}
			
			feriado = feriados.get(0);
		}
		
		return (feriado != null) ? true : false;
	}
	
	private void validarCadastroFeriado(CalendarioFeriadoDTO calendarioFeriado) {
		
		Date data = calendarioFeriado.getDataFeriado();
		String descricao = calendarioFeriado.getDescricaoFeriado();
		
		if(data == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data do feriado não foi informada");
		}
		
		if(descricao == null || descricao.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Descrição do feriado não foi informada");
		}
		
		TipoFeriado tipoFeriado = calendarioFeriado.getTipoFeriado();
		
		if(TipoFeriado.FEDERAL.equals(tipoFeriado)){
			calendarioFeriado.setUfSigla(null);
			calendarioFeriado.setIdLocalidade(null);
		}
		
		if(TipoFeriado.ESTADUAL.equals(tipoFeriado)){
			calendarioFeriado.setIdLocalidade(null);
		}

		if(TipoFeriado.MUNICIPAL.equals(tipoFeriado)){
			calendarioFeriado.setUfSigla(null);
		}

		
	}
	
	/**
	 * Cadastra ou atualiza registro de feriado.
	 * 
	 * @param calendarioFeriado
	 */
	@Transactional
	public void cadastrarFeriado(CalendarioFeriadoDTO calendarioFeriado) {
	
		validarCadastroFeriado(calendarioFeriado);
		
		Date data = calendarioFeriado.getDataFeriado();
		String descricao = calendarioFeriado.getDescricaoFeriado();
		TipoFeriado tipoFeriado = calendarioFeriado.getTipoFeriado();
		String uf = calendarioFeriado.getUfSigla();
		
		boolean indOpera = calendarioFeriado.isIndOpera();
		boolean indRepeteAnualmente = calendarioFeriado.isIndRepeteAnualmente();
		boolean indEfetuaCobranca = calendarioFeriado.isIndEfetuaCobranca();
		
		Long idLocalidade = calendarioFeriado.getIdLocalidade();
		
		Feriado feriado = feriadoRepository.obterFeriado(data, tipoFeriado, uf, idLocalidade);
		
		if(feriado!=null) {
			
			feriado.setDescricao(descricao);
			feriado.setIndEfetuaCobranca(indEfetuaCobranca);
			feriado.setIndOpera(indOpera);
			feriado.setIndRepeteAnualmente(indRepeteAnualmente);
			
			feriadoRepository.alterar(feriado);
			
		} else {

			Localidade localidade = localidadeRepository.buscarPorId(idLocalidade);
			
			if(localidade == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Localidade não foi encontrada");
			}
			
			UnidadeFederacao unidadeFederacao = unidadeFederacaoRepository.buscarPorId(uf);
			
			if(unidadeFederacao == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Uf não foi encontrada");
			}
			
			feriado = new Feriado();
			
			feriado.setData(data);
			feriado.setDescricao(descricao);
			
			feriado.setIndEfetuaCobranca(indEfetuaCobranca);
			feriado.setIndOpera(indOpera);
			feriado.setIndRepeteAnualmente(indRepeteAnualmente);
			
			feriado.setLocalidade(localidade);
			feriado.setTipoFeriado(tipoFeriado);
			feriado.setUnidadeFederacao(unidadeFederacao);
			
			feriadoRepository.adicionar(feriado);
			
		}
		
		
	}
	
	@Transactional
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriado(Date dataFeriado) {
		
		feriadoRepository.obterListaCalendarioFeriado(dataFeriado);
		
		return null;
		
	}
	
	@Transactional
	public Map<Date, String> obterListaDataFeriado(int anoVigencia) {
		
		Calendar calendarInicial = Calendar.getInstance();
		calendarInicial.set(anoVigencia, Calendar.JANUARY, 1);
		
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.set(anoVigencia, Calendar.DECEMBER, 31);
		
		Date dataInicial = calendarInicial.getTime();
		
		Date dataFinal	= calendarFinal.getTime();
		
		List<CalendarioFeriadoDTO> listaDataFeriado = feriadoRepository.obterListaDataFeriado(dataInicial, dataFinal);
		
		Map<Date, String> mapaFeriado = new HashMap<Date, String>();
		
		for(CalendarioFeriadoDTO calendario : listaDataFeriado) {
			mapaFeriado.put(calendario.getDataFeriado(), calendario.getDescricaoFeriado());
		}
		
		return mapaFeriado;
		
	}
	
	
	
}
