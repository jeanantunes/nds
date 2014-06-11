package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.TipoMovimentoDTO.GrupoOperacao;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.TipoMovimentoEstoqueRepository;
import br.com.abril.nds.repository.TipoMovimentoRepository;
import br.com.abril.nds.service.TipoMovimentoService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.movimentacao.TipoMovimento}
 * 
 * @author Discover Technology
 */
@Service
public class TipoMovimentoServiceImpl implements TipoMovimentoService {

	@Autowired
	private TipoMovimentoRepository tipoMovimentoRepository;
	
	@Autowired
	private TipoMovimentoEstoqueRepository tipoMovimentoEstoqueRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<TipoMovimento> obterTiposMovimento() {
		
		return tipoMovimentoRepository.obterTiposMovimento();
	}

	@Override
	@Transactional(readOnly = true)
	public List<TipoMovimentoDTO> obterTiposMovimento(FiltroTipoMovimento filtro) {
		
		return tipoMovimentoRepository.obterTiposMovimento(filtro);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Integer countObterTiposMovimento(FiltroTipoMovimento filtro) {		
		return tipoMovimentoRepository.countObterTiposMovimento(filtro);
	}

	@Override
	@Transactional
	public void salvarTipoMovimento(TipoMovimentoDTO tipoMovimentoDTO) {
			
		if(tipoMovimentoDTO == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "tipoMovimentoDTO não deve ser nulo.");
		
		TipoMovimento tipoMovimento = null;
		
		if(GrupoOperacao.ESTOQUE.equals(tipoMovimentoDTO.getGrupoOperacaoValue())) {
			
			tipoMovimento = new TipoMovimentoEstoque(
						tipoMovimentoDTO.getIncideDividaValue().getIncide(), 
						null, 
						OperacaoEstoque.valueOf(tipoMovimentoDTO.getOperacaoValue().name()));
				
		} else if(GrupoOperacao.FINANCEIRO.equals(tipoMovimentoDTO.getGrupoOperacaoValue())) {
			
			tipoMovimento = new TipoMovimentoFinanceiro(
					null, 
					OperacaoFinaceira.valueOf(tipoMovimentoDTO.getOperacaoValue().name()));
			
		} else {
			throw new ValidacaoException(TipoMensagem.WARNING, "Grupo de Operação não existe.");
		}
		
		if(tipoMovimentoDTO.getDescricao() == null || tipoMovimentoDTO.getDescricao().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Descrição deve ser preenchida.");		
		
		tipoMovimento.setDescricao(tipoMovimentoDTO.getDescricao());	
		tipoMovimento.setAprovacaoAutomatica(tipoMovimentoDTO.getAprovacaoValue().getAprovado());		
		
		tipoMovimentoRepository.merge(tipoMovimento);
	}

	@Override
	@Transactional
	public void editarTipoMovimento(TipoMovimentoDTO tipoMovimentoDTO, Usuario usuario) {
		
		if(tipoMovimentoDTO == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "tipoMovimentoDTO não deve ser nulo.");
		
		if(tipoMovimentoDTO.getCodigo() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Código não deve ser nulo.");
		
		TipoMovimento tipoMovimento = tipoMovimentoRepository.buscarPorId(tipoMovimentoDTO.getCodigo());
		
		if(tipoMovimento == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de Movimento não encontrado.");
				
		if(tipoMovimento instanceof TipoMovimentoEstoque) {
			
			if(((TipoMovimentoEstoque) tipoMovimento).isIncideDivida()) {
				//TODO Obter permissão do usuário.
				//throw new ValidacaoException(TipoMensagem.WARNING, "Esta esclusão exige usuário de nível gerencial.");
			}
			
			if ( ((TipoMovimentoEstoque) tipoMovimento).getGrupoMovimentoEstoque() != null) 
				throw new ValidacaoException(TipoMensagem.WARNING, "O Tipo de Movimento foi cadastrado por interface e não pode ser alterado.");
			
			((TipoMovimentoEstoque) tipoMovimento).setIncideDivida(tipoMovimentoDTO.getIncideDividaValue().getIncide());
			((TipoMovimentoEstoque) tipoMovimento).setOperacaoEstoque((OperacaoEstoque.valueOf(tipoMovimentoDTO.getOperacaoValue().name())));
				
		} else if(tipoMovimento instanceof TipoMovimentoFinanceiro) {
			
			if ( ((TipoMovimentoFinanceiro) tipoMovimento).getGrupoMovimentoFinaceiro() != null) 
				throw new ValidacaoException(TipoMensagem.WARNING, "O Tipo de Movimento foi cadastrado por interface e não pode ser alterado.");
			
			((TipoMovimentoFinanceiro) tipoMovimento).setOperacaoFinaceira((OperacaoFinaceira.valueOf(tipoMovimentoDTO.getOperacaoValue().name())));
			
		} else {
			throw new ValidacaoException(TipoMensagem.WARNING, "Grupo de Operação não existe.");
		}
		
		if(tipoMovimentoDTO.getDescricao() == null || tipoMovimentoDTO.getDescricao().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "Descrição deve ser preenchida.");		
		
		tipoMovimento.setDescricao(tipoMovimentoDTO.getDescricao());	
		tipoMovimento.setAprovacaoAutomatica(tipoMovimentoDTO.getAprovacaoValue().getAprovado());		
		
		tipoMovimentoRepository.alterar(tipoMovimento);
		
	}

	@Override
	@Transactional
	public void excluirTipoMovimento(Long codigo, Usuario usuario) {
		
		if(codigo==null) 
			throw new ValidacaoException(TipoMensagem.WARNING,"Código não dever ser nulo.");
		
		TipoMovimento tipoMovimento = tipoMovimentoRepository.buscarPorId(codigo);
		
		if(tipoMovimento instanceof TipoMovimentoEstoque) {
			
			if(((TipoMovimentoEstoque) tipoMovimento).isIncideDivida()) {
				//TODO Obter permissão do usuário.
				//throw new ValidacaoException(TipoMensagem.WARNING, "Esta esclusão exige usuário de nível gerencial.");
			}
			
			if ( ((TipoMovimentoEstoque) tipoMovimento).getGrupoMovimentoEstoque() != null) 
				throw new ValidacaoException(TipoMensagem.WARNING, "O Tipo de Movimento foi cadastrado por interface e não pode ser excluido.");
			
		} else if(tipoMovimento instanceof TipoMovimentoFinanceiro) {
			
			if ( ((TipoMovimentoFinanceiro) tipoMovimento).getGrupoMovimentoFinaceiro() != null) 
				throw new ValidacaoException(TipoMensagem.WARNING, "O Tipo de Movimento foi cadastrado por interface e não pode ser excluido.");
			
		}
			
		
		try {
			tipoMovimentoRepository.remover(tipoMovimento);
		} catch(DataIntegrityViolationException e) {
			throw new ValidacaoException(TipoMensagem.WARNING,"Tipo de Movimento está em uso, não pode ser excluido.");
		}
	}

	@Override
	@Transactional
	public TipoMovimentoEstoque buscarTipoMovimentoEstoque(
			GrupoMovimentoEstoque grupoMovimentoEstoque) {
		return tipoMovimentoEstoqueRepository.buscarTipoMovimentoEstoque(grupoMovimentoEstoque);
	}

	@Override
	@Transactional
	public List<TipoMovimentoEstoque> buscarTiposMovimentoEstoque(
			List<GrupoMovimentoEstoque> gruposMovimentoEstoque) {
		return tipoMovimentoEstoqueRepository.buscarTiposMovimentoEstoque(gruposMovimentoEstoque);
	}
}
