

<script>

$(function() {
		
	$('#inicioHorario').mask('99:99'); 
	$('#fimHorario').mask('99:99'); 
	$("#porcentagemFaturamento").numeric();
	$("#qntFuncionarios").numeric();
	$("#numerolicenca").numeric();
	
	
	var options = {
			success: tratarRetornoUploadImagem,
		};
		
		$('#formBaixaAutomatica').ajaxForm(options);		
});

function tratarRetornoUploadImagem(data) {
	
	data = replaceAll(data, "<pre>", "");
	data = replaceAll(data, "</pre>", "");
	
	data = replaceAll(data, "<PRE>", "");
	data = replaceAll(data, "</PRE>", "");
	
	var responseJson = jQuery.parseJSON(data);
	
	var mensagens = responseJson.result[0];
	var status = responseJson.result[1];
	var pathArquivo = responseJson.result[2];
		
	if(pathArquivo) {
		$("#idImagem").attr("src","${pageContext.request.contextPath}/" + pathArquivo);
	} else {
		$("#idImagem").attr("src","${pageContext.request.contextPath}/images/pdv/no_image.jpeg");
	}	
	
	if(mensagens!=null && mensagens.length!=0) {
		exibirMensagemDialog(status,mensagens);
	}
}

</script>


<div id="dialog-img" title="Incluir Foto do PDV">
<br />

	<form action="<c:url value='/cadastro/pdv/uploadImagem' />" id="formBaixaAutomatica"
		  method="post" enctype="multipart/form-data" >
	
		<input type="hidden" name="formUploadAjax" value="true" />
		
	<input name="uploadedFile" type="file" id="uploadedFile" size="40" />
									
	</form>


</div>

<fieldset>
<legend>Dados Básicos</legend>
<table width="777" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="572" valign="top">
    <table width="572" border="0" cellspacing="1" cellpadding="1">
      <tr>
        <td>Status:</td>
        <td width="135">
        	<select name="selectStatus" id="selectStatus">
	          
	          <option selected="selected" value="-1"> </option>
	          
	          <c:forEach items="${listaStatus}" var="item">
	          	<option value="${item.key}">${item.value}</option>	          
	          </c:forEach>
	          
          	</select>
          </td>
        <td width="80">Data Início:</td>
        	<td width="215">
        		<input value="${dataAtual}" type="text" id="dataInicio"  style="width:80px;" disabled="disabled" />
        	</td>
      </tr>
      <tr>
        <td width="129">Nome PDV:</td>
        <td colspan="3">
        	<input type="text" name="nomePDV" id="nomePDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td>Contato:</td>
        <td colspan="3">
        	<input type="text" name="contatoPDV" id="contatoPDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td>Site:</td>
        <td colspan="3">
        	<input type="text" name="sitePDV" id="sitePDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td>E-mail:</td>
        <td colspan="3">
        	<input type="text" name="emailPDV" onchange="PDV.validarEmail(this.value);" id="emailPDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td>Ponto de Referência:</td>
        <td colspan="3">
        	<input type="text" name="pontoReferenciaPDV" id="pontoReferenciaPDV" style="width:300px;"/>
        </td>
        </tr>
      <tr>
        <td align="right">
        	<input type="checkbox" name=dentroOutroEstabelecimento" id="dentroOutroEstabelecimento"  onclick="PDV.opcaoEstabelecimento('#dentroOutroEstabelecimento');" />
        </td>
        <td colspan="3">Dentro de Outro Estabelecimento?</td>
        </tr>
      <tr>
        <td align="right">&nbsp;</td>
        <td colspan="3">
          <div id="divTipoEstabelecimento" style="display:none;">
            Tipo Estabelecimento:
          
            <select name="selectTipoEstabelecimento" id="selectTipoEstabelecimento" style="width:180px;">
             
             
             <option value="-1" selected="selected">Selecione</option>
		     
		      <c:forEach items="${listaTipoEstabelecimento}" var="item">
	          	<option value="${item.key}">${item.value}</option>	          
	          </c:forEach>
		             
            </select>
            </div>
        </td>
        </tr>
      </table>
      <br />

    </td>
    <td width="191" align="center" valign="top">
    	<img id="idImagem" src="${pageContext.request.contextPath}/images/pdv/no_image.jpeg" width="191" height="136" alt="Banca"/>
    	    	
    	<br />
    	<a href="javascript:" onclick="PDV.popup_img();"><img src="${pageContext.request.contextPath}/images/bt_cadastros.png" alt="Editar Imagem" width="15" height="15" hspace="10" vspace="3" border="0"  /></a>
    	<a id="idBtnExcluir" href="javascript:" onclick="PDV.excluirImagem();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir Imagem" width="15" height="15" hspace="10" vspace="3" border="0" /></a>
    </td>
  </tr>
</table>
    <table width="777" border="0" cellspacing="1" cellpadding="1">
      <tr>
	    <td width="135">Dias Funcionamento:</td>
	    <td width="252" class="diasFunc">
	    	<select name="selectDiasFuncionamento" id="selectDiasFuncionamento" onchange="PDV.selecionarDiaFuncionamento(this)" style="width:230px;">
		      
		      <option value="-1" selected="selected">Selecione</option>
		      
		       <c:forEach items="${listaDiasFuncionamento}" var="item">
	          	<option value="${item.key}">${item.value}</option>	          
	          </c:forEach>
	          
	      </select>
    	</td>
    	<td width="47">Horário:</td>
    	<td width="179"><input type="text" name="inicioHorario" id="inicioHorario" style="width:60px;"/>
      		As
      	<input type="text" name="fimHorario" id="fimHorario" style="width:60px;"/></td>
    	<td width="148"><span class="bt_add"><a href="javascript:;" onclick="PDV.adicionarDiaFuncionamento()">Incluir Novo</a></span></td>
  	</tr>
</table>

<table width="777" border="0" cellspacing="1" cellpadding="1">
      <tbody id="listaDiasFuncionais"></tbody>
</table>

<br />
<table width="777" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td>Tamanho:</td>
    <td>
    	<select name="selectTamanhoPDV" id="selectTamanhoPDV" style="width:200px;">
	      	     	
     	 <option selected="selected" value="-1">Selecione...</option>
            
          <c:forEach items="${listaTamanhoPDV}" var="item">
          	<option value="${item.key}">${item.value}</option>	          
          </c:forEach>
     
   		 </select>
   </td>
    <td>Qtde  Funcionários:</td>
    <td>
    	<input type="text" name="qntFuncionarios" id="qntFuncionarios" style="width:60px;"/>
    </td>
  </tr>
  <tr>
    <td>Sistema IPV:</td>
    <td>
    	<input name="sistemaIPV" id="sistemaIPV" type="checkbox" />
    </td>
    <td>% Faturamento:</td>
    <td>
    	<input type="text" name="porcentagemFaturamento" id="porcentagemFaturamento" style="width:60px;"/>
    </td>  
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td width="136">Tipo de Licença:</td>
    <td width="239">
    	
    	<select name="selectTipoLicenca" id="selectTipoLicenca" style="width:232px;">
      		
      		<option selected="selected">Selecione</option>
      		
      		<option selected="selected" value="-1">Selecione</option>
            
             <c:forEach items="${listaTipoLicencaMunicipal}" var="item">
	         	<option value="${item.key}">${item.value}</option>	          
	         </c:forEach>
      			
      	</select></td>
    
    <td width="132">Número da Licença:</td>
    <td width="257">
    	<input type="text" name="numerolicenca" id="numerolicenca" style="width:225px;"/>
    </td>
  </tr>
  <tr>
    <td>Nome  da Licença:</td>
    <td><span class="diasFunc">
      <input type="text" name="nomeLicenca" id="nomeLicenca" style="width:225px;"/>
      </span></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
<br />
</fieldset>

