<head>
</head>

<div id="dadosCadastraisPF" style="display:none">
	<legend>Entregador - Pessoa Física</legend>
	<div>
	<form id="formDadosEntregadorPF">
		<input type="hidden" name="entregador.id" id="idEntregadorPF"/>
       <table width="754" cellpadding="2" cellspacing="2" style="text-align:left;">
       <tr>
         <td><strong>Código:</strong></td>
         <td><input type="text" name="entregador.codigo" maxlength="20" id="codigoEntregadorPF" style="width:100px" /></td>
         <td><strong>Início de Atividade:</strong></td>
         <td colspan="3"><span id="inicioAtividadePF"></span></td>
       </tr>
       <tr>
           <td width="118">Nome:</td>
           <td width="237"><input type="text"  id="nomeEntregador" style="width:230px " /></td>
           <td width="134">Apelido:</td>
           <td colspan="3"><input maxlength="25" type="text"  id="apelido" style="width:230px" /></td>
       </tr>
       <tr>
         <td>CPF:</td>
         <td><input type="text" name="cpf" id="cpf" style="width:150px" /></td>
         <td>R. G.:</td>
         <td colspan="3"><input type="text" id="rg" style="width:150px" maxlength="15" /></td>
       </tr>
       <tr>
         <td>Data Nascimento:</td>
         <td><input type="text" id="dataNascimento" style="width:150px" /></td>
         <td>Orgão Emissor:</td>
         <td width="59">
         	<input type="text" id="dados-cadastral-orgaoEmissor" style="width:50px" />
         </td>
         <td width="31">UF:</td>
         <td width="135"><select id="ufOrgaoEmissor" style="width:50px">
         </select></td>
       </tr>
       <tr>
         <td>Estado Civil:</td>
         <td><select id="estadoCivil" style="width:155px;">
           <option selected="selected"></option>
           <option value="SOLTEIRO">Solteiro</option>
           <option value="CASADO">Casado</option>
           <option value="DIVORCIADO">Divorciado</option>
           <option value="VIUVO">Víuvo</option>
         </select></td>
         <td>Sexo:</td>
         <td colspan="3"><select id="sexo" style="width:155px">
             <option selected="selected"></option>
             <option value="MASCULINO">Masculino</option>
             <option value="FEMININO">Feminino</option>
           </select></td>
       </tr>
       <tr>
         <td>Nacionalidade:</td>
         <td><input type="text" style="width:150px" id="nacionalidade" /></td>
         <td>Natural:</td>
         <td colspan="3"><input type="text" id="natural" style="width:150px" /></td>
       </tr>
       <tr>
         <td>E-mail:</td>
         <td><input type="text" id="emailPF" style="width:230px" /></td>
         <td>&nbsp;</td>
         <td colspan="3">&nbsp;</td>
       </tr>
       <tr>
         <td>É comissionado?</td>
         <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td width="10%"><input type="radio" name="comissionadoPF" id="comissionadoPF" /></td>
             <td width="15%">Sim</td>
             <td width="10%"><input type="radio" name="comissionadoPF" id="naoComissionadoPF" /></td>
             <td width="65%">Não</td>
           </tr>
         </table></td>
         <td><span class="comissionadoPf" style="display:none; font-size: 9px;">
     Informe o % de Comissão s/ Faturamento (Preço Capa):
     </span></td>
         <td colspan="3"><span class="comissionadoPf" style="display:none">
     <input type="text" name="entregador.percentualComissao" maxlength="19" 
     		id="percentualComissaoPF" style="width:100px" />
     </span></td>
       </tr>
     </table>
    </form>
    </div>
 </div>
