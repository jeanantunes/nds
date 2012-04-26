package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.FinanceiroVO;
import br.com.abril.nds.dto.ContratoTransporteDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PessoaContratoDTO;
import br.com.abril.nds.dto.PessoaContratoDTO.TipoPessoa;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.ContaBancaria;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.service.FinanceiroService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class FinanceiroServiceImpl implements FinanceiroService {
	

	
	@Autowired
	private CotaRepository cotaRepository;
		
	@Autowired
	private BancoRepository bancoRepository;
	
	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
	@Autowired
	private ConcentracaoCobrancaCotaRepository concentracaoCobrancaRepository;
	
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	
	
	/**
	 * Método responsável por obter bancos para preencher combo da camada view
	 * @return comboBancos: bancos cadastrados
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Integer, String>> getComboBancosTipoCobranca(TipoCobranca tipoCobranca) {
		List<ItemDTO<Integer,String>> comboBancos =  new ArrayList<ItemDTO<Integer,String>>();
		List<Banco> bancos = formaCobrancaRepository.obterBancosPorTipoDeCobranca(tipoCobranca);
		for (Banco itemBanco : bancos){
			comboBancos.add(new ItemDTO<Integer,String>(itemBanco.getId().intValue(), itemBanco.getNumeroBanco()+" - "+itemBanco.getNome()));
		}
		return comboBancos;
	}
	
	
	
	 /**
	  * Método responsável por obter tipos de cobrança para preencher combo da camada view
	  * @return comboTiposPagamento: Tipos de cobrança padrão.
	  */
	@Override
	public List<ItemDTO<TipoCobranca, String>> getComboTiposCobranca() {
		List<ItemDTO<TipoCobranca,String>> comboTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
		for (TipoCobranca itemTipoCobranca: TipoCobranca.values()){
			comboTiposCobranca.add(new ItemDTO<TipoCobranca,String>(itemTipoCobranca, itemTipoCobranca.getDescTipoCobranca()));
		}
		return comboTiposCobranca;
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public FinanceiroVO obterDadosCotaCobranca(Long idCota) {
		
		Cota cota = cotaRepository.buscarPorId(idCota);

		FinanceiroVO cotaCobrancaVO = null;
		if (cota!=null){
			
			
			ParametroCobrancaCota parametroCobranca = cota.getParametroCobranca();
			
			Set<ConcentracaoCobrancaCota> concentracoesCobranca = parametroCobranca.getConcentracaoCobrancaCota();
			
			PoliticaSuspensao politicaSuspensao = parametroCobranca.getPoliticaSuspensao();
			
			FormaCobranca formaCobranca = parametroCobranca.getFormaCobranca();
			
			ContaBancaria contaBancaria = parametroCobranca.getContaBancariaCota();
			
			
			cotaCobrancaVO = new FinanceiroVO(); 

			
			cotaCobrancaVO.setIdCota(cota.getId());
			cotaCobrancaVO.setNumCota(cota.getNumeroCota());
			cotaCobrancaVO.setComissao(cota.getFatorDesconto());
			cotaCobrancaVO.setSugereSuspensao(cota.isSugereSuspensao());
			cotaCobrancaVO.setContrato(cota.isPossuiContrato());
			
			
			if (parametroCobranca!=null){
			    cotaCobrancaVO.setRecebeEmail(parametroCobranca.isRecebeCobrancaEmail());
			    cotaCobrancaVO.setFatorVencimento(parametroCobranca.getFatorVencimento());
			}
			
			
			if (contaBancaria!=null){
				cotaCobrancaVO.setNumBanco(contaBancaria.getNumeroBanco());
				cotaCobrancaVO.setNomeBanco(contaBancaria.getNomeBanco());
				cotaCobrancaVO.setAgencia(contaBancaria.getDvAgencia());
				cotaCobrancaVO.setAgenciaDigito(contaBancaria.getDvAgencia());
				cotaCobrancaVO.setConta(contaBancaria.getConta()!=null?Long.toString(contaBancaria.getConta()):"");
				cotaCobrancaVO.setContaDigito(contaBancaria.getDvConta());
			}
			
			
			if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
				for (ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
					
					DiaSemana dia = itemConcentracaoCobranca.getDiaSemana();
					if (dia==DiaSemana.DOMINGO){
						cotaCobrancaVO.setDomingo(true);
					}

					if (dia==DiaSemana.SEGUNDA_FEIRA){
					    cotaCobrancaVO.setSegunda(true);
					}    
					
					if (dia==DiaSemana.TERCA_FEIRA){
					    cotaCobrancaVO.setTerca(true);
					}    
					
					if (dia==DiaSemana.QUARTA_FEIRA){
				        cotaCobrancaVO.setQuarta(true);
					}
					
				    if (dia==DiaSemana.QUINTA_FEIRA){
					    cotaCobrancaVO.setQuinta(true);
				    }    
				    
					if (dia==DiaSemana.SEXTA_FEIRA){
					    cotaCobrancaVO.setSexta(true);
					}    
					
					if (dia==DiaSemana.SABADO){
					    cotaCobrancaVO.setSabado(true);
					}
					
				}
			}
			
			
			if (formaCobranca!=null){
				Banco banco = formaCobranca.getBanco();
				if (banco!=null){
					cotaCobrancaVO.setIdBanco(banco.getId());
				}
				cotaCobrancaVO.setTipoCobranca(formaCobranca.getTipoCobranca());
			    cotaCobrancaVO.setValorMinimo(formaCobranca.getValorMinimoEmissao());
			}
			
			
			if (politicaSuspensao!=null){
			    cotaCobrancaVO.setQtdDividasAberto(politicaSuspensao.getNumeroAcumuloDivida());
			    cotaCobrancaVO.setVrDividasAberto(politicaSuspensao.getValor());
			}
		}
		
		
		return cotaCobrancaVO;
	}
	

	
	@Override
	@Transactional
	public void postarDadosCotaCobranca(FinanceiroVO cotaCobranca) {
		

		//COTA
		Cota cota = cotaRepository.buscarPorId(cotaCobranca.getIdCota());

		if (cota!=null){
		
			
			
			//PARAMETROS DE COBRANCA DA COTA
			ParametroCobrancaCota parametroCobranca = cota.getParametroCobranca();
			
			//CONCENTRACOES DE COBRANCA DA COTA
			Set<ConcentracaoCobrancaCota> concentracoesCobranca = parametroCobranca.getConcentracaoCobrancaCota();
			
			//POLITICA DE SUSPENSAO DO PARAMETRO DE COBRANCA DA COTA
			PoliticaSuspensao politicaSuspensao = parametroCobranca.getPoliticaSuspensao();
			
			//FORMA DE COBRANCA DO PARAMETRO DE COBRANCA DA COTA
			FormaCobranca formaCobranca = parametroCobranca.getFormaCobranca();
			
			//INFORMACOES DE CONTA BANCARIA DO PARAMETRO DE COBRANCA DA COTA
			ContaBancaria contaBancariaCota = parametroCobranca.getContaBancariaCota();
			
			Banco banco = this.bancoRepository.buscarPorId(cotaCobranca.getIdBanco());
			
			
			
			parametroCobranca = (parametroCobranca==null?new ParametroCobrancaCota():parametroCobranca);
			
			politicaSuspensao = (politicaSuspensao==null?new PoliticaSuspensao():politicaSuspensao);
			
			contaBancariaCota = (contaBancariaCota==null?new ContaBancaria():contaBancariaCota);
			
			
			
			parametroCobranca.setRecebeCobrancaEmail(cotaCobranca.isRecebeEmail());
			parametroCobranca.setFatorVencimento((int) cotaCobranca.getFatorVencimento());
			parametroCobranca.setValorMininoCobranca(cotaCobranca.getValorMinimo());
			
			
			
			politicaSuspensao.setNumeroAcumuloDivida(cotaCobranca.getQtdDividasAberto());
			politicaSuspensao.setValor(cotaCobranca.getVrDividasAberto());

			parametroCobranca.setPoliticaSuspensao(politicaSuspensao);
			
			
			
			if (cotaCobranca.getTipoCobranca()!=null){

				formaCobranca = formaCobrancaRepository.obterPorTipoEBanco(cotaCobranca.getTipoCobranca(), banco);
				
				if (formaCobranca==null){

	            	formaCobranca = new FormaCobranca();
	            	formaCobranca.setValorMinimoEmissao(cotaCobranca.getValorMinimo());
	    			formaCobranca.setTipoCobranca(cotaCobranca.getTipoCobranca());
	    			formaCobranca.setBanco(banco);
	    			formaCobrancaRepository.adicionar(formaCobranca);
    			
			    }
				
		    }	

			parametroCobranca.setFormaCobranca(formaCobranca);


			
			contaBancariaCota.setNumeroBanco(cotaCobranca.getNumBanco());
			contaBancariaCota.setNomeBanco(cotaCobranca.getNomeBanco());
			contaBancariaCota.setAgencia(Long.getLong(cotaCobranca.getAgencia()));
			contaBancariaCota.setDvAgencia(cotaCobranca.getAgenciaDigito());
			contaBancariaCota.setConta(Long.getLong(cotaCobranca.getConta()));
			contaBancariaCota.setDvConta(cotaCobranca.getContaDigito());
	
			parametroCobranca.setContaBancariaCota(contaBancariaCota);
			
	
			
			//APAGA CONCENTRACOES COBRANCA DA COTA
			if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
				parametroCobranca.setConcentracaoCobrancaCota(null);
				for(ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
					this.concentracaoCobrancaRepository.remover(itemConcentracaoCobranca);
				}
			}
			
			//CONCENTRACAO COBRANCA (DIAS DA SEMANA)
			ConcentracaoCobrancaCota concentracaoCobranca;
			if (cotaCobranca.isDomingo()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
				concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (cotaCobranca.isSegunda()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
				concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (cotaCobranca.isTerca()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
				concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (cotaCobranca.isQuarta()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
				concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (cotaCobranca.isQuinta()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
				concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (cotaCobranca.isSexta()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
				concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (cotaCobranca.isSabado()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
				concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			
			parametroCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
			

			
			cota.setParametroCobranca(parametroCobranca);
			cota.setFatorDesconto(cotaCobranca.getComissao());
			cota.setSugereSuspensao(cotaCobranca.isSugereSuspensao());
			cota.setPossuiContrato(cotaCobranca.isContrato());
			
			this.cotaRepository.merge(cota);
			
		}
		
	}


	
	
	
	
  /**
   * !!!
   */
	@Override
	@Transactional
	public List<FormaCobranca> obterFormasCobrancaPorCota(/*FiltroConsultaFormasCobrancaCotaDTO filtro*/) {
		List<FormaCobranca> formasCobranca = this.formaCobrancaRepository.buscarTodos();
		return formasCobranca;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.FinanceiroService#obtemContratoTransporte(long)
	 */
	@Override
	public ContratoTransporteDTO obtemContratoTransporte(long idCota){
		ContratoTransporteDTO contrato = new ContratoTransporteDTO();
		
		
		Distribuidor distribuidor = distribuidorRepository.obter();	
		
		PessoaJuridica pessoaJuridica = distribuidor.getJuridica();		
		PessoaContratoDTO contratante = new PessoaContratoDTO();		
		
		contratante.setNome(pessoaJuridica.getNome());
		contratante.setDocumento(pessoaJuridica.getDocumento());
		contratante.setTipoPessoa(TipoPessoa.JURIDICA);
		
		EnderecoDistribuidor enderecoDistribuidor= distribuidorRepository.obterEnderecoPrincipal();
		
		if (enderecoDistribuidor != null) {
			contratante.setEndereco(enderecoDistribuidor.getEndereco());
		}
		contrato.setContratante(contratante);
		
		
		
		Cota cota = cotaRepository.buscarPorId(idCota);	
		if (cota!=null) {
			PessoaContratoDTO contratada = new PessoaContratoDTO();
			contratada.setNome(cota.getPessoa().getNome());
			contratada.setDocumento(cota.getPessoa().getDocumento());
			
			contratante.setTipoPessoa((cota.getPessoa()instanceof PessoaJuridica)?TipoPessoa.JURIDICA:TipoPessoa.FISICA);
			EnderecoCota enderecoCota = cotaRepository
					.obterEnderecoPrincipal(cota.getId());
			if (enderecoCota != null) {
				contratada.setEndereco(enderecoCota.getEndereco());
			}
			contrato.setContratada(contratada);
		}else{
			throw new IllegalArgumentException("Id da cota não cadastrada");
		}
		
		
		//TODO: Dados dos gestores do Contrato
		
		//TODO: Dados da contratação(Prazo, Inicio e Termino e Aviso Prévio para Recisão)
		
		return contrato;
	}

}
