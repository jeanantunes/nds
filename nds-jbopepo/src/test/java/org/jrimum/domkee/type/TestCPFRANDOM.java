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
 * Created at: 30/03/2008 - 19:11:24
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
 * Criado em: 30/03/2008 - 19:11:24
 * 
 */


package org.jrimum.domkee.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.jrimum.domkee.comum.pessoa.id.cprf.AbstractCPRF;
import org.jrimum.domkee.comum.pessoa.id.cprf.CPF;
import org.junit.Test;


/**
 * 
 * Teste da classe <code>CadastroDePessoaFísica</code>
 * 
 * 
 * @author Gabriel Guimarães
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a> 
 * @author <a href="mailto:misaelbarreto@gmail.com">Misael Barreto</a>
 * @author <a href="mailto:romulomail@gmail.com">Rômulo Augusto</a>
 * 
 * @since JRimum 1.0
 * 
 * @version 1.0
 */
public class TestCPFRANDOM{

	@Test
	public void testGetInstanceString() {
		
		for (int i = 0; i < 9000; i++) {			
			System.out.println(this.generate());
		}
		
	}
	
	/**
    * Gera numero de CPF randomico.
    * @return Novo CPF
    * @see generateDigits(String digitsBase)
    */
    public String generate(){
 
        Random r = new Random();
 
        StringBuilder sbCpfNumber = new StringBuilder();
 
        for(int i = 0; i < 9; i++){
 
            sbCpfNumber.append(r.nextInt(9));
 
        }
 
        return generateDigits(sbCpfNumber.toString());
 
    }
 
    /**
    * Valida o CPF.
    * @param cpf Numero de CPF sem pontuacao.
    * @return true para CPF valido ou o contrario.
    */
    public boolean validateCPF(String cpf){
 
        if(cpf.length() == 11){
 
            if(cpf.equals(generateDigits(cpf.substring(0, 9)))){
 
                return true;
 
            }
 
        }
 
        return false;
 
    }
 
    /**
    * Gera digitos validadores.
    * @param digitsBase 9 digitos iniciais.
    * @return CPF com digitos validadores sem pontuacao.
    */
    private String generateDigits(String digitsBase) {
 
        StringBuilder sbCpfNumber = new StringBuilder(digitsBase);
 
        int total = 0;
 
        int multiple = digitsBase.length() + 1;
 
        for (char digit : digitsBase.toCharArray()) {
 
            long parcial = Integer.parseInt(String.valueOf(digit)) * (multiple--);
 
            total += parcial;
 
        }
 
        int resto = Integer.parseInt(String.valueOf(Math.abs(total % 11)));
 
        if (resto < 2) {
 
            resto = 0;
 
        } else {
 
            resto = 11 - resto;
 
        }
 
        sbCpfNumber.append(resto);
 
        if (sbCpfNumber.length() < 11) {
 
            return generateDigits(sbCpfNumber.toString());
 
        }
 
        return sbCpfNumber.toString();
 
    }
 

}
