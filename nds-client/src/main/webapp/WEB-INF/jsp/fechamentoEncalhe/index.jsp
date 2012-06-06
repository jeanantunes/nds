<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script language="javascript" type="text/javascript">

	var contextPath = '${pageContext.request.contextPath}';
	
	function pesquisar() {
		
		$(".fechamentoGrid").flexOptions({
			"url" : contextPath + '/devolucao/fechamentoEncalhe/pesquisar',
			params : [{
				name : "dataEncalhe",
				value : $('#datepickerDe').val()
			}, {
				name : "idFornecedor",
				value : $('#selectFornecedor').val()
			}, {
				name : "idBox",
				value : $('#selectBoxEncalhe').val()
			}],
			newp:1
		});
		
		$(".fechamentoGrid").flexReload();
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

					
				},
				"Cobrar": function() {
					
				},
				"Cancelar": function() {
					$(this).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
			}
		});

		var dataEncalhe = $("#datepickerDe").val();
		
		$(".cotasGrid").flexOptions({
			url: "<c:url value='/devolucao/fechamentoEncalhe/cotasAusentes' />",
			params: [{name:'dataEncalhe', value: dataEncalhe }],
			newp: 1,
		});
		
		$(".cotasGrid").flexReload();
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

	function checarTodasCotasGrid(checked) {
		
		for (var i=0;i<document.formGridCotas.elements.length;i++) {
		     var x = document.formGridCotas.elements[i];
		     if (x.name == 'checkboxGridCotas') {
		    	 x.checked = checked;
		     }    
		}
		
		if (checked) {
			var elem = document.getElementById("textoCheckAllCotas");
			elem.innerHTML = "Desmarcar todos";
        } else {
			var elem = document.getElementById("textoCheckAllCotas");
			elem.innerHTML = "Marcar todos";
		}
	}

	function preprocessamentoGrid(resultado) {	
		
		if (resultado.mensagens) {
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			$(".cotasGrid").hide();
			return resultado;
		}
		
		document.getElementById("checkTodasCotas").checked = false;
		checarTodasCotasGrid(false);
		
		$.each(resultado.rows, function(index, row) {

			var checkBox = '<input type="checkbox" name="checkboxGridCotas" id="checkbox_'+ row.cell.numeroCota +'" />';	
			
		    row.cell.check = checkBox;
		});

		
		$(".cotasGrid").show();
		
		return resultado;
	}

	function obterCotasMarcadas() {

		var cotasMarcadas='';
		var table = document.getElementById("tabelaGridCotas");
		
		for(i = 0; i < table.rows.length; i++) {   
			
			if (document.getElementById("checkbox_" + table.rows[i].cells[0].textContent).checked) {
			    table.rows[i].cells[0].textContent; 
			    cotasMarcadas+='idsCotas='+ table.rows[i].cells[0].textContent + '&';
		    }

		} 
		
		return cotasMarcadas;
	}
		
	function postergarCotas() {
		
		var dataEncalhe = $("#datepickerDe").val();

		$.postJSON("<c:url value='/devolucao/fechamentoEncalhe/postergarCotas' />",
					"dataEncalhe=" + dataEncalhe +
					"&" + obterCotasMarcadas(),
					function (result) {
			
					},
				  	null,
				   	true
			);
	}

	function cobrarCotas() {

		var dataEncalhe = $("#datepickerDe").val();
		
		$.postJSON("<c:url value='/devolucao/fechamentoEncalhe/cobrarCotas' />",
					"dataEncalhe=" + dataEncalhe +
					"&" + obterCotasMarcadas(),
					function (result) {
			
					},
				  	null,
				   	true
			);
		
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
				<form id="formGridCotas" name="formGridCotas" >
					<table class="cotasGrid" id="tabelaGridCotas" ></table>
				</form>
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir </a></span>
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
				<td width="114"><input name="datepickerDe" type="text" id="datepickerDe" style="width:80px;" value="${dataOperacao}" /></td>
				<td width="67">Fornecedor:</td>
				<td width="216">
					<select name="selectFornecedor" id="selectFornecedor" style="width:200px;">
					<option>Selecione...</option>
					<c:forEach var="fornecedor" items="${listaFornecedores}">
						<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
					</c:forEach>
					</select>
				</td>
				<td width="97">Box de Encalhe:</td>
				<td width="239">
					<select name="selectBoxEncalhe" id="selectBoxEncalhe" style="width:100px;">
					<option>Selecione...</option>
					<c:forEach var="box" items="${listaBoxes}">
						<option value="${box.id}">${box.nome}</option>
					</c:forEach>
					</select>
				</td>
				<td width="106"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();">Pesquisar</a></span></td>
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
			<span class="bt_novos" title="Encerrar Opera&ccedil;&atilde;o Encalhe"><a href="javascript:;" onclick="popup_encerrar();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Encerrar Opera&ccedil;&atilde;o Encalhe</a></span>
			<span class="bt_sellAll" style="float:right;"><input type="checkbox" id="sel" name="Todos" onclick="checkAll();" style="float:right;margin-right:55px;"/><label for="sel">Selecionar Todos</label></span>
        	<br clear="all" />
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir </a></span>
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
				display : 'Ação',
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
			height : 240
		});
		$(".fechamentoGrid").flexigrid({
			dataType : 'json',
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
