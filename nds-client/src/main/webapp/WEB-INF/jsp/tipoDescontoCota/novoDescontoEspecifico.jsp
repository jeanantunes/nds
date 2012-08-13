
<script>

$(function() {
	
	$("select[name='selectFornecedorSelecionado_especifico']").multiSelect("select[name='selectFornecedor_especifico']", {trigger: "#linkFornecedorVoltarTodos_especifico"});
	
	$("select[name='selectFornecedor_especifico']").multiSelect("select[name='selectFornecedorSelecionado_especifico']", {trigger: "#linkFornecedorEnviarTodos_especifico"});
	
});

var DESCONTO_ESPECIFICO = {
	
	popup_especifico:function() {		
		 
		$("#selectFornecedorSelecionado_option_especifico").clear();
		
		TIPO_DESCONTO.carregarFornecedores("#selectFornecedor_option_especifico");
		
		$( "#dialog-especifico" ).dialog({
			resizable: false,
			height:400,
			width:560,
			modal: true,
			buttons: {
				"Confirmar": function() {
					DESCONTO_ESPECIFICO.novoDescontoEspecifico();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});		      
	},
	
	novoDescontoEspecifico:function() {
		
		var cotaEspecifica = $("#numCotaEspecifico").val();
		var descontoEspecifico = $("#descontoEspecifico").val();
		
		var fornecedores ="";
		
		$("#selectFornecedorSelecionado_option_especifico option").each(function (index) {
			 fornecedores = fornecedores + "fornecedores["+index+"]="+ $(this).val() +"&";
		});
		
		$.postJSON("<c:url value='/administracao/tipoDescontoCota/novoDescontoEspecifico'/>",
				"numeroCota=" + cotaEspecifica	+				
				"&desconto=" + descontoEspecifico + "&" +
				fornecedores
				,				   
				function(result) {
			           
						 if (result.tipoMensagem && result.tipoMensagem !="SUCCESS" && result.listaMensagens) {			      
							   exibirMensagemDialog(result.tipoMensagem, result.listaMensagens, "idModalDescontoEspecifico");
					       }
						   else{
							   exibirMensagem(result.tipoMensagem, result.listaMensagens, "");
							   TIPO_DESCONTO.fecharDialogs();
							   TIPO_DESCONTO.pesquisar();
							   $(".tiposDescEspecificoGrid").flexReload();
						   }
	               },
				   null,
				   true,"idModalDescontoEspecifico");
		
		$(".tiposDescEspecificoGrid").flexReload();
	},
	
	pesquisarCotaSuccessCallBack:function(){},
	
	pesquisarCotaErrorCallBack:function(){
		
		exibirMensagemDialog("WARNING", [' Cota não encontrada!'], "idModalDescontoEspecifico");
	}
		
};





</script>

<div id="dialog-especifico" title="Novo Tipo de Desconto Especifico" style="display:none;"> 


<jsp:include page="../messagesDialog.jsp">
	
	<jsp:param value="idModalDescontoEspecifico" name="messageDialog"/>

</jsp:include>
   
<table width="480" border="0" align="center" cellpadding="2" cellspacing="2">
  
  <tr class="especialidades">
    <td colspan="4" valign="top">
    	<fieldset style="width:500px;">
    		<legend>Selecione a Cota</legend>
    		Cota:
    		<input name="numCotaEspecifico" 
           		   id="numCotaEspecifico" 
           		   type="text"
           		   maxlength="11"
           		   style="width:70px;"
           		   onchange="cota.pesquisarPorNumeroCota('#numCotaEspecifico', '#descricaoCotaEspecifico',true,
           	  											DESCONTO_ESPECIFICO.pesquisarCotaSuccessCallBack, 
           	  											DESCONTO_ESPECIFICO.pesquisarCotaErrorCallBack);" />
    		Nome:
    		<input  name="descricaoCotaEspecifico" 
		      		 id="descricaoCotaEspecifico" 
		      		 type="text" 
		      		 class="nome_jornaleiro" 
		      		 maxlength="255"
		      		 style="width:200px;"
		      		 onkeyup="cota.autoCompletarPorNome('#descricaoCotaEspecifico');" 
		      		 onblur="cota.pesquisarPorNomeCota('#numCotaEspecifico', '#descricaoCotaEspecifico',true,
										      			DESCONTO_ESPECIFICO.pesquisarCotaSuccessCallBack,
										      			DESCONTO_ESPECIFICO.pesquisarCotaErrorCallBack);" />
    		
    	</fieldset>
    </td>
  </tr>
  
  <tr class="especialidades">
    	
    	<td width="202" valign="top">
    		<fieldset>
      			<legend>Selecione os Fornecedores</legend>
      			<select style="height:130px; width:200px;" id="selectFornecedor_option_especifico" multiple="multiple" size="10" name="selectFornecedor_especifico">
      				<option></option>
			    </select>
    		</fieldset>
    	</td>
    
	    <td width="56" align="center">
			<a id="linkFornecedorEnviarTodos_especifico" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_vai_todos.png" border="0"></a>
			<br><br>
			<a id="linkFornecedorVoltarTodos_especifico" href="javascript:;"><img height="30" width="39" src="${pageContext.request.contextPath}/images/seta_volta_todos.png" border="0"></a>
			<br>
		</td>
    
	    <td width="202" valign="top">
		    <fieldset>
		      <legend>Fornecedores selecionados</legend>
		      <select style="height:130px; width:200px;" id="selectFornecedorSelecionado_option_especifico" multiple="multiple" size="10" name="selectFornecedorSelecionado_especifico">
			  </select>
		      <br>
		    </fieldset>
	    </td>
  </tr>
  
  <tr class="especialidades">
    <td colspan="3" valign="top">
    
    <fieldset style="width:500px;">
    	<legend>Informe o Percentual de Desconto</legend>
    	Desconto: <input type="text" name="descontoEspecifico" id="descontoEspecifico" style="width:60px; margin-left:10px; margin-right:10px;"/> %
    </fieldset>
   
   </td>
   
  </tr>
  
</table>       

</div>
