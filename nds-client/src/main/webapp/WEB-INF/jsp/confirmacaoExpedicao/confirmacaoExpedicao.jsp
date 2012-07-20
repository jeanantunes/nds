
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<script type="text/javascript"	
	src="${pageContext.request.contextPath}/scripts/jquery-dateFormat/jquery.dateFormat-1.0.js"></script>

<title>NDS - Novo Distrib</title>

<script type="text/javascript">
	

	$(function() {
		definirAcaoPesquisaTeclaEnter();
		//Define foco inicial no campo Data Lançamento.
		$('#idDataLancamento').focus();
	});
	
	function gerarCheckbox(id,name,idLancamento,selecionado) {
		var html = "";
		html+= ' <input ';
		html+= ' id="'+id+'"';
		html+= ' name="'+name+'"';
		html+= ' type="checkbox"';
		html+= ' onclick="adicionarSelecao('+idLancamento+',this);"';
		html+= ' style="float: left;"';
		
		if(selecionado==true) {
			html+= ' checked="checked" ' ;	
		}
		
		html+= ' />';
		return html;
		
	}
	
	function adicionarSelecao(id, check) {
		
		if(check.checked==false) {
			$("#selecionarTodosID").attr("checked",false);
		}
		
		$.postJSON("<c:url value='/confirmacaoExpedicao/selecionarLancamento'/>", 
				"idLancamento="+id +"&selecionado="+check.checked, 
				retornoSemAcao);				
	}
	
	function retornoSemAcao(data) {
		
	}

	
	function selecionarTodos(elementoCheck) {
		
		var selects =  document.getElementsByName("selecao");

		$.postJSON("<c:url value='/confirmacaoExpedicao/selecionarTodos'/>", 
				"selecionado="+elementoCheck.checked, 
				retornoSemAcao);	
		
		$.each(selects, function(index, row) {
			row.checked=elementoCheck.checked;
		});
		
	}
			
	function processaRetornoPesquisa(data) {
				
		var grid = data[0];
		var mensagens = data[1];
		var status = data[2];
		
		if(mensagens!=null && mensagens.length!=0) {
			exibirMensagem(status,mensagens);
		}
		
		if(!grid.rows) {
			return grid;
		}
		
		for(var i=0; i<grid.rows.length; i++) {			
			
			var cell = grid.rows[i].cell;
								
			if(cell.estudo) {
				cell.selecionado = gerarCheckbox('idCheck'+i,'selecao', cell.idLancamento,cell.selecionado);
			} else {
				cell.estudo="";
				cell.selecionado="";
			}
		}
		
		return grid;
	}
	
	var change = false;
	
	function cliquePesquisar() {
		
		$("#selecionarTodosID").attr("checked",false);
		change= !change;
		
		var dataLancamento = $('#idDataLancamento').attr('value');
		var idFornecedor = $('#idFornecedor').attr('value');
		var estudo= ($('#idEstudo').attr('checked')) == 'checked';
		
		$(".confirmaExpedicaoGrid").flexOptions({			
			url : '<c:url value="/confirmacaoExpedicao/pesquisarExpedicoes"/>',
			dataType : 'json',
			preProcess:processaRetornoPesquisa,
			params:[{name:'dtLancamento',value:dataLancamento},
			        {name:'idFornecedor',value:idFornecedor},
			        {name:'estudo',value:estudo},
			        {name:'ultimaPesquisa',value:new Date()}]		
		});
		
		$(".confirmaExpedicaoGrid").flexReload();
	}
	
	$(function() {	
		$(".confirmaExpedicaoGrid").flexigrid($.extend({},{
			colModel : [ {
				display : 'Data Entrada',
				name : 'dataEntrada',
				width : 65,
				sortable : true,
				align : 'center'
			}, {
				display : 'C&oacute;digo',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edi&ccedil;&atilde;o',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Classifica&ccedil;&atilde;o',
				name : 'classificacao',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Pre&ccedilo',
				name : 'preco',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pct. Padr&atilde;o',
				name : 'pctPadrao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Chamada',
				name : 'dataChamada',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 80,
				sortable : false,
				align : 'left'
			}, {
				display : 'Estudo',
				name : 'estudo',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : '',
				name : 'selecionado',
				width : 20,
				sortable : false,
				align : 'center'
			} ],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		})); 	
		
		$(".grids").show();		
	});
		
	function popupConfirmar() {

		$("#dialog-confirmar").dialog({
			resizable : false,
			height : 140,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$("#selecionarTodosID").attr("checked",false);
					$(".confirmaExpedicaoGrid").flexOptions({			
						url : '<c:url value="/confirmacaoExpedicao/confirmarExpedicao"/>',
						dataType : 'json',
						preProcess:processaRetornoPesquisa		
					});
					
					$(".confirmaExpedicaoGrid").flexReload();
										
					$(this).dialog("close");
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	};

	$(function() {		
				
		$("#idDataLancamento")
				.datepicker(
						{
							showOn : "button",
							buttonImage : "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
							buttonImageOnly : true
						});
		$( "#idDataLancamento" ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idDataLancamento").mask("99/99/9999");

	});
	
</script>

</head>

<body>

	

	<form action="" method="get" id="form1" name="form1">
		
		<div id="dialog-confirmar" title="Matriz de Expedição" style="display: none">
			<p>Confirmar Matriz de Expedição?</p>
		</div>

		<div class="corpo"></div>

		<div class="container">

			<div id="idMensagem" class="ui-state-highlight ui-corner-all"
				style="display: none;">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"> </span> <b id="idTextoMensagem"></b>
				</p>
			</div>

			<fieldset class="classFieldset">
				<legend> Pesquisar Expedi&ccedil;&atilde;o</legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1"
					class="filtro">
					<tr>
						<td width="111">Data Lan&ccedil;amento:</td>
						<td width="100">
						
<!-- DATA LANCAMENTO --> 
							<input id="idDataLancamento" type="text" name="dataLancamento" style="width: 70px;" value="${dataLancamento}"/>

						</td>
						<td colspan="3">Fornecedor:</td>
						<td width="358">
						
<!-- FORNECEDOR -->			
							<select id="idFornecedor" name="fornecedor" id="select" style="width: 350px;">
								
								<option value="">Todos</option>
								
								<c:forEach items="${fornecedores}" var="fornecedor">				
									<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
								</c:forEach>
								
							</select>
						
						</td>
						
						<td width="75" align="right" valign="bottom"><label
							for="estudo">Estudo:</label>
						</td>
						<td width="94">
<!-- ESTUDO --> 			
							<input name="" id="idEstudo" type="checkbox" value="" />
						
						</td>
						<td width="111">
							<span class="bt_pesquisar">
<!-- PESQUISAR -->								
								<a id="idBotaoPesquisar" href="javascript:;" onclick="cliquePesquisar();">Pesquisar</a>
							
							 </span>
						</td>
					</tr>
				</table>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>

			<fieldset class="classFieldset">
				<legend>Expedi&ccedil;&otilde;es Cadastradas</legend>
				
				<div class="grids" style="display: none;">
					
					<table class="confirmaExpedicaoGrid"></table>

					<span class="bt_confirmar">
<!-- CONFIRMAR -->						
						<a href="javascript:popupConfirmar();">Confirmar</a> 
					</span> 
					
					<span class="bt_sellAll" style="float: right;">
					<label for="sel">Selecionar Todos</label>					
<!-- SELECIONAR TODOS -->	
						<input type="checkbox" name="Todos" id="selecionarTodosID"onclick="selecionarTodos(this);" style="float: left;" /> </span>
				
				</div>






			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>




		</div>
		</div>
	</form>


</body>