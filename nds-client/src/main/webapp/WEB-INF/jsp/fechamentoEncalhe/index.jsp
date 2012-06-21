<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script language="javascript" type="text/javascript">

	var contextPath = '${pageContext.request.contextPath}';
	var vDataEncalhe = '';
	var vFornecedorId = '';
	var vBoxId = '';
	
	function pesquisar(aplicaRegraMudancaTipo ) {
		if (aplicaRegraMudancaTipo == null ){
			aplicaRegraMudancaTipo = false;
		}
		
		$('#divBotoesPrincipais').show();
		
		$(".fechamentoGrid").flexOptions({
			"url" : contextPath + '/devolucao/fechamentoEncalhe/pesquisar',
			params : [{
				name : "dataEncalhe",
				value : $('#datepickerDe').val()
			}, {
				name : "fornecedorId",
				value : $('#selectFornecedor').val()
			}, {
				name : "boxId",
				value : $('#selectBoxEncalhe').val()
			},
			{
				name : "aplicaRegraMudancaTipo",
				value : aplicaRegraMudancaTipo
			}],


			
			newp:1
		});
		
		$(".fechamentoGrid").flexReload();
		$(".grids").show();
		
		vDataEncalhe = $('#datepickerDe').val();
		vFornecedorId = $('#selectFornecedor').val();
		vBoxId = $('#selectBoxEncalhe').val();
	}
	
	function preprocessamentoGridFechamento(resultado) {
		
		$.each(resultado.rows, function(index, row) {
			
			if (row.cell.diferenca == "0") {
				row.cell.diferenca = "";
			}
			
			var valorFisico = row.cell.fisico == null ? '' : row.cell.fisico;
			var fechado = row.cell.fechado == false ? '' : 'disabled="disabled"';
			row.cell.fisico = '<input type="text" style="width: 60px" id = "'+row.cell.produtoEdicao+'"  name="fisico" value="' + valorFisico + '" onchange="onChangeFisico(this, ' + index + ')" ' + fechado + '/>';
			
			row.cell.replicar = '<span title="Replicar"><a href="javascript:;" onclick="replicar(' + index + ')"><img src="${pageContext.request.contextPath}/images/ico_atualizar.gif" border="0" /></a></span>';
			
			if (fechado != '') {
				$('#divBotoesPrincipais').hide();
			}
		});
		
		return resultado;
	}
	
	function replicarTodos() {
	
		var tabela = $('.fechamentoGrid').get(0);
		for (i=0; i<tabela.rows.length; i++) {
			replicar(i);
		}
	}
	
	function replicar(index) {
		
		var tabela = $('.fechamentoGrid').get(0);
		var valor = tabela.rows[index].cells[4].firstChild.innerHTML;
		var campo = tabela.rows[index].cells[6].firstChild.firstChild;
		var diferenca = tabela.rows[index].cells[7].firstChild;

		campo.value = valor;
		diferenca.innerHTML = "0";
	}
	
	function onChangeFisico(campo, index) {
		
		var tabela = $('.fechamentoGrid').get(0);
		var devolucao = parseInt(tabela.rows[index].cells[4].firstChild.innerHTML);
		var diferenca = tabela.rows[index].cells[7].firstChild;
		
		if (campo.value == "") {
			diferenca.innerHTML = "";
		} else {
			diferenca.innerHTML = devolucao - campo.value;			
		}
	}
	
	function gerarArrayFisico() {
		
		var tabela = $('.fechamentoGrid').get(0);
		var fisico;
		var arr = new Array();
		
		for (i=0; i<tabela.rows.length; i++) {
			fisico = tabela.rows[i].cells[6].firstChild.firstChild.value;
			arr.push(fisico);
		}
		
		return arr;
	}
	
	function salvar() {
			$.postJSON(
				"<c:url value='/devolucao/fechamentoEncalhe/salvar' />",
				populaParamentrosFechamentoEncalheInformados(),
				function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagem(tipoMensagem, listaMensagens);
					}
					pesquisar();
					
				},
			  	null,
			   	false
			);
			
		}
	
	function popup_encerrarEncalhe() {

		var dataEncalhe = $("#datepickerDe").val();
		
		$(".cotasGrid").flexOptions({
			url: "<c:url value='/devolucao/fechamentoEncalhe/cotasAusentes' />",
			params: [{name:'dataEncalhe', value: dataEncalhe }],
			newp: 1,
		});
		
		$(".cotasGrid").flexReload();
	};





	
	function verificarEncerrarOperacaoEncalhe() {

		$.postJSON(
			"<c:url value='/devolucao/fechamentoEncalhe/verificarEncerrarOperacaoEncalhe' />",
			{ 'dataEncalhe' : $('#datepickerDe').val() , 
			  'operacao' : 'VERIFICACAO' 
		},
			function (result) {

				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens);
				}

				if (result == 'NAO_ENCERRAR') {
					popup_encerrarEncalhe();
				} else if (result == 'ENCERRAR'){
					popup_encerrar();
				}			
			},
		  	null,
		   	false
		);
	}
	
	function popup_encerrar() {
		
		$("#dataConfirma").html($("#datepickerDe").val());
		
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {

					$.postJSON(
						"<c:url value='/devolucao/fechamentoEncalhe/verificarEncerrarOperacaoEncalhe' />",
						{ 'dataEncalhe' : $('#datepickerDe').val() , 
						  'operacao' : 'CONFIRMACAO' },
						function (result) {

							$("#dialog-confirm").dialog("close");
							
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens);
							}

							if (result == 'NAO_ENCERRAR') {
								popup_encerrarEncalhe();
							} else if (result == 'ENCERRAR'){
								popup_encerrar();
							}
						},
					  	null,
					   	false
					);
				},
				
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				$(".fechamentoGrid").flexReload();
			}
		});
	};
	
	
	function encerrarFechamento() {
		
		salvar();
		
		/* verificar cotas pendentes */
		
		$.postJSON(
			"<c:url value='/devolucao/fechamentoEncalhe/encerrarFechamento' />",
			{ 'dataEncalhe' : $('#datepickerDe').val() },
			function (result) {
				
			},
		  	null,
		   	true
		);
	}

	
	$(function() {
		$("#datepickerDe").datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$("#dtPostergada").datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
	
	function confirmar(){
		$(".dados").show();
	}
	
	function checarTodasCotasGrid(checked) {
				
		if (checked) {
			$("input[type=checkbox][name='checkboxGridCotas']").each(function() {
				if (this.disabled == false) {
					var elem = document.getElementById("textoCheckAllCotas");
					elem.innerHTML = "Desmarcar todos";
					$(this).attr('checked', true);
				}
			});
				
        } else {
			var elem = document.getElementById("textoCheckAllCotas");
			elem.innerHTML = "Marcar todos";

			$("input[type=checkbox][name='checkboxGridCotas']").each(function(){
				$(this).attr('checked', false);
			});
		}
	}

	function preprocessamentoGrid(resultado) {	
		
		if (resultado.mensagens) {
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			$(".cotasGrid").hide();
			return resultado;
		}

		$( "#dialog-encerrarEncalhe" ).dialog({
			resizable: false,
			height:500,
			width:650,
			modal: true,
			buttons: {
				"Postergar": function() {
					postergarCotas();
				},
				"Cobrar": function() {
					cobrarCotas();
				},
				"Cancelar": function() {
					$(this).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			}
		});
		
		document.getElementById("checkTodasCotas").checked = false;
		checarTodasCotasGrid(false);
		
		$.each(resultado.rows, function(index, row) {
			
			var checkBox = '<span></span>';
			
			if (row.cell.acao == null || row.cell.acao == '') { 
				checkBox = '<input type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" value="' + row.cell.idCota + '" />';	
			} else {
				checkBox = '<input type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" value="' + row.cell.idCota + '" disabled="disabled"/>';	
			}
			
		    row.cell.check = checkBox;
		});

		
		$(".cotasGrid").show();
		
		return resultado;
	}

	function obterCotasMarcadas() {
 
		var cotasAusentesSelecionadas = new Array();

		$("input[type=checkbox][name='checkboxGridCotas']:checked").each(function(){
			cotasAusentesSelecionadas.push(parseInt($(this).val()));
		});

		return cotasAusentesSelecionadas;
	}
	
	function postergarCotas() {

		var cotasSelecionadas = obterCotasMarcadas();

		if (cotasSelecionadas.length > 0) {
			
			$("#dialog-postergar").dialog({
				resizable: false,
				height:'auto',
				width:250,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						var dataPostergacao = $("#dtPostergada").val();
						var dataEncalhe = $("#datepickerDe").val();
						
						$.postJSON("<c:url value='/devolucao/fechamentoEncalhe/postergarCotas' />",
									{ 'dataPostergacao' : dataPostergacao, 
									  'dataEncalhe' : dataEncalhe, 
									  'idsCotas' : obterCotasMarcadas() },
									function (result) {
	
										$("#dialog-postergar").dialog("close");
										
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemEncerrarEncalhe');
										}

										$(".cotasGrid").flexReload();
									},
								  	null,
								   	true,
								   	'dialogMensagemEncerrarEncalhe'
							);
					},
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					$("#dtPostergada").val("");
					clearMessageDialogTimeout('dialogMensagemEncerrarEncalhe');
				}
			});
	
			carregarDataPostergacao();
			
		} else {
			var listaMensagens = new Array();
			listaMensagens.push('Selecione pelo menos uma cota para postergar!');
			exibirMensagemDialog('WARNING', listaMensagens, 'dialogMensagemEncerrarEncalhe');
		}
	}

	function carregarDataPostergacao() {

		var dataPostergacao = $("#dtPostergada").val();
		var dataEncalhe = $("#datepickerDe").val();
		
		$.postJSON("<c:url value='/devolucao/fechamentoEncalhe/carregarDataPostergacao' />",
				{ 'dataEncalhe' : dataEncalhe, 'dataPostergacao' : dataPostergacao },
				function (result) {

					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					
					if (tipoMensagem && listaMensagens) {
						exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemPostergarCotas');
					} else {
						$("#dtPostergada").val(result);
					}
				},
			  	null,
			   	true,
			   	'dialogMensagemPostergarCotas'
		);

	}
	
	function cobrarCotas() {

		var dataOperacao = $("#datepickerDe").val();
		
		$.postJSON("<c:url value='/devolucao/fechamentoEncalhe/cobrarCotas' />",
					{ 'dataOperacao' : dataOperacao, 'idsCotas' : obterCotasMarcadas() },
					function (result) {
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemEncerrarEncalhe');
						}

						$(".cotasGrid").flexReload();
					},
				  	null,
				   	true
			);
	}

	function gerarArquivoCotasAusentes(fileType) {

		var dataEncalhe = $("#datepickerDe").val();
		
		window.location = 
			contextPath + 
			"/devolucao/fechamentoEncalhe/exportarArquivo?" + 
			"dataEncalhe=" + dataEncalhe + 
			"&sortname=" + $(".cotasGrid").flexGetSortName() +
			"&sortorder=" + $(".cotasGrid").getSortOrder() +
			"&rp=" + $(".cotasGrid").flexGetRowsPerPage() +
			"&page=" + $(".cotasGrid").flexGetPageNumber() +
			"&fileType=" + fileType;

		return false;
	}
	
	function imprimirArquivo(fileType) {

		var dataEncalhe = $("#datepickerDe").val();
		
		window.location = contextPath + "/devolucao/fechamentoEncalhe/imprimirArquivo?"
			+ "dataEncalhe=" + vDataEncalhe
			+ "&fornecedorId="+ vFornecedorId
			+ "&boxId=" + vBoxId
			+ "&sortname=" + $(".fechamentoGrid").flexGetSortName()
			+ "&sortorder=" + $(".fechamentoGrid").getSortOrder()
			+ "&rp=" + $(".fechamentoGrid").flexGetRowsPerPage()
			+ "&page=" + $(".fechamentoGrid").flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	}

	function verificarMensagemConsistenciaDados() {
		$.postJSON(
			"<c:url value='/devolucao/fechamentoEncalhe/verificarMensagemConsistenciaDados' />",
			{ 
			    'dataEncalhe' : $('#datepickerDe').val(),
			    'fornecedorId' : $('#selectFornecedor').val(),
				'boxId' :  $('#selectBoxEncalhe').val()
		    },
			function (result) {

				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					$('#mensagemConsistenciaDados').html(listaMensagens[0])
					popup_mensagem_consistencia_dados();
				} else {
					pesquisar(false);
				}

    		},
		  	null,
		   	false
		);
	}	

	function popup_mensagem_consistencia_dados() {
		$( "#dialog-mensagem-consistencia-dados" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					pesquisar(true);
					$(this).dialog( "close" );
				},
				
				"Cancelar": function() {
					$(this).dialog( "close" );
				}
			},
			beforeClose: function() {
				//clearMessageDialogTimeout('dialogMensagemNovo');
			}
		});
	}

	 function populaParamentrosFechamentoEncalheInformados(){
		var dados ="";
		var index = 0;
		$("input[type=text][name='fisico']").each(function(){
			if (dados != ""){
				dados+=",";
			}

		    if ( $(this).val() != null &&  $(this).val() !=  "" ){
				  var  qtd = parseInt($(this).val());
		     	  dados+='{name:"listaFechamento['+index+'].produtoEdicao",value:'+$(this).attr('id')+'}, {name:"listaFechamento['+index+'].fisico",value:'+qtd+'}';
		     	  index++;
		    }
			
		});
		var fornecedorId = null;
		if ($('#selectFornecedor').val() !=""){
		    fornecedorId = $('#selectFornecedor').val();
		}
		
		var boxId = null;
		if ($('#selectBoxEncalhe').val() !=""){
		    boxId = $('#selectBoxEncalhe').val();
		}
		
		dados+=',{name:"dataEncalhe",value:"'+$('#datepickerDe').val()+'"},{name:"fornecedorId",value:'+fornecedorId+'},{name:"boxId",value:'+boxId+'}';
		var params = '['+dados+ ']';
		return eval(params);
	}

	 function limpaGridPesquisa(){
		 $(".fechamentoGrid").clear();
		 $('#divFechamentoGrid').css("display", "none");
		 
	}

	 function salvarNoEncerrementoOperacao() {
			$.postJSON(
				"<c:url value='/devolucao/fechamentoEncalhe/salvarNoEncerrementoOperacao' />",
				populaParamentrosFechamentoEncalheInformados(),
				function (result) {
					var tipoMensagem = result.tipoMensagem;
					var listaMensagens = result.listaMensagens;
					if (tipoMensagem && listaMensagens) {
						exibirMensagem(tipoMensagem, listaMensagens);
					} else {
						verificarEncerrarOperacaoEncalhe();
					}
				},
			  	null,
			   	false
			);
			
		}

	
	</script>

	<style type="text/css">
 		#dialog-encerrarEncalhe fieldset{width:600px!important;}
	</style>
	
</head>

<body>

	<div id="dialog-confirm" title="Encerrar Opera&ccedil;&atilde;o" style="display:none;">
		<p>Confirma o encerramento da opera&ccedil;&atilde;o do dia <span id="dataConfirma"></span>:</p>
	</div>
	
	<div id="dialog-mensagem-consistencia-dados" title="Fechamento Encalhe" style="display:none;">
		<p id="mensagemConsistenciaDados" ></p>
	</div>

	<div id="dialog-postergar" title="Postergar Encalhe" style="display:none;">
	
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialogMensagemPostergarCotas" name="messageDialog"/>
		</jsp:include> 
		
		<fieldset style="width:200px!important;">
	    	<legend>Postergar Encalhe</legend>
			<table border="0" cellspacing="2" cellpadding="0">
	          <tr>
	            <td width="70">Nova Data:</td>
	            <td width="103"><input name="dtPostergada" type="text" id="dtPostergada" style="width:80px;" onchange="carregarDataPostergacao();" /></td>
	          </tr>
	        </table>
	    </fieldset>
	</div>
	
	<div id="dialog-encerrarEncalhe" title="Opera&ccedil;&atilde;o de Encalhe" style="display:none;">
		
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialogMensagemEncerrarEncalhe" name="messageDialog"/>
		</jsp:include> 
		
		<fieldset>
			<legend>Cotas Ausentes</legend>
			<form id="formGridCotas" name="formGridCotas" >
				<table class="cotasGrid" id="tabelaGridCotas" ></table>
			</form>
			<span class="bt_novos" title="Gerar Arquivo" >
				<a href="javascript:gerarArquivoCotasAusentes('XLS');">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>
			<span class="bt_novos" title="Imprimir">
				<a href="javascript:gerarArquivoCotasAusentes('PDF');">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					Imprimir 
				</a>
			</span>
			<span class="bt_sellAll" style="float:right;">
				<input type="checkbox" id="checkTodasCotas" name="checkTodasCotas" onchange="checarTodasCotasGrid(this.checked);" style="float:right;margin-right:25px;"/>
				<label for="checkTodasCotas" id="textoCheckAllCotas" ></label>
			</span>
		</fieldset>
	</div>

    
    <fieldset class="classFieldset">
    	<legend> Pesquisar Fornecedor</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="75">Data Encalhe:</td>
				<td width="114"><input name="datepickerDe" type="text" id="datepickerDe" style="width:80px;" value="${dataOperacao}" onchange="limpaGridPesquisa()" /></td>
				<td width="67">Fornecedor:</td>
				<td width="216">
					<select name="selectFornecedor" id="selectFornecedor" style="width:200px;" onchange="limpaGridPesquisa()">
					<option value="">Selecione...</option>
					<c:forEach var="fornecedor" items="${listaFornecedores}">
						<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
					</c:forEach>
					</select>
				</td>
				<td width="97">Box de Encalhe:</td>
				<td width="239">
					<select name="selectBoxEncalhe" id="selectBoxEncalhe" style="width:100px;" onchange="limpaGridPesquisa()">
					<option value="">Selecione...</option>
					<c:forEach var="box" items="${listaBoxes}">
						<option value="${box.id}">${box.nome}</option>
					</c:forEach>
					</select>
				</td>
				<td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="verificarMensagemConsistenciaDados();">Pesquisar</a></span></td>
			</tr>
		</table>
    </fieldset>

    <div class="linha_separa_fields">&nbsp;</div>
      
    <fieldset class="classFieldset">
       	<legend> Fechamento Encalhe</legend>
        <div class="grids" style="display:none;" id="divFechamentoGrid">
			
			<table class="fechamentoGrid"></table>
			
			<div id="divBotoesPrincipais" style="display:none;">
	            <span class="bt_novos" title="Salvar"><a href="javascript:;" onclick="salvar()"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Salvar </a></span>
				<span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="popup_encerrarEncalhe();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Cotas Ausentes</a></span>
				<span class="bt_novos" title="Encerrar Opera&ccedil;&atilde;o Encalhe"><a href="javascript:;" onclick="salvarNoEncerrementoOperacao();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Encerrar Opera&ccedil;&atilde;o Encalhe</a></span>
				<span class="bt_sellAll" style="float:right;"><a href="javascript:;" id="sel" onclick="replicarTodos();"><img src="${pageContext.request.contextPath}/images/ico_atualizar.gif" border="0" /></a><label for="sel">Replicar Todos</label></span>
			</div>
			
        	<br clear="all" />
        	
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;" onclick="imprimirArquivo('XLS');"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			<span class="bt_novos" title="Imprimir"><a href="javascript:;" onclick="imprimirArquivo('PDF');"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir </a></span>
        </div>
	</fieldset>
    
    <div class="linha_separa_fields">&nbsp;</div>

	<script>
		$(".cotasGrid").flexigrid({
			preProcess: preprocessamentoGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'colaboradorName',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'boxName',
				width : 37,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'roteiroName',
				width : 85,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rotaName',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'A&ccedil;ao?',
				name : 'acao',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : ' ',
				name : 'check',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 240,
			singleSelect : true
		});
		$(".fechamentoGrid").flexigrid({
			dataType : 'json',
			preProcess: preprocessamentoGridFechamento,
			colModel : [ {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi&ccedil;&atilde;o',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Pre&ccedil;o Capa R$',
				name : 'precoCapaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Exempl. Devolu&ccedil;&atilde;o',
				name : 'exemplaresDevolucaoFormatado',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'totalFormatado',
				width : 80,
				sortable : false,
				align : 'right'
			}, {
				display : 'F&iacute;sico',
				name : 'fisico',
				width : 80,
				sortable : false,
				align : 'center'
			}, {
				display : 'Diferen&ccedil;a',
				name : 'diferenca',
				width : 50,
				sortable : false,
				align : 'right'
			}, {
				display : 'Replicar Qtde.',
				name : 'replicar',
				width : 80,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180,
			singleSelect : true
		});
	</script>

</body>
