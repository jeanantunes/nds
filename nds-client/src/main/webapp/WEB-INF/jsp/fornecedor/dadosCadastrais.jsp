<div id="fornecedorController-tabpj-1">
	
	<input type="hidden" name="fornecedorDTO.idFornecedor" id="fornecedorController-idFornecedor" style="width:100px" />

    <input type="hidden" name="fornecedorDTO.origem" id="fornecedorController-origem"/>


    <table width="754" cellpadding="2" cellspacing="2" style="text-align:left;">
    <tr>
      <td><strong>Código:</strong></td>
      <td>
      	<input type="text" name="fornecedorDTO.codigoInterface" id="fornecedorController-codigoInterface" 
      		maxlength="4" style="width:100px" onkeyup="this.value = this.value.replace(/[^0-9\.]/g,'');" />
      </td>
      <td><strong>Início de Atividade:</strong></td>
      <td><span id="fornecedorController-inicioAtividade"></span></td>
    </tr>
    <tr>
        <td width="120">Razão Social:</td>
        <td width="242">
        	<input type="text" name="fornecedorDTO.razaoSocial" id="fornecedorController-razaoSocial" maxlength="255" style="width:230px " />
        </td>
        <td width="134">Nome Fantasia:</td>
        <td width="230">
        	<input type="text" name="fornecedorDTO.nomeFantasia" id="fornecedorController-nomeFantasia" maxlength="255" style="width:230px " />
        </td>
    </tr>
    <tr>
      <td>CNPJ:</td>
      <td>
      	<input type="text" name="fornecedorDTO.cnpj" id="fornecedorController-cnpj" maxlength="255" style="width:230px" />
      </td>
      <td>Inscrição Estadual:</td>
      <td>
      	<input type="text" name="fornecedorDTO.inscricaoEstadual" id="fornecedorController-inscricaoEstadual" style="width:230px" />
      </td>
    </tr>
    <tr>
      <td>Contato:</td>
      <td>
      	<input type="text" name="fornecedorDTO.responsavel" id="fornecedorController-responsavel" maxlength="255" style="width:230px" />
      </td>
      <td>E-mail:</td>
      <td>
      	<input type="text" name="fornecedorDTO.email" id="fornecedorController-email" maxlength="255" style="width:230px" />
      </td>
    </tr>
    <tr>
      <td>Tipo Fornecedor:</td>
      <td>
      
	      <select name="fornecedorDTO.tipoFornecedor" id="fornecedorController-tipoFornecedor" style="width:237px ">
	      	<option></option>
	      	<c:forEach items="${combo}" var="tipoFornecedor">
	      		<option value="${tipoFornecedor.id}">${tipoFornecedor.descricao}</option>
	      	</c:forEach>
	      </select>
      
      </td>
      <td>E-mail NF-e:</td>
      <td>
      	<input type="text" name="fornecedorDTO.emailNfe" id="fornecedorController-emailNfe" maxlength="255" style="width:230px" />
      </td>
	</tr>
    <tr>
	    <td>Tem Contrato?</td>
	    <td>
	    	<input type="checkbox" name="fornecedorDTO.possuiContrato" id="fornecedorController-possuiContrato" onclick="fornecedorController.mostraValidade();" />
	    </td>
	    <td class="fornecedorController-validade">Informe a Validade</td>
      	<td class="fornecedorController-validade">
      	<input name="fornecedorDTO.validadeContrato" type="text" style="width:100px;" id="fornecedorController-validadeContrato" />
      </td>
    </tr>
    <tr>
      <td>Banco:</td>
      <td>
      	<select name="fornecedorDTO.idBanco" id="fornecedorController-banco" style="width: 150px;">
			<option value=""></option>
			<c:forEach varStatus="counter" var="banco" items="${listaBancos}">
				<option value="${banco.key}">${banco.value}</option>
			</c:forEach>
		</select>
      </td>
    </tr>
    <tr>
    	<td>Canal Distribui&ccedil;&atilde;o</td>
    	<td>
    		<select id="fornecedorController-canalDistribuicao" name="fornecedorDTO.canalDistribuicao" 
    			style="width: 150px;">
	    		<c:forEach items="${canaisDistribuicao}" var="canal">
	    			<option value="${canal}">${canal.descricao}</option>
	    		</c:forEach>	
    		</select>
    	</td>
    </tr>
    <tr>
    	<td>Integra GFS:</td>
	    <td>
	      	<input type="checkbox" name="fornecedorDTO.integraGFS" id="fornecedorController-integraGFS"/>
	    </td>
	</tr>
	<tr>
    	<td>Destinação de Encalhe:</td>
	    <td>
	      	<input type="checkbox" name="fornecedorDTO.destinacaoEncalhe" id="fornecedorController-destinacaoEncalhe" />
	    </td>
	</tr>
    </table>
    <br/>
</div>