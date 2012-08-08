<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">

	var tipoFormaCobranca = null;
	var idPolitica = null;

	$(function() {		
		$("#valorMinimo, #diasDoMes, #diasDoMes1, #diasDoMes2, #taxaMulta,#valorMulta,#taxaJuros").numeric();
	    carregarFormasEmissao(null,"");
	});

	$(function() {
		$(".parametrosGrid").flexigrid({
			preProcess: getDataFromResult,
			dataType : 'json',
			colModel :[ {
				display : 'Forma Pagto',
				name : 'forma',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'banco',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vlr. M&iacute;n. Emiss&atilde;o R$',
				name : 'valorMinimoEmissao',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Acumula Divida',
				name : 'acumulaDivida',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cobran&ccedil;a Unif.',
				name : 'cobrancaUnificada',
				width : 80,
				sortable : true,
				align : 'center',
			}, {
				display : 'Forma Emiss&atilde;o',
				name : 'formaEmissao',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Envia E-Mail',
				name : 'envioEmail',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedores',
				name : 'fornecedores',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Concentra&ccedil;&atilde;o Pgtos',
				name : 'concentracaoPagamentos',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Principal',
				name : 'principal',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 45,
				sortable : true,
				align : 'center'
			}],
			sortname : "forma",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
	});		
	
	
	
    function mostrarGridConsulta() {
    	
		/*PASSAGEM DE PARAMETROS*/
		$(".parametrosGrid").flexOptions({
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: "<c:url value='/distribuidor/parametroCobranca/consultaParametrosCobranca' />",
			params: [
			         {name:'idBanco', value:$("#filtroBanco").val()},
			         {name:'tipoCobranca', value:$("#filtroTipoCobranca").val()}
			        ] ,
			        newp: 1
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".parametrosGrid").flexReload();
		
		$(".grids").show();
	}	
    
    
	
	function getDataFromResult(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagemDialog(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids").hide();
			return resultado;
		}	
		
		$.each(resultado.rows, function(index, row) {
			
			var linkEditar = '<a href="javascript:;" id="bt_alterar" onclick="popup_alterar(' + row.cell.idPolitica + ');" style="cursor:pointer">' +
					     	  	'<img title="Aprovar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
					  		  '</a>';
			
			var linkExcluir = '<a href="javascript:;" id="bt_excluir" onclick="popup_excluir(' + row.cell.idPolitica + ');" style="cursor:pointer">' +
							   	 '<img title="Rejeitar" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
							   '</a>';
			
			row.cell.acao = linkEditar + linkExcluir;
			
			
			if(row.cell.principal){
				row.cell.principal = '<img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0px"/>';
			}else{
				row.cell.principal = '';
			}
			
			
		});
			
		$(".grids").show();
		
		return resultado;
	}
	
	
	//VERIFICA A EXISTENCIA DE UMA POLITICA DE COBRANCA PRINCIPAL
	function existPrincipal(){
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/existPrincipal' />",
				   null,
				   function(mensagens) {
		        	   if (mensagens){
						   var tipoMensagem = mensagens.tipoMensagem;
						   var listaMensagens = mensagens.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
						   return false;
		        	   }
		        	   else{
		        		   return true;
		        	   }
		        	   
		           },
		           null,
				   true,
				   "idModal");
	}

	
    function popup() {
		
		preparaCadastroParametro();

		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:680,
			width:890,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   postarParametro();
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
			beforeClose: function() {
				clearMessageDialogTimeout("idModal");
				/*
				if (!existPrincipal()){
					return false;
				}
				*/
				
		    }
		});
	};
	
	
	
	function popup_alterar(idPolitica) {
		
		obterParametro(idPolitica);
		
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:680,
			width:890,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   postarParametro();
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
			beforeClose: function() {
				clearMessageDialogTimeout("idModal");
                /*
				if (!existPrincipal()){
					return false;
				}
                */
				
		    }
		});	
		      
	};
	
	
	
	function popup_excluir(idPolitica) {
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   desativarParametro(idPolitica);
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
			beforeClose: function() {
				clearMessageDialogTimeout("idModal");
		    }
		});
	};
	

	
	//MODOS DE EXIBIÇÃO
	function opcaoPagto(op){
		if ((op=='BOLETO')||(op=='DEPOSITO')||(op=='TRANSFERENCIA_BANCARIA')){
			$('.tdComboBanco').show();
			$('.tdValorMinimo').show();			
			$('.tdMulta').show();			
			$('.tdJuros').show();
			
	    }
		
		else if ((op=='CHEQUE')||(op=='DINHEIRO')){
			$('.tdComboBanco').hide();			
			$('.tdValorMinimo').show();
			$('.tdMulta').show();			
			$('.tdJuros').show();

	    }
		
		else if (op=='BOLETO_EM_BRANCO'){
			$('.tdComboBanco').show();			
			$('.tdValorMinimo').hide();			
			$('.tdMulta').hide();			
			$('.tdJuros').hide();
			$('.formPgto').show();
		}    
		
		else if ((op=='OUTROS')){
			$('.tdComboBanco').hide();			
			$('.tdValorMinimo').hide();			
			$('.tdMulta').show();			
			$('.tdJuros').show();
	    }
		
		if((op=='BOLETO') || (op=='BOLETO_EM_BRANCO')){
			$('.formPgto').show();
		}else{
			$('.formPgto').hide();
		}
		
	};	
	
	function mudaConcentracaoPagamento(op){
		tipoFormaCobranca = (op.valueOf().toUpperCase());
		$( ".mensal,.semanal,.quinzenal" ).hide();
		$( "." + op ).show();
		
	};	
	
	
	function opcaoTipoFormaCobranca(op){
		op = op.toLowerCase();		
		
		$("#radio_"+op).attr("checked", true);
		//document.formularioParametro[op].checked = true;
		mudaConcentracaoPagamento(op);
	};
	
	
	function fecharDialogs() {
		$( "#dialog-novo" ).dialog( "close" );
	    $( "#dialog-excluir" ).dialog( "close" );
	}
	
	
	
	//OBTEM UM PARÂMETRO PARA ALTERAÇÃO
	function obterParametro(idPolitica){
		var data = [{name: 'idPolitica', value: idPolitica}];
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/obterParametroCobranca' />",
				   data,
				   sucessCallbackObterParametro, 
				   null,
				   true);
	}
	
	function sucessCallbackObterParametro(resultado) {
		
		var formaEmissao = resultado.formaEmissao;
		
		idPolitica  = resultado.idPolitica;
		
		$("#tipoCobranca").val(resultado.tipoCobranca);
		
		$("#formaCobranca").val(resultado.formaCobranca);
		$("#banco").val(resultado.idBanco);
		
		$("#valorMinimo").val(resultado.valorMinimo);
		$("#taxaMulta").val(resultado.taxaMulta);
		$("#valorMulta").val(resultado.valorMulta);
		$("#taxaJuros").val(resultado.taxaJuros);
		
		$("#instrucoes").val(resultado.instrucoes);

		$("#acumulaDivida").val(resultado.envioEmail?'S':'N');
		$("#vencimentoDiaUtil").val(resultado.envioEmail?'S':'N');
		$("#unificada").val(resultado.envioEmail?'S':'N');
		$("#envioEmail").val(resultado.envioEmail?'S':'N');
		
		if(resultado.tipoFormaCobranca == 'MENSAL'){
			if(resultado.diasDoMes[0]){
				$('#diaDoMes').val(resultado.diasDoMes[0]);
			}else{
				$('#diaDoMes').val("");
			}
		}else if(resultado.tipoFormaCobranca == 'QUINZENAL'){			
			if(resultado.diasDoMes[0]){
				$('#diaDoMes1').val(resultado.diasDoMes[0]);
			}else{
				$('#diaDoMes1').val("");
			}
			if(resultado.diasDoMes[1]){
				$('#diaDoMes2').val(resultado.diasDoMes[1]);
			}else{
				$('#diaDoMes2').val("");
			}
		}		
		$("#principal").attr('checked',resultado.principal);		
		$("#PS").attr('checked',resultado.segunda);
		$("#PT").attr('checked',resultado.terca);
		$("#PQ").attr('checked',resultado.quarta);
		$("#PQu").attr('checked',resultado.quinta);
		$("#PSex").attr('checked',resultado.sexta);
		$("#PSab").attr('checked',resultado.sabado);
		$("#PDom").attr('checked',resultado.domingo);


		opcaoPagto(resultado.tipoCobranca);
		opcaoTipoFormaCobranca(resultado.tipoFormaCobranca);
		
		$("input[name='checkGroupFornecedores']").each(function(i) {			
			$(this).attr('checked',false);
		});	
		
		$.each(resultado.fornecedoresId, function(index, value) { 
			 $("#fornecedor_" + value).attr('checked', true);
		});
		
		
		$("input[name='radioFormaCobrancaBoleto']").each(function(i) {			
			if($(this).val() == resultado.formaCobrancaBoleto){
				$(this).attr('checked', true);
			}
		});
		
		carregarFormasEmissao(resultado.tipoCobranca,formaEmissao);
	}	
	
	//INCLUSÃO DE NOVO PARAMETRO
    function postarParametro(novo) {
		if(novo){
			idPolitica = null;
		}
		var parametroCobranca = {
				idPolitica: idPolitica,
				tipoFormaCobranca: tipoFormaCobranca,
				tipoCobranca : $("#tipoCobranca").val(),
				formaEmissao : $("#formaEmissao").val(),
				idBanco : $("#banco").val(),
				valorMinimo : $("#valorMinimo").val(),
				taxaMulta : $("#taxaMulta").val(),
				valorMulta : $("#valorMulta").val(),
				taxaJuros : $("#taxaJuros").val(),
				instrucoes : $("#instrucoes").val(),
				acumulaDivida : $("#acumulaDivida").val() == 'S',
				vencimentoDiaUtil : $("#vencimentoDiaUtil").val() == 'S',
				unificada : $("#unificada").val() == 'S',
				envioEmail : $("#envioEmail").val() == 'S',
				principal : $("#principal").attr('checked')  == 'checked',
				segunda : $("#PS").attr('checked')  == 'checked',		
				terca : $("#PT").attr('checked')  == 'checked',
				quarta : $("#PQ").attr('checked')  == 'checked',		
				quinta : $("#PQu").attr('checked')  == 'checked',	
				sexta : $("#PSex").attr('checked')  == 'checked',		
				sabado : $("#PSab").attr('checked')  == 'checked',		
				domingo : $("#PDom").attr('checked')  == 'checked'
		};
		
		$("input[name='radioFormaCobrancaBoleto']:checked").each(function(i) {			
			parametroCobranca.formaCobrancaBoleto =  $(this).val();
		});	
		
		
		var postObject = serializeObjectToPost("parametros", parametroCobranca);
		if(tipoFormaCobranca == 'MENSAL'){
			postObject = serializeArrayToPost("parametros.diasDoMes",[$('#diaDoMes').val()] ,postObject);
		}else if(tipoFormaCobranca == 'QUINZENAL'){
			postObject = serializeArrayToPost("parametros.diasDoMes",[$('#diaDoMes1').val(),$('#diaDoMes2').val()] ,postObject);
		}
		var listaIdsFornecedores = new Array();
		$("input[name='checkGroupFornecedores']:checked").each(function(i) {			
			listaIdsFornecedores.push($(this).val());
		});	
		
		
		postObject = serializeArrayToPost("parametros.fornecedoresId",listaIdsFornecedores ,postObject);
		
		$.postJSON(contextPath + '/distribuidor/parametroCobranca/postarParametroCobranca',postObject,
				   function(mensagens) {
					   fecharDialogs();
		        	   telaMensagem=null;
		        	   if (mensagens){
						   var tipoMensagem = mensagens.tipoMensagem;
						   var listaMensagens = mensagens.listaMensagens;
						   if (tipoMensagem && listaMensagens) {
						       exibirMensagem(tipoMensagem, listaMensagens);
					       }
		        	   }
			           mostrarGridConsulta();
		            },
		   			null,
		   			true,
		   			null);
		
	}

	
	
	//EXCLUI (DESATIVA) UM PARÂMETRO
    function desativarParametro(idPolitica) {
    	var data = [{name: 'idPolitica', value: idPolitica}];
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/desativaParametroCobranca'/>",
				   data,
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
		               mostrarGridConsulta();
		           },
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
		               mostrarGridConsulta();
		           }
			       );
	}
	
	
	
	//LIMPA TODOS OS DADOS PARA INCLUSÃO DE NOVO REGISTRO
    function preparaCadastroParametro() {
		
    	$("input[name='checkGroupFornecedores']:checked").each(function(i) {
			$(this).attr('checked',false);
		});
		
		$( ".semanal,.mensal,.quinzenal" ).hide();
		
		idPolitica  = null;
		
		$("#tipoCobranca").val('');
		
		$("#formaCobranca").val('');
		$("#banco").val('');
		
		$("#valorMinimo").val('');
		$("#taxaMulta").val('');
		$("#valorMulta").val('');
		$("#taxaJuros").val('');
		
		$("#instrucoes").val('');

		$("#acumulaDivida").val('S');
		$("#vencimentoDiaUtil").val('S');
		$("#unificada").val('S');
		$("#envioEmail").val('S');		
		$('#diaDoMes').val("");
		$('#diaDoMes1').val("");	
		$('#diaDoMes2').val("");			
		
		$("#principal").attr('checked',false);		
		$("#PS").attr('checked',false);
		$("#PT").attr('checked',false);
		$("#PQ").attr('checked',false);
		$("#PQu").attr('checked',false);
		$("#PSex").attr('checked',false);
		$("#PSab").attr('checked',false);
		$("#PDom").attr('checked',false);

		
		carregarFormasEmissao(null,"");
		$("input[name='checkGroupFornecedores']").each(function(i) {			
			$(this).attr('checked',false);
		});	

	}	
	
	function montarComboBox(result,name,onChange,selected) {

		var options = "";
		
		options += "<select name='"+name+"' id='"+name+"' style='width:150px;' onchange='"+onChange+"'>";
		options += "<option value=''>Selecione</option>";
		$.each(result, function(index, row) {
			if (selected == row.key.$){
			    options += "<option selected='true' value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
			}
			else{
				options += "<option value='" + row.key.$ + "'>"+ row.value.$ +"</option>";	
			}
		});
		options += "</select>";
		
		return options;
	}
	
	function carregarFormasEmissao(tipoCobranca,selected){
		var data = [{name: 'tipoCobranca', value: tipoCobranca}];
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/obterFormasEmissao' />",
				   data,
				   function(resultado){
			           criaComponenteFormasEmissao(resultado,selected)
		           }
				   ,
				   null,
				   true);
	}
	
	function criaComponenteFormasEmissao(result,selected) {
	    var comboFormasEmissao =  montarComboBox(result,"formaEmissao","",selected);
		$("#formasEmissao").html(comboFormasEmissao);
	}

	
	
	function obterDadosBancarios(idBanco){
		var data = [{name: 'idBanco', value: idBanco}];
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/obterDadosBancarios' />",
				   data,
				   sucessCallbackCarregarDadosBancarios,
				   null,
				   true);
	}
	
	function sucessCallbackCarregarDadosBancarios(result) {
		$("#taxaMulta").val(result.multa);
		$("#valorMulta").val(result.vrMulta);
		$("#taxaJuros").val(result.juros);
		$("#instrucoes").val(result.instrucoes);
	}
	

	
	</script>


	<style type="text/css">
	#dialog-excluir, #dialog-novo{display:none;}
	.linha_fornecedor{display:none;}
	</style>


</head>

<div id="dialog-excluir" title="Excluir Parâmetro de Cobrança">
	<p>Confirma a exclusão deste Parâmetro de Cobrança?</p>
</div>


<div id="dialog-novo" title="Incluir Forma de Recebimento">

	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModal" name="messageDialog"/>
	</jsp:include>

    <form id="formularioParametro" name="formularioParametro">
    
    	<fieldset style="width:890px!important;">
    <legend>Formas de Pagamento</legend>
  <table width="880" border="0" cellspacing="2" cellpadding="2">
   <tr>
     <td width="132">Forma de Pagamento:</td>
     <td><select name="tipoCobranca" id="tipoCobranca" style="width:200px;" onchange="opcaoPagto(this.value);carregarFormasEmissao(this.value,'');">
   			<option value="">Selecione</option>
            <c:forEach varStatus="counter" var="tipoCobranca" items="${listaTiposCobranca}">
     		 <option value="${tipoCobranca.key}">${tipoCobranca.value}</option>
 			</c:forEach>
      </select></td>
     <td width="202">Acumula D&iacute;vida:</td>
     <td width="302"><select name="acumulaDivida" id="acumulaDivida" style="width:80px;">
       <option value="S">Sim</option>
       <option value="N">N&atilde;o</option>
      </select></td>
   </tr>
  <tr>
    <td width="132"><label class="tdComboBanco" for="banco">Banco:</label></td>
    <td><select class="tdComboBanco" name="banco" id="banco" style="width:200px;" onchange="obterDadosBancarios(this.value);">
      <option value="">Selecione</option>
       <c:forEach varStatus="counter" var="banco" items="${listaBancos}">
          <option value="${banco.key}">${banco.value}</option>
       </c:forEach>
    </select></td>
    <td>Vencimentos somente em dia &uacute;til:</td>
    <td><select name="vencimentoDiaUtil" id="vencimentoDiaUtil" style="width:80px;">
      <option value="S">Sim</option>
	  <option value="N">N&atilde;o</option>
    </select></td>
  </tr>
  <tr>
    <td valign="top"><label class="tdValorMinimo" for="banco">BVlr. M&iacute;nimo Emiss&atilde;o:</label></td>
    <td valign="top"><input class="tdValorMinimo" type="text" maxlength="16" name="valorMinimo" id="valorMinimo" style="width:100px;" /></td>
    <td>Cobran&ccedil;a Unificada:</td>
    <td><select name="unificada" id="unificada" style="width:80px;">
 		<option value="S">Sim</option>
		<option value="N">N&atilde;o</option>
    </select>
    <br clear="all" /></td>
  </tr>
  <tr>
    <td><label class="tdMulta" for="taxaMulta">Multa  %:</label></td>
    <td width="218"><table class="tdMulta" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="37%"><input type="text" readonly="true" name="taxaMulta" id="taxaMulta" style="width:70px;" /></td>
          <td width="24%">&nbsp;ou  R$:</td>
          <td width="39%"><input type="text" readonly="true" name="valorMulta" id="valorMulta" style="width:70px;" /></td>
        </tr>
    </table></td>
    <td width="202">Envio por E-mail:</td>
    <td colspan="2"><select name="envioEmail" id="envioEmail" style="width:80px;">
      <option value="S">Sim</option>
      <option value="N">N&atilde;o</option>
    </select></td>
  </tr>
  <tr>
    <td><label class="tdJuros" for="taxaJuros">Juros %:</label></td>
    <td><input class="tdJuros" type="text" readonly="true" name="taxaJuros" id="taxaJuros" style="width:100px;" /></td>
    <td>Impress&atilde;o:</td>
    <td>
    	<div id="formasEmissao"/>
    </td>
  </tr>
  <tr>
    <td><span class="formPgto">Forma de Cobran&ccedil;a:</span></td>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0" class="formPgto">
      <tr>
        <td width="9%"><input class="formPgto" type="radio" name="radioFormaCobrancaBoleto" id="radioFormaCobrancaBoleto.REGISTRADA" value="REGISTRADA"  /></td>
        <td width="34%"><label class="formPgto" for="radioFormaCobrancaBoleto.REGISTRADA">Registrado</label></td>
        <td width="10%"><input class="formPgto" type="radio" name="radioFormaCobrancaBoleto" id="radioFormaCobrancaBoleto.NAO_REGISTRADA" value="NAO_REGISTRADA"/></td>
        <td width="47%"><label class="formPgto" for="radioFormaCobrancaBoleto.NAO_REGISTRADA">N&atilde;o Registrado</label></td>
        </tr>
    </table></td>
    <td align="right"><input type="checkbox" name="principal" id="principal" /></td>
    <td><label for="principal">Principal</label></td>
  </tr>
  <tr>
    <td valign="top">Instru&ccedil;&otilde;es:</td>
    <td colspan="3"><textarea name="instrucoes" rows="2" maxlength="100" id="instrucoes" style="width:645px;" readonly="true"></textarea></td>
    </tr>
  <tr>
    <td valign="top">Fornecedores:</td>
    <td rowspan="2" valign="top">
    <table width="100%" border="0" cellspacing="1" cellpadding="0">
     <c:set var="qauntidadeFornecedores" value="${fn:length(listaFornecedores)}"/>
     <c:forEach step="2" items="${listaFornecedores}" varStatus ="status">    
      <tr>
       <td width="11%"> <input type="checkbox" name="checkGroupFornecedores" id="fornecedor_<c:out value="${listaFornecedores[status.index].key}" />" value='<c:out value="${listaFornecedores[status.index].key}" />' /></td>
       <td width="41%"><c:out value="${listaFornecedores[status.index].value}"/></td>
        <c:if test="${qauntidadeFornecedores gt (status.index+1)}">
	        <td width="11%"><input type="checkbox" name="checkGroupFornecedores" id="fornecedor_<c:out value="${listaFornecedores[status.index+1].key}" />" value='<c:out value="${listaFornecedores[status.index+1].key}" />' /></td>
	        <td width="41%"><c:out value="${listaFornecedores[status.index+1].value}"/></td>
        </c:if>
        </tr>      
      </c:forEach>
    </table></td>
    <td>Concentra&ccedil;&atilde;o de Pagamentos:</td>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="6%"><input type="radio" name="concentracaoPagamento" id="radio_diaria" value="diaria" onclick='mudaConcentracaoPagamento("diaria");' /></td>
        <td width="14%"><label for="diario">Di&aacute;ria</label></td>
        <td width="7%"><input type="radio" name="concentracaoPagamento" id="radio_semanal" value="semanal" onclick="mudaConcentracaoPagamento('semanal');" /></td>
        <td width="19%"><label for="semanal">Semanal</label></td>
        <td width="7%"><input type="radio" name="concentracaoPagamento" id="radio_quinzenal" value="quinzenal" onclick="mudaConcentracaoPagamento('quinzenal');" /></td>
        <td width="21%"><label for="quinzenal">Quinzenal</label></td>
        <td width="7%"><input type="radio" name="concentracaoPagamento" id="radio_mensal" value="mensal" onclick="mudaConcentracaoPagamento('mensal');" /></td>
        <td width="19%"><label for="mensal">Mensal</label></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
    <td>&nbsp;</td>
    <td>
    	<table width="100%" border="0" cellspacing="1" cellpadding="1" class="quinzenal">
        <tr>
          <td width="72">Todo dia:</td>
          <td width="83"><input type="text" name="diasDoMes[]" id="diaDoMes1" style="width:60px;"/></td>
          <td width="23">e:</td>
          <td width="111"><input type="text" name="diasDoMes[]" id="diaDoMes2" style="width:60px;"/></td>
          </tr>
        </table>
      <table width="100%" border="0" cellspacing="1" cellpadding="1" class="mensal">
        <tr>
          <td width="72">Todo dia:</td>
          <td width="223"><input type="text" name="diasDoMes[]" id="diaDoMes" style="width:60px;"/></td>
          </tr>
        </table>
      <table width="100%" border="0" cellspacing="1" cellpadding="1" class="semanal">
        <tr>
          <td width="20"><input type="checkbox" name="PS" id="PS" /></td>
          <td width="113"><label for="PS">Segunda-feira</label></td>
          <td width="20"><input type="checkbox" name="PT" id="PT" /></td>
          <td width="121"> <label for="PT">Ter&ccedil;a-feira</label></td>
        </tr>
        <tr>
          <td><input type="checkbox" name="PQ" id="PQ" /></td>
          <td><label for="PQ">Quarta-feira</label></td>
          <td><input type="checkbox" name="PQu" id="PQu" /></td>
          <td><label for="PQu">Quinta-feira</label></td>
        </tr>
        <tr>
          <td><input type="checkbox" name="PSex" id="PSex" /></td>
          <td><label for="PSex">Sexta-feira</label></td>
          <td><input type="checkbox" name="PSab" id="PSab" /></td>
          <td><label for="PSab">S&aacute;bado</label></td>
        </tr>
        <tr>
          <td><input type="checkbox" name="PDom" id="PDom" /></td>
          <td><label for="PDom">Domingo</label></td>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
  </table>
</fieldset>		
    </form>	
    
</div>

<div class="container">	
   <fieldset class="classFieldset">
  	    <legend>Pesquisar Par&aacute;metros de Cobran&ccedil;a</legend>
       <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
           <tr>
           
              <td width="113">Forma Pagamento:</td>
              <td colspan="3">
             
              <select name="filtroTipoCobranca" id="filtroTipoCobranca" style="width:150px;">
                   <option></option>
                   <c:forEach varStatus="counter" var="filtroTipoCobranca" items="${listaTiposCobranca}">
			          <option value="${filtroTipoCobranca.key}">${filtroTipoCobranca.value}</option>
			       </c:forEach>
	          </select>
	          
          </td>
          
          <td width="43">Banco:</td>
          <td width="450">
          
              <select name="filtroBanco" id="filtroBanco" style="width:150px;">
                   <option></option>
                   <c:forEach varStatus="counter" var="filtroBanco" items="${listaBancos}">
			          <option value="${filtroBanco.key}">${filtroBanco.value}</option>
			       </c:forEach>
	          </select>
	          
          </td>
          
          <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrarGridConsulta();">Pesquisar</a></span></td>
        </tr>
    </table>
</fieldset>

<div class="linha_separa_fields">&nbsp;</div>
   <fieldset class="classFieldset">
 
   	  <legend>Par&aacute;metros de Cobran&ccedil;as Cadastrados</legend>
      <div class="grids" style="display:none;">
    	  <table class="parametrosGrid"></table>
      </div>

      <span class="bt_novo" id="bt_novo" title="Novo"><a href="javascript:;" onclick="popup();">Novo</a></span>
   </fieldset>
  
   <div class="linha_separa_fields">&nbsp;</div>
</div>




