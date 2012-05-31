<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script language="javascript" type="text/javascript">

	function mostrar(){
		$(".grids").show();
	}
	
	function popup_encerrarEncalhe() {
		$( "#dialog-encerrarEncalhe" ).dialog({
			resizable: false,
			height:500,
			width:650,
			modal: true,
			buttons: {
				"Postergar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cobrar": function() {
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};

	function popup_encerrar() {
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};

	$(function() {
		$( "#datepickerDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dtOperacao" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
	
	function confirmar(){
		$(".dados").show();
	}
	
	function pesqEncalhe(){
		$(".dadosFiltro").show();
	}

	</script>

	<style type="text/css">
 		#dialog-encerrarEncalhe fieldset{width:600px!important;}
	</style>
	
</head>

<body>

	<div id="dialog-confirm" title="Encerrar Opera&ccedil;&atilde;o" style="display:none;">
		<p>Confirma o encerramento da opera&ccedil;&atilde;o do dia 99/99/9999:</p>
	</div>

	
	<div id="dialog-encerrarEncalhe" title="Opera&ccedil;&atilde;o de Encalhe" style="display:none;">
		<fieldset>
			<legend>Cotas Ausentes</legend>
			<table class="cotasGrid"></table>
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir </a></span>
			<span class="bt_sellAll" style="float:right;"><input type="checkbox" id="sel" name="Todos4" onclick="checkAll_cotas();" style="float:right;margin-right:25px;"/><label for="sel">Selecionar Todos</label></span>
		</fieldset>
	</div>

    
    <fieldset class="classFieldset">
    	<legend> Pesquisar Fornecedor</legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="75">Data Encalhe:</td>
				<td width="114"><input name="datepickerDe" type="text" id="datepickerDe" style="width:80px;" /></td>
				<td width="67">Fornecedor:</td>
				<td width="216"><select name="select" id="select" style="width:200px;">
				</select></td>
				<td width="97">Box de Encalhe:</td>
				<td width="239"><select name="select2" id="select2" style="width:100px;">
				</select></td>
				<td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
			</tr>
		</table>
    </fieldset>

    <div class="linha_separa_fields">&nbsp;</div>
      
    <fieldset class="classFieldset">
       	<legend> Fechamento Encalhe</legend>
        <div class="grids" style="display:none;">
			<table class="fechamentoGrid"></table>
            <span class="bt_novos" title="Salvar"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Salvar </a></span>
			<span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="popup_encerrarEncalhe();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Cotas Ausentes</a></span>
			<span class="bt_novos" title="Encerrar Operação Encalhe"><a href="javascript:;" onclick="popup_encerrar();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Encerrar Operação Encalhe</a></span>
			<span class="bt_sellAll" style="float:right;"><input type="checkbox" id="sel" name="Todos" onclick="checkAll();" style="float:right;margin-right:55px;"/><label for="sel">Selecionar Todos</label></span>
        	<br clear="all" />
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir </a></span>
        </div>
	</fieldset>
    
    <div class="linha_separa_fields">&nbsp;</div>

	<script>
		$(".cotasGrid").flexigrid({
			url : '../xml/cotas_fechamento-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'box',
				width : 37,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'roteiro',
				width : 85,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rota',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : ' ',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 240
		});
		$(".fechamentoGrid").flexigrid({
			url : '../xml/fechamento-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
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
				display : 'Edição',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Exempl. Devolução',
				name : 'exemplarDevolucao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Físico',
				name : 'fisico',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'diferenca',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Replicar Qtde.',
				name : 'replicarQtde',
				width : 80,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	</script>

</body>
