package org.jrimum.bopepo.campolivre;

import static org.jrimum.vallia.digitoverificador.Modulo.MOD11;

import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.utilix.text.Field;
import org.jrimum.utilix.text.Filler;
import org.jrimum.vallia.digitoverificador.Modulo;

/**
 * 
 * O campo livre do Banco do Brasil com o nosso número de 17 dígitos e convênio
 * de 7 posições deve seguir esta forma:
 * 
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: * collapse" bordercolor="#111111" width="60%" id="campolivre">
 * <tr>
 * <thead>
 * <th>Posição</th>
 * <th>Tamanho</th>
 * <th>Picture</th>
 * <th>Conteúdo (terminologia padrão)</th>
 * <th>Conteúdo (terminologia do banco)</th>
 * </thead>
 * </tr>
 * <tr>
 * <td>20-25</td>
 * <td>6</td>
 * <td>9(6)</td>
 * <td>Constante zeros = "000000"</td>
 * <td>Constante zeros = "000000"</td>
 * </tr>
 * <tr>
 * <td>26-42</td>
 * <td>17</td>
 * <td>9(17)</td>
 * <td>Nosso Número (sem dígito) composto pelo número do convênio fornecido pelo
 * Banco (CCCCCCC) e complemento do Nosso-Número, sem DV (NNNNNNNNNN)</td>
 * <td>Nosso Número (sem dígito) CCCCCCCNNNNNNNNNN</td>
 * </tr>
 * <tr>
 * <td>43-44</td>
 * <td>2</td>
 * <td>9(2)</td>
 * <td>Código da carteira</td>
 * <td>Tipo de Carteira/Modalidade de Cobrança</td>
 * </tr>
 * </table>
 * 
 * 
 * 
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L</a>
 * 
 * @since 0.2
 * 
 * @version 0.2
 */
class CLBancoBRB extends AbstractCLBancoBRB {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6997812168875449834L;

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
	
	private static final int FIXO = 0;
	
	/**
	 * <p>
	 * Cria um campo livre instanciando o número de fields ({@code
	 * FIELDS_LENGTH}) deste campo.
	 * </p>
	 * 
	 * @since 0.2
	 */
	protected CLBancoBRB() {

		super(FIELDS_LENGTH);
	}
	
	/**
	 * <p>
	 * Dado um título, cria um campo livre para o padrão do Banco Caixa Econômica
	 * Federal que tenha o serviço SINCO.
	 * </p>
	 * @param titulo Título com as informações para geração do campo livre.
	 */
	CLBancoBRB(Titulo titulo) {
		super(FIELDS_LENGTH);

		ContaBancaria conta = titulo.getContaBancaria();
		String nossoNumero = titulo.getNossoNumero();

		Integer dVContaCorrente = calculeDigitoVerificador(conta.getNumeroDaConta().getCodigoDaConta().toString());

		this.add(new Field<Integer>(conta.getNumeroDaConta().getCodigoDaConta(), 6, Filler.ZERO_LEFT));
		this.add(new Field<Integer>(dVContaCorrente, 1));
		this.add(new Field<String>(nossoNumero.substring(2, 5), 3));
		
		this.add(new Field<Integer>(FIXO, 1));
		
		if(conta.getCarteira().isComRegistro()){
			
			this.add(new Field<Integer>(COBRANCA_REGISTRADA, 1));
			
		}else{
			
			this.add(new Field<Integer>(COBRANCA_NAO_REGISTRADA, 1));
		}

		this.add(new Field<String>(nossoNumero.substring(3, 6), 3));
		// this.add(new Field<Integer>(EMISSAO_CEDENTE, 1));
		this.add(new Field<String>(nossoNumero.substring(8, 17), 9));

		this.add(new Field<Integer>(calculeDigitoVerificador(gereCampoLivre()), 1));
	}
	
	@Override
	protected void checkValues(Titulo titulo) {

		checkNossoNumero(titulo);
		checkTamanhoDoNossoNumero(titulo, NN17);
		checkCarteiraNotNull(titulo);
		//checkCodigoDaCarteira(titulo);
		checkCodigoDaCarteiraMenorOuIgualQue(titulo, 99);
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
}