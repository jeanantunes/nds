package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroNaturezaOperacaoDTO;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.repository.SerieRepository;
import br.com.abril.nds.service.NaturezaOperacaoService;

@Service
public class NaturezaOperacaoServiceImpl implements NaturezaOperacaoService {

	@Autowired
	private NaturezaOperacaoRepository naturezaOperacaoRepository;

	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired 
	private SerieRepository serieRepository;

	@Override
	@Transactional
	public NaturezaOperacao obterPorId(Long id) {

		return this.naturezaOperacaoRepository.buscarPorId(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer obterQuantidadeNaturezasOperacoes(FiltroNaturezaOperacaoDTO filtro) {
		return naturezaOperacaoRepository.obterQuantidadeTiposNotasFiscais(filtro);
	}


	/**
	 * 
	 */
	@Override
	@Transactional
	public Long proximoNumeroDocumentoFiscal(int serie) {		
		return serieRepository.next(serie);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoNotaFiscalService#carregarComboTiposNotasFiscaisPorFornecedores(java.util.List)
	 */
	@Override
	@Transactional
	public List<ItemDTO<Long, String>> carregarComboNaturezasOperacoes(TipoAtividade tipoAtividade) {

		List<NaturezaOperacao> listaNaturezasOperacoes = this.obterNaturezasOperacoes(tipoAtividade);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (NaturezaOperacao no : listaNaturezasOperacoes) {

			ItemDTO<Long, String> itemNotaFiscal = new ItemDTO<Long, String>();

			itemNotaFiscal.setKey(no.getId());
			itemNotaFiscal.setValue(no.getDescricao()); // + " - " + tipoNotaFiscal.getEmitente());

			listaItensNotasFiscais.add(itemNotaFiscal);
		}

		return listaItensNotasFiscais;
	}
	
	@Override
	@Transactional
	public List<NaturezaOperacao> obterNaturezasOperacoes(TipoAtividade tipoAtividade){
		
		if (tipoAtividade == null) {
			
			tipoAtividade = this.distribuidorRepository.tipoAtividade();
		}
		
		return this.naturezaOperacaoRepository.obterNaturezasOperacoesCotasNaoContribuintesPor(tipoAtividade);
	}

	@Override
	@Transactional
	public List<ItemDTO<Long, String>> carregarComboNaturezasOperacoes(TipoOperacao tipoOperacao) {

		if(!this.distribuidorRepository.obrigacaoFiscal()) {
			return null;
		}

		List<NaturezaOperacao> listaNaturezasOperacoes = this.naturezaOperacaoRepository.obterNaturezasOperacoes(tipoOperacao);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (NaturezaOperacao no : listaNaturezasOperacoes) {

			ItemDTO<Long, String> itemNotaFiscal = new ItemDTO<Long, String>();

			itemNotaFiscal.setKey(no.getId());
			itemNotaFiscal.setValue(no.getDescricao());

			listaItensNotasFiscais.add(itemNotaFiscal);
		}

		return listaItensNotasFiscais;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<NaturezaOperacao> obterNaturezasOperacoesPorTipoAtividadeDistribuidor() {

		return naturezaOperacaoRepository.obterNaturezasOperacoesPorTipoAtividadeDistribuidor(
				this.distribuidorRepository.tipoAtividade());
	}

	@Override
	@Transactional
	public List<NaturezaOperacao> consultarNaturezasOperacoes(FiltroNaturezaOperacaoDTO filtro) {
		
		List<NaturezaOperacao> naturezasOperacao = naturezaOperacaoRepository.consultarNaturezaOperacao(filtro);
		
		for(NaturezaOperacao no : naturezasOperacao) {
			if(no.getProcesso() != null) no.getProcesso().isEmpty();
		}
		
		return naturezasOperacao;
	}

	@Override
	@Transactional
	public List<ItemDTO<Long, String>> carregarComboNaturezasOperacoes(TipoOperacao tipoOperacao,
			TipoUsuarioNotaFiscal tipoDestinatario, TipoUsuarioNotaFiscal tipoEmitente,
			GrupoNotaFiscal[] grupoNotaFiscal) {

		List<NaturezaOperacao> listaNaturezasOperacoes = this.naturezaOperacaoRepository.obterNaturezasOperacoes(tipoOperacao, tipoDestinatario, tipoEmitente, grupoNotaFiscal);

		List<ItemDTO<Long, String>> listaItensNotasFiscais = new ArrayList<ItemDTO<Long,String>>();

		for (NaturezaOperacao no : listaNaturezasOperacoes) {

			ItemDTO<Long, String> itemNotaFiscal = new ItemDTO<Long, String>();

			itemNotaFiscal.setKey(no.getId());
			itemNotaFiscal.setValue(no.getDescricao());

			listaItensNotasFiscais.add(itemNotaFiscal);
		}

		return listaItensNotasFiscais;

	}
	
	@Override
	@Transactional
	public NaturezaOperacao obterNaturezaOperacaoPorId(Long idNaturezaOperacao) {
		return naturezaOperacaoRepository.buscarPorId(idNaturezaOperacao);
	}

}