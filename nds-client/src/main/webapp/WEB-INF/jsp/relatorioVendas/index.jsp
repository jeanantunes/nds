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
		if ($('#filtro_distrib').attr("checked") == "checked") {
			
			var dataDe = $("#datepickerDe").val();
			var dataAte = $("#datepickerAte").val();
			
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
			
			//$.postJSON("<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCDistribuidor'/>", data, exibirResultado);
			
		} else if ($('#filtro_editor').attr("checked") == "checked") {
			alert('Editor');
			
		} else if ($('#filtro_produto').attr("checked") == "checked") {
			alert('Produto');
			
		} else if ($('#filtro_cota').attr("checked") == "checked") {
			alert('Cota');
			
		}
	}
	
	function pesquisarAvancada() {
		
		var selectFornecedor = $("select#selectFornecedor").val();
		var codigoProduto    = $("#codigoProduto").val();
		var nomeProduto      = $("#nomeProduto").val();
		var edicao           = $("#edicaoProduto").val();
		var selectEditor     = $("select#selectEditor").val();
		var numerocota       = $("#numerocota").val();
		var nomeCota         = $("#nomeCota").val();
		var selectMunicipio  = $("select#selectMunicipio").val();
		
		if ($('#filtro_distrib').attr("checked") == "checked") {
			
			var dataDe = $("#datepickerDe").val();
			var dataAte = $("#datepickerAte").val();
			
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
			
			//$.postJSON("<c:url value='/lancamento/relatorioVendas/pesquisarCurvaABCDistribuidor'/>", data, exibirResultado);
			
		} else if ($('#filtro_editor').attr("checked") == "checked") {
			alert('Editor');
			
		} else if ($('#filtro_produto').attr("checked") == "checked") {
			alert('Produto');
			
		} else if ($('#filtro_cota').attr("checked") == "checked") {
			alert('Cota');
			
		}
		
	}
	
	function exibirResultado(result) {
		alert(result);
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
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

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

	<div class="container">

		<div id="effect" style="padding: 0 .7em;"
			class="ui-state-highlight ui-corner-all">
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
					<td width="20"><input type="radio" name="filtro"
						id="filtro_distrib" onclick="mostra_distrib();" value="radio" /></td>
					<td colspan="3"><label for="filtro_distrib">Curva ABC Distribuidor</label></td>
					<td width="20"><input type="radio" name="filtro"
						id="filtro_editor" value="radio" onclick="mostra_editor();" /></td>
					<td width="94"><label for="filtro_editor">Curva ABC Editor</label></td>
					<td width="20"><input type="radio" name="filtro"
						id="filtro_produto" onclick="mostra_produto();" value="radio" /></td>
					<td width="114"><label for="filtro_produto">Curva ABC Produto</label></td>
					<td width="21" align="right"><input type="radio" name="filtro"
						id="filtro_cota" value="radio" onclick="mostra_cota();" /></td>
					<td width="90"><label for="filtro_cota">Curva ABC Cota</label></td>
					<td width="47">Período:</td>
					<td width="86"><input type="text" name="datepickerDe"
						id="datepickerDe" style="width: 60px;" /></td>
					<td width="24">Até:</td>
					<td width="87"><input type="text" name="datepickerAte"
						id="datepickerAte" style="width: 60px;" /></td>
					<td width="104" rowspan="3" valign="top"><span
						class="bt_pesquisar"><a href="javascript:;"
							onclick="pesquisar();">Pesquisar</a></span></td>
					<td width="20" rowspan="3" align="center" valign="top"><a
						href="javascript:;" onclick="mostra_pesq_avancada();"><img
							src="${pageContext.request.contextPath}/images/ico_pesq_avancada.jpg" alt="Pesquisa Avançada"
							width="20" height="20" vspace="10" border="0" /></a></td>
				</tr>
				<tr class="linhaCota">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="6"><label>Cota:</label> <input type="text"
						style="width: 80px; float: left; margin: 5px;" /> <label>Nome:</label>
						<input type="text" style="width: 200px; float: left; margin: 5px;" />
					</td>
				</tr>
				<tr class="linhaProduto">
					<td>&nbsp;</td>
					<td colspan="3">&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td colspan="8"><label>Código:</label> <input type="text"
						style="width: 80px; float: left; margin: 5px;" /> <label>Produto:</label>
						<input type="text" style="width: 200px; float: left; margin: 5px;" />
					</td>
				</tr>
			</table>

		</fieldset>
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset" id="pesquisaAvancada"
			style="display: none;">
			<legend>Busca Avançada</legend>
			<table width="950" border="0" cellpadding="2" cellspacing="1"
				class="filtro">
				<tr>
					<td width="69">Fornecedor:</td>
					<td width="255"><select name="select" id="selectFornecedor"
						style="width: 240px;">
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
					<td><input type="text" style="width: 100px;" id="edicoesCamp" name="edicaoProduto" id="edicaoProduto" /></td>
					<td><a href="javascript:;" onclick="esconde_pesq_avancada();"><img
							src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" width="15"
							height="15" border="0" /></a></td>
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
					<td><input type="text" name="numerocota"
						id="numerocota" style="width: 80px;" /></td>
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
						</select>
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><span class="bt_pesquisar"><a href="javascript:;"
							onclick="pesquisarAvancada();">Pesquisar</a></span></td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

		<div class="grids" style="display: none;">
			<fieldset class="classFieldset" id="relatorioDistribuidor"
				style="display: none;">
				<legend>Curva ABC Distribuidor</legend>
				<table class="abcDistribuidorGrid"></table>

				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="441"><span class="bt_novos" title="Gerar Arquivo"><a
								href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=1"><img src="${pageContext.request.contextPath}/images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos"
							title="Imprimir"><a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=1"><img
									src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
						</td>
						<td width="121"><strong>Total:</strong></td>
						<td width="130"><span id="qtdeTotalVendaExemplares"></span></td>
						<td width="258"><span id="totalFaturamentoCapa"></span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioEditor"
				style="display: none;">
				<legend>Curva ABC Editor</legend>
				<table class="abcEditorGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="329"><span class="bt_novos" title="Gerar Arquivo"><a
								href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=2"><img src="${pageContext.request.contextPath}/images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos"
							title="Imprimir"><a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=2"><img
									src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
						</td>
						<td width="80"><strong>Total:</strong></td>
						<td width="215"><span id="qtdeTotalVendaExemplares"></span></td>
						<td width="326"><span id="totalFaturamentoCapa"></span></td>
					</tr>
				</table>
			</fieldset>

			<fieldset class="classFieldset" id="relatorioProduto"
				style="display: none;">
				<legend>Curva ABC Produto</legend>

				<table class="abcProdutoGrid"></table>
				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="441"><span class="bt_novos" title="Gerar Arquivo"><a
								href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=3"><img src="${pageContext.request.contextPath}/images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos"
							title="Imprimir"><a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=3"><img
									src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
						</td>
						<td width="151"><strong>Total:</strong></td>
						<td width="114"><span id="qtdeTotalVendaExemplares"></span></td>
						<td width="244"><span id="totalFaturamentoCapa"></span></td>
					</tr>
				</table>

			</fieldset>

			<fieldset class="classFieldset" id="relatorioCota"
				style="display: none;">
				<legend>Curva ABC Cota</legend>

				<table class="abcCotaGrid"></table>

				<table width="950" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="432"><span class="bt_novos" title="Gerar Arquivo"><a
								href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=XLS&tipoRelatorio=4"><img src="${pageContext.request.contextPath}/images/ico_excel.png"
									hspace="5" border="0" />Arquivo</a></span> <span class="bt_novos"
							title="Imprimir"><a href="${pageContext.request.contextPath}/lancamento/relatorioVendas/exportar?fileType=PDF&tipoRelatorio=4"><img
									src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
						</td>
						<td width="73"><strong>Total:</strong></td>
						<td width="205"><span id="qtdeTotalVendaExemplares"></span></td>
						<td width="240"><span id="totalFaturamentoCapa"></td>
					</tr>
				</table>
			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>

			<div class="linha_separa_fields">&nbsp;</div>
		</div>

	</div>

	<script>
		$(".popEditorGrid").flexigrid({
			url : '../xml/pop-editor-xml.xml',
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
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vdaExempl',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percVenda',
				width : 50,
				sortable : true,
				align : 'right'
			} ],

			width : 560,
			height : 255
		});

		$(".abcEditorGrid").flexigrid({
			url : '../xml/abc-editor-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Editor',
				name : 'editor',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExempl',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda Exs.',
				name : 'percExempl',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapa',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacao',
				width : 72,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'partAcumulada',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Hist.',
				name : 'hist',
				width : 30,
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
			height : 255
		});

		$(".abcDistribuidorGrid").flexigrid({
			preProcess: executarPreProcessamento,
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
				name : 'vendaExemplares',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapa',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacao',
				width : 52,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumulada',
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
			url : '../xml/abc-produtos-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Município',
				name : 'municipio',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde PDV´s',
				name : 'qtdePdv',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exempl. R$',
				name : 'vdaExempl',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacao',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'partAcumulada',
				width : 70,
				sortable : true,
				align : 'right'
			} ],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcCotaGrid").flexigrid({
			url : '../xml/abc-cotas-xml.xml',
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
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vdaExempl',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda %',
				name : 'percVda',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacao',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'partAcumulada',
				width : 70,
				sortable : true,
				align : 'right'
			} ],
			sortname : "produto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		function executarPreProcessamento(resultado) {
			if (resultado.mensagens) {
				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				$(".grids").hide();
				return resultado;
			}

			$("#qtdeTotalVendaExemplares").html(resultado.totalVendaExemplares);
			$("#totalFaturamentoCapa").html("R$ " + resultado.totalFaturamento);
			
			$(".grids").show();
			return resultado.tableModel;
		}
		
	</script>
</body>
</html>
