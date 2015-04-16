package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.FormaCobrancaDefaultVO;
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

		Cota cota = cotaRepository.buscarPorId(idCota);
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorCota(cota.getParametroCobranca(), idFormaCobranca);

		for (FormaCobranca itemFormaCobranca : formas) {

			for (int i = 0; i < idFornecedores.size(); i++) {

				Fornecedor fornecedor = this.fornecedorService.obterFornecedorPorId(idFornecedores.get(i));

				if (itemFormaCobranca.getFornecedores().contains(fornecedor)) {

					if (tipoFormaCobranca.equals(TipoFormaCobranca.DIARIA)
							|| itemFormaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.DIARIA)) {

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

		Cota cota = cotaRepository.buscarPorId(idCota);
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorCota(cota.getParametroCobranca(), idFormaCobranca);

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
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaCota(Long idCota, 
			                                    Long idFornecedor,
												Date data) {

		Integer diaDoMes = DateUtil.obterDiaDoMes(data);

		Integer diaDaSemana = SemanaUtil.obterDiaDaSemana(data);

		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(idCota, idFornecedor, diaDoMes, diaDaSemana);

		return formaCobranca;
	}

	/**
	 * Obtem FormaCobranca do Distribuidor com os Parâmetros passados
	 * 
	 * @param idFornecedor
	 * @param data
	 * @return FormaCobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaDistribuidor(Long idFornecedor,
														Date data) {

		Integer diaDoMes = DateUtil.obterDiaDoMes(data);

		Integer diaDaSemana = SemanaUtil.obterDiaDaSemana(data);

		FormaCobranca formaCobranca = this.formaCobrancaRepository
				.obterFormaCobranca(idFornecedor, diaDoMes, diaDaSemana, true);

		return formaCobranca;
	}
	
	/**
	 * Verifica se valor à cobrar é menor ou igual ao valor minimo para cobranca estipulado para a Cota
	 * 
	 * @param valorMinimoCota
	 * @param valorTotalCobrar
	 * @return boolean
	 */
	@Override
	@Transactional
	public boolean isValorMinimoAtingido(BigDecimal valorMinimoCota, BigDecimal valorTotalCobrar){
		
		return valorMinimoCota.compareTo(valorTotalCobrar) <= 0;
	}
	
	/**
	 * Verifica se valor à cobrar é menor ou igual ao valor minimo para cobranca estipulado para a Cota
	 * 
	 * @param idCota
	 * @param valorTotalCobrar
	 * @return boolean
	 */
	@Override
	@Transactional
	public boolean isValorMinimoAtingido(Long idCota, BigDecimal valorTotalCobrar){
		
		Cota cota = this.cotaRepository.buscarCotaPorID(idCota);
		
		return this.isValorMinimoAtingido(cota.getValorMinimoCobranca(), valorTotalCobrar);
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
			
			if (cota.getValorMinimoCobranca() == null){
			    
			    throw new ValidacaoException(TipoMensagem.ERROR, "Cota " + cota.getNumeroCota() + " não possui valor mínimo de cobrança cadastrado.");
			}
			
			boolean valorMinimoAtingido = this.isValorMinimoAtingido(cota.getValorMinimoCobranca(), valor);
			
			if (!valorMinimoAtingido){
				
				return null;
			}
		}

		if (idFornecedor == null && cota != null) {

			idFornecedor = cota.getParametroCobranca() != null ? cota
					.getParametroCobranca().getFornecedorPadrao() != null ? cota
					.getParametroCobranca().getFornecedorPadrao().getId()
					: null
					: null;
		}

		if (idFornecedor == null) {

			PoliticaCobranca politica = this.politicaCobrancaRepository.buscarPoliticaCobrancaPrincipal();

			idFornecedor = politica.getFornecedorPadrao() != null ? 
					politica.getFornecedorPadrao().getId()
					: null;

			if (idFornecedor == null) {
				throw new ValidacaoException(
					TipoMensagem.WARNING,
					"Para a obtenção de uma Forma de Cobrança é necessário que seja informado um [Fornecedor] ou que haja [Fornecedor Padrão] definido nos parâmetros financeiros da [Cota]!");
			}
		}

		FormaCobranca formaCobranca = this.obterFormaCobrancaCota(idCota, idFornecedor, data);

		if (formaCobranca == null) {

			//Se a cota possuir uma Forma de Cobrança ativa, 
	        //mesmo que não se enquadre nos parâmetros passados, 
	        //retorna null e a Cobrança é postergada
			if (cota!=null && this.cotaPossuiFormaCobranca(cota)) { 
				  
		        return null; 
			}
			
	        formaCobranca = this.obterFormaCobrancaDistribuidor(idFornecedor, data);
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

		Cota cota = this.cotaRepository.buscarPorId(idCota);
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(cota.getParametroCobranca());

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
	
	/**
     * Obtem FormaCobranca principal do Distribuidor com dados de fornecedor e concentração
     * 
     * @return FormaCobranca
     */
    @Override
    @Transactional(readOnly = true)
    public FormaCobranca obterFormaCobrancaPrincipalDistribuidorCompleto() {

        return this.formaCobrancaRepository.obterFormaCobrancaCompleto();
    }
	@Override
	@Transactional(readOnly=true)
	public List<FormaCobrancaDefaultVO> obterFormaCobrancaDefault() {
		
		return this.formaCobrancaRepository.obterFormaCobrancaDefault();
	}

	@Override
	@Transactional
	public FormaCobranca obterFormaCobrancaPrincipalCota(Integer numeroCota) {
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
		FormaCobranca formaCobranca = this.formaCobrancaRepository.obterFormaCobranca(cota.getParametroCobranca());

		return formaCobranca;
	}
	
	/**
	 * Obtem mapa com as formas de cobrança ativas
	 * De todos os fornecedores cadastrados
	 * Onde a concentração de pagamento é compatível com a data de operação atual
	 * 
	 * @param dataOperacao
	 * @return Map<Fornecedor,List<FormaCobranca>>
	 */
	@Override
	@Transactional
	public Map<Fornecedor,List<FormaCobranca>> obterMapFornecedorFormasCobranca(Date dataOperacao) {
		
		Map<Fornecedor,List<FormaCobranca>> mapFormasCobrancaFornecedor = new HashMap<Fornecedor,List<FormaCobranca>>();
		
		List<Fornecedor> todosFornecedores = this.fornecedorService.obterFornecedores();
		
		Integer diaDoMes = DateUtil.obterDiaDoMes(dataOperacao);

		Integer diaDaSemana = SemanaUtil.obterDiaDaSemana(dataOperacao);
		
		for (Fornecedor fornecedor : todosFornecedores){
		
			List<FormaCobranca> formasCobranca = this.formaCobrancaRepository.obterFormasCobrancaPorFornecedor(fornecedor.getId(),diaDoMes,diaDaSemana);
			
			mapFormasCobrancaFornecedor.put(fornecedor, formasCobranca);
		}

		return mapFormasCobrancaFornecedor;
	}
}
