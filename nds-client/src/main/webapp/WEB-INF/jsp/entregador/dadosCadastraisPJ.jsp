<head>
</head>
<div id="dadosCadastraisPJ" style="display:none">
	<legend>Entregador - Pessoa Jurídica</legend>
	<div>
	<form id="formDadosEntregadorPJ">
		<input type="hidden" name="entregador.id" id="idEntregadorPJ"/>
	    <table width="765" cellpadding="2" cellspacing="2" style="text-align:left;">
	    <tr>
	      <td><strong>Código:</strong></td>
	      <td><input type="text" name="entregador.codigo" id="codigoEntregadorPJ" maxlength="20" style="width:100px" /></td>
	      <td><strong>Início de Atividade:</strong></td>
	      <td><span id="inicioAtividadePJ" name="entregador.inicioAtividade"></span></td>
	    </tr>
	    <tr>
	      <td>Razão Social:</td>
	      <td><input type="text" id="razaoSocial" style="width:230px " /></td>
	      <td>Nome Fantasia:</td>
	      <td><input type="text" id="nomeFantasia" style="width:230px " /></td>
	    </tr>
	    <tr>
	      <td>CNPJ:</td>
	      <td><input type="text" id="cnpj" style="width:230px" /></td>
	      <td>Inscrição Estadual:</td>
	      <td><input type="text" id="inscricaoEstadual" style="width:230px" maxlength="20" /></td>
	    </tr>
	    <tr>
	        <td width="128">E-mail:</td>
	        <td width="234"><input type="text" id="emailPJ" style="width:230px" /></td>
	        <td width="138">&nbsp;</td>
	        <td width="237">&nbsp;</td>
	    </tr>
	    <tr>
	      <td>É comissionado?</td>
	      <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="10%"><input type="radio" name="comissaoPJ" id="comissionadoPJ" value="radio" /></td>
	    <td width="23%">Sim</td>
	    <td width="9%"><input type="radio" name="comissaoPJ" id="naoComissionadoPJ" value="radio" /></td>
	    <td width="58%">Não</td>
	  </tr>
	</table></td>
	      <td><span class="comissionadoPJ" style="font-size: 9px;">
	      Informe o % de Comissão s/ Faturamento (Preço Capa):
	      </span></td>
	      <td><span class="comissionadoPJ">
	      <input type="text" name="entregador.percentualComissao" maxlength="19" id="percentualComissaoPJ" style="width:100px" />
	      </span></td>
	    </tr>
	     </table>
	</form>
	</div>
</div>