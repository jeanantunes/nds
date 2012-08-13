<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produtoEdicao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript">

var DESCONTO_PRODUTO = {
		
	inicializarModal: function() {
		
		$("#formTipoDescontoProduto")[0].reset();
		
		esconderGridCota();
		
		mostraEdicao();
	},

	popup_produto:function() {
		
		DESCONTO_PRODUTO.inicializarModal();

		$( "#dialog-produto" ).dialog({
			resizable: false,
			height:550,
			width:450,
			modal: true,
			buttons: {
				"Confirmar": function() {
					DESCONTO_PRODUTO.novoDescontoProduto();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
	},

	novoDescontoProduto:function() {
		
		var data = DESCONTO_PRODUTO.obterParametrosNovoDescontoProduto();

		$.postJSON("<c:url value='/administracao/tipoDescontoCota/novoDescontoProduto'/>",
				   data,
				   function(result) {

					   TIPO_DESCONTO.pesquisarDescontoProduto();

					   $( "#dialog-produto" ).dialog( "close" );

					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
	               },
				   null,
				   true);
	},

	obterParametrosNovoDescontoProduto: function() {
		
		var codigoProduto = $("#pCodigoProduto").val();
		var edicaoProduto = $("#edicaoProduto").val();
		var quantidadeEdicoes = $("#quantidadeEdicoes").val();
		var descontoProduto = $("#descontoProduto").val();
		var descontoPredominante = $("#descontoPredominante").attr("checked") ? true : false;
		var hasCotaEspecifica = document.getElementById("radioCotasEspecificas").checked;
		var isTodasCotas = document.getElementById("radioTodasCotas").checked;
		
		var data = new Array();
		
		data.push({name:'desconto.codigoProduto' , value: codigoProduto});
		data.push({name:'desconto.edicaoProduto' , value: edicaoProduto});
		data.push({name:'desconto.descontoProduto' , value: descontoProduto});
		data.push({name:'desconto.quantidadeEdicoes' , value: quantidadeEdicoes});
		data.push({name:'desconto.descontoPredominante' , value: descontoPredominante});
		data.push({name:'desconto.hasCotaEspecifica' , value: hasCotaEspecifica});
		data.push({name:'desconto.isTodasCotas' , value: isTodasCotas});
		
		$("input[id^=cotaInput]").each(function(index, value) {
			if ($(this).val()) {
				data.push({name:'cotas' , value: $(this).val()});
			}
		});
		
		return data;
	}
};

function mostrarGridCota(){
	$('.especificaCota').show();
}

function esconderGridCota(){
	
	$('.especificaCota').hide();
	
	resetGridCota();
}

function mostraEdicao() {

	$("#mostrarEdicao").attr("checked") ? $('.aEdicao').show() : $('.aEdicao').hide();
}

function resetGridCota() {
	
	$("tr[id^='trCota']").remove();
	
	$("#gridCotas").append(
		'<tr id="trCota1">' +
		'<td>' +
		'<input type="text" name="cotaInput" id="cotaInput1" style="width:120px;" maxlength="255" ' +
		'onblur="cota.pesquisarPorNumeroCota(\'#cotaInput1\', \'#nomeInput1\', true);"/>' +
		'</td>' +
		'<td>' +
		'<input type="text" name="nomeInput" id="nomeInput1" style="width:245px;" maxlength="255"' +
		'onkeyup="cota.autoCompletarPorNome(\'#nomeInput1\');" ' +
		'onblur="cota.pesquisarPorNomeCota(\'#cotaInput1\', \'#nomeInput1\',adicionarLinhaCota(1));"/>' +
		'</td>' +
		'</tr>'
	);
}

function adicionarLinhaCota(linhaAtual){
	
	if ($('#trCota' + (linhaAtual + 1)).length == 0 && $('#cotaInput' + (linhaAtual)).val() != ""){
		
		var tr = $('<tr class="trCotas" id="trCota'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual + 1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
				'<td><input type="text" name="cotaInput" maxlength="255" id="cotaInput'+ (linhaAtual + 1) +'" onblur="cota.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', true);" style="width:120px;" /></td>' +
				'<td>'+
					 '<input type="text" name="nomeInput" maxlength="255" id="nomeInput'+ (linhaAtual + 1) +'" style="width:245px;" '+
						 ' onkeyup="cota.autoCompletarPorNome(nomeInput'+ (linhaAtual + 1) +');" ' +
						 ' onblur="cota.pesquisarPorNomeCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', adicionarLinhaCota('+ (linhaAtual + 1) +'));" ' +
					 '/>'+
				'</td>' +
				'</tr>'
		);
		
		$("#gridCotas").append(tr);
		
		$("#cotaInput" + (linhaAtual + 1)).focus();
		
		$("#cotaInput"+ (linhaAtual + 1)).numeric();
	}
}

</script>

<div id="dialog-produto" title="Novo Tipo de Desconto Produto" style="display:none;">
<jsp:include page="../messagesDialog.jsp" />    

<form id="formTipoDescontoProduto">
  <table width="394" border="0" cellpadding="2" cellspacing="1" class="filtro" style="font-size:8pt">
          <tr>
            <td width="100">Código:</td>
            <td width="100">
            	<input type="text" name="pCodigoProduto" id="pCodigoProduto" maxlength="255" 
					   style="width:100px; float:left; margin-right:5px;"
					   onblur="produtoEdicao.pesquisarPorCodigoProduto('#pCodigoProduto', '#pNomeProduto', true,
							   undefined,
							   undefined);"/>
            </td>
          </tr>
          <tr>
            <td>Produto:</td>
            <td>
            	<input type="text" name="pNomeProduto" id="pNomeProduto" maxlength="255" 
									style="width:160px;"
									onkeyup="produtoEdicao.autoCompletarPorNomeProduto('#pNomeProduto', false);"
									onblur="produtoEdicao.pesquisarPorNomeProduto('#pCodigoProduto', '#pNomeProduto', true,
										undefined,
										undefined);" />
            </td>
          </tr>
          <tr>
            <td>Edição:</td>
            <td><input type="checkbox" name="checkbox" id="mostrarEdicao" onclick="mostraEdicao();" /></td>
          </tr>
		  
          <tr class="aEdicao" style="display:none;">

	          <td>Edição Específica:</td>
	          <td><input type="text" name="edicaoProduto" id="edicaoProduto" style="width:60px;"/>
	          ou por
	          <input type="text" name="quantidadeEdicoes" id="quantidadeEdicoes" style="width:60px;"/>
	          Edições</td>
            
		  </tr>
		  <tr>
            <td>Desconto %:</td>
            <td><input type="text" name="descontoProduto" id="descontoProduto" style="width:100px;"/></td>
          </tr>
          <tr>
            <td>Cotas:</td>
            <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="10%"><input type="radio" name="cotas" id="radioTodasCotas" value="radio" onchange="esconderGridCota();" /></td>
                <td width="29%">Todas</td>
                <td width="8%"><input type="radio" name="cotas" id="radioCotasEspecificas" value="radio" onchange="mostrarGridCota();" /></td>
                <td width="53%">Específica</td>
              </tr>
            </table></td>
          </tr>
    </table>       


		<div id="fieldCota" class="especificaCota" style="display:none;">
			
			<fieldset style="width:395px!important;">
				<legend>Cotas</legend>
				<div style="overflow: auto; height: 240px;">
	    			<table border="0" cellspacing="1" cellpadding="1" class="especificaCota" id="gridCotas" style="display:none;width:100%" >
						
						<tr class="header_table">
			                <td width="34%">Cota</td>
			                <td width="66%">Nome</td>
						</tr>
						<tr id="trCota1">
							<td>
								<input type="text" name="cotaInput" id="cotaInput1" style="width:120px;" maxlength="255"
									onblur="cota.pesquisarPorNumeroCota('#cotaInput1', '#nomeInput1', true);"/>
							</td>
							<td>
								<input type="text" name="nomeInput" id="nomeInput1" style="width:245px;" maxlength="255"
									onkeyup="cota.autoCompletarPorNome('#nomeInput1');" 
									onblur="cota.pesquisarPorNomeCota('#cotaInput1', '#nomeInput1',adicionarLinhaCota(1));"/>
							</td>
						</tr>
						<tfoot>
							<tr>
								<td colspan="2">
									Este desconto predomina sobre os demais (geral / Específico)?
									<input type="checkbox" name="descontoPredominante" id="descontoPredominante" />
								</td>
							</tr>
				      	</tfoot>
					</table>
				</div>
			</fieldset>
		</div>            


 </form>

  </div>

