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
				height:550,
				width:850,
				modal: true,
				buttons: {
					"Confirmar": function() {

						salvarProduto();
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					limparModalCadastro();
					clearMessageDialogTimeout();
				}
			});
			
			carregarNovoProduto(
				function() {
					limparModalCadastro();
					carregarProdutoEditado(id);		
				}
			);
			
			
		}
		
		function carregarProdutoEditado(id) {

			$.postJSON("<c:url value='/produto/carregarProdutoParaEdicao' />", 
					   	"id=" + id,
					   	function(result) {
				   
							$("#idProduto").val(result.id);
							$("#codigoProdutoCadastro").val(result.codigo);
							$("#nomeProduto").val(result.nome);
							$("#sloganProduto").val(result.slogan);
							$("#peb").val(result.peb);
							$("#pacotePadrao").val(result.pacotePadrao);
							$("#comboPeriodicidade").val(result.periodicidade);
							$("#grupoEditorial").val(result.grupoEditorial);
							$("#subGrupoEditorial").val(result.subGrupoEditorial);
							$("#comboEditor").val(result.codigoEditor);
							$("#comboFornecedoresCadastro").val(result.codigoFornecedor);
							$("#comboTipoDesconto").val(result.codigoTipoDesconto);
							$("#comboTipoProdutoCadastro").val(result.codigoTipoProduto)

							if (result.formaComercializacao == 'CONTA_FIRME') {
								$("#formaComercializacaoContaFirme").attr('checked', true);
							} else if (result.formaComercializacao == 'CONSIGNADO') {
								$("#formaComercializacaoConsignado").attr('checked', true);
							}
							
							if (result.tributacaoFiscal == 'TRIBUTADO') {
								$("#radioTributado").attr('checked', true);
							} else if (result.tributacaoFiscal == 'ISENTO') {
								$("#radioIsento").attr('checked', true);
							} else if (result.tributacaoFiscal == 'OUTROS') {
								$("#radioTributacaoOutros").attr('checked', true);
							}							
						},
						null,
						true
					);
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
				height:550,
				width:850,
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
					limparModalCadastro();
					clearMessageDialogTimeout();
				}
			});

			carregarNovoProduto(limparModalCadastro);
		}

		function carregarNovoProduto(callback) {

			$.postJSON("<c:url value='/produto/carregarDadosProduto' />",
						null,
						function (result) {

							popularCombo(result[0], $("#comboTipoProdutoCadastro"));
							popularCombo(result[1], $("#comboFornecedoresCadastro"));
							popularCombo(result[2], $("#comboEditor"));
							popularCombo(result[3], $("#comboTipoDesconto"));

							if (callback) {
								callback();
							}
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
			$("#comboPeriodicidade").val("");

			$("#formaComercializacaoContaFirme").attr('checked', false);
			$("#formaComercializacaoConsignado").attr('checked', false);
						
			$("#radioTributado").attr('checked', false);
			$("#radioIsento").attr('checked', false);
			$("#radioTributacaoOutros").attr('checked', false);
			
			$("#percentualDesconto").val("");
			$("#grupoEditorial").val("");
			$("#subGrupoEditorial").val("");
		}
		
		function salvarProduto() {

			 var params = [{name:"produto.id",value:$("#idProduto").val()},
            			   {name:"produto.codigo",value:$("#codigoProdutoCadastro").val()},
            			   {name:"produto.nome",value:$("#nomeProduto").val()},
            			   {name:"produto.peb",value:$("#peb").val()},
            			   {name:"produto.pacotePadrao",value:$("#pacotePadrao").val()},
            			   {name:"produto.slogan",value:$("#sloganProduto").val()},
            			   {name:"produto.periodicidade",value:$("#comboPeriodicidade").val()},
            			   {name:"produto.formaComercializacao",value:PesquisaProduto.buscarValueRadio('formaComercializacao')},
            			   {name:"produto.tributacaoFiscal",value:PesquisaProduto.buscarValueRadio('radioTributacaoFiscal')},
            			   {name:"produto.grupoEditorial",value:$("#grupoEditorial").val()},
            			   {name:"produto.subGrupoEditorial",value:$("#subGrupoEditorial").val()},
            			   {name:"codigoEditor",value:$("#comboEditor").val()},
            			   {name:"codigoFornecedor",value:$("#comboFornecedoresCadastro").val()},
            			   {name:"codigoTipoDesconto",value:$("#comboTipoDesconto").val()},
            			   {name:"codigoTipoProduto",value:$("#comboTipoProdutoCadastro").val()} ];
			 
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
     	
     	<fieldset style="margin-bottom: 10px;">
     		<legend>Dados Basicos</legend>
			<table width="800" border="0" cellspacing="1" cellpadding="1">
				<tr>
					<td width="135"><strong>C&oacute;digo:</strong></td>
					<td width="260"><input type="text" name="codigoProdutoCadastro" id="codigoProdutoCadastro" style="width:130px;" maxlength="30" /></td>
					<td width="142"><strong>Produto:</strong></td>
					<td width="250"><input type="text" name="nomeProduto" id="nomeProduto" style="width:250px;" maxlength="60" /></td>
				</tr>
				<tr>
					<td><strong>Fornecedor:</strong></td>
					<td>
						<select name="comboFornecedoresCadastro" id="comboFornecedoresCadastro" style="width:200px;" >
						</select>
					</td>	
					<td><strong>Editor:</strong></td>
					<td>
						<select name="comboEditor" id="comboEditor" style="width:210px;" >
						</select>
					</td>
				</tr>
				<tr>
					<td><strong> Slogan do Produto:</strong></td>
					<td colspan="3"><input type="text" name="sloganProduto" id="sloganProduto" maxlength="50" style="width:657px;" /></td>
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
								<td width="21"><input type="radio" name="formaComercializacao" id="formaComercializacaoConsignado" value="CONSIGNADO" /></td>
								<td width="86">Consignado</td>
								<td width="21"><input type="radio" name="formaComercializacao" id="formaComercializacaoContaFirme" value="CONTA_FIRME" /></td>
								<td width="88">Conta Firme</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td><strong>PEB:</strong></td>
					<td><input type="text" name="peb" id="peb" style="width:80px;" maxlength="20" /></td>
					<td><strong>Pacote Padr&atilde;o:</strong></td>
					<td><input type="text" name="pacotePadrao" id="pacotePadrao" style="width:80px;" maxlength="20" /></td>
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
			</table>
		</fieldset>
		
		
		<table width="800" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="400" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>	
								<fieldset style="width:380px!important; margin-bottom:10px; height: 42px;">
									<legend>Outros</legend>
										
									<table>
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
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
						
						<tr>
							<td valign="top">
			   
								<fieldset style="width:380px!important; float:left; margin-bottom:10px; margin-right:0px!important;">
									<legend>Editorial</legend>
									<table width="380" border="0" cellspacing="1" cellpadding="1">
										<tr>
											<td width="100">Grupo Editorial:</td>
											<td ><input type="text" name="grupoEditorial" id="grupoEditorial" maxlength="25" value="" style="width:200px;" ></td>
										</tr>
										<tr>
											<td width="120" >SubGrupo Editorial:</td>
											<td><input type="text" name="subGrupoEditorial" id="subGrupoEditorial" maxlength="25" value="" style="width:200px;" ></td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
						
						<tr>
							<td>
								<fieldset style="width:381px!important; float:left; margin-bottom:10px; margin-right:0px;">
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
							</td>
						</tr>
					</table>
				</td>
				
				<td style="vertical-align: top;" >
					<fieldset style="width:385px!important; margin:0 auto!important 10px auto!important; height: 205px;">
						<legend>Segmenta&ccedil;&atilde;o</legend>
						<table width="380" border="0" cellspacing="1" cellpadding="1">
							<tr>
								<td width="380" valign="top">
									<table width="380" border="0" cellspacing="1" cellpadding="1">
										<tr>
											<td width="160"><strong>Classe Social:</strong></td>
											<td width="281">
												<select name="select12" id="select13" style="width:200px;" disabled="disabled" >
													<option selected="selected"></option>
												</select>
											</td>
										</tr>
										<tr>
											<td><strong>Sexo:</strong></td>
											<td>
												<select name="select13" id="select14" style="width:200px;" disabled="disabled" >
													<option selected="selected"></option>
												</select>
											</td>
										</tr>
										<tr>
											<td><strong>Faixa-Et&aacute;ria:</strong></td>
											<td>
												<select name="select14" id="select15" style="width:200px;" disabled="disabled" >
													<option selected="selected"></option>
												</select>
											</td>
										</tr>
										<tr>
											<td width="137"><strong>Formato:</strong></td>
											<td width="200">
												<select name="select12" id="select13" style="width:200px;" disabled="disabled" >
													<option selected="selected"></option>
												</select>
											</td>
										</tr>
										<tr>
											<td><strong>Tipo de Lan&ccedil;amento:</strong></td>
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
				</td>
			</tr>
		</table>
		
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