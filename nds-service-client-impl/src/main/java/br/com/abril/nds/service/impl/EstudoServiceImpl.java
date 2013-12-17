package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EstudoRepository;
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
	private DistribuidorRepository distribuidorRepository;

	@Transactional(readOnly = true)
	public Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao) {
		
		return this.estudoRepository.obterEstudoDoLancamentoPorDataProdutoEdicao(dataReferencia, idProdutoEdicao);
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
	
	@Transactional
	public Estudo criarEstudo(ProdutoEdicao produtoEdicao,BigInteger quantidadeReparte,Date dataLancamento){
		
		Date dataOperacao = distribuidorRepository.obterDataOperacaoDistribuidor();
		
		Estudo estudo = new Estudo();
		estudo.setDataCadastro(dataOperacao);
		estudo.setDataLancamento(dataLancamento);
		estudo.setProdutoEdicao(produtoEdicao);
		estudo.setQtdeReparte(quantidadeReparte);
		estudo.setReparteDistribuir(quantidadeReparte);
		estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
		
		return estudoRepository.merge(estudo);
	}
	
	
}
