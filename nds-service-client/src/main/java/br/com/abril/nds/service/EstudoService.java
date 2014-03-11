package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoGerado;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoService {
	
	EstudoGerado obterEstudo(Long id);
	
	void gravarEstudo(EstudoGerado estudo);
	
	EstudoGerado criarEstudo(ProdutoEdicao produtoEdicao,BigInteger quantidadeReparte,Date dataLancamento,Long lancamentoId);

	ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId);
	
	void excluirEstudo(long id);
	
	public EstudoGerado obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO);

	public List<Long> salvarDivisao(EstudoGerado estudoOriginal, List<EstudoGerado> listEstudo, DivisaoEstudoDTO divisaoEstudo);
	
	public void setIdLancamentoNoEstudo(Long idLancamento, Long idEstudo);
	
	public Long obterUltimoAutoIncrement();
	
	Estudo liberar(Long idEstudoGerado);
	
}
