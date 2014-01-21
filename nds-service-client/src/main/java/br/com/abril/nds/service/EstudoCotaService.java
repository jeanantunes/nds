package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.EstudoCota}.  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoCotaService {
	
	EstudoCota obterEstudoCota(Integer numeroCota, Date dataReferencia);
	
	EstudoCota obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, Long idProdutoEdicao, Integer numeroCota);
	
	EstudoCota criarEstudoCotaJuramentado(ProdutoEdicao produtoEdicao, Estudo estudo, BigInteger reparte,Cota cota);

	List<EstudoCota> obterEstudosCota(Long idEstudo);
}
