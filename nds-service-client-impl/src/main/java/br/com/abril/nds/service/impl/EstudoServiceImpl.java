package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.EstudoService;

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
	private EstudoRepository estudoRepository;

	@Autowired
	private EstudoCotaRepository estudoCotaRepository;

        @Autowired
        private LancamentoRepository lancamentoRepository;

	@Transactional(readOnly = true)
	public Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao) {
		
		return this.estudoRepository.obterEstudoDoLancamentoPorDataProdutoEdicao(dataReferencia, idProdutoEdicao);
	}

	@Transactional(readOnly = true)
	@Override
	public Estudo obterEstudo(Long id) {
		return this.estudoRepository.buscarPorId(id);
	}

	@Override
	public void gravarEstudo(Estudo estudo) {
	    estudoRepository.adicionar(estudo);
	    for (EstudoCota estudoCota : estudo.getEstudoCotas()) {
		estudoCota.setEstudo(estudo);
		estudoCotaRepository.adicionar(estudoCota);
	    }
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
		
		List<Estudo> listEstudos = estudoRepository.obterEstudosPorIntervaloData(dataStart, dataEnd);
		
		for (Estudo estudo:listEstudos) {
			
			try {
				
				estudoRepository.remover(estudo);
			} catch (Exception e) {
				
				System.out.println("Erro ao excluir estudo:" + estudo.getId());
				e.printStackTrace();
			}
			
		}
	}

	@Override
	@Transactional
	public ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId) {
		return estudoRepository.obterResumoEstudo(estudoId);
	}

	@Override
	@Transactional
	public void excluirEstudo(long id) {
		this.estudoRepository.removerPorId(id);
	}

	@Override
	public void criarNovoEstudo(ProdutoDistribuicaoVO produto) {
	    Estudo estudo = new Estudo();
	    estudo.setLiberado(0);
	    estudo.setReparteDistribuir(produto.getRepDistrib());
	    estudo.setDataLancamento(produto.getDataLanctoSemFormatacao());
	    estudo.setDataCadastro(new Date());
	    estudo.setDistribuicaoPorMultiplos(0);
	    estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
	    estudo.setQtdeReparte(produto.getRepDistrib());
	    estudo.setProdutoEdicao(new ProdutoEdicao(produto.getIdProdutoEdicao().longValue()));
	    estudoRepository.adicionar(estudo);
	    produto.setIdEstudo(BigInteger.valueOf(estudo.getId()));
	}
	
    @Transactional(readOnly = true)
    public Estudo obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO) {

	return this.estudoRepository.obterEstudoByEstudoOriginalFromDivisaoEstudo(divisaoEstudoVO);
    }

    @Transactional(readOnly = true)
    public Long obterMaxId() {
	return this.estudoRepository.obterMaxId();
    }

    @Transactional
    public List<Long> salvarDivisao(Estudo estudoOriginal, List<Estudo> listEstudo) {

	List<Long> listIdEstudoAdicionado = null;

	if (listEstudo != null && !listEstudo.isEmpty()) {

	    listIdEstudoAdicionado = new ArrayList<Long>();

	    List<EstudoCota> listEstudoCota = this.estudoCotaRepository.obterEstudoCotaPorEstudo(estudoOriginal);
	    List<Lancamento> listLancamento = this.lancamentoRepository.obterPorEstudo(estudoOriginal);

	    int iEstudo = 0;
	    while (iEstudo < listEstudo.size()) {

		Estudo estudo = listEstudo.get(iEstudo);

		Set<Lancamento> setLancamento = new HashSet<Lancamento>();
		Set<EstudoCota> setEstudoCota = new HashSet<EstudoCota>();

		int iEstudoCota = 0;
		while (iEstudoCota < listEstudoCota.size()) {

		    EstudoCota estudoCota = (EstudoCota) SerializationUtils.clone(listEstudoCota.get(iEstudoCota));
		    estudoCota.setId(null);
		    estudoCota.setEstudo(estudo);

		    setEstudoCota.add(estudoCota);

		    iEstudoCota++;
		}

		estudo.setEstudoCotas(setEstudoCota);

		int iLancamento = 0;
		while (iLancamento < listLancamento.size()) {

		    Lancamento lancamento = (Lancamento) SerializationUtils.clone(listLancamento.get(iLancamento));
		    lancamento.setId(null);
		    lancamento.setEstudo(estudo);

		    setLancamento.add(lancamento);

		    iLancamento++;
		}

		estudo.setLancamentos(setLancamento);

		Long idEstudo = this.estudoRepository.adicionar(estudo);

		listIdEstudoAdicionado.add(idEstudo);

		iEstudo++;
	    }
	}

	return listIdEstudoAdicionado;
    }
	
}
