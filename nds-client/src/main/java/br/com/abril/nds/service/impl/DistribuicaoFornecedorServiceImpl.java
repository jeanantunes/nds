package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroDiaOperacaoFornecedorVO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.service.DistribuicaoFornecedorService;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.DistribuicaoFornecedor}
 * @author InfoA2
 */
@Service
public class DistribuicaoFornecedorServiceImpl implements DistribuicaoFornecedorService {

	Map<Enum<DiaSemana>, String> diasMaps;
	
	private static final String DOMINGO = "Dom";
	private static final String SEGUNDA = "Seg";
	private static final String TERCA   = "Ter";
	private static final String QUARTA  = "Qua";
	private static final String QUINTA  = "Qui";
	private static final String SEXTA   = "Sex";
	private static final String SABADO  = "Sab";
	
	/**
	 * Construtor padrão da classe. Popula o Map que será utilizado para atualizar o dia da semana
	 */
	public DistribuicaoFornecedorServiceImpl() {
		diasMaps = new TreeMap<Enum<DiaSemana>, String>();
		diasMaps.put(DiaSemana.DOMINGO, 	  DOMINGO);
		diasMaps.put(DiaSemana.SEGUNDA_FEIRA, SEGUNDA);
		diasMaps.put(DiaSemana.TERCA_FEIRA,   TERCA);
		diasMaps.put(DiaSemana.QUARTA_FEIRA,  QUARTA);
		diasMaps.put(DiaSemana.QUINTA_FEIRA,  QUINTA);
		diasMaps.put(DiaSemana.SEXTA_FEIRA,   SEXTA);
		diasMaps.put(DiaSemana.SABADO,        SABADO);
	}
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;

	@Autowired
	private FornecedorRepository fornecedorRepository;

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.DistribuicaoFornecedorService#buscarTodos()
	 */
	@Transactional(readOnly = true)
	@Override
	public List<RegistroDiaOperacaoFornecedorVO> buscarDiasOperacaoFornecedor() {

		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor = distribuicaoFornecedorRepository.obterTodosOrdenadoId();
		List<RegistroDiaOperacaoFornecedorVO> listaRegistros = new ArrayList<RegistroDiaOperacaoFornecedorVO>();

		DistribuicaoFornecedor distribuidorFornecedor = null;
		Fornecedor fornecedor = null;
		RegistroDiaOperacaoFornecedorVO registro = null;

		StringBuilder diasLancamento = new StringBuilder();
		StringBuilder diasRecolhimento = new StringBuilder();
		
		// Popula uma lista de diasOperacaoFornecedor
		int i=0;
		while (i < listaDistribuicaoFornecedor.size()) {
			distribuidorFornecedor = listaDistribuicaoFornecedor.get(i);
			
			if ( fornecedor == null ) {
				registro = new RegistroDiaOperacaoFornecedorVO();
				fornecedor = distribuidorFornecedor.getFornecedor();
			}

			// Caso o fornecedor seja o mesmo do registro anterior, armazena os dias no registro
			if ( fornecedor.equals(distribuidorFornecedor.getFornecedor()) ) {
				// Caso seja enum DISTRIBUICAO, atualiza os diasLancamento, caso contrario, atualiza os diasRecolhimento
				if (distribuidorFornecedor.getOperacaoDistribuidor() == OperacaoDistribuidor.DISTRIBUICAO) {
					diasLancamento = atualizaDias(diasLancamento, distribuidorFornecedor);
				} else {
					diasRecolhimento = atualizaDias(diasRecolhimento, distribuidorFornecedor);
				}	

			// Caso não seja o mesmo fornecedor, armazena o registro anterior na lista e cria um novo registro
			} else {
				registro.setFornecedor(fornecedor);
				registro.setDiasLancamento(diasLancamento.toString());
				registro.setDiasRecolhimento(diasRecolhimento.toString());
				listaRegistros.add(registro);
				
				registro = new RegistroDiaOperacaoFornecedorVO();
				diasLancamento = new StringBuilder();
				diasRecolhimento = new StringBuilder();

				if (distribuidorFornecedor.getOperacaoDistribuidor() == OperacaoDistribuidor.DISTRIBUICAO) {
					diasLancamento = atualizaDias(diasLancamento, distribuidorFornecedor);
				} else {
					diasRecolhimento = atualizaDias(diasRecolhimento, distribuidorFornecedor);
				}	
				
				fornecedor = distribuidorFornecedor.getFornecedor();
			}

			// Caso seja o último registro, armazena o registro na lista
			if (i == (listaDistribuicaoFornecedor.size()-1) ) {
				registro.setFornecedor(fornecedor);
				registro.setDiasLancamento(diasLancamento.toString());
				registro.setDiasRecolhimento(diasRecolhimento.toString());
				listaRegistros.add(registro);				
			}
			
			i++;

		}
		
		return listaRegistros;
	}

	/**
	 * Atualiza os dias baseado no Map criado no construtor da classe
	 * @param dias
	 * @param distribuidorFornecedor
	 * @return
	 */
	private StringBuilder atualizaDias(StringBuilder dias, DistribuicaoFornecedor distribuidorFornecedor) {
		if (!dias.toString().isEmpty())
			dias.append(" - ");
		dias.append(diasMaps.get(distribuidorFornecedor.getDiaSemana()));
		return dias;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.DistribuicaoFornecedorService#excluirDadosFornecedor(long)
	 */
	@Transactional(readOnly = true)
	@Override
	public void excluirDadosFornecedor(long codigoFornecedor) {
		distribuicaoFornecedorRepository.excluirDadosFornecedor(fornecedorRepository.buscarPorId(codigoFornecedor));
	}
	
}
