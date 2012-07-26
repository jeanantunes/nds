<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script language="javascript" type="text/javascript">

	$(function() {		
		$("#valorMinimo").numeric();
		$("#taxaMulta").numeric();
		$("#valorMulta").numeric();
		$("#taxaJuros").numeric();
		$("#diaDoMes").numeric();
	    carregarFornecedores();
	    carregarFormasEmissao(null,"");
	});

	$(function() {
		$(".parametrosGrid").flexigrid({
			preProcess: getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Forma de Pagamento',
				name : 'forma',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Banco',
				name : 'banco',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor Mínimo Emissão R$',
				name : 'vlr_minimo_emissao',
				width : 140,
				sortable : true,
				align : 'right'
			}, {
				display : 'Acumula Divida',
				name : 'acumula_divida',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cobrança Unificada',
				name : 'cobranca_unificada',
				width : 100,
				sortable : true,
				align : 'center',
			}, {
				display : 'Forma Emissão',
				name : 'formaEmissao',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Envio por E-Mail',
				name : 'envio_email',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "Nome",
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
			//TODOS
			$('#tdTitleComboBanco').show();
			$('#tdComboBanco').show();

			$('#tdTitleValorMinimo').show();
			$('#tdValorMinimo').show();
			
			$('#tdTitleMulta').show();
			$('#tdMulta').show();
			
			$('#tdTitleJuros').show();
			$('#tdJuros').show();
	    }
		
		else if ((op=='CHEQUE')||(op=='DINHEIRO')){
			//TODOS, MENOS BANCO
			$('#tdTitleComboBanco').hide();
			$('#tdComboBanco').hide();

			$('#tdTitleValorMinimo').show();
			$('#tdValorMinimo').show();
			
			$('#tdTitleMulta').show();
			$('#tdMulta').show();
			
			$('#tdTitleJuros').show();
			$('#tdJuros').show();

	    }
		
		else if (op=='BOLETO_EM_BRANCO'){
			//TODOS, MENOS VRMINIMO,JUROS E MULTA
			$('#tdTitleComboBanco').show();
			$('#tdComboBanco').show();
			
			$('#tdTitleValorMinimo').hide();
			$('#tdValorMinimo').hide();
			
			$('#tdTitleMulta').hide();
			$('#tdMulta').hide();
			
			$('#tdTitleJuros').hide();
			$('#tdJuros').hide();
			
			//Adiciona em Forma de Emissao:	Individual, agregada à chamada de encalhe 
		}    
		
		else if ((op=='OUTROS')){
			//TODOS, MENOS BANCO E VRMINIMO
			$('#tdTitleComboBanco').hide();
			$('#tdComboBanco').hide();
			
			$('#tdTitleValorMinimo').hide();
			$('#tdValorMinimo').hide();
			
			$('#tdTitleMulta').show();
			$('#tdMulta').show();
			
			$('#tdTitleJuros').show();
			$('#tdJuros').show();
	    }
		
	};
	
	
	function mostraSemanal(){
		$("#tipoFormaCobranca").val('SEMANAL');
		document.formularioFormaCobranca.mensal.checked = false;
		$( ".semanal" ).show();
		$( ".mensal" ).hide();
	};
		
	
	function mostraMensal(){
		$("#tipoFormaCobranca").val('MENSAL');
		document.formularioFormaCobranca.semanal.checked = false;
		$( ".semanal" ).hide();
		$( ".mensal" ).show();
	};
	
	
	function opcaoTipoFormaCobranca(op){
		if (op=='SEMANAL'){
			document.formularioFormaCobranca.semanal.checked = true;
			document.formularioFormaCobranca.mensal.checked = false;
			mostraSemanal();
	    }
		else if (op=='MENSAL'){
			document.formularioFormaCobranca.semanal.checked = false;
			document.formularioFormaCobranca.mensal.checked = true;
			mostraMensal();
		}    
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
		
		$("#_idPolitica").val(resultado.idPolitica);
		
		$("#tipoCobranca").val(resultado.tipoCobranca);
		
		$("#formaCobranca").val(resultado.formaCobranca);
		$("#banco").val(resultado.idBanco);
		
		$("#valorMinimo").val(resultado.valorMinimo);
		$("#taxaMulta").val(resultado.taxaMulta);
		$("#valorMulta").val(resultado.valorMulta);
		$("#taxaJuros").val(resultado.taxaJuros);
		
		$("#instrucoes").val(resultado.instrucoes);

		$("#acumulaDivida").val(0);
		$("#vencimentoDiaUtil").val(0);
		$("#unificada").val(0);
		$("#envioEmail").val(0);
		
		if (resultado.acumulaDivida){
		    $("#acumulaDivida").val(1);
		}
		if (resultado.vencimentoDiaUtil){
		    $("#vencimentoDiaUtil").val(1);
		}
		if (resultado.unificada){
		    $("#unificada").val(1);
		}    
		if (resultado.envioEmail){
		    $("#envioEmail").val(1);
		}
		
		$("#diaDoMes").val(resultado.diaDoMes);
		
		$("#principal").val(resultado.principal);
		document.formularioParametro.principal.checked = resultado.principal;

		$("#PS").val(resultado.segunda);
		document.formularioFormaCobranca.PS.checked = resultado.segunda;
		
		$("#PT").val(resultado.terca);
		document.formularioFormaCobranca.PT.checked = resultado.terca;
		
		$("#PQ").val(resultado.quarta);
		document.formularioFormaCobranca.PQ.checked = resultado.quarta;
		
		$("#PQu").val(resultado.quinta);
		document.formularioFormaCobranca.PQu.checked = resultado.quinta;
		
		$("#PSex").val(resultado.sexta);
		document.formularioFormaCobranca.PSex.checked = resultado.sexta;
		
		$("#PSab").val(resultado.sabado);
		document.formularioFormaCobranca.PSab.checked = resultado.sabado;
		
		$("#PDom").val(resultado.domingo);
		document.formularioFormaCobranca.PDom.checked = resultado.domingo;

		opcaoPagto(resultado.tipoCobranca);
		opcaoTipoFormaCobranca(resultado.tipoFormaCobranca);
		obterFornecedoresUnificados(resultado.fornecedoresId);
		
		carregarFormasEmissao(resultado.tipoCobranca,formaEmissao);
	}
	
	//OBTEM FORNECEDORES UNIFICADOS
	function obterFornecedoresUnificados(unificados) {
		$("input[name='checkGroupFornecedores']:checked").each(function(i) {
			document.getElementById("fornecedor_"+$(this).val()).checked = false;
		});
		for(i=0;i<unificados.length;i++){
			document.getElementById("fornecedor_"+unificados[i]).checked = true;
		}
	}

	
	
	//OBTEM FORNECEDORES MARCADOS PELO USUARIO PARA UNIFICÁ-LOS
	function obterFornecedoresMarcados() {
		var fornecedorMarcado = "";
		$("input[name='checkGroupFornecedores']:checked").each(function(i) {
			fornecedorMarcado += 'listaIdsFornecedores=' + $(this).val() + '&';
		});
		return fornecedorMarcado;
	}
	
	
	//INCLUSÃO DE NOVO PARAMETRO
    function postarParametro(novo) {
		
    	var telaMensagem="idModal";

		var idPolitica = $("#_idPolitica").val();
		
    	var tipoCobranca = $("#tipoCobranca").val();
    	var tipoFormaCobranca = $("#tipoFormaCobranca").val();
    	var formaEmissao = $("#formaEmissao").val();
		var banco = $("#banco").val();
		
		var valorMinimo = $("#valorMinimo").val();
		var taxaMulta = $("#taxaMulta").val();
		var valorMulta = $("#valorMulta").val();
		var taxaJuros = $("#taxaJuros").val();
		
		var instrucoes = $("#instrucoes").val();

		var acumulaDivida = $("#acumulaDivida").val();
		var vencimentoDiaUtil = $("#vencimentoDiaUtil").val();
		var unificada = $("#unificada").val();
		var envioEmail = $("#envioEmail").val();
		
		$("#principal").val(0);
    	if (document.formularioParametro.principal.checked){
    		$("#principal").val(1);
		}
		var principal      = $("#principal").val();

		var diaDoMes       = $("#diaDoMes").val();
		
		$("#PS").val(0);
		if (document.formularioFormaCobranca.PS.checked){
			$("#PS").val(1);
		}
		var segunda = $("#PS").val();
		
		$("#PT").val(0);
		if (document.formularioFormaCobranca.PT.checked){
			$("#PT").val(1);
		}
		var terca = $("#PT").val();
		
		$("#PQ").val(0);
		if (document.formularioFormaCobranca.PQ.checked){
			$("#PQ").val(1);
		}
		var quarta = $("#PQ").val();
		
		$("#PQu").val(0);
		if (document.formularioFormaCobranca.PQu.checked){
			$("#PQu").val(1);
		}
		var quinta = $("#PQu").val();
		
		$("#PSex").val(0);
		if (document.formularioFormaCobranca.PSex.checked){
			$("#PSex").val(1);
		}
		var sexta  = $("#PSex").val();
		
		$("#PSab").val(0);
		if (document.formularioFormaCobranca.PSab.checked){
			$("#PSab").val(1);
		}
		var sabado = $("#PSab").val();
		
		$("#PDom").val(0);
		if (document.formularioFormaCobranca.PDom.checked){
			$("#PDom").val(1);
		}
		var domingo  = $("#PDom").val();

		if(novo){
			$.postJSON("<c:url value='/distribuidor/parametroCobranca/postarParametroCobranca'/>",
					   "parametros.tipoCobranca="+tipoCobranca+
					   "&parametros.idBanco="+ banco +
					   "&parametros.valorMinimo="+ valorMinimo+
					   "&parametros.taxaMulta="+ taxaMulta +
					   "&parametros.valorMulta="+ valorMulta+
					   "&parametros.taxaJuros="+ taxaJuros+
					   "&parametros.instrucoes="+ instrucoes+
					   "&parametros.acumulaDivida="+ acumulaDivida+
					   "&parametros.vencimentoDiaUtil="+ vencimentoDiaUtil+
					   "&parametros.unificada="+ unificada+
					   "&parametros.envioEmail="+ envioEmail+
					   "&parametros.formaEmissao="+ formaEmissao+
					   "&parametros.principal="+ principal+
					   
					   "&parametros.domingo="+domingo+    
					   "&parametros.segunda="+segunda+            
					   "&parametros.terca="+terca+            
					   "&parametros.quarta="+quarta+            
					   "&parametros.quinta="+quinta+            
					   "&parametros.sexta="+sexta+            
					   "&parametros.sabado="+sabado+
					   "&parametros.diaDoMes="+diaDoMes+
					   "&tipoFormaCobranca="+tipoFormaCobranca+
					   "&"+obterFornecedoresMarcados(),
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
			   			telaMensagem);
		}
		else{
			$.postJSON("<c:url value='/distribuidor/parametroCobranca/postarParametroCobranca'/>",
					   "parametros.idPolitica="+idPolitica+
					   "&parametros.tipoCobranca="+tipoCobranca+
					   "&parametros.idBanco="+ banco +
					   "&parametros.valorMinimo="+ valorMinimo+
					   "&parametros.taxaMulta="+ taxaMulta +
					   "&parametros.valorMulta="+ valorMulta+
					   "&parametros.taxaJuros="+ taxaJuros+
					   "&parametros.instrucoes="+ instrucoes+
					   "&parametros.acumulaDivida="+ acumulaDivida+
					   "&parametros.vencimentoDiaUtil="+ vencimentoDiaUtil+
					   "&parametros.unificada="+ unificada+
					   "&parametros.envioEmail="+ envioEmail+
					   "&parametros.formaEmissao="+ formaEmissao+
					   "&parametros.principal="+ principal+
					   
					   "&parametros.domingo="+domingo+    
					   "&parametros.segunda="+segunda+            
					   "&parametros.terca="+terca+            
					   "&parametros.quarta="+quarta+            
					   "&parametros.quinta="+quinta+            
					   "&parametros.sexta="+sexta+            
					   "&parametros.sabado="+sabado+
					   "&parametros.diaDoMes="+diaDoMes+
					   "&tipoFormaCobranca="+tipoFormaCobranca+
					   "&"+obterFornecedoresMarcados(),
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
			   			telaMensagem);
		}
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
			document.getElementById("fornecedor_"+$(this).val()).checked = false;
		});
		
		$( ".semanal" ).hide();
		$( ".mensal" ).hide();
		document.formularioFormaCobranca.mensal.checked = false;
		document.formularioFormaCobranca.semanal.checked = false;
		
        $("#_idPolitica").val("");
		
		$("#tipoCobranca").val("");
		
		$("#formaEmissao").val("");
		$("#formaCobranca").val("");
		$("#banco").val("");
		
		$("#valorMinimo").val("");
		$("#taxaMulta").val("");
		$("#valorMulta").val("");
		$("#taxaJuros").val("");
		
		$("#instrucoes").val("");

		$("#acumulaDivida").val(0);
		$("#vencimentoDiaUtil").val(0);
		$("#unificada").val(0);
		$("#envioEmail").val(0);
		
		$("#diaDoMes").val("");
		document.formularioParametro.principal.checked = false;
		document.formularioFormaCobranca.PS.checked = false;
		document.formularioFormaCobranca.PT.checked = false;
		document.formularioFormaCobranca.PQ.checked = false;
		document.formularioFormaCobranca.PQu.checked = false;
		document.formularioFormaCobranca.PSex.checked = false;
		document.formularioFormaCobranca.PSab.checked = false;
		document.formularioFormaCobranca.PDom.checked = false;
		
		carregarFormasEmissao(null,"");

	}

	
	
    function montarTrRadioBox(result,name,nameItemIdent) {

		var options = "";
		
		$.each(result, function(index, row) {

			options += "<tr> <td width='23'>";
			options += "<input id='" + nameItemIdent + row.key.$ +"' value='" + row.key.$ + "' name='"+name+"' type='checkbox' />";
			options += "</td> <td width='138'>";	
			options += "<label for='" + nameItemIdent + row.key.$ +"' >"+ row.value.$ +"</label>";
		    options += "</td>";
		    options += "</tr>";   

		});
		
		return options;
	}
	
	function carregarFornecedores(){
		$.postJSON("<c:url value='/distribuidor/parametroCobranca/obterFornecedores' />",
				   null,
				   sucessCallbackCarregarFornecedores,
				   null,
				   true);
	}
	
	function sucessCallbackCarregarFornecedores(result) {
	    var radioBoxes =  montarTrRadioBox(result,"checkGroupFornecedores","fornecedor_");
		$("#fornecedores").html(radioBoxes);
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


<input type="hidden" name="_idPolitica" id="_idPolitica"/>
<input type="hidden" name="tipoFormaCobranca" id="tipoFormaCobranca"/>


<div id="dialog-excluir" title="Excluir Parâmetro de Cobrança">
	<p>Confirma a exclusão deste Parâmetro de Cobrança?</p>
</div>


<div id="dialog-novo" title="Incluir Forma de Recebimento">

	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idModal" name="messageDialog"/>
	</jsp:include>

    <form id="formularioParametro" name="formularioParametro">

		<table width="809" border="0" cellspacing="2" cellpadding="2">
		   
		   
		   <tr>
		       <td width="200">Principal</td>
			   <td width="200">
			       <input name="principal" type="checkbox"
			       id="principal" style="float: left;" value="" checked="checked" />
			   </td>
		   </tr>
		   
		   
		   <tr>
		     <td width="144">Forma de Pagamento:</td>
		     <td>
		     
			      <select name="tipoCobranca" id="tipoCobranca" style="width:150px;" onchange="opcaoPagto(this.value);carregarFormasEmissao(this.value,'');">
	                   <option value="">Selecione</option>
	                   <c:forEach varStatus="counter" var="tipoCobranca" items="${listaTiposCobranca}">
				          <option value="${tipoCobranca.key}">${tipoCobranca.value}</option>
				       </c:forEach>
		          </select>  
		          
		     </td>
		     
		     <td width="204">Acumula Dívida:</td>
		     <td width="234">
			     <select name="acumulaDivida" id="acumulaDivida" style="width:80px;">
			        <option value="1">Sim</option>
			        <option value="0">Não</option>
			     </select>
		     </td>
		   </tr>
		   
		   
		   <tr>
			    <td id="tdTitleComboBanco" width="144">Banco:</td>
			    <td id="tdComboBanco">
			    
				    <select name="banco" id="banco" style="width:150px;" onchange="obterDadosBancarios(this.value);">
				       <option value="">Selecione</option>
	                   <c:forEach varStatus="counter" var="banco" items="${listaBancos}">
				          <option value="${banco.key}">${banco.value}</option>
				       </c:forEach>
		            </select>
		            
			    </td>
			    
			    <td>Vencimentos somente em dia útil:</td>
			    <td>
				    <select name="vencimentoDiaUtil" id="vencimentoDiaUtil" style="width:80px;">
				      <option value="1">Sim</option>
				      <option value="0">Não</option>
				    </select>
			    </td>
		   </tr>

		   
		   <tr>
			    <td id="tdTitleValorMinimo" valign="top">Valor Mínimo Emissão:</td>
			    <td id="tdValorMinimo" valign="top">
			        <input type="text" maxlength="16" name="valorMinimo" id="valorMinimo" style="width:100px;" />
			    </td>
			    
			    <td>Cobrança Unificada:</td>
			    <td>
				    <select name="unificada" id="unificada" style="width:80px;">
				      <option value="1">Sim</option>
				      <option value="0">Não</option>
				    </select>
			        <br clear="all" />
			    </td>
		    </tr>
		    
		    
		    <tr>
			    <td id="tdTitleMulta">Multa  %:</td>
			    <td id="tdMulta" width="201"><table width="100%" border="0" cellspacing="0" cellpadding="0" style="color: buttonface;">
			        <tr>
			          <td width="37%">
			              <input type="text" maxlength="16" readonly="true" name="taxaMulta" id="taxaMulta" style="width:70px;" />
			          </td>
			          <td width="24%">&nbsp;ou  R$:</td>
			          <td width="39%">
			              <input type="text" maxlength="16" readonly="true" name="valorMulta" id="valorMulta" style="width:70px;" />
			          </td>
			        </tr>
			    </table></td>
			    <td width="204">Envio por E-mail:</td>
			    <td colspan="2"><select name="envioEmail" id="envioEmail" style="width:80px;">
			      <option value="1">Sim</option>
			      <option value="0">Não</option>
			    </select></td>
		    </tr>
		  
		  
		    <tr>
			    <td id="tdTitleJuros">Juros%:</td>
			    <td id="tdJuros"><input type="text" maxlength="16" readonly="true" name="taxaJuros" id="taxaJuros" style="width:100px;" /></td>
			    <td>Impressão:</td>
			    
			    <td>
				    <div id="formasEmissao"/>
			    </td>
		    </tr>
		  
		  
		    <tr>
			    <td valign="top">Instruções:</td>
			    <td colspan="3">
			        <textarea readonly="true" name="instrucoes" maxlength="100" rows="4" id="instrucoes" style="width:645px;"></textarea>
			    </td>
		    </tr>
		    
		</table>
		
    </form>		
    
    
    
    <legend>Unificar Cobranças</legend>
    
	<form name="formularioFormaCobranca" id="formularioFormaCobranca">		
                         
	    <table width="800" height="25" border="0" cellpadding="1" cellspacing="1">
		    
		     <tr class="header_table">
		         <td align="left">Fornecedores</td>
		         <td align="left">&nbsp;</td>
		         <td align="left">Concentração de Pagamentos</td>
		     </tr>
		     
	         <tr>
	         
               
                 <td width="400" align="left" valign="top" style="border:1px solid #ccc;">

	                 <table width="168" border="0" cellspacing="1" cellpadding="1">
	                     <div id="fornecedores"/>
		             </table>
		             
	                 <p><br clear="all" />
		                 <br clear="all" />
		                 <br clear="all" />
		                 <br clear="all" />
	                 </p>
                 
                 </td>
                 
                 
			     <td width="6" align="left" valign="top">&nbsp;</td>
			     <td width="148" align="left" valign="top"style="border:1px solid #ccc;">

			         <table width="100%" border="0" cellspacing="1" cellpadding="1">
				         <tr>
				             <td width="20"><input type="radio" name="mensal" id="mensal" value="radio" onclick="mostraMensal();" /></td>
				             <td width="173">Mensal</td>
				             <td width="20"><input type="radio" name="semanal" id="semanal" value="radio" onclick="mostraSemanal();" /></td>
				             <td width="173">Semanal</td>
				         </tr>
				     </table>
				    
				     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="mensal">
				         <tr>
				             <td width="68">Todo dia:</td>
				             <td width="156"><input maxlength="2" type="text" name="diaDoMes" id="diaDoMes" style="width:60px;"/></td>
				         </tr>
				     </table>
			     
		        
                     <table width="100%" border="0" cellspacing="1" cellpadding="1" class="semanal">
					        
			             <tr>
			                 <td>
			                     <input type="checkbox" name="PS" id="PS" />
			                 </td>    
			                 <td>
			                     <label for="PS">Segunda-feira</label>
			                 </td>
			             </tr>
					            
					     <tr>
			                 <td>           
					             <input type="checkbox" name="PT" id="PT" />
					         </td>    
			                 <td>    
					             <label for="PT">Terça-feira</label>
					         </td>
			             </tr>
			             
			             <tr>
			                 <td>            
					             <input type="checkbox" name="PQ" id="PQ" />
					         </td>    
			                 <td>      
					             <label for="PQ">Quarta-feira</label>
					         </td>
			              </tr>    
					                          
					      <tr>
			                 <td>          
					             <input type="checkbox" name="PQu" id="PQu" />
					          </td>    
			                  <td>  
					             <label for="PQu">Quinta-feira</label>
					          </td>
			              </tr>
					                  
					      <tr>
			                 <td>          
					             <input type="checkbox" name="PSex" id="PSex" />
					         </td>    
			                 <td>      
					             <label for="PSex">Sexta-feira</label>
					         </td>
			              </tr>    
					               
					      <tr>
			                 <td>    
					             <input type="checkbox" name="PSab" id="PSab" />
					             </td>    
			                 <td>  
					             <label for="PSab">Sábado</label>
					         </td>
			              </tr>
					                   
					      <tr>
			                  <td>
					             <input type="checkbox" name="PDom" id="PDom" />
					             </td>    
			                 <td>  
					             <label for="PDom">Domingo</label>
					         </td>
			              </tr>
					
					 </table>
					 
				 	
					 
			     </td>
  
             </tr>  

			 <tr>
			    <td valign="top">&nbsp;</td>
			    <td valign="top">&nbsp;</td>
			    <td valign="top">&nbsp;</td>
			 </tr>

		</table>
		
    </form>
    
    
    
    
</div>

<div class="container">	
<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
			<b>Parâmetro < evento > com < status >.</b></p>
</div>

   <fieldset class="classFieldset">
  	    <legend>Pesquisar Parâmetros de Cobrança</legend>
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
 
   	  <legend>Parâmetros de Cobranças Cadastrados</legend>
      <div class="grids" style="display:none;">
    	  <table class="parametrosGrid"></table>
      </div>

      <span class="bt_novo" id="bt_novo" title="Novo"><a href="javascript:;" onclick="popup();">Novo</a></span>
   </fieldset>
  
   <div class="linha_separa_fields">&nbsp;</div>
</div>




