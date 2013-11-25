<<<<<<< HEAD
package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.FormaCobrancaExcepion;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.SemanaUtil;

@Service
public class FormaCobrancaServiceImpl implements FormaCobrancaService {

	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;

	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private CotaRepository cotaRepository;

	/**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja
	 * cadastrar(Valida por Fornecedor e Concentração)
	 * 
	 * @param tipoCobranca
	 * @param idPoliticaCobranca
	 * @param distribuidor
	 * @param idFornecedores
	 * @param diasDoMes
	 * @return Boolean
	 */
	@Override
	@Transactional
	public boolean validarFormaCobrancaMensal(TipoCobranca tipoCobranca,
			                                  Long idPoliticaCobranca,
											  Long idDistribuidor, 
											  TipoFormaCobranca tipoFormaCobranca,
											  List<Long> idFornecedores, 
											  List<Integer> diasDoMes) {

		Long idFormaCobrancaExcept = null;

		if (idPoliticaCobranca != null) {

			PoliticaCobranca politica = this.politicaCobrancaRepository
					.buscarPorId(idPoliticaCobranca);

			idFormaCobrancaExcept = politica.getFormaCobranca().getId();
		}

		List<FormaCobranca> formas = this.formaCobrancaRepository
				.obterPorDistribuidor(idDistribuidor, idFormaCobrancaExcept);

		for (FormaCobranca itemFormaCobranca : formas) {
			
			//Nos parametros de Cobrança do Distribuidor o Tipo de Cobranca é considerado na validação de cadastro de nova forma cobrança 
			if (!tipoCobranca.equals(itemFormaCobranca.getTipoCobranca())){
				
				continue;
			}

			for (int i = 0; i < idFornecedores.size(); i++) {

				Fornecedor fornecedor = this.fornecedorService
						.obterFornecedorPorId(idFornecedores.get(i));

				if (itemFormaCobranca.getFornecedores().contains(fornecedor)) {

					if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA)
							|| itemFormaCobranca.getTipoFormaCobranca().equals(
									TipoFormaCobranca.DIARIA)) {

						return false;
					}

					if (itemFormaCobranca.getDiasDoMes() != null) {

						for (Integer d : diasDoMes) {

							if (itemFormaCobranca.getDiasDoMes().contains(d)) {

								return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Verifica se ja existe a Forma Cobranca Semanal que o usuário deseja
	 * cadastrar(Valida por Fornecedor e Concentração)
	 * 
	 * @param tipoCobranca
	 * @param idPoliticaCobranca
	 * @param distribuidor
	 * @param tipoFormaCobranca
	 * @param idFornecedores
	 * @param domingo
	 * @param segunda
	 * @param terca
	 * @param quarta
	 * @param quinta
	 * @param sexta
	 * @param sabado
	 * @return Boolean
	 */
	@Override
	@Transactional
	public boolean validarFormaCobrancaSemanal(TipoCobranca tipoCobranca,
			                                   Long idPoliticaCobranca,
											   Long idDistribuidor, 
											   TipoFormaCobranca tipoFormaCobranca,
											   List<Long> idFornecedores, 
											   Boolean domingo, 
											   Boolean segunda,
											   Boolean terca, 
											   Boolean quarta, 
											   Boolean quinta, 
											   Boolean sexta,
											   Boolean sabado) {

		Long idFormaCobrancaExcept = null;

		if (idPoliticaCobranca != null) {

			PoliticaCobranca politica = this.politicaCobrancaRepository
					.buscarPorId(idPoliticaCobranca);

			idFormaCobrancaExcept = politica.getFormaCobranca().getId();
		}

		List<FormaCobranca> formas = this.formaCobrancaRepository
				.obterPorDistribuidor(idDistribuidor, idFormaCobrancaExcept);

		for (FormaCobranca itemFormaCobranca : formas) {
			
			//Nos parametros de Cobrança do Distribuidor o Tipo de Cobranca é considerado na validação de cadastro de nova forma cobrança 
			if (!tipoCobranca.equals(itemFormaCobranca.getTipoCobranca())){
				
				continue;
			}

			for (Long idFornecedor : idFornecedores) {

				Fornecedor fornecedor = this.fornecedorService
						.obterFornecedorPorId(idFornecedor);

				if (itemFormaCobranca.getFornecedores().contains(fornecedor)) {

					if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA)
							|| itemFormaCobranca.getTipoFormaCobranca().equals(
									TipoFormaCobranca.DIARIA)) {

						return false;
					}

					for (ConcentracaoCobrancaCota itemConcentracao : itemFormaCobranca
							.getConcentracaoCobrancaCota()) {

						if ((domingo && (itemConcentracao.getDiaSemana() == DiaSemana.DOMINGO))
								|| (segunda && (itemConcentracao.getDiaSemana() == DiaSemana.SEGUNDA_FEIRA))
								|| (terca && (itemConcentracao.getDiaSemana() == DiaSemana.TERCA_FEIRA))
								|| (quarta && (itemConcentracao.getDiaSemana() == DiaSemana.QUARTA_FEIRA))
								|| (quinta && (itemConcentracao.getDiaSemana() == DiaSemana.QUINTA_FEIRA))
								|| (sexta && (itemConcentracao.getDiaSemana() == DiaSemana.SEXTA_FEIRA))
								|| (sabado && (itemConcentracao.getDiaSemana() == DiaSemana.SABADO))) {

							return false;
						}

					}

				}
			}
		}

		return true;
	}

	/**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja
	 * cadastrar(Valida por Fornecedor e Concentração)
	 * 
	 * @param idFormaCobranca
	 * @param idCota
	 * @param idFornecedores
	 * @param tipoFormaCobranca
	 * @param diasDoMes
	 * @return Boolean
	 */
	@Override
	@Transactional
	public boolean validarFormaCobrancaMensal(Long idFormaCobranca,
											  Long idCota, 
											  List<Long> idFornecedores,
											  TipoFormaCobranca tipoFormaCobranca, 
											  List<Integer> diasDoMes) {

		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorCota(
				idCota, idFormaCobranca);

		for (FormaCobranca itemFormaCobranca : formas) {

			for (int i = 0; i < idFornecedores.size(); i++) {

				Fornecedor fornecedor = this.fornecedorService
						.obterFornecedorPorId(idFornecedores.get(i));

				if (itemFormaCobranca.getFornecedores().contains(fornecedor)) {

					if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA)
							|| itemFormaCobranca.getTipoFormaCobranca().equals(
									TipoFormaCobranca.DIARIA)) {

						return false;
					}

					if (itemFormaCobranca.getDiasDoMes() != null) {

						for (Integer d : diasDoMes) {

							if (itemFormaCobranca.getDiasDoMes().contains(d)) {

								return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Verifica se ja existe a Forma Cobranca Semanal que o usuário deseja
	 * cadastrar(Valida por Fornecedor e Concentração)
	 * 
	 * @param idFormaCobranca
	 * @param idCota
	 * @param idFornecedores
	 * @param tipoFormaCobranca
	 * @param domingo
	 * @param segunda
	 * @param terca
	 * @param quarta
	 * @param quinta
	 * @param sexta
	 * @param sabado
	 * @return Boolean
	 */
	@Override
	@Transactional
	public boolean validarFormaCobrancaSemanal(Long idFormaCobranca,
											   Long idCota, 
											   List<Long> idFornecedores,
											   TipoFormaCobranca tipoFormaCobranca, 
											   Boolean domingo,
											   Boolean segunda, 
											   Boolean terca, 
											   Boolean quarta, 
											   Boolean quinta,
											   Boolean sexta, 
											   Boolean sabado) {

		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorCota(
				idCota, idFormaCobranca);

		for (FormaCobranca itemFormaCobranca : formas) {

			for (int i = 0; i < idFornecedores.size(); i++) {

				Fornecedor fornecedor = this.fornecedorService
						.obterFornecedorPorId(idFornecedores.get(i));

				if (itemFormaCobranca.getFornecedores().contains(fornecedor)) {

					if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA)
							|| itemFormaCobranca.getTipoFormaCobranca().equals(
									TipoFormaCobranca.DIARIA)) {

						return false;
					}

					for (ConcentracaoCobrancaCota itemConcentracao : itemFormaCobranca
							.getConcentracaoCobrancaCota()) {

						if ((domingo && (itemConcentracao.getDiaSemana() == DiaSemana.DOMINGO))
								|| (segunda && (itemConcentracao.getDiaSemana() == DiaSemana.SEGUNDA_FEIRA))
								|| (terca && (itemConcentracao.getDiaSemana() == DiaSemana.TERCA_FEIRA))
								|| (quarta && (itemConcentracao.getDiaSemana() == DiaSemana.QUARTA_FEIRA))
								|| (quinta && (itemConcentracao.getDiaSemana() == DiaSemana.QUINTA_FEIRA))
								|| (sexta && (itemConcentracao.getDiaSemana() == DiaSemana.SEXTA_FEIRA))
								|| (sabado && (itemConcentracao.getDiaSemana() == DiaSemana.SABADO))) {

							return false;
						}

					}

				}
			}
		}

		return true;
	}

	/**
	 * Verifica se a Cota possui Parametro de Cobranca e Forma de Cobranca Ativa
	 * cadastrados
	 * 
	 * @param cota
	 * @return boolean
	 */
	private boolean cotaPossuiFormaCobranca(Cota cota) {

		ParametroCobrancaCota pcc = cota.getParametroCobranca();

		if (pcc == null) {

			return false;
		} else {

			if (pcc.getFormasCobrancaCota() == null
					|| pcc.getFormasCobrancaCota().isEmpty()) {

				return false;
			}
			else{
				
				Set<FormaCobranca> formasCobranca = pcc.getFormasCobrancaCota(); 
				
				for(FormaCobranca fc : formasCobranca){
					
					if (fc.isAtiva()){
						
						return true;
					}
				}
				
			}
		}

		return false;
	}

	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados
	 * 
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaCota(Long idCota, 
			                                    Long idFornecedor,
												Date data, 
												BigDecimal valor) {

		Integer diaDoMes = DateUtil.obterDiaDoMes(data);

		Integer diaDaSemana = SemanaUtil.obterDiaDaSemana(data);

		FormaCobranca formaCobranca = this.formaCobrancaRepository
				.obterFormaCobranca(idCota, idFornecedor, diaDoMes,
						diaDaSemana, valor);

		return formaCobranca;
	}

	/**
	 * Obtem FormaCobranca do Distribuidor com os Parâmetros passados
	 * 
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaDistribuidor(Long idFornecedor,
														Date data, 
														BigDecimal valor) {

		Integer diaDoMes = DateUtil.obterDiaDoMes(data);

		Integer diaDaSemana = SemanaUtil.obterDiaDaSemana(data);

		
		FormaCobranca formaCobranca = this.formaCobrancaRepository
				.obterFormaCobranca(idFornecedor, diaDoMes, diaDaSemana, valor, true);

		return formaCobranca;
	}

	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados. Caso não encontre
	 * e a cota não tenha FormaCobranca cadastrada, busca FormaCobranca do
	 * Distribuidor
	 * 
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobranca(Long idCota, 
											Long idFornecedor,
											Date data, 
											BigDecimal valor) {

		Cota cota = null;

		if (idCota != null) {

			cota = this.cotaRepository.buscarPorId(idCota);
		}

		if (idFornecedor == null && cota != null) {

			idFornecedor = cota.getParametroCobranca() != null ? cota
					.getParametroCobranca().getFornecedorPadrao() != null ? cota
					.getParametroCobranca().getFornecedorPadrao().getId()
					: null
					: null;
		}

		if (idFornecedor == null) {

			throw new ValidacaoException(
					TipoMensagem.WARNING,
					"Para a obtenção de uma Forma de Cobrança é necessário que seja informado um [Fornecedor] ou que haja [Fornecedor Padrão] definido nos parâmetros financeiros da [Cota]!");
		}

		FormaCobranca formaCobranca = this.obterFormaCobrancaCota(idCota,
				idFornecedor, data, valor);

		if (formaCobranca == null) {

	        formaCobranca = this.obterFormaCobrancaDistribuidor(idFornecedor,
					data, valor);

	        //Se a cota possuir uma Forma de Cobrança ativa, 
	        //mesmo que não se enquadre nos parâmetros passados, 
	        //retorna null e a Cobrança é postergada
			if (cota!=null && this.cotaPossuiFormaCobranca(cota)) { 
				  
		        return null; 
			}
		}

		return formaCobranca;
	}

	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados, caso não
	 * encontre, busca FormaCobranca do Distribuidor Caso não encontre Forma de
	 * Cobranca, retorna excecao com informacoes
	 * 
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaValidacao(Long idCota,
													 Long idFornecedor, 
													 Date data, 
													 BigDecimal valor) {

		Cota cota = null;

		if (idCota != null) {

			cota = this.cotaRepository.buscarPorId(idCota);
		}

		Fornecedor fornecedor = this.fornecedorService
				.obterFornecedorPorId(idFornecedor);

		FormaCobranca formaCobranca = this.obterFormaCobranca(idCota,
				idFornecedor, data, valor);

		if (formaCobranca == null) {

			throw new FormaCobrancaExcepion(TipoMensagem.WARNING,
					"Forma de Cobrança não encontrada para a [Data "
							+ DateUtil.formatarDataPTBR(data)
							+ "] [Fornecedor "
							+ fornecedor.getJuridica().getNome()
							+ "] [Valor Mínimo "
							+ CurrencyUtil.formatarValorComSimbolo(valor)
							+ "]"
							+ (cota != null ? " [Cota " + cota.getNumeroCota()
									+ "]." : "."));
		}

		return formaCobranca;
	}

	/**
	 * Obtem FormaCobranca principal da Cota
	 * 
	 * @param idCota
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota) {

		FormaCobranca formaCobranca = this.formaCobrancaRepository
				.obterFormaCobranca(idCota);

		return formaCobranca;
	}

	/**
	 * Obtem FormaCobranca principal do Distribuidor
	 * 
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaPrincipalDistribuidor() {

		return this.formaCobrancaRepository.obterFormaCobranca();
	}
=======
package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

@Service
public class FormaCobrancaServiceImpl implements FormaCobrancaService {

	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	/**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param idPoliticaCobranca
	 * @param distribuidor
	 * @param idFornecedores
	 * @param diasDoMes
	 * @return Boolean
	 */
	@Override
	@Transactional
	public boolean validarFormaCobrancaMensal(Long idPoliticaCobranca, Long idDistribuidor, TipoFormaCobranca tipoFormaCobranca,
			List<Long> idFornecedores, List<Integer> diasDoMes) {
		
		Long idFormaCobrancaExcept = null;
		
		if (idPoliticaCobranca!=null){
			
		    PoliticaCobranca politica = this.politicaCobrancaRepository.buscarPorId(idPoliticaCobranca);
		    
		    idFormaCobrancaExcept = politica.getFormaCobranca().getId();
		}    
		
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorDistribuidor(idDistribuidor, idFormaCobrancaExcept);
		
		for (FormaCobranca itemFormaCobranca:formas){
			
			for (int i=0; i<idFornecedores.size();i++){
				
				Fornecedor fornecedor= this.fornecedorService.obterFornecedorPorId(idFornecedores.get(i));
				
				if (itemFormaCobranca.getFornecedores().contains(fornecedor)){
					
                    if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA) || itemFormaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.DIARIA)){
						
						return false;
					}
					
                    if (itemFormaCobranca.getDiasDoMes() !=null){
                    	
	                    for (Integer d:diasDoMes){
	                    	
							if (itemFormaCobranca.getDiasDoMes().contains(d)){
								
								return false;
							}
	                    }
                    }
				}
			}
		}
		
		return true;
	}

	/**
	 * Verifica se ja existe a Forma Cobranca Semanal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param idPoliticaCobranca
	 * @param distribuidor
	 * @param tipoFormaCobranca
	 * @param idFornecedores
	 * @param domingo
	 * @param segunda
	 * @param terca
	 * @param quarta
	 * @param quinta
	 * @param sexta
	 * @param sabado
	 * @return Boolean
	 */
	@Override
	@Transactional
	public boolean validarFormaCobrancaSemanal(Long idPoliticaCobranca, Long idDistribuidor, TipoFormaCobranca tipoFormaCobranca, List<Long> idFornecedores, 
			Boolean domingo, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado) {
		
        Long idFormaCobrancaExcept = null;
		
		if (idPoliticaCobranca!=null){
			
			PoliticaCobranca politica = this.politicaCobrancaRepository.buscarPorId(idPoliticaCobranca);
			
			idFormaCobrancaExcept = politica.getFormaCobranca().getId();
		}	
		
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorDistribuidor(idDistribuidor, idFormaCobrancaExcept);
		
		for (FormaCobranca itemFormaCobranca:formas){
			
			for (Long idFornecedor : idFornecedores){
				
				Fornecedor fornecedor= this.fornecedorService.obterFornecedorPorId(idFornecedor);
				
				if (itemFormaCobranca.getFornecedores().contains(fornecedor)){
					
					if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA) || itemFormaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.DIARIA)){
						
						return false;
					}
					
					for(ConcentracaoCobrancaCota itemConcentracao:itemFormaCobranca.getConcentracaoCobrancaCota()){
						
						if (
								(domingo && (itemConcentracao.getDiaSemana()==DiaSemana.DOMINGO))||
								(segunda && (itemConcentracao.getDiaSemana()==DiaSemana.SEGUNDA_FEIRA))||
								(terca && (itemConcentracao.getDiaSemana()==DiaSemana.TERCA_FEIRA))||
								(quarta && (itemConcentracao.getDiaSemana()==DiaSemana.QUARTA_FEIRA))||
								(quinta && (itemConcentracao.getDiaSemana()==DiaSemana.QUINTA_FEIRA))||
								(sexta && (itemConcentracao.getDiaSemana()==DiaSemana.SEXTA_FEIRA))||
								(sabado && (itemConcentracao.getDiaSemana()==DiaSemana.SABADO))
						    ){
							
							return false;
						}
	
					}
	
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Verifica se ja existe a Forma Cobranca Mensal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param idFormaCobranca
	 * @param idCota
	 * @param idFornecedores
	 * @param tipoFormaCobranca
	 * @param diasDoMes
	 * @return Boolean
	 */
	@Override
	@Transactional
	public boolean validarFormaCobrancaMensal(Long idFormaCobranca, Long idCota, List<Long> idFornecedores, TipoFormaCobranca tipoFormaCobranca, List<Integer> diasDoMes) {
		
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorCota(idCota, idFormaCobranca);
		
		for (FormaCobranca itemFormaCobranca:formas){
			
			for (int i=0; i<idFornecedores.size();i++){
				
				Fornecedor fornecedor= this.fornecedorService.obterFornecedorPorId(idFornecedores.get(i));
				
				if (itemFormaCobranca.getFornecedores().contains(fornecedor)){
					
                    if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA) || itemFormaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.DIARIA)){
						
						return false;
					}
					
                    if (itemFormaCobranca.getDiasDoMes() !=null){
                    	
	                    for (Integer d:diasDoMes){
	                    	
							if (itemFormaCobranca.getDiasDoMes().contains(d)){
								
								return false;
							}
	                    }
                    }
				}
			}
		}
		
		return true;
	}

	/**
	 * Verifica se ja existe a Forma Cobranca Semanal que o usuário deseja cadastrar(Valida por Fornecedor e Concentração)
	 * @param idFormaCobranca
	 * @param idCota
	 * @param idFornecedores
	 * @param tipoFormaCobranca
	 * @param domingo
	 * @param segunda
	 * @param terca
	 * @param quarta
	 * @param quinta
	 * @param sexta
	 * @param sabado
	 * @return Boolean
	 */
	@Override
	@Transactional
	public boolean validarFormaCobrancaSemanal(Long idFormaCobranca, Long idCota, List<Long> idFornecedores, TipoFormaCobranca tipoFormaCobranca,
			Boolean domingo, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado) {
		
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorCota(idCota, idFormaCobranca);
		
		for (FormaCobranca itemFormaCobranca:formas){
			
			for (int i=0; i<idFornecedores.size();i++){
				
				Fornecedor fornecedor= this.fornecedorService.obterFornecedorPorId(idFornecedores.get(i));
				
				if (itemFormaCobranca.getFornecedores().contains(fornecedor)){
					
					if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA) || itemFormaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.DIARIA)){
						
						return false;
					}
					
					for(ConcentracaoCobrancaCota itemConcentracao:itemFormaCobranca.getConcentracaoCobrancaCota()){
						
						if (
								(domingo && (itemConcentracao.getDiaSemana()==DiaSemana.DOMINGO))||
								(segunda && (itemConcentracao.getDiaSemana()==DiaSemana.SEGUNDA_FEIRA))||
								(terca && (itemConcentracao.getDiaSemana()==DiaSemana.TERCA_FEIRA))||
								(quarta && (itemConcentracao.getDiaSemana()==DiaSemana.QUARTA_FEIRA))||
								(quinta && (itemConcentracao.getDiaSemana()==DiaSemana.QUINTA_FEIRA))||
								(sexta && (itemConcentracao.getDiaSemana()==DiaSemana.SEXTA_FEIRA))||
								(sabado && (itemConcentracao.getDiaSemana()==DiaSemana.SABADO))
						    ){
							
							return false;
						}
	
					}
	
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaCota(Long idCota, Long idFornecedor, Date data, BigDecimal valor) {
		
		Integer diaDoMes = DateUtil.obterDiaDoMes(data);
		
		Integer diaDaSemana = DateUtil.obterDiaDaSemana(data);
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(idCota, idFornecedor, diaDoMes, diaDaSemana, valor);

		return formaCobranca;
	}
	
	/**
	 * Obtem FormaCobranca do Distribuidor com os Parâmetros passados
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaDistribuidor(Long idFornecedor, Date data, BigDecimal valor) {
		
		Integer diaDoMes = DateUtil.obterDiaDoMes(data);
		
		Integer diaDaSemana = DateUtil.obterDiaDaSemana(data);
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(idFornecedor, diaDoMes, diaDaSemana, valor);

		return formaCobranca;
	}
	
	/**
	 * Obtem FormaCobranca da Cota com os Parâmetros passados, caso não encontre, busca FormaCobranca do Distribuidor 
	 * @param idCota
	 * @param idFornecedor
	 * @param data
	 * @param valor
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobranca(Long idCota, Long idFornecedor, Date data, BigDecimal valor) {
		
		Cota cota = null;
		
		if (idCota!=null){
		    
			cota = this.cotaRepository.buscarPorId(idCota);
		}
		
		if (idFornecedor == null && cota!=null){
			
			idFornecedor = cota.getParametroCobranca()!=null?cota.getParametroCobranca().getFornecedorPadrao()!=null?cota.getParametroCobranca().getFornecedorPadrao().getId():null:null;
		}
		
		if (idFornecedor==null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Para a obtenção de uma Forma de Cobrança é necessário que seja informado um [Fornecedor] ou que haja [Fornecedor Padrão] definido nos parâmetros financeiros da [Cota]!");
		}
		
		Fornecedor fornecedor = this.fornecedorService.obterFornecedorPorId(idFornecedor);
		
		FormaCobranca formaCobranca = this.obterFormaCobrancaCota(idCota, idFornecedor, data, valor);

		if (formaCobranca == null){
			
			formaCobranca = this.obterFormaCobrancaDistribuidor(idFornecedor, data, valor);
		}
		
		if (formaCobranca == null){
	    	
	    	throw new ValidacaoException(TipoMensagem.WARNING, "Forma de Cobrança não encontrada para a [Data "+DateUtil.formatarDataPTBR(data)+"] [Fornecedor "+fornecedor.getJuridica().getNome()+"] [Valor "+CurrencyUtil.formatarValorComSimbolo(valor)+"]"+(cota!=null?" [Cota "+cota.getNumeroCota()+"].":"."));
	    }
		
		return formaCobranca;
	}

	/**
     * Obtem FormaCobranca principal da Cota
     * @param idCota
     * @return FormaCobranca
     */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota) {
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(idCota);

		return formaCobranca;
	}

	/**
     * Obtem FormaCobranca principal do Distribuidor
     * @return FormaCobranca
     */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaPrincipalDistribuidor() {
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca();

		return formaCobranca;
	}
>>>>>>> fase2
}