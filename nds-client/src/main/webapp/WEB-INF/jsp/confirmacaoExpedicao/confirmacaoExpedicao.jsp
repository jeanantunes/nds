
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>NDS - Novo Distrib</title>

<script type="text/javascript">

	var gui ;

	
	function dateToString(date) {
		return $.format.date(date.$+" 00:00:000", "dd/MM/yyyy");
	}
	
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
		
		alert("idLancamento="+id +",selecionado="+check.checked);
		
		$.postJSON("<c:url value='/confirmacaoExpedicao/selecionarLancamento'/>", 
				"idLancamento="+id +"&selecionado="+check.checked, 
				funcaoRetornoSelecao);
		
		
	}
	
	function funcaoRetornoSelecao(selecionado) {
		alert('Gui');
	}
			
	function processaRetorno(data) {
		
		var status = data[0];
		var mensagens = data[1];
		var grid =  data[2];
		
		if(mensagens!=null && mensagens.length>0) {
			exibirMensagem(status, mensagens);
		}
			
		for(var i=0; i<grid.rows.length; i++) {			
			gui=grid.rows[i].cell.dataEntrada;
			grid.rows[i].cell.dataEntrada = dateToString(grid.rows[i].cell.dataEntrada);
			
			grid.rows[i].cell.dataChamada = dateToString(grid.rows[i].cell.dataChamada);
			
			grid.rows[i].cell.selecionado = gerarCheckbox('idCheck'+i,'check'+i, grid.rows[i].cell.idLancamento,grid.rows[i].cell.selecionado);
		}
		
		return grid;
	}
	
	
	function cliquePesquisar() {
		
		var dataLancamento = $('#idDataLancamento').attr('value');
		var idFornecedor = $('#idFornecedor').attr('value');
		var estudo= ($('#idEstudo').attr('checked')) == 'checked';
		
		$(".confirmaExpedicaoGrid").flexOptions({			
			url : '<c:url value="/confirmacaoExpedicao/pesquisarExpedicoes"/>',
			dataType : 'json',
			preProcess:processaRetorno,
			params:[{name:'dtLancamento',value:dataLancamento},
			        {name:'idFornecedor',value:idFornecedor},
			        {name:'estudo',value:estudo}]		
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
				sortable : true,
				align : 'left'
			}, {
				display : 'Estudo',
				name : 'estudo',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'selecionado',
				width : 20,
				sortable : true,
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
	
	function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-novo").dialog({
			resizable : false,
			height : 370,
			width : 410,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");
				},
			}
		});
	};

	function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-novo").dialog({
			resizable : false,
			height : 430,
			width : 410,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").hide("highlight", {}, 1000, callback);

				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

	};

	function popup_excluir() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-excluir").dialog({
			resizable : false,
			height : 140,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$.getJSON("<c:url value='/confirmacaoExpedicao/confirmarExpedicao'/>", 
							null, 
							alert('Guilherme'));
					
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
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

	});
	
</script>

</head>

<body>

	

	<form action="" method="get" id="form1" name="form1">
		<div id="dialog-excluir" title="Matriz de Expedi��o">
			<p>Confirma Matriz de aExpedi&ccedil;�o?</p>
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
								
								<option value=""></option>
								
								<c:forEach items="${fornecedores}" var="fornecedor">				
									<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
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
								<a href="javascript:;" onclick="cliquePesquisar();">Pesquisar</a>
							
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
						<a href="javascript:popup_excluir();">Confirmar</a> 
					</span> 
					
					<span class="bt_sellAll" style="float: right;">
					<label for="sel">Selecionar Todos</label>					
<!-- SELECIONAR TODOS -->	
						<input type="checkbox" name="Todos" id="sel"onclick="checkAll();" style="float: left;" /> </span>
				
				</div>






			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>




		</div>
		</div>
	</form>


</body>