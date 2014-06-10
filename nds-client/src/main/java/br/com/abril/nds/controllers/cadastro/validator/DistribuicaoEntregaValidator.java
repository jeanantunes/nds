package br.com.abril.nds.controllers.cadastro.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DistribuicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroModalDistribuicao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ModalidadeCobranca;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;
import br.com.abril.nds.util.DateUtil;

public class DistribuicaoEntregaValidator {
	
	public static void validar(final DistribuicaoDTO distribuicao){
		
		validarModalidadeDeCobranca(distribuicao);
		
		validarValoresCobranca(distribuicao);
		
		validarPeriodoCobranca(distribuicao);
		
		validarPeriodoCarencia(distribuicao.getInicioPeriodoCarencia(), distribuicao.getFimPeriodoCarencia());
	}
	
	public static void validar(final FiltroModalDistribuicao distribuicao){
		
		DistribuicaoDTO distribuicaoDTO = new DistribuicaoDTO();
		
		distribuicaoDTO.setBaseCalculo(distribuicao.getBaseCalculo());
		distribuicaoDTO.setDiaCobranca(distribuicao.getDiaCobranca());
		distribuicaoDTO.setDescricaoTipoEntrega(distribuicao.getDescricaoTipoEntrega());
		distribuicaoDTO.setDiaSemanaCobranca(distribuicao.getDiaSemanaCobranca());
		distribuicaoDTO.setModalidadeCobranca(distribuicao.getModalidadeCobranca());
		distribuicaoDTO.setPercentualFaturamento(distribuicao.getPercentualFaturamento());
		distribuicaoDTO.setTaxaFixa(distribuicao.getTaxaFixa());
		distribuicaoDTO.setPeriodicidadeCobranca(distribuicao.getPeriodicidadeCobranca());
		distribuicaoDTO.setInicioPeriodoCarencia(DateUtil.formatarDataPTBR(distribuicao.getCarenciaInicio()));
		distribuicaoDTO.setFimPeriodoCarencia(DateUtil.formatarDataPTBR(distribuicao.getCarenciaFim()));
		
		validar(distribuicaoDTO);
	}
	
	private static void validarModalidadeDeCobranca(final DistribuicaoDTO distribuicao) {
		
		if (distribuicao.getModalidadeCobranca() == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,("Modalidade de Cobrança é obrigatório."));
		}
	}
	
	private static void validarValoresCobranca(final DistribuicaoDTO distribuicao) {
		
		final boolean isPercentual = ModalidadeCobranca.PERCENTUAL.equals(distribuicao.getModalidadeCobranca());
		
		if (isPercentual) {
			
			if (distribuicao.getPercentualFaturamento() == null) {
			
				throw new ValidacaoException(TipoMensagem.WARNING,("É necessário informar um Percentual Faturamento."));
			
			} else {
				
				validarPercentualFaturamento(distribuicao.getPercentualFaturamento());
			}
			
			if(distribuicao.getBaseCalculo() == null){
				
				throw new ValidacaoException(TipoMensagem.WARNING,("Selecione uma Base de Cálculo"));
			}
		}
		else{
			
			if (distribuicao.getTaxaFixa() == null) {
				
				throw new ValidacaoException(TipoMensagem.WARNING,("É necessário informar um Valor R$."));
			}
		}
	}
	
	
	private static void validarPeriodoCobranca(final DistribuicaoDTO distribuicao) {
		
		if(distribuicao.getPeriodicidadeCobranca() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING,("Selecione uma Periodicidade."));
		}
		
		if (PeriodicidadeCobranca.SEMANAL == distribuicao.getPeriodicidadeCobranca() && 
				(distribuicao.getDiaSemanaCobranca() == null)){
			
			throw new ValidacaoException(TipoMensagem.WARNING,("Selecione os dias da semana para cobrança semanal."));
		
		} else if ((PeriodicidadeCobranca.QUINZENAL == distribuicao.getPeriodicidadeCobranca() ||
				PeriodicidadeCobranca.MENSAL == distribuicao.getPeriodicidadeCobranca()) && 
				(distribuicao.getDiaCobranca() == null || distribuicao.getDiaCobranca() <= 0 || distribuicao.getDiaCobranca() > 31)){
			
			throw new ValidacaoException(TipoMensagem.WARNING,("Dia da cobrança inválido"));
		}
	}
	
	    /**
	* Valida o período de carência informado.
	* 
	* @param inicioPeriodoCarenciaFormatado - início do período de carência
	*            formatado
	* @param fimPeriodoCarenciaFormatado - fim do período de carência formatado
	*/
	private static void validarPeriodoCarencia(String inicioPeriodoCarenciaFormatado,
							   				  String fimPeriodoCarenciaFormatado) {

		List<String> listaMensagens = new ArrayList<String>();

		if ((inicioPeriodoCarenciaFormatado == null || inicioPeriodoCarenciaFormatado
				.trim().isEmpty())
				&& (fimPeriodoCarenciaFormatado == null || fimPeriodoCarenciaFormatado
						.trim().isEmpty())) {

			return;
		}

		if (inicioPeriodoCarenciaFormatado == null
				|| inicioPeriodoCarenciaFormatado.trim().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"O início do Período de Carência deve ser preenchido!");
		}

		if (fimPeriodoCarenciaFormatado == null
				|| fimPeriodoCarenciaFormatado.trim().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"O fim do Período de Carência deve ser preenchido!");
		}

		if (!DateUtil.isValidDatePTBR(inicioPeriodoCarenciaFormatado)) {

			listaMensagens.add("Início do Período de Carência inválido!");
		}

		if (!DateUtil.isValidDatePTBR(fimPeriodoCarenciaFormatado)) {

			listaMensagens.add("Fim do Período de Carência inválido!");
		}

		if (!listaMensagens.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, listaMensagens);
		}

		Date inicioPeriodoCarencia = DateUtil
				.parseDataPTBR(inicioPeriodoCarenciaFormatado);
		Date fimPeriodoCarencia = DateUtil
				.parseDataPTBR(fimPeriodoCarenciaFormatado);

		if (DateUtil.isDataInicialMaiorDataFinal(inicioPeriodoCarencia,
				fimPeriodoCarencia)) {

			throw new ValidacaoException(
					TipoMensagem.WARNING,
					"O início do Período de Carência deve ser menor que o fim do Período de Carência!");
		}
	}
	
	private static void validarPercentualFaturamento(final BigDecimal valorPercentual) {
		
		final boolean isMaiorCem = 100D < valorPercentual.doubleValue();
		
		if (isMaiorCem) {
		
			throw new ValidacaoException(TipoMensagem.WARNING,("O Percentual Faturamento não deve ser maior que 100%."));
		}
	}
	
	
}
