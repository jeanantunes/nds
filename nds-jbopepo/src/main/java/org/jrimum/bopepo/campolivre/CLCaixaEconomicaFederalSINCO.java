/*
 * Copyright 2008 JRimum Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * Created at: 30/03/2008 - 18:09:45
 * 
 * ================================================================================
 * 
 * Direitos autorais 2008 JRimum Project
 * 
 * Licenciado sob a Licença Apache, Versão 2.0 ("LICENÇA"); você não pode usar
 * esse arquivo exceto em conformidade com a esta LICENÇA. Você pode obter uma
 * cópia desta LICENÇA em http://www.apache.org/licenses/LICENSE-2.0 A menos que
 * haja exigência legal ou acordo por escrito, a distribuição de software sob
 * esta LICENÇA se dará “COMO ESTÁ”, SEM GARANTIAS OU CONDIÇÕES DE QUALQUER
 * TIPO, sejam expressas ou tácitas. Veja a LICENÇA para a redação específica a
 * reger permissões e limitações sob esta LICENÇA.
 * 
 * Criado em: 30/03/2008 - 18:09:45
 * 
 */

package org.jrimum.bopepo.campolivre;

import static org.jrimum.vallia.digitoverificador.Modulo.MOD11;

import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.utilix.text.Field;
import org.jrimum.utilix.text.Filler;
import org.jrimum.vallia.digitoverificador.Modulo;

/**
 * <p>
 * O campo livre para o modelo SINCO segue esta forma:
 * </p>
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
 * collapse" bordercolor="#111111" width="60%" id="campolivre">
 * <thead>
 * <tr>
 * <td>Posição</td>
 * <td>Tamanho</td>
 * <td>Conteúdo</td>
 * </tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td>01</td>
 * <td>1</td>
 * <td>Número "1" (valor fixo)</td>
 * </tr>
 * <tr>
 * <td>02-07</td>
 * <td>6</td>
 * <td>Código do cliente CEDENTE (fornecido pela CAIXA)</td>
 * </tr>
 * <tr>
 * <td>08</td>
 * <td>1</td>
 * <td>Número "9" (valor fixo)</td>
 * </tr>
 * <tr>
 * <td>09-25</td>
 * <td>17</td>
 * <td>Posições livres do "nosso número"</td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L</a>
 * @author <a href="mailto:misaelbarreto@gmail.com">Misael Barreto</a> 
 * @author <a href="mailto:romulomail@gmail.com">Rômulo Augusto</a>
 * @author <a href="http://www.nordestefomento.com.br">Nordeste Fomento Mercantil</a>
 * 
 * @since 0.2
 * 
 * @version 0.2
 */
class CLCaixaEconomicaFederalSINCO extends AbstractCLCaixaEconomicaFederal {

	private static final long serialVersionUID = -7642075752245778160L;
	
	/**
	 * Constante que indica emissão de boleto pelo cedente. 
	 */
	private static final int EMISSAO_CEDENTE = 4;
	
	/**
	 * Quantidade de campos. Tamanho da lista de campos.
	 */
	private static final Integer FIELDS_LENGTH = 8;
	
	/**
	 * Modalidade de cobrança.
	 */
	private static final int COBRANCA_REGISTRADA = 1;
	
	/**
	 * Modalidade de cobrança.
	 */
	private static final int COBRANCA_NAO_REGISTRADA = 2;
	
	/**
	 * <p>
	 * Dado um título, cria um campo livre para o padrão do Banco Caixa Econômica
	 * Federal que tenha o serviço SINCO.
	 * </p>
	 * @param titulo Título com as informações para geração do campo livre.
	 */
	CLCaixaEconomicaFederalSINCO(Titulo titulo) {
		super(FIELDS_LENGTH);

		ContaBancaria conta = titulo.getContaBancaria();
		String nossoNumero = titulo.getNossoNumero();

		Integer dVCodigoDoCedente = calculeDigitoVerificador(conta.getNumeroDaConta().getCodigoDaConta().toString());

		this.add(new Field<Integer>(conta.getNumeroDaConta().getCodigoDaConta(), 6, Filler.ZERO_LEFT));
		this.add(new Field<Integer>(dVCodigoDoCedente, 1));
		this.add(new Field<String>(nossoNumero.substring(2, 5), 3));
		
		if(conta.getCarteira().isComRegistro()){
			
			this.add(new Field<Integer>(COBRANCA_REGISTRADA, 1));
			
		}else{
			
			this.add(new Field<Integer>(COBRANCA_NAO_REGISTRADA, 1));
		}

		this.add(new Field<String>(nossoNumero.substring(3, 6), 3));
		this.add(new Field<Integer>(EMISSAO_CEDENTE, 1));
		this.add(new Field<String>(nossoNumero.substring(8, 17), 9));

		this.add(new Field<Integer>(calculeDigitoVerificador(gereCampoLivre()), 1));
	}
	
	/**
	 * <p>
	 * Este dígito é calculado através do Módulo 11 com os pesos 2 e 9.
	 * </p>
	 * 
	 * @param numeroParaCalculo
	 * @return digito
	 * 
	 * @since 0.2
	 */
	private int calculeDigitoVerificador(String numeroParaCalculo) {
		
		int soma = Modulo.calculeSomaSequencialMod11(numeroParaCalculo.toString(), 2, 9);

		int dvCampoLivre;
		
		if (soma < MOD11) {
			
			dvCampoLivre = MOD11 - soma;
			
		} else {
		
			int restoDiv11 = soma % MOD11;
			
			int subResto = MOD11 - restoDiv11;
			
			if (subResto > 9) {
			
				dvCampoLivre = 0;
			
			} else {
				
				dvCampoLivre = subResto;
			}
		}
		
		return dvCampoLivre;
	}
	
	
	/**
	 * <p>
	 * Gera o número que serve para calcular o digito verificador do campoLivre, que é todo o campo livre menos o dígito verificador.
	 * </p>
	 * <p>
	 * Os campos utilizados são:
	 * <ul>
	 * <li>Código do Cedente: 06 posições</li>
	 * <li>Dígito Verificador do Código do Cedente: 01 posição</li>
	 * <li>Nosso Número – Seqüência 1: 03 posições</li>
	 * <li>Constante 1: 01 posição</li>
	 * <li>Nosso Número – Seqüência 2: 03 posições</li>
	 * <li>Constante 2: 01 posição</li>
	 * <li>Nosso Número – Seqüência 3: 09 posições</li>
	 * </ul>
	 * </p>
	 * 
	 * @param titulo
	 *            - Título com os dados para geração do campo livre.
	 * @param dVCodigoDoCedente
	 *            - Dígito verificador do código do cedente.
	 * @return String com os dígitos que compõem o campo livro, exceto o dígito verificador.
	 * 
	 * @since 0.2
	 */
	private String gereCampoLivre() {

		return writeFields();
	}
	
	@Override
	protected void addFields(Titulo titulo) {
		// TODO IMPLEMENTAR
		throw new UnsupportedOperationException("AINDA NÃO IMPLEMENTADO!");
	}

	@Override
	protected void checkValues(Titulo titulo) {
		// TODO IMPLEMENTAR
		throw new UnsupportedOperationException("AINDA NÃO IMPLEMENTADO!");
	}
}
