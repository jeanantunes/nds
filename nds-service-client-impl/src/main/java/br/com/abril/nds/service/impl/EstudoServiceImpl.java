package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.util.DateUtil;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 *
 */
@Service
public class EstudoServiceImpl implements EstudoService {
	
	@Autowired
	private EstudoGeradoRepository estudoGeradoRepository;
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private EstudoCotaRepository estudoCotaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    @Autowired
    private LancamentoService lancamentoService;

	@Transactional(readOnly = true)
	@Override
	public EstudoGerado obterEstudo(Long id) {
		return this.estudoGeradoRepository.buscarPorId(id);
	}

	@Override
	@Transactional
	public void gravarEstudo(EstudoGerado estudo) {
	    
		estudo.setId(this.obterUltimoAutoIncrement());
		
	    for (EstudoCota estudoCota : estudo.getEstudoCotas()) {
			estudoCota.setEstudo(estudo);
	    }
	    
	    estudoGeradoRepository.adicionar(estudo);
	}

	@Override
	@Transactional
	public void excluirEstudosAnoPassado() {
		
		Calendar c = Calendar.getInstance();
		
		c.set(Calendar.HOUR_OF_DAY,   0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
		
		Date dataStart = c.getTime();
		
		c.set(Calendar.HOUR_OF_DAY,   23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MONTH, Calendar.DECEMBER);
		c.set(Calendar.DAY_OF_MONTH, 31);
		
		Date dataEnd = c.getTime();
		
		System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(dataStart));
		System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(dataEnd));
		
		List<EstudoGerado> listEstudos = estudoGeradoRepository.obterEstudosPorIntervaloData(dataStart, dataEnd);
		
		for (EstudoGerado estudo:listEstudos) {
			
			try {
				
				estudoGeradoRepository.remover(estudo);
			} catch (Exception e) {
				
				System.out.println("Erro ao excluir estudo:" + estudo.getId());
				e.printStackTrace();
			}
			
		}
	}

	@Override
	@Transactional
	public ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId) {
		return estudoGeradoRepository.obterResumoEstudo(estudoId);
	}

	@Override
	@Transactional
	public void excluirEstudo(long id) {
		this.estudoGeradoRepository.removerPorId(id);
	}

	@Override
	@Transactional
	public void criarNovoEstudo(ProdutoDistribuicaoVO produto) {
	    EstudoGerado estudo = new EstudoGerado();
	    estudo.setLiberado(false);
	    estudo.setReparteDistribuir(produto.getRepDistrib());
	    estudo.setDataLancamento(produto.getDataLanctoSemFormatacao());
	    estudo.setDataCadastro(new Date());
	    estudo.setDistribuicaoPorMultiplos(0);
	    estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
	    estudo.setQtdeReparte(produto.getRepDistrib());
	    estudo.setProdutoEdicao(new ProdutoEdicao(produto.getIdProdutoEdicao().longValue()));
	    estudoGeradoRepository.adicionar(estudo);
	    produto.setIdEstudo(BigInteger.valueOf(estudo.getId()));
	}
	
    @Transactional(readOnly = true)
    public EstudoGerado obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO) {

	return this.estudoGeradoRepository.obterEstudoByEstudoOriginalFromDivisaoEstudo(divisaoEstudoVO);
    }

    @Transactional
	public List<Long> salvarDivisao(EstudoGerado estudoOriginal,List<EstudoGerado> listEstudo,DivisaoEstudoDTO divisaoEstudo) {

		List<Long> listIdEstudoAdicionado = null;

		EstudoGerado obterEstudo = this.obterEstudo(listEstudo.get(0).getId());
		EstudoGerado obterEstudo2 = this.obterEstudo(listEstudo.get(1).getId());
		
		if(obterEstudo!=null && obterEstudo2!=null ){
			throw new ValidacaoException(TipoMensagem.WARNING, " Número dos estudo gerados já estão sendo utilizados.");
		}
		
		if (listEstudo != null && !listEstudo.isEmpty()) {

			// 2 estudo para ser salvo
			EstudoGerado segundoEstudo = listEstudo.get(1);

			// verificando existencia de lancamentos na data de lancamento
			// informada em tela para produto_edicao do estudo original
			Lancamento lancamentoSegundoEstudo = this.lancamentoRepository
					.buscarPorDataLancamentoProdutoEdicao(segundoEstudo
							.getDataLancamento(), segundoEstudo
							.getProdutoEdicao().getId());

			if(lancamentoSegundoEstudo==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Não foram encontrados lançamentos para data "+DateUtil.formatarDataPTBR(segundoEstudo.getDataLancamento()));
			}
			
			listIdEstudoAdicionado = new ArrayList<Long>();
			Integer quantidadeReparte = (divisaoEstudo.getQuantidadeReparte()==null)?0:divisaoEstudo.getQuantidadeReparte();

			List<EstudoCota> listEstudoCota = this.estudoCotaRepository.obterEstudoCotaPorEstudo(estudoOriginal);

			int iEstudo = 0;
			HashMap<Long,BigInteger> diffEstudosMap = new HashMap<Long,BigInteger>();

			for (EstudoGerado estudo : listEstudo) {

				estudo.setDataLancamento(null);
				Set<EstudoCota> setEstudoCota = new HashSet<EstudoCota>();
				
				for (EstudoCota ec : listEstudoCota) {
					
					EstudoCota estudoCota = (EstudoCota) SerializationUtils.clone(ec);
					estudoCota.setId(null);
					estudoCota.setEstudo(estudo);

					//nao dividir reparte menor que o informado em tela
					if(estudoCota.getReparte()!=null && estudoCota.getReparte().compareTo(new BigInteger(quantidadeReparte.toString()))>0){
						//Primeiro estudo gerado
						if(iEstudo==0){
							BigDecimal toDivide = null;
							BigDecimal i = new BigDecimal(estudoCota.getReparte());
							toDivide = new BigDecimal(divisaoEstudo.getPercentualDivisaoPrimeiroEstudo()).divide(new BigDecimal("100"));
							BigInteger bigInteger = i.multiply(toDivide).setScale(0,BigDecimal.ROUND_HALF_UP).toBigInteger();
							estudoCota.setReparte(bigInteger);
							
							diffEstudosMap.put(ec.getId(), ec.getReparte().subtract(bigInteger));
							
							/*BigDecimal bd  = new BigDecimal(3);
							BigDecimal toDivide  = new BigDecimal(50).divide(new BigDecimal("100"));
							System.out.println(bd.multiply(toDivide).setScale(0,BigDecimal.ROUND_HALF_UP));*/
							
						}else{
							//Segundo estudo gerado
//							toDivide = new BigDecimal(divisaoEstudo.getPercentualDivisaoSegundoEstudo()).divide(new BigDecimal("100"));
							estudoCota.setReparte(diffEstudosMap.get(ec.getId()));
						}
					
						setEstudoCota.add(estudoCota);
						
					}else{
						
						if(iEstudo==1 ){
							estudoCota.setReparte(BigInteger.ZERO);
						}
						setEstudoCota.add(estudoCota);
					}
				}

				BigInteger somarReparteParaEstudo = BigInteger.ZERO;
				for (EstudoCota estudoCota : setEstudoCota) {
					BigInteger r = estudoCota.getReparte()==null?BigInteger.ZERO:estudoCota.getReparte();
					somarReparteParaEstudo=somarReparteParaEstudo.add(r);
					estudoCota.setQtdeEfetiva(estudoCota.getReparte());
					estudoCota.setQtdePrevista(estudoCota.getReparte());
				}
				estudo.setQtdeReparte(somarReparteParaEstudo);
				estudo.setEstudoCotas(setEstudoCota);

				// Estudo 1 possui o mesmo lancamentoID do estudo original
				if (iEstudo == 0) {
					estudo.setLancamentoID(estudoOriginal.getLancamentoID());
				}
				else{
					estudo.setLancamentoID(lancamentoSegundoEstudo.getId());
				}
				
				
				this.estudoGeradoRepository.adicionar(estudo);
				listIdEstudoAdicionado.add(estudo.getId());

				iEstudo++;

			}

		}
		
		return listIdEstudoAdicionado;
	}

	@Override
	@Transactional
	public void setIdLancamentoNoEstudo(Long idLancamento, Long idEstudo) {
		this.estudoGeradoRepository.setIdLancamentoNoEstudo(idLancamento, idEstudo);
	}

	@Override
	@Transactional(readOnly = true)
	public Long obterUltimoAutoIncrement() {
		return this.estudoGeradoRepository.obterUltimoAutoIncrement();
		
	}

	@Transactional
	public EstudoGerado criarEstudo(ProdutoEdicao produtoEdicao,BigInteger quantidadeReparte,Date dataLancamento){
		
		Date dataOperacao = distribuidorRepository.obterDataOperacaoDistribuidor();
		
		EstudoGerado estudo = new EstudoGerado();
		
		estudo.setId(this.obterUltimoAutoIncrement());
		estudo.setDataCadastro(dataOperacao);
		estudo.setDataLancamento(dataLancamento);
		estudo.setProdutoEdicao(produtoEdicao);
		estudo.setQtdeReparte(quantidadeReparte);
		estudo.setReparteDistribuir(quantidadeReparte);
		estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
		
		return estudoGeradoRepository.merge(estudo);
	}
	
	@Transactional
	public EstudoGerado liberar(Long idEstudoGerado) {
		
		EstudoGerado estudoGerado = this.estudoGeradoRepository.buscarPorId(idEstudoGerado);
		
		estudoGerado.setLiberado(true);
		
		this.estudoGeradoRepository.alterar(estudoGerado);
		
		this.estudoGeradoRepository.flush();
		this.estudoGeradoRepository.clear();
		
		return estudoGerado;
	}
	
	@Transactional
	public Estudo criarEstudoLiberado(EstudoGerado estudoGerado) {

		Estudo estudo = new Estudo();
		
		try {
			
			PropertyUtils.copyProperties(estudo, estudoGerado);
			
		} catch (Exception e) {

			throw new RuntimeException(e);
		}

		try {
			
			this.estudoRepository.adicionar(estudo);
			
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		
		return estudo;
	}
	
	
}
