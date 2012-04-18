package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CotaCobrancaVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.ContaBancaria;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
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
	private ConcentracaoCobrancaCotaRepository concentracaoCobrancaCotaRepository;


	
	
	
	/**
	 * Método responsável por obter bancos para preencher combo da camada view
	 * @return comboBancos: bancos cadastrados
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Integer, String>> getComboBancosTipoCobranca(TipoCobranca tipoCobranca) {
		List<ItemDTO<Integer,String>> comboBancos =  new ArrayList<ItemDTO<Integer,String>>();
		List<Banco> bancos = bancoRepository.obterBancosPorTipoDeCobranca(tipoCobranca);
		for (Banco itemBanco : bancos){
			comboBancos.add(new ItemDTO<Integer,String>(itemBanco.getId().intValue(), itemBanco.getNumeroBanco()+" - "+itemBanco.getNome()));
		}
		return comboBancos;
	}
	
	
	 /**
	  * Método responsável por obter tipos de cobrança para preencher combo da camada view
	  * @return comboTiposPagamento: Tipos de cobrança padrão.
	  */
	@Transactional(readOnly=true)
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
	public CotaCobrancaVO obterDadosCotaCobranca(Integer numeroCota) {
		
		Cota cota = cotaRepository.obterPorNumerDaCota(numeroCota);
		
		ParametroCobrancaCota parametrosCobranca = cota.getParametroCobranca();
		
		CotaCobrancaVO cotaCobrancaVO = null;
		if (cota!=null){
			cotaCobrancaVO = new CotaCobrancaVO(); 

			cotaCobrancaVO.setNumCota(cota.getNumeroCota());
			cotaCobrancaVO.setTipoCobranca(parametrosCobranca.getFormaCobranca().getTipoCobranca());
			cotaCobrancaVO.setIdBanco(parametrosCobranca.getFormaCobranca().getBanco().getId());
			cotaCobrancaVO.setRecebeEmail(parametrosCobranca.isRecebeCobrancaEmail());
			
			cotaCobrancaVO.setNumBanco(parametrosCobranca.getContaBancariaCota().getNumeroBanco());
			cotaCobrancaVO.setNomeBanco(parametrosCobranca.getContaBancariaCota().getNomeBanco());
			cotaCobrancaVO.setAgencia(parametrosCobranca.getContaBancariaCota().getDvAgencia());
			cotaCobrancaVO.setAgenciaDigito(parametrosCobranca.getContaBancariaCota().getDvAgencia());
			cotaCobrancaVO.setConta(parametrosCobranca.getContaBancariaCota().getConta().toString());
			cotaCobrancaVO.setContaDigito(parametrosCobranca.getContaBancariaCota().getDvConta());
			
			cotaCobrancaVO.setFatorVencimento(parametrosCobranca.getFatorVencimento());
			cotaCobrancaVO.setSugereSuspensao(cota.isSugereSuspensao());
			cotaCobrancaVO.setContrato(cota.getContratoCota()==null);
			
			Set<ConcentracaoCobrancaCota> concentracaoCobrancas = parametrosCobranca.getConcentracaoCobrancaCota();
			if ((concentracaoCobrancas!=null)&&(concentracaoCobrancas.size() > 0)){
				for (ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracaoCobrancas){
					
					DiaSemana dia = itemConcentracaoCobranca.getDiaSemana();
					cotaCobrancaVO.setDomingo(dia == DiaSemana.DOMINGO);
					cotaCobrancaVO.setSegunda(dia == DiaSemana.SEGUNDA_FEIRA);
					cotaCobrancaVO.setTerca(dia == DiaSemana.TERCA_FEIRA);
				    cotaCobrancaVO.setQuarta(dia == DiaSemana.QUARTA_FEIRA);
					cotaCobrancaVO.setQuinta(dia == DiaSemana.QUINTA_FEIRA);
					cotaCobrancaVO.setSexta(dia == DiaSemana.SEXTA_FEIRA);
					cotaCobrancaVO.setSabado(dia == DiaSemana.SABADO);
					
				}
			}
			
			cotaCobrancaVO.setValorMinimo(parametrosCobranca.getFormaCobranca().getValorMinimoEmissao());
			cotaCobrancaVO.setComissao(cota.getFatorDesconto());
			cotaCobrancaVO.setQtdDividasAberto(parametrosCobranca.getPoliticaSuspensao().getNumeroAcumuloDivida());
			cotaCobrancaVO.setVrDividasAberto(parametrosCobranca.getPoliticaSuspensao().getValor());
		}
		
		return cotaCobrancaVO;
	}
	

	
	
	
	
	
	@Override
	@Transactional
	public void postarDadosCotaCobranca(CotaCobrancaVO cotaCobranca) {
		
		

		//COTA
		Cota cota = cotaRepository.obterPorNumerDaCota(cotaCobranca.getNumCota());
		
 
		
		//PARAMETROS DE COBRANCA DA COTA
		ParametroCobrancaCota parametroCobranca = cota.getParametroCobranca();
		PoliticaSuspensao politicaSuspensao=null;
		FormaCobranca formaCobranca = null;
		
		
		
		
		if (parametroCobranca==null){
			parametroCobranca = new ParametroCobrancaCota();
		}
		else{
			

			
			//POLITICA DE SUSPENSAO DO PARAMETRO DE COBRANCA DA COTA
			politicaSuspensao = parametroCobranca.getPoliticaSuspensao();
			if (politicaSuspensao==null){
				politicaSuspensao = new PoliticaSuspensao();
			}
			politicaSuspensao.setNumeroAcumuloDivida(cotaCobranca.getQtdDividasAberto());
			politicaSuspensao.setValor(cotaCobranca.getVrDividasAberto());
			
			
			
			
			//FORMA DE COBRANCA DO PARAMETRO DE COBRANCA DA COTA
			formaCobranca = parametroCobranca.getFormaCobranca();
			if (formaCobranca==null){
				formaCobranca=new FormaCobranca();
			}
			formaCobranca.setValorMinimoEmissao(cotaCobranca.getValorMinimo());
			formaCobranca.setTipoCobranca(cotaCobranca.getTipoCobranca());
			
			Banco banco = this.bancoRepository.buscarPorId(cotaCobranca.getIdBanco());
			
			formaCobranca.setBanco(banco);
		}

		
		
		
		
		parametroCobranca.setRecebeCobrancaEmail(cotaCobranca.isRecebeEmail());
		parametroCobranca.setFatorVencimento((int) cotaCobranca.getFatorVencimento());
		
		parametroCobranca.setFormaCobranca(formaCobranca);
		parametroCobranca.setPoliticaSuspensao(politicaSuspensao);
		parametroCobranca.setValorMininoCobranca(cotaCobranca.getValorMinimo());
		
		ContaBancaria contaBancariaCota = parametroCobranca.getContaBancariaCota();
		if (contaBancariaCota == null){
			contaBancariaCota = new ContaBancaria();
		}
		contaBancariaCota.setNumeroBanco(cotaCobranca.getNumBanco());
		contaBancariaCota.setNomeBanco(cotaCobranca.getNomeBanco());
		contaBancariaCota.setAgencia(Long.getLong(cotaCobranca.getAgencia()));
		contaBancariaCota.setDvAgencia(cotaCobranca.getAgenciaDigito());
		contaBancariaCota.setConta(Long.getLong(cotaCobranca.getConta()));
		contaBancariaCota.setDvConta(cotaCobranca.getContaDigito());

		
		
		
		
		/*
		//CONCENTRACAO COBRANCA (DIAS DA SEMANA)
		Set<ConcentracaoCobrancaCota> concentracoesCobranca = new HashSet<ConcentracaoCobrancaCota>();
		ConcentracaoCobrancaCota concentracaoCobranca;
		if (cotaCobranca.isDomingo()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (cotaCobranca.isSegunda()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (cotaCobranca.isTerca()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (cotaCobranca.isQuarta()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (cotaCobranca.isQuinta()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (cotaCobranca.isSexta()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (cotaCobranca.isSabado()){
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
			concentracaoCobranca.setParametroCobrancaCota(parametroCobranca);
			this.concentracaoCobrancaCotaRepository.merge(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		parametroCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
		*/
		
		
		
		
		
		cota.setParametroCobranca(parametroCobranca);
		cota.setFatorDesconto(cotaCobranca.getComissao());
		cota.setSugereSuspensao(cotaCobranca.isSugereSuspensao());
		cota.setPossuiContrato(cotaCobranca.isContrato());
		
		this.cotaRepository.merge(cota);
		
	}
	

	
	
	
	
}
