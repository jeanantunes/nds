<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script language="javascript" type="text/javascript">

	var contextPath = '${pageContext.request.contextPath}';
	var vDataEncalhe = '';
	var vFornecedorId = '';
	var vBoxId = '';
	
	function pesquisar() {
		
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
			
			var valorFisico = row.cell.fisico == null ? "" : row.cell.fisico;
			row.cell.fisico = '<input type="text" style="width: 60px" name="fisico[' + index + ']" value="' + valorFisico + '" onchange="onChangeFisico(this, ' + index + ')"/>';
			
			row.cell.replicar = '<span title="Replicar"><a href="javascript:;" onclick="replicar(' + index + ')"><img src="${pageContext.request.contextPath}/images/ico_atualizar.gif" border="0" /></a></span>';
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
		
		$(".fechamentoGrid").flexOptions({
			"url" : contextPath + '/devolucao/fechamentoEncalhe/salvar',
			params : [{
				name : "dataEncalhe",
				value : $('#datepickerDe').val()
			}, {
				name : "fornecedorId",
				value : $('#selectFornecedor').val()
			}, {
				name : "boxId",
				value : $('#selectBoxEncalhe').val()
			}, {
				name : "fisico",
				value : gerarArrayFisico()
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
			var elem = document.getElementById("textoCheckAllCotas");
			elem.innerHTML = "Desmarcar todos";

			$("input[type=checkbox][name='checkboxGridCotas']").each(function(){
				$(this).attr('checked', true);
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
		
		document.getElementById("checkTodasCotas").checked = false;
		checarTodasCotasGrid(false);
		
		$.each(resultado.rows, function(index, row) {

			var checkBox = '<input type="checkbox" name="checkboxGridCotas" id="checkboxGridCotas" value="' + row.cell.idCota + '" />';	
			
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

		$("#dialog-postergar").dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					var dataEncalhe = $("#dtPostergada").val();

					$.postJSON("<c:url value='/devolucao/fechamentoEncalhe/postergarCotas' />",
								{ 'dataEncalhe' : dataEncalhe, 'idsCotas' : obterCotasMarcadas() },
								function (result) {
						
								},
							  	null,
							   	true
						);
				},
				
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	}

	function cobrarCotas() {

		var dataEncalhe = $("#datepickerDe").val();
		
		$.postJSON("<c:url value='/devolucao/fechamentoEncalhe/cobrarCotas' />",
					{ 'dataEncalhe' : dataEncalhe, 'idsCotas' : obterCotasMarcadas() },
					function (result) {
			
					},
				  	null,
				   	true
			);
	}

	function exportarCotasAusentes() {

		var dataEncalhe = $("#datepickerDe").val();
		
		window.location = contextPath + "/devolucao/fechamentoEncalhe/exportarArquivo?dataEncalhe=" + dataEncalhe + "&fileType=XLS";

		return false;
	}
	
	function imprimirArquivo(fileType) {

		var dataEncalhe = $("#datepickerDe").val();
		
		window.location = contextPath + "/devolucao/fechamentoEncalhe/imprimirArquivo?"
			+ "dataEncalhe=" + vDataEncalhe
			+ "&fornecedorId="+ vFornecedorId
			+ "&boxId=" + vBoxId
			+ "&sortname="
			+ "&sortorder="
			+ "&rp="
			+ "&page="
			+ "&fileType=" + fileType;

		return false;
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

	<div id="dialog-postergar" title="Postergar Encalhe" style="display:none;">
		<fieldset style="width:255px!important;">
	    	<legend>Postergar Encalhe</legend>
			<table width="230" border="0" cellspacing="2" cellpadding="0">
	          <tr>
	            <td width="121">Nova Data:</td>
	            <td width="103"><input name="dtPostergada" type="text" id="dtPostergada" style="width:80px;" /></td>
	          </tr>
	        </table>
	    </fieldset>
	</div>
	
	<div id="dialog-encerrarEncalhe" title="Opera&ccedil;&atilde;o de Encalhe" style="display:none;">
		<fieldset>
			<legend>Cotas Ausentes</legend>
			<form id="formGridCotas" name="formGridCotas" >
				<table class="cotasGrid" id="tabelaGridCotas" ></table>
			</form>
			<span class="bt_novos" title="Gerar Arquivo" >
				<a href="javascript:exportarCotasAusentes();">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>
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
					<option value="">Selecione...</option>
					<c:forEach var="fornecedor" items="${listaFornecedores}">
						<option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
					</c:forEach>
					</select>
				</td>
				<td width="97">Box de Encalhe:</td>
				<td width="239">
					<select name="selectBoxEncalhe" id="selectBoxEncalhe" style="width:100px;">
					<option value="">Selecione...</option>
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
            <span class="bt_novos" title="Salvar"><a href="javascript:;" onclick="salvar()"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Salvar </a></span>
			<span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="popup_encerrarEncalhe();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Cotas Ausentes</a></span>
			<span class="bt_novos" title="Encerrar Opera&ccedil;&atilde;o Encalhe"><a href="javascript:;" onclick="popup_encerrar();"><img src="${pageContext.request.contextPath}/images/ico_check.gif" hspace="5" border="0" />Encerrar Opera&ccedil;&atilde;o Encalhe</a></span>
			<span class="bt_sellAll" style="float:right;"><a href="javascript:;" id="sel" onclick="replicarTodos();"><img src="${pageContext.request.contextPath}/images/ico_atualizar.gif" border="0" /></a><label for="sel">Replicar Todos</label></span>
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
				sortable : true,
				align : 'right'
			}, {
				display : 'F&iacute;sico',
				name : 'fisico',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Diferen&ccedil;a',
				name : 'diferenca',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Replicar Qtde.',
				name : 'replicar',
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
