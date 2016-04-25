package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DistribuicaoVendaMediaDTO;
import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.EstudoGeradoPreAnaliseDTO;
import br.com.abril.nds.model.planejamento.EstudoPDV;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoService {
	
	EstudoGeradoPreAnaliseDTO obterEstudoPreAnalise(Long id);
	
	EstudoGerado obterEstudo(Long id);
	
	void gravarEstudo(EstudoGerado estudo);
	
	EstudoGerado criarEstudo(ProdutoEdicao produtoEdicao,BigInteger quantidadeReparte,Date dataLancamento,Long lancamentoId);
	
	Estudo atualizarEstudo(Long estudoId, BigInteger reparteAtualizar);

	ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId, Long codigoProduto, Long numeroEdicao);
	
	void excluirEstudo(long id);
	
	public EstudoGerado obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO);

	public List<Long> salvarDivisao(EstudoGerado estudoOriginal, List<EstudoGerado> listEstudo, DivisaoEstudoDTO divisaoEstudo);
	
	public void setIdLancamentoNoEstudo(Long idLancamento, Long idEstudo);
	
	Estudo liberar(Long idEstudoGerado);

    void gravarDadosVendaMedia(Long estudoId, DistribuicaoVendaMediaDTO distribuicaoVendaMedia);
    
    void gerarEstudoPDV(final EstudoGerado estudo, final Cota cota, final BigInteger reparte);
    
    EstudoPDV gerarEstudoPDV(final EstudoGerado estudo, final Cota cota,final PDV pdv, final BigInteger reparte);

	EstudoCotaGerado obterEstudoCotaGerado(Long cotaId, Long estudoId);

	void criarRepartePorPDV(Long id);

	Long countEstudosPorLancamento(Long idlancamento, Date dataLancamento);
	
	 EstudoGerado obterEstudoSql(Long id);
}
