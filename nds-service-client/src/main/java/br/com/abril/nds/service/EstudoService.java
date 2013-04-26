package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.Estudo;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoService {
	
	Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao);

	Estudo obterEstudo(Long id);
	
	void gravarEstudo(Estudo estudo);

	public abstract void excluirEstudosAnoPassado();

	ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId);
	
	void excluirEstudo(long id);

	void criarNovoEstudo(ProdutoDistribuicaoVO produto);
	
	public Estudo obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO);
	
	public Long obterMaxId();

	public List<Long> salvarDivisao(Estudo estudoOriginal, List<Estudo> listEstudo);

	public void alterarEstudo(Estudo estudo);
	
	public void setIdLancamentoNoEstudo(Long idLancamento, Long idEstudo);
}
