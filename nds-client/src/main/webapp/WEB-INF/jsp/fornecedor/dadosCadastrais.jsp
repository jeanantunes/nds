<div id="tabpj-1">
<form id="formNovoFornecedor">
	<input type="hidden" name="fornecedorDTO.idFornecedor" id="idFornecedor" style="width:100px" />
    <table width="754" cellpadding="2" cellspacing="2" style="text-align:left;">
    <tr>
      <td><strong>Código:</strong></td>
      <td>
      	<input type="text" name="fornecedorDTO.codigoInterface" id="codigoInterface" style="width:100px" />
      </td>
      <td><strong>Início de Atividade:</strong></td>
      <td><span id="inicioAtividade"></span></td>
    </tr>
    <tr>
        <td width="120">Razão Social:</td>
        <td width="242">
        	<input type="text" name="fornecedorDTO.razaoSocial" id="razaoSocial" style="width:230px " />
        </td>
        <td width="134">Nome Fantasia:</td>
        <td width="230">
        	<input type="text" name="fornecedorDTO.nomeFantasia" id="nomeFantasia" style="width:230px " />
        </td>
    </tr>
    <tr>
      <td>CNPJ:</td>
      <td>
      	<input type="text" name="fornecedorDTO.cnpj" id="cnpj" style="width:230px" />
      </td>
      <td>Inscrição Estadual:</td>
      <td>
      	<input type="text" name="fornecedorDTO.inscricaoEstadual" id="inscricaoEstadual" style="width:230px" />
      </td>
    </tr>
    <tr>
      <td>Responsável:</td>
      <td>
      	<input type="text" name="fornecedorDTO.responsavel" id="responsavel" style="width:230px" />
      </td>
      <td>E-mail:</td>
      <td>
      	<input type="text" name="fornecedorDTO.email" id="email" style="width:230px" />
      </td>
    </tr>
    <tr>
      <td>Tipo Fornecedor:</td>
      <td>
      
	      <select name="fornecedorDTO.tipoFornecedor" id="tipoFornecedor" style="width:237px ">
	      	<option></option>
	      	<c:forEach items="${combo}" var="tipoFornecedor">
	      		<option value="${tipoFornecedor.id}">${tipoFornecedor.descricao}</option>
	      	</c:forEach>
	      </select>
      
      </td>
      <td>Tem Contrato?</td>
      <td>
      	<input type="checkbox" name="fornecedorDTO.possuiContrato" id="possuiContrato" onclick="mostraValidade();" />
      </td>
    </tr>
    <tr class="validade">
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>Informe a Validade</td>
      <td>
      	<input name="fornecedorDTO.validadeContrato" type="text" style="width:100px;" id="validadeContrato" />
      </td>
    </tr>
    </table>
    <br />
</form>
</div>