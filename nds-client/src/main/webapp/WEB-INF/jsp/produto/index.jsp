<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produto.js"></script>
	
	<script type="text/javascript">

		$(function() {
				$( "#tabProduto" ).tabs();
		});
		
		var PesquisaProduto = {

			buscarValueRadio:function(radioName) {

				var valueRadio = new Array();
		
				$("input[type=radio][name='"+radioName+"']:checked").each(function() {
					valueRadio.push($(this).val());
				});
		
				return valueRadio;
			},
			
			buscarValueCheckBox:function(checkName) {
				return $("#"+checkName).is(":checked");
			},
				
			pesquisarProdutosSuccessCallBack:function() {
				
				PesquisaProduto.pesquisarBox(PesquisaProduto.getCodigoProdutoPesquisa());
				PesquisaProduto.pesquisarFornecedor(PesquisaProduto.getCodigoProdutoPesquisa());
				
				$("#dataProgramada").val("");
				
			},
			
			pesquisarProdutosErrorCallBack: function() {
					
				PesquisaProduto.pesquisarBox(PesquisaProduto.getCodigoProdutoPesquisa());
				PesquisaProduto.pesquisarFornecedor(PesquisaProduto.getCodigoProdutoPesquisa());
				
				$("#dataProgramada").val("");
				
			},

			getCodigoProdutoPesquisa: function () {
				return  "codigoProduto=" + $("#codigoProduto").val();
			},
			
			pesquisarBox:function(data) {
				
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarBox' />",
						   data, PesquisaProduto.montarComboBoxs);
			},		

			montarComboBoxs:function(result) {
				var comboBoxes = "<option selected='selected'  value='-1'></option>";  
				
				comboBoxes = comboBoxes + montarComboBox(result, true);
				
				$("#box").html(comboBoxes);
			}, 

			pesquisarFornecedor:function(data){
			
				$.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarFornecedor' />",
						   data, PesquisaProduto.montarComboFornecedores);
			},

			montarComboFornecedores:function(result) {
				var comboFornecedores =  montarComboBox(result, true);
				
				$("#fornecedor").html(comboFornecedores);
			},
			
			validarEdicaoSuccessCallBack : function(){
				
				 var data = [{name:"codigoProduto",value:$("#codigoProduto").val()},
	             			 {name:"numeroEdicao",value:$("#edicao").val()},
							];
				
				 $.postJSON("<c:url value='/devolucao/chamadaEncalheAntecipada/pesquisarDataProgramada' />",
						   data, function(result) {
					 $("#dataProgramada").val(result);
				 });
			},
			
			validarEdicaoErrorCallBack: function() {
				 $("#dataProgramada").val("");
			},
		};

		function carregarPercentualDesconto() {

			var codigoTipoDesconto = $("#comboTipoDesconto").val();

			if (codigoTipoDesconto == '0') {
				$("#percentualDesconto").val("");
			}
			
			$.postJSON("<c:url value='/produto/carregarPercentualDesconto' />",
						"codigoTipoDesconto=" + codigoTipoDesconto, 
						function(result) {

							if (result == 0) {
								result = "";
							}

							$("#percentualDesconto").val(result);
					});

		}
				
		$(function() {
			
			inicializar();
		});
		
		function iniciarGrid() {
			$(".produtosGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'C&oacute;digo',
					name : 'codigo',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'produtoDescricao',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'Tipo Produto',
					name : 'tipoProdutoDescricao',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Editor',
					name : 'nomeEditor',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'Fornecedor',
					name : 'tipoContratoFornecedor',
					width : 150,
					sortable : true,
					align : 'left'
				}, {
					display : 'PEB',
					name : 'peb',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Situa&ccedil;&atilde;o',
					name : 'situacao',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
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
				height : 255,
				singleSelect : true
			});
		}			

		function popularCombo(data, combo) {
			opcoes = "";
			selecionado = "-1";
			$.each(data, function(i,n){
				if (n["checked"]) {
					opcoes+="<option value="+n["value"]+" selected='selected'>"+n["label"]+"</option>";
					selecionado = n["value"];
				} else {
					opcoes+="<option value="+n["value"]+">"+n["label"]+"</option>";
				}
			});
			$(combo).clear().append(opcoes);
			$(combo).val(selecionado);
		}

		function inicializar() {

			iniciarGrid();
		}

		function pesquisar() {
			
			var codigo = $("#codigoProduto").val();
			var produto = $("#produto").val();
			var periodicidade = $("#periodicidade").val();
			var fornecedor = $("#fornecedor").val();
			var editor = $("#edicao").val();
			var codigoTipoProduto = $("#comboTipoProduto").val();
			
			$(".produtosGrid").flexOptions({
				url: "<c:url value='/produto/pesquisarProdutos' />",
				params: [{name:'codigo', value: codigo },
					     {name:'produto', value: produto },
					     {name:'fornecedor', value: fornecedor },
					     {name:'editor', value: editor },
					     {name:'codigoTipoProduto', value : codigoTipoProduto}],
				newp: 1,
			});
			
			$(".produtosGrid").flexReload();
		}

		function editarProduto(id) {

			$("#dialog-novo").dialog({
				resizable: false,
				height:600,
				width:950,
				modal: true,
				buttons: {
					"Confirmar": function() {

						salvarProduto();
						
						$("#dialog-novo").dialog( "close" );
				   		$(".produtosGrid").flexReload();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});
			
			carregarNovoProduto();

			carregarProdutoEditado(id);
		}

		function carregarProdutoEditado(id) {

		}
		
		function removerProduto(id) {
			
			$("#dialog-excluir").dialog( {
				resizable : false,
				height : 'auto',
				width : 450,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						$.postJSON("<c:url value='/produto/removerProduto' />", 
								   "id=" + id,
								   function(result) {
								   		
								   		$("#dialog-excluir").dialog("close");
								   		
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											
											exibirMensagem(tipoMensagem, listaMensagens);
										}
												
										$(".produtosGrid").flexReload();
								   },
								   null,
								   true
						);
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});
		}

		function novoProduto() {

			$("#dialog-novo").dialog({
				resizable: false,
				height:600,
				width:950,
				modal: true,
				buttons: {
					"Confirmar": function() {

						salvarProduto();
						
				   		$(".produtosGrid").flexReload();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
			});

			carregarNovoProduto();
			limparModalCadastro();
		}

		function carregarNovoProduto() {

			$.postJSON("<c:url value='/produto/carregarDadosProduto' />",
						null,
						function (result) {

							popularCombo(result[0], $("#comboTipoProdutoCadastro"));
							popularCombo(result[1], $("#comboFornecedoresCadastro"));
							popularCombo(result[2], $("#comboEditor"));
							popularCombo(result[3], $("#comboTipoDesconto"));
						},
					  	null,
					   	true
				);
		}

		function limparModalCadastro() {

			$("#idProduto").val("");
			$("#codigoProdutoCadastro").val("");
			$("#nomeProduto").val("");
			$("#sloganProduto").val("");
			$("#peb").val("");
			$("#pacotePadrao").val("");
			$("#pesoProduto").val("");
			$("#comprimentoProduto").val("");
			$("#larguraProduto").val("");
			$("#espessuraProduto").val("");
			$("#comboPeriodicidade").val("");
			$("#percentualDesconto").val("");
		}
		
		function salvarProduto() {

			 var params = [{name:"produto.id",value:$("#idProduto").val()},
            			 {name:"produto.codigo",value:$("#codigoProdutoCadastro").val()},
            			 {name:"produto.nome",value:$("#nomeProduto").val()},
            			 {name:"produto.peb",value:$("#peb").val()},
            			 {name:"produto.pacotePadrao",value:$("#pacotePadrao").val()},
            			 {name:"produto.peso",value:$("#pesoProduto").val()},
            			 {name:"produto.slogan",value:$("#sloganProduto").val()},
            			 {name:"dimensao.largura",value:$("#larguraProduto").val()},
            			 {name:"dimensao.comprimento",value:$("#comprimentoProduto").val()},
            			 {name:"dimensao.espessura",value:$("#espessuraProduto").val()},
            			 {name:"periodicidadeProduto",value:$("#comboPeriodicidade").val()},
            			 {name:"formaComercializacao",value:PesquisaProduto.buscarValueRadio('formaComercializacao')},
            			 {name:"codigoEditor",value:$("#comboEditor").val()},
            			 {name:"codigoFornecedor",value:$("#comboFornecedoresCadastro").val()},
            			 {name:"codigoTipoDesconto",value:$("#comboTipoDesconto").val()},
            			 {name:"codigoTipoProduto",value:$("#comboTipoProdutoCadastro").val()},
            			 {name:"percentualAbrangencia",value:$("#percentualAbrangencia").val()},
            			 {name:"algoritmo",value:$("#comboAlgoritmo").val()},
            			 {name:"parametrosAbertos",value:PesquisaProduto.buscarValueCheckBox("checkParametrosAbertos")},
            			 {name:"lancamentoImediato",value:PesquisaProduto.buscarValueCheckBox("checkLcntoImediato")},
            			 {name:"tributacaoFiscal",value:PesquisaProduto.buscarValueRadio('radioTributacaoFiscal')},
            			 {name:"situacaoTributaria",value:PesquisaProduto.buscarValueRadio('radioSituacaoTributaria')} ];
			  
			$.postJSON("<c:url value='/produto/salvarProduto' />", 
				   	params,
				   	function (result) {

						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagem(tipoMensagem, listaMensagens);
						} 

						if (tipoMensagem == 'SUCCESS') {
							$("#dialog-novo").dialog( "close" );
						}
						
					},
				  	null,
				   	true,
				   	'dialogMensagemNovo'
			);
		}
		
		function executarPreProcessamento(resultado) {

			$.each(resultado.rows, function(index, row) {
				
				var linkAprovar = '<a href="javascript:;" onclick="editarProduto(' + row.cell.id + ');" style="cursor:pointer">' +
						     	  	'<img title="Editar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a href="javascript:;" onclick="removerProduto(' + row.cell.id + ');" style="cursor:pointer">' +
								   	 '<img title="Excluir" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAprovar + linkExcluir;
			});
				
			$(".grids").show();
			
			return resultado;
		}
		
	</script>

	<style>
		label { 
			vertical-align:super; 
		}
		
		#dialog-novo label { 
			width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px; 
		}
		
		.ui-tabs .ui-tabs-panel {
		   padding: 6px!important;
		}
	</style>

</head>

<body>

	<div id="dialog-excluir" title="Excluir Produto">
		<p>Confirma a exclus&atilde;o deste Produto?</p>
	</div>

	<div id="dialog-novo" title="Incluir Novo Produto">
     
		<input id="idProduto" type="hidden" />
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialogMensagemNovo" name="messageDialog"/>
		</jsp:include> 
     
		<div id="tabProduto">
			<ul>
				<li><a href="#tabProduto-1">Dados B&aacute;sicos</a></li>
				<li><a href="#tabProduto-2">Informa&ccedil;&otilde;es Adicionais</a></li>
				<li><a href="#tabProduto-3">Segmenta&ccedil;&atilde;o</a></li>
			</ul>

			<div id="tabProduto-1">
				<table width="800" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="135"><strong>C&oacute;digo:</strong></td>
						<td width="260"><input type="text" name="codigoProdutoCadastro" id="codigoProdutoCadastro" style="width:130px;" maxlength="30" /></td>
						<td width="142"><strong>Produto:</strong></td>
						<td width="250"><input type="text" name="nomeProduto" id="nomeProduto" style="width:250px;" maxlength="60" /></td>
					</tr>
					<tr>
						<td><strong>Editor:</strong></td>
						<td>
							<select name="comboEditor" id="comboEditor" style="width:210px;" >
							</select>
						</td>
						<td><strong>Fornecedor:</strong></td>
						<td>
							<select name="comboFornecedoresCadastro" id="comboFornecedoresCadastro" style="width:200px;" >
							</select>
						</td>
					</tr>
					<tr>
						<td><strong> Slogan do Produto:</strong></td>
						<td colspan="3"><input type="text" name="sloganProduto" id="sloganProduto" style="width:657px;" /></td>
					</tr>
					<tr>
						<td><strong>Tipo de Desconto:</strong></td>
						<td>
							<select name="comboTipoDesconto" id="comboTipoDesconto" style="width:210px;" onchange="carregarPercentualDesconto();" >
							</select>
						</td>
						<td><strong>% Desconto:</strong></td>
						<td><input type="text" name="percentualDesconto" id="percentualDesconto" style="width:80px;" readonly="readonly" disabled="disabled" /></td>
					</tr>
					<tr>
						<td><strong>Tipo de Produto:</strong></td>
						<td>
							<select name="comboTipoProdutoCadastro" id="comboTipoProdutoCadastro" style="width:210px;" >
							</select>
						</td>
						<td><strong>Forma Comercializa&ccedil;&atilde;o:</strong></td>
						<td>
							<table width="229" border="0" cellspacing="1" cellpadding="1">
								<tr>
									<td width="21"><input type="radio" name="formaComercializacao" id="formaComercializacaoConsignado" value="C" /></td>
									<td width="86">Consignado</td>
									<td width="21"><input type="radio" name="formaComercializacao" id="formaComercializacaoContaFirme" value="CF" /></td>
									<td width="88">Conta Firme</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td><strong>PEB:</strong></td>
						<td><input type="text" name="peb" id="peb" style="width:80px;" /></td>
						<td><strong>Pacote Padr&atilde;o:</strong></td>
						<td><input type="text" name="pacotePadrao" id="pacotePadrao" style="width:80px;" /></td>
					</tr>
				</table>
			</div> 
          
			<div id="tabProduto-2">
				<table width="890" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="508" valign="top">
							<fieldset style="width:505px!important; margin-bottom:5px;">
								<legend>Outros</legend>
									<table width="500" border="0" cellspacing="1" cellpadding="1">
										<tr>
											<td width="105">Periodicidade: </td>
											<td width="209">
												<select name="comboPeriodicidade" id="comboPeriodicidade" style="width:150px;" >
													<option value="" selected="selected"></option>
													<option value="SEMANAL" >Semanal</option>
													<option value="QUINZENAL" >Quinzenal</option>
													<option value="MENSAL" >Mensal</option>
													<option value="TRIMESTRAL" >Trimestral</option>
													<option value="SEMESTRAL" >Semestral</option>
													<option value="ANUAL" >Anual</option>
												</select>
											</td>
											<td width="131">Par&acirc;metros Abertos:</td>
											<td width="42"><input type="checkbox" name="checkParametrosAbertos" id="checkParametrosAbertos" /></td>
										</tr>
										<tr>
											<td>% Abrang&ecirc;ncia:</td>
											<td><input type="text" name="percentualAbrangencia" id="percentualAbrangencia" style="width:80px;" /></td>
											<td>Lancto Imediato:</td>
											<td><input type="checkbox" name="checkLcntoImediato" id="checkLcntoImediato" /></td>
										</tr>
										<tr>
											<td>Algoritmo:</td>
											<td>
												<select name="comboAlgoritmo" id="comboAlgoritmo" style="width:150px;" >
													<option value="0" selected="selected"></option>
												</select>
											</td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
									</table>
							</fieldset>
	   	
							<fieldset style="width:505px!important; float:left; margin-bottom:5px;">
								<legend>Sele&ccedil;&atilde;o</legend>
								<table width="500" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="116">Combina&ccedil;&atilde;o 1:</td>
										<td width="20"><input type="checkbox" name="checkbox5" id="checkbox5" disabled="disabled" /></td>
										<td width="349">
											<select name="select6" id="select6" style="width:330px;" disabled="disabled" >
												<option selected="selected"></option>
											</select>
										</td>
									</tr>
									<tr>
										<td>
										  	<table width="115" border="0" cellspacing="1" cellpadding="1">
												<tr>
													<td width="20"><input type="radio" name="radio" id="radio3" value="radio" disabled="disabled" /></td>
													<td width="32">E</td>
													<td width="20"><input type="radio" name="radio" id="radio4" value="radio" disabled="disabled" /></td>
													<td width="30">Ou</td>
												</tr>
											</table>
										</td>
										<td><input type="checkbox" name="checkbox" id="checkbox" disabled="disabled" /></td>
										<td>
											<select name="select7" id="select7" style="width:330px;" disabled="disabled" >
												<option>Combina&ccedil;&atilde;o 2</option>
											</select>
										</td>
									</tr>
									<tr>
										<td>
											<table width="115" border="0" cellspacing="1" cellpadding="1">
												<tr>
													<td width="20"><input type="radio" name="radio" id="radio15" value="radio" disabled="disabled" /></td>
													<td width="32">E</td>
													<td width="20"><input type="radio" name="radio" id="radio16" value="radio" disabled="disabled" /></td>
													<td width="30">Ou</td>
												</tr>
											</table>
										</td>
										<td><input type="checkbox" name="checkbox2" id="checkbox2" disabled="disabled" /></td>
										<td>
											<select name="select8" id="select8" style="width:330px;" disabled="disabled" >
												<option>Combina&ccedil;&atilde;o 3</option>
											</select>
										</td>
									</tr>
								</table>
							</fieldset>
	    
							<fieldset style="width:505px!important; float:left; margin-bottom:5px;">
								<legend>Caracter&iacute;sticas</legend>
								<table width="500" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="94" height="24">Comprimento:</td>
										<td width="165"><input type="text" name="comprimentoProduto" id="comprimentoProduto" style="width:80px;" /></td>
										<td width="54">Largura:</td>
										<td width="174"><input type="text" name="larguraProduto" id="larguraProduto" style="width:80px;" /></td>
									</tr>
									<tr>
										<td height="24">Espessura:</td>
										<td><input type="text" name="espessuraProduto" id="espessuraProduto" style="width:80px;" /></td>
										<td>Peso:</td>
										<td><input type="text" name="pesoProduto" id="pesoProduto" style="width:80px;" /></td>
									</tr>
								</table>
							</fieldset>
						        
							<br clear="all" />
							<br clear="all" />
	    
						</td>
					    
					    <td width="20" style="width:20px;">
					    
					    <td width="362" valign="top">
	    
							<fieldset style="width:323px!important; float:left; margin-bottom:5px; margin-right:0px;">
								<legend>Tributa&ccedil;&atilde;o Fiscal</legend>
								<table width="229" border="0" cellpadding="1" cellspacing="1">
									<tr>
										<td width="20"><input type="radio" name="radioTributacaoFiscal" id="radioTributado" value="TRIBUTADO" /></td>
										<td width="59">Tributado</td>
										<td width="20"><input type="radio" name="radioTributacaoFiscal" id="radioIsento" value="ISENTO" /></td>
										<td width="37">Isento</td>
										<td width="20"><input type="radio" name="radioTributacaoFiscal" id="radioTributacaoOutros" value="OUTROS" /></td>
										<td width="81"> Outros</td>
									</tr>
								</table>
							</fieldset>
							
							<br clear="all" />
	    
							<fieldset style="width:323px!important; float:left; margin-bottom:5px;">
								<legend>Situa&ccedil;&atilde;o Tribut&aacute;ria</legend>
								<table width="294" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="20"><input type="radio" name="radioSituacaoTributaria" id="radioSituacaoIsento" value="ISENTO" /></td>
										<td width="267">Produto Nacional Isento ICMS/IPI</td>
									</tr>
									<tr>
										<td><input type="radio" name="radioSituacaoTributaria" id="radioSituacaoImune" value="IMUNE" /></td>
										<td>Produto Nacional Imune ICMS/IPI </td>
									</tr>
									<tr>
										<td><input type="radio" name="radioSituacaoTributaria" id="radioSituacaoOutros" value="OUTROS" /></td>
										<td>Outros</td>
									</tr>
								</table>
							</fieldset>
	
							<br clear="all" />
							
							<fieldset style="width:323px!important; float:left; margin-bottom:5px; margin-right:0px!important;">
								<legend>Regime de Recolhimento: Normal</legend>
								<table width="319" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="160" height="24">Classe Histograma Anal&iacute;tico</td>
										<td width="152">
											<select name="comboHistogramaAnalitico" id="comboHistogramaAnalitico" style="width:150px;" >
												<option value="" selected="selected"></option>
											</select>
										</td>
									</tr>
									<tr>
										<td height="24">% Limite  Cota Fixa&ccedil;&atilde;o:</td>
										<td><input type="text" name="percentualLimiteCotaFixacao" id="percentualLimiteCotaFixacao" style="width:80px;" /></td>
									</tr>
									<tr>
										<td height="24">% Limite Reparte Fixa&ccedil;&atilde;o:</td>
										<td><input type="text" name="percentualLimiteReparteFixacao" id="percentualLimiteReparteFixacao" style="width:80px;" /></td>
									</tr>
								</table>
							</fieldset>
	    
							<br clear="all" />
	    
							<fieldset style="width:323px!important; float:left; margin-bottom:5px; margin-right:0px!important;">
								<table width="338" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="109">Grupo Editorial:</td>
										<td width="207"><input type="text" name="grupoEditorial" id="grupoEditorial" value="" style="width:200px;" ></td>
									</tr>
									<tr>
										<td>SubGrupo Editorial:</td>
										<td><input type="text" name="subGrupoEditorial" id="subGrupoEditorial" value="" style="width:200px;" ></td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</div>
		
			<div id="tabProduto-3">
	
				<fieldset style="width:880px!important; margin:auto auto!important;">
					<legend>P&uacute;blico Alvo</legend>
					<table width="786" border="0" cellspacing="1" cellpadding="1">
						<tr>
							<td width="402" valign="top">
								<table width="400" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="112"><strong>Classe Social:</strong></td>
										<td width="281">
											<select name="select12" id="select13" style="width:250px;" disabled="disabled" >
												<option selected="selected"></option>
											</select>
										</td>
									</tr>
									<tr>
										<td><strong>Sexo:</strong></td>
										<td>
											<select name="select13" id="select14" style="width:250px;" disabled="disabled" >
												<option selected="selected"></option>
											</select>
										</td>
									</tr>
									<tr>
										<td><strong>Faixa-Et&aacute;ria:</strong></td>
										<td>
											<select name="select14" id="select15" style="width:250px;" disabled="disabled" >
												<option selected="selected"></option>
											</select>
										</td>
									</tr>
								</table>
							</td>
							<td width="34" style="width:34px;">&nbsp;</td>
							<td width="344" valign="top">
								<table width="344" border="0" cellspacing="1" cellpadding="1">
									<tr>
										<td width="137"><strong>Forma F&iacute;sica:</strong></td>
										<td width="200">
											<select name="select12" id="select13" style="width:200px;" disabled="disabled" >
												<option selected="selected"></option>
											</select>
										</td>
									</tr>
									<tr>
										<td><strong>Lan&ccedil;amento:</strong></td>
										<td>
											<select name="select13" id="select14" style="width:200px;" disabled="disabled"  >
												<option selected="selected"></option>
											</select>
										</td>
									</tr>
									<tr>
										<td><strong>Tema Principal:</strong></td>
										<td>
											<select name="select14" id="select15" style="width:200px;" disabled="disabled" >
												<option selected="selected"></option>
											</select>
										</td>
									</tr>
									<tr>
										<td><strong>Tema Secund&aacute;rio:</strong></td>
										<td>
											<select name="select15" id="select16" style="width:200px;" disabled="disabled" >
												<option selected="selected"></option>
											</select>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</fieldset>
				
				<br clear="all" />
			</div>
		</div>
	</div>

	<fieldset class="classFieldset">
		<legend> Pesquisar Produtos</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="43">C&oacute;digo:</td>
				<td width="123" >
					
			    	<input type="text" name="codigoProduto" id="codigoProduto"
						   style="width: 80px; float: left; margin-right: 5px;" maxlength="255"
						   onchange="produto.pesquisarPorCodigoProduto('#codigoProduto', '#produto', '#edicao', false,
								   									   PesquisaProduto.pesquisarProdutosSuccessCallBack,
								   									   PesquisaProduto.pesquisarProdutosErrorCallBack);" />
				</td>
				
				<td width="55">Produto:</td>
				<td width="237">
					<input type="text" name="produto" id="produto" style="width: 222px;" maxlength="255"
					       onkeyup="produto.autoCompletarPorNomeProduto('#produto', false);"
					       onblur="produto.pesquisarPorNomeProduto('#codigoProduto', '#produto', '#edicao', false,
														    	   PesquisaProduto.pesquisarProdutosSuccessCallBack,
														    	   PesquisaProduto.pesquisarProdutosErrorCallBack);"/>
				</td>
				<td width="99">Fornecedor:</td>
				<td width="251"><input type="text" id="fornecedor" style="width:200px;"/></td>
				<td width="106">&nbsp;</td>
			</tr>
			<tr>
				<td>Editor:</td>
				<td colspan="3" >
					<input type="text" style="width:410px;" name="edicao" id="edicao" maxlength="20" disabled="disabled"
							   onchange="produto.validarNumEdicao('#codigoProduto', '#edicao', false,
							   										PesquisaProduto.validarEdicaoSuccessCallBack,
						    	   									PesquisaProduto.validarEdicaoErrorCallBack);"/>
				</td>
				<td>Tipo de Produto:</td>
				<td>
					<select id="comboTipoProduto" style="width:207px;">
						<option value="0"></option>
						<c:forEach items="${listaTipoProduto}" var="tipoProduto" >
							<option value="${tipoProduto.id}">${tipoProduto.descricao}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<span class="bt_pesquisar">
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>

	<div class="linha_separa_fields">&nbsp;</div>
	
	<fieldset class="classFieldset">
		<legend>Produtos Cadastrados</legend>
			<div class="grids" style="display:none;">
				<table class="produtosGrid"></table>
			</div>
	
		<span class="bt_novos" title="Novo">
			<a href="javascript:;" onclick="novoProduto();">
				<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo
			</a>
		</span>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>

</body>