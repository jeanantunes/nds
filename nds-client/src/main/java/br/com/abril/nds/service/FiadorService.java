package br.com.abril.nds.service;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.model.cadastro.EnderecoFiador;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Garantia;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.TelefoneFiador;

public interface FiadorService {

	ConsultaFiadorDTO obterFiadores(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO);
	
	void cadastrarFiador(Fiador fiador,
			List<Pessoa> sociosAidcionar,
			Set<Long> sociosRemover,
			List<EnderecoAssociacaoDTO> listaEnderecosAdicionar,
			List<EnderecoAssociacaoDTO> listaEnderecosRemover,
			List<TelefoneAssociacaoDTO> listaTelefoneAdicionar,
			Set<Long> listaTelefoneRemover,
			List<Garantia> listaGarantiaAdicionar,
			Set<Long> listaGarantiaRemover, 
			List<Integer> listaCotasAssociar,
			Set<Integer> listaCotasDesassociar);
	
	Pessoa buscarPessoaFiadorPorId(Long idFiador);

	void excluirFiador(Long idFiador);
	
	List<TelefoneAssociacaoDTO> buscarTelefonesFiador(Long idFiador, Set<Long> idsIgnorar);

	List<EnderecoAssociacaoDTO> buscarEnderecosFiador(Long idFiador, Set<Long> idsIgnorar);

	TelefoneFiador buscarTelefonePorTelefoneFiador(Long idFiador, Long idTelefone);

	EnderecoFiador buscarEnderecoPorEnderecoFiador(Long idFiador, Long idEndereco);
}
