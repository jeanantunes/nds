<head>
<title>Relatório de Vendas</title>
<style type="text/css">
.linhaCota, .linhaProduto{display:none;}
.filtro label{width:auto!important; margin-bottom: 0px!important;  margin-top: 4px!important; margin-left: 0px!important; margin-right: 0px;!important}
</style>
<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
<script language="javascript" type="text/javascript">

	$(function() {
		$("#datepickerDe").datepicker({
			showOn : "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});
		
		$("#datepickerDe").mask("99/99/9999");
	
		$("#datepickerAte").datepicker({
			showOn : "button",
			buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});
		
		$("#datepickerAte").mask("99/99/9999");
	});

	function pesquisar() {

		var dataDe = $("#datepickerDe").val();
		var dataAte = $("#datepickerAte").val();

		if ($('#filtro_distrib').attr("checked") == "checked") {
			$(".abcDistribuidorGrid").flexOptions({
				url: "<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCDistribuidor' />",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte}
			    ],
			    newp: 1,
			});
			
			$(".abcDistribuidorGrid").flexReload();
			mostra_distrib();
			
		} else if ($('#filtro_editor').attr("checked") == "checked") {
			$(".abcEditorGrid").flexOptions({
				url: "<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCEditor' />",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			    ],
			    newp: 1,
			});
			
			$(".abcEditorGrid").flexReload();
			mostra_editor();			
			
		} else if ($('#filtro_produto').attr("checked") == "checked") {
			
			var codigoProduto=$('#codigoProdutoListaProduto').val();
			var nomeProduto=$('#nomeProdutoListaProduto').val();
			
			$(".abcProdutoGrid").flexOptions({
				url: "<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCProduto' />",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'codigoProduto', value: codigoProduto},
			         {name:'nomeProduto', value: nomeProduto}
			    ],
			    newp: 1,
			});
			
			$(".abcProdutoGrid").flexReload();
			mostra_produto();
			
		} else if ($('#filtro_cota').attr("checked") == "checked") {
			
			var codigoCota=$('#numeroCotaListaCota').val();
			var nomeCota=$('#nomeCotaListaCota').val();
			
			$(".abcCotaGrid").flexOptions({
				url: "<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCCota' />",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'codigoCota', value: codigoCota},
			         {name:'nomeCota', value: nomeCota}
			    ],
			    newp: 1,
			});
			
			$(".abcCotaGrid").flexReload();
			mostra_cota();					
		}
	}
	
	function pesquisarAvancada() {

		var dataDe = $("#datepickerDe").val();
		var dataAte = $("#datepickerAte").val();

		var selectFornecedor = $("select#selectFornecedor").val();
		var codigoProduto    = $("#codigoProduto").val();
		var nomeProduto      = $("#nomeProduto").val();
		var edicao           = $("#edicaoProduto").val();
		var selectEditor     = $("select#selectEditor").val();
		var numerocota       = $("#numerocota").val();
		var nomeCota         = $("#nomeCota").val();
		var selectMunicipio  = $("select#selectMunicipio").val();
		
		if ($('#filtro_distrib').attr("checked") == "checked") {
			
			$(".abcDistribuidorGrid").flexOptions({
				url: "<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCDistribuidorAvancada' />",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'codigoFornecedor', value: selectFornecedor},
			         {name:'codigoProduto', value: codigoProduto},
			         {name:'nomeProduto', value: nomeProduto},
			         {name:'edicaoProduto', value: edicao},
			         {name:'codigoEditor', value: selectEditor},
			         {name:'codigoCota', value: numerocota},
			         {name:'nomeCota', value: nomeCota},
			         {name:'municipio', value: selectMunicipio}
			    ],
			    newp: 1,
			});
			
			$(".abcDistribuidorGrid").flexReload();
			mostra_distrib();
			
		} else if ($('#filtro_editor').attr("checked") == "checked") {
			
			$(".abcEditorGrid").flexOptions({
				url: "<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCEditorAvancada' />",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'codigoFornecedor', value: selectFornecedor},
			         {name:'codigoProduto', value: codigoProduto},
			         {name:'nomeProduto', value: nomeProduto},
			         {name:'edicaoProduto', value: edicao},
			         {name:'codigoEditor', value: selectEditor},
			         {name:'codigoCota', value: numerocota},
			         {name:'nomeCota', value: nomeCota},
			         {name:'municipio', value: selectMunicipio}
			    ],
			    newp: 1,
			});
			
			$(".abcEditorGrid").flexReload();
			mostra_editor();			
			
		} else if ($('#filtro_produto').attr("checked") == "checked") {
			
			if ($('#nomeProdutoListaProduto').val() != "") {
				codigoProduto=$('#codigoProdutoListaProduto').val();
			}
			if ($('#codigoProdutoListaProduto').val() != "") {
				nomeProduto=$('#nomeProdutoListaProduto').val();
			}
			
			$(".abcProdutoGrid").flexOptions({
				url: "<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCProdutoAvancada' />",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'codigoFornecedor', value: selectFornecedor},
			         {name:'codigoProduto', value: codigoProduto},
			         {name:'nomeProduto', value: nomeProduto},
			         {name:'edicaoProduto', value: edicao},
			         {name:'codigoEditor', value: selectEditor},
			         {name:'codigoCota', value: numerocota},
			         {name:'nomeCota', value: nomeCota},
			         {name:'municipio', value: selectMunicipio}
			    ],
			    newp: 1,
			});
			
			$(".abcProdutoGrid").flexReload();
			mostra_produto();
			
		} else if ($('#filtro_cota').attr("checked") == "checked") {
			
			if ($('#numeroCotaListaCota').val() != "") {
				numerocota=$('#numeroCotaListaCota').val();
			}
			if ($('#nomeCotaListaCota').val() != "") {
				nomeCota=$('#nomeCotaListaCota').val();
			}
			
			$(".abcCotaGrid").flexOptions({
				url: "<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCCotaAvancada' />",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'codigoFornecedor', value: selectFornecedor},
			         {name:'codigoProduto', value: codigoProduto},
			         {name:'nomeProduto', value: nomeProduto},
			         {name:'edicaoProduto', value: edicao},
			         {name:'codigoEditor', value: selectEditor},
			         {name:'codigoCota', value: numerocota},
			         {name:'nomeCota', value: nomeCota},
			         {name:'municipio', value: selectMunicipio}
			    ],
			    newp: 1,
			});
			
			$(".abcCotaGrid").flexReload();
			mostra_cota();			
			
		}
		
	}
	
	function abrirPopUpHistoricoEditor(dataDe, dataAte, codigoEditora) {
		$(".popEditorGrid").flexOptions({
			url: "<c:url value='/lancamento/relatorioVendas/pesquisarHistoricoEditor' />",
			params: [
		         {name:'dataDe', value: dataDe},
		         {name:'dataAte', value: dataAte},
		         {name:'codigoEditor', value: codigoEditora}
		    ],
		    newp: 1,
		});
		
		$(".popEditorGrid").flexReload();
		popup_editor();			
	
	}
	
	function mostra_pesq_avancada() {
		$('#pesquisaAvancada').fadeIn("slow");
	}
	
	function esconde_pesq_avancada() {
		$('#pesquisaAvancada').fadeOut("slow");
	}

	function mostra_distrib() {
		$('#relatorioDistribuidor').show();
		$('#relatorioEditor').hide();
		$('#relatorioProduto').hide();
		$('#relatorioCota').hide();
		$('.linhaCota').hide();
		$('.linhaProduto').hide();
	}
	
	function mostra_editor() {
		$('#relatorioDistribuidor').hide();
		$('#relatorioEditor').show();
		$('#relatorioProduto').hide();
		$('#relatorioCota').hide();
		$('.linhaCota').hide();
		$('.linhaProduto').hide();
	}
	
	function mostra_produto() {
		$('#relatorioDistribuidor').hide();
		$('#relatorioEditor').hide();
		$('#relatorioProduto').show();
		$('#relatorioCota').hide();
		$('.linhaCota').hide();
		$('.linhaProduto').show();
	}
	
	function mostra_cota() {
		$('#relatorioDistribuidor').hide();
		$('#relatorioEditor').hide();
		$('#relatorioProduto').hide();
		$('#relatorioCota').show();
		$('.linhaCota').show();
		$('.linhaProduto').hide();
	}

	function popup_editor() {
		$("#dialog-editor").dialog({
			resizable : false,
			height : 450,
			width : 600,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");

				},

			}
		});
	};

	$(function() {
		var availableTags = [ "4455", "4566", "4567", "5678", "1223", "1234" ];
		function split(val) {
			return val.split(/,\s*/);
		}
		function extractLast(term) {
			return split(term).pop();
		}

		$("#edicoesCamp")
		// don't navigate away from the field on tab when selecting an item
		.bind(
				"keydown",
				function(event) {
					if (event.keyCode === $.ui.keyCode.TAB
							&& $(this).data("autocomplete").menu.active) {
						event.preventDefault();
					}
				}).autocomplete(
				{
					minLength : 0,
					source : function(request, response) {
						// delegate back to autocomplete, but extract the last term
						response($.ui.autocomplete.filter(availableTags,
								extractLast(request.term)));
					},
					focus : function() {
						// prevent value inserted on focus
						return false;
					},
					select : function(event, ui) {
						var terms = split(this.value);
						// remove the current input
						terms.pop();
						// add the selected item
						terms.push(ui.item.value);
						// add placeholder to get the comma-and-space at the end
						terms.push("");
						this.value = terms.join(", ");
						return false;
					}
				});
	});
</script>
</head>
<body>

	<div id="dialog-editor" title="Histórico de Produtos" style="display:none;">
	<fieldset style="width:560px;">
		<legend>Editor: <span name="nomeEditorPopUp" id="nomeEditorPopUp"></span></legend>
	    <table class="popEditorGrid"></table>
	        <span class="bt_novos" title="Gerar Arquivo"><a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=5"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			<span class="bt_novos" title="Imprimir"><a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=5"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
	</fieldset>
	</div>

	<div class="container">

		<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all">
			<p>
				<span style="float: left; margin-right: .3em;"
					class="ui-icon ui-icon-info"></span> <b>Parcial < evento > com
					< status >.</b>
			</p>
		</div>

		<fieldset class="classFieldset">
			<legend> Relatório de Vendas</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="20"><input type="radio" name="filtro" id="filtro_distrib" onclick="mostra_distrib();" value="radio" /></td>
					<td colspan="3"><label for="filtro_distrib">Curva ABC Distribuidor</label></td>
					<td width="20"><input type="radio" name="filtro" id="filtro_editor" value="radio" onclick="mostra_editor();" /></td>
					<td width="94"><label for="filtro_editor">Curva ABC Editor</label></td>
					<td width="20"><input type="radio" name="filtro" id="filtro_produto" onclick="mostra_produto();" value="radio" /></td>
					<td width="114"><label for="filtro_produto">Curva ABC Produto</label></td>
					<td width="21" align="right"><input type="radio" name="filtro" id="filtro_cota" value="radio" onclick="mostra_cota();" /></td>
					<td width="90"><label for="filtro_cota">Curva ABC Cota</label></td>
					<td width="47">Período:</td>
					<td width="86"><input type="text" name="datepickerDe" id="datepickerDe" style="width: 60px;" /></td>
					<td width="24">Até:</td>
					<td width="87"><input type="text" name="datepickerAte" id="datepickerAte" style="width: 60px;" /></td>
					<td width="104" rowspan="3" valign="top"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();">Pesquisar</a></span></td>
					<td width="20" rowspan="3" align="center" valign="top"><a
						href="javascript:;" onclick="mostra_pesq_avancada();"><img src="${pageContext.request.contextPath}/images/ico_pesq_avancada.jpg" alt="Pesquisa Avançada" width="20" height="20" vspace="10" border="0" /></a></td>
				</tr>
				<tr class="linhaCota">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="6">
						<label>Cota:</label> <input type="text" style="width: 80px; float: left; margin: 5px;" name="numeroCotaListaCota" id="numeroCotaListaCota" /> 
						<label>Nome:</label> <input type="text" style="width: 200px; float: left; margin: 5px;" name="nomeCotaListaCota" id="nomeCotaListaCota" />
					</td>
				</tr>
				<tr class="linhaProduto">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="8">
						<label>Código:</label> <input type="text" style="width: 80px; float: left; margin: 5px;" id="codigoProdutoListaProduto" name="codigoProdutoListaProduto" /> 
						<label>Produto:</label> <input type="text" style="width: 200px; float: left; margin: 5px;" id="nomeProdutoListaProduto" name="nomeProdutoListaProduto" />
					</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset" id="pesquisaAvancada" style="display: none;">
			<legend>Busca Avançada</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="69">Fornecedor:</td>
					<td width="255"><select name="select" id="selectFornecedor" style="width: 240px;">
							<option>Todos</option>
                    		<c:forEach items="${fornecedores}" var="fornecedor">
								<option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
                   			</c:forEach> 
							<option>Dinap</option>
							<option>FC</option>
					</select></td>
					<td width="47" colspan="-1">Código:</td>
					<td width="108"><input type="text" style="width: 80px;" name="codigoProduto" id="codigoProduto" /></td>
					<td width="52">Produto:</td>
					<td width="213"><input type="text" style="width: 200px;" name="nomeProduto" id="nomeProduto" /></td>
					<td width="41">Edição:</td>
					<td><input type="text" style="width: 100px;" name="edicaoProduto" id="edicaoProduto" /></td>
					<td><a href="javascript:;" onclick="esconde_pesq_avancada();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" width="15" height="15" border="0" /></a></td>
				</tr>
				<tr>
					<td>Editor:</td>
					<td><select name="select2" id="selectEditor" style="width: 240px;">
							<option>Todos</option>
                    		<c:forEach items="${editores}" var="editor">
								<option value="${editor.id}">${editor.nome}</option>
                   			</c:forEach> 
					</select></td>
					<td colspan="-1">Cota:</td>
					<td><input type="text" name="numeroCota" id="numeroCota" style="width: 80px;" /></td>
					<td>Nome:</td>
					<td><input type="text" style="width: 200px;" id="nomeCota" name="nomeCota" /></td>
					<td>&nbsp;</td>
					<td width="104">&nbsp;</td>
					<td width="15">&nbsp;</td>
				</tr>
				<tr>
					<td>Municipio:</td>
					<td><select name="selectMunicipio" id="selectMunicipio" style="width: 240px;">
							<option selected="selected">Todos</option>
                    		<c:forEach items="${municipios}" var="municipio">
								<option value="${municipio}">${municipio}</option>
                   			</c:forEach> 
						</select>
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisarAvancada();">Pesquisar</a></span></td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<div class="grids" style="display: none;">
			<fieldset class="classFieldset" id="relatorioDistribuidor" style="display: none;">
				<legend>Curva ABC Distribuidor</legend>
				<table class="abcDistribuidorGrid"></table>

				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="441">
							<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=1"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
							</span> 
							<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=1"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
							</span>
						</td>
						<td width="121"><strong>Total:</strong></td>
						<td width="130"><span id="qtdeTotalVendaExemplaresDistribuidor"></span></td>
						<td width="258"><span id="totalFaturamentoCapaDistribuidor"></span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioEditor" style="display: none;">
				<legend>Curva ABC Editor</legend>
				<table class="abcEditorGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="329">
							<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=2"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
							</span>
							<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=2"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
							</span>
						</td>
						<td width="80"><strong>Total:</strong></td>
						<td width="215"><span id="qtdeTotalVendaExemplaresEditor"></span></td>
						<td width="326"><span id="totalFaturamentoCapaEditor"></span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioProduto" style="display: none;">
				<legend>Curva ABC Produto</legend>
				<table class="abcProdutoGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="441">
							<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=3"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
							</span>
							<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=3"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
							</span>
						</td>
						<td width="151"><strong>Total:</strong></td>
						<td width="114"><span id="qtdeTotalVendaExemplaresProduto"></span></td>
						<td width="244"><span id="totalFaturamentoCapaProduto"></span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioCota"
				style="display: none;">
				<legend>Curva ABC Cota</legend>
				<table class="abcCotaGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="432">
							<span class="bt_novos" title="Gerar Arquivo">
								<a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=4"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
							</span> 
							<span class="bt_novos" title="Imprimir">
								<a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=4"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a>
							</span>
						</td>
						<td width="73"><strong>Total:</strong></td>
						<td width="205"><span id="qtdeTotalVendaExemplaresCota"></span></td>
						<td width="240"><span id="totalFaturamentoCapaCota"></td>
					</tr>
				</table>
			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>

			<div class="linha_separa_fields">&nbsp;</div>
		</div>

	</div>

	<script>
		$(".popEditorGrid").flexigrid({
			preProcess: executarPreProcessamentoPopUp,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicaoProduto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparteFormatado',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'porcentagemVendaFormatado',
				width : 50,
				sortable : true,
				align : 'right'
			} ],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : false,
			useRp : false,
			width : 560,
			height : 255
		});

		$(".abcEditorGrid").flexigrid({
			preProcess: executarPreProcessamentoEditor,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoEditor',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Editor',
				name : 'nomeEditor',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparteFormatado',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda Exs.',
				name : 'porcentagemVendaExemplaresFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapaFormatado',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 72,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Hist.',
				name : 'hist',
				width : 30,
				sortable : false,
				align : 'center'
			} ],
			sortname : "codigoEditor",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcDistribuidorGrid").flexigrid({
			preProcess: executarPreProcessamentoDistribuidor,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Município',
				name : 'municipio',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde PDVs',
				name : 'quantidadePdvs',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapaFormatado',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 52,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			} ],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcProdutoGrid").flexigrid({
			preProcess: executarPreProcessamentoProduto,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Município',
				name : 'municipio',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde PDVs',
				name : 'quantidadePdvs',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapaFormatado',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 52,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			} ],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcCotaGrid").flexigrid({
			preProcess: executarPreProcessamentoCota,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicaoProduto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparteFormatado',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda %',
				name : 'porcentagemVendaFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento R$',
				name : 'faturamentoFormatado',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 70,
				sortable : true,
				align : 'right'
			} ],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		function executarPreProcessamentoDistribuidor(resultado) {
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}

			$("#qtdeTotalVendaExemplaresDistribuidor").html(resultado.totalVendaExemplaresFormatado);
			$("#totalFaturamentoCapaDistribuidor").html("R$ " + resultado.totalFaturamentoFormatado);
			
			$(".grids").show();
			return resultado.tableModel;
		}

		function executarPreProcessamentoEditor(resultado) {
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}

			$("#qtdeTotalVendaExemplaresEditor").html(resultado.totalVendaExemplaresFormatado);
			$("#totalFaturamentoCapaEditor").html("R$ " + resultado.totalFaturamentoFormatado);
			
			$.each(resultado.tableModel.rows, function(index, row) {
				
				var linkHistorico = '<a href="javascript:;" onclick="abrirPopUpHistoricoEditor( \'' + row.cell.dataDe + '\', \'' + row.cell.dataAte + '\', ' + row.cell.codigoEditor + ');" style="cursor:pointer">' +
						     	  	'<img title="Histórico" src="${pageContext.request.contextPath}/images/ico_detalhes.png" hspace="5" border="0px" />' +
						  		    '</a>';
						  		    
				row.cell[8] = linkHistorico;
			});
			
			$(".grids").show();
			return resultado.tableModel;
		}

		function executarPreProcessamentoProduto(resultado) {
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}

			$("#qtdeTotalVendaExemplaresProduto").html(resultado.totalVendaExemplaresFormatado);
			$("#totalFaturamentoCapaProduto").html("R$ " + resultado.totalFaturamentoFormatado);
			
			$(".grids").show();
			return resultado.tableModel;
		}

		function executarPreProcessamentoCota(resultado) {
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}

			$("#qtdeTotalVendaExemplaresCota").html(resultado.totalVendaExemplaresFormatado);
			$("#totalFaturamentoCapaCota").html("R$ " + resultado.totalFaturamentoFormatado);
			
			$(".grids").show();
			return resultado.tableModel;
		}

		function executarPreProcessamentoPopUp(resultado) {
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".dialog-editor").hide();
				return resultado;
			}

			$("#nomeEditorPopUp").html(resultado.rows[0].cell.nomeEditor);
			
			$(".dialog-editor").show();
			return resultado;
		}

	</script>
</body>
</html>
