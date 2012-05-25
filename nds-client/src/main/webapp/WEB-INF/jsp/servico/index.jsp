<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
		
	<script>

		function buscaCobrancaSelecionada() { 
	
			var cobranca = new Array();
	
			$("input[type=radio][name='radioCobranca']:checked").each(function(){
				cobranca.push($(this).val());
			});
	
			return cobranca;
		}
	
		function buscaPeriodicidadeSelecionada() { 
	
			var periodicidade = new Array();
	
			$("input[type=radio][name='periodicidadeCadastro']:checked").each(function(){
				periodicidade.push($(this).val());
			});
	
			return periodicidade;
		}

		$(function() {
			
			inicializar();
		});

		function mostra_semanal() {
			$('.semanal').show();
			$('.diaMes').hide();
		}

		function mostra_mensal() {
			$('.diaMes').show();
			$('.semanal').hide();
		}

		function mostra_diario( ) {
			$('.semanal').hide();
			$('.diaMes').hide();
		}
			
		function mostra_faturamento() {
			$('.faturamento').show();

			$("#percentualFaturamento").val("");
			
			$('.taxa').hide();
		}

		function mostra_taxa() {
			
			$('.taxa').show();

			$('.faturamento').hide();
			
			$("#percentualFaturamento").val("");
			$("#baseCalculoCadastro").val(0);
		}
		
		function iniciarGrid() {
			$(".serviceGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'C&oacute;digo',
					name : 'codigo',
					width : 85,
					sortable : true,
					align : 'left'
				}, {
					display : 'Descri&ccedil;&atilde;o',
					name : 'descricao',
					width : 250,
					sortable : true,
					align : 'left'
				}, {
					display : 'Taxa R$',
					name : 'taxa',
					width : 70,
					sortable : true,
					align : 'right'
				}, {
					display : 'Base de C&aacute;lculo',
					name : 'baseCalculo',
					width : 270,
					sortable : true,
					align : 'left'
				}, {
					display : '% C&aacute;lculo sobre Base',
					name : 'percentualCalculoBase',
					width : 120,
					sortable : true,
					align : 'right'
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
		
		function inicializar() {
			
			iniciarGrid();
			aplicarMascaras();
		}

		function aplicarMascaras() {

			$("#codigoCadastro").numeric();
			$("#taxaFixaCadastro").numeric();
			$("#percentualCalculoBase").numeric();   	
		}

		function limparModalCadastro() {
						
			$("#idServico").val("");
			$("#codigoCadastro").val("");
			$("#descricaoCadastro").val("");
			$("#taxaFixaCadastro").val("");
			$("#baseCalculoCadastro").val("");
			$("#percentualFaturamento").val("");   	
			$("#diaSemana").val("");
			$("#diaMes").val("");

			$("#radioCobrancaTF").attr('checked', false);
			$("#radioCobrancaPF").attr('checked', false);

			$("#periodicidadeCadastroDiario").attr('checked', false);
			$("#periodicidadeCadastroSemanal").attr('checked', false);
			$("#periodicidadeCadastroMensal").attr('checked', false);

			$('.diaMes').hide();
			$('.semanal').hide();
			$('.taxa').hide();
			$('.faturamento').hide();
			
			aplicarMascaras();			
		}
		
		function pesquisar() {
	
			var codigo = $("#codigo").val();
			var descricao = $("#descricao").val();
			var periodicidade = $("#periodicidade").val();
	
			$(".serviceGrid").flexOptions({
				url: "<c:url value='/servico/cadastroServico/pesquisarServicos' />",
				params: [{name:'codigo', value: codigo },
					     {name:'descricao', value: descricao },
					     {name:'periodicidade', value: periodicidade }],
				newp: 1,
			});
			
			$(".serviceGrid").flexReload();
		}

		function editarServico(id) {

			$.postJSON(
				"<c:url value='/servico/cadastroServico/buscarServicoPeloCodigo' />",
				"id=" + id,
				function (result) {
					
					$("#codigoCadastro").attr('disabled', '');
					carregarEdicao(result);
					incluirNovo();
				}
			);
		}

		function carregarEdicao(servico) {

			$("#idServico").val(servico.id);
			$("#codigoCadastro").val(servico.id);
			$("#descricaoCadastro").val(servico.descricao);

			var tipoCobranca = servico.tipoCobranca;

			if (tipoCobranca == 'TF') {
				$("#radioCobrancaTF").attr('checked', true);
				mostra_taxa();
			} else if (tipoCobranca == 'PF') {
				$("#radioCobrancaPF").attr('checked', true);
				mostra_faturamento();
			}

			var periodicidade = servico.periodicidade;

			if (periodicidade == 'D') {
				$("#periodicidadeCadastroDiario").attr('checked', true);
				mostra_diario();
			} else if (periodicidade == 'S') {
				$("#periodicidadeCadastroSemanal").attr('checked', true);
				mostra_semanal();
			} else if (periodicidade == 'M') {
				$("#periodicidadeCadastroMensal").attr('checked', true);
				mostra_mensal();
			}

			$("#taxaFixaCadastro").val(servico.taxa);
			$("#percentualFaturamento").val(servico.percentualCalculoBase);
			$("#baseCalculoCadastro").val(servico.baseCalculo);
			$("#diaSemana").val(servico.diaSemana);
			$("#diaMes").val(servico.diaMes);
		}

		function incluirENovoServico() {

			salvarServico(false);
			limparModalCadastro();
		}

		function salvarServico(isConfirm) {

			var id = $("#idServico").val(); 
			var descricao = $("#descricaoCadastro").val();
			var taxa = $("#taxaFixaCadastro").val();
			var percentualFaturamento = $("#percentualFaturamento").val();
			var baseCalculo = $("#baseCalculoCadastro").val();
			var periodicidadeCadastro = buscaPeriodicidadeSelecionada();
			var diaSemana = $("#diaSemana").val();
			var diaMes = $("#diaMes").val();
			var cobranca = buscaCobrancaSelecionada();
			
			$.postJSON("<c:url value='/servico/cadastroServico/salvarServico' />", 
					   "id=" + id +
					   "&descricao=" + descricao +
					   "&taxaFixa="+ taxa +
					   "&percentualFaturamento=" + percentualFaturamento +
					   "&baseCalculo=" + baseCalculo +
					   "&periodicidadeCadastro=" + periodicidadeCadastro +
					   "&diaSemana=" + diaSemana +
					   "&diaMes=" + diaMes +
					   "&cobranca=" + cobranca,
					   function(result) {
					   							   		
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {

								if (isConfirm) {
									exibirMensagem(tipoMensagem, listaMensagens);
								} else {
									exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
								}
							}
					   },
					   null,
					   isConfirm
			);

			$(".serviceGrid").flexReload();
		}
		
		function incluirNovo() {

			$("#dialog-novo").dialog({
				resizable : false,
				height : 390,
				width : 600,
				modal : true,
				buttons : {
					"Confirmar" : function() {

						salvarServico(true);

				   		$("#dialog-novo").dialog("close");
						$(".serviceGrid").flexReload();
						limparModalCadastro();
					},
					"Cancelar" : function() {
						$(".serviceGrid").flexReload();
						$(this).dialog("close");
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
					limparModalCadastro();
				}
			});
		}
		
		function removerServico(id) {
			
			$("#dialog-excluir").dialog({
				resizable : false,
				height : 'auto',
				width : 450,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						$.postJSON("<c:url value='/servico/cadastroServico/removerServico' />", 
								   "id=" + id,
								   function(result) {
								   		
								   		$("#dialog-excluir").dialog("close");
								   		
										var tipoMensagem = result.tipoMensagem;
										var listaMensagens = result.listaMensagens;
										
										if (tipoMensagem && listaMensagens) {
											
											exibirMensagem(tipoMensagem, listaMensagens);
										}
												
										$(".serviceGrid").flexReload();
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
		
		function executarPreProcessamento(resultado) {
			
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids").hide();

				return resultado;
			}
			
			$.each(resultado.rows, function(index, row) {
				
				var linkAprovar = '<a href="javascript:;" onclick="editarServico(' + row.cell.id + ');" style="cursor:pointer">' +
						     	  	'<img title="Editar" src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" />' +
						  		  '</a>';
				
				var linkExcluir = '<a href="javascript:;" onclick="removerServico(' + row.cell.id + ');" style="cursor:pointer">' +
								   	 '<img title="Excluir" src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" />' +
								   '</a>';
				
				row.cell.acao = linkAprovar + linkExcluir;
			});
				
			$(".grids").show();
			
			return resultado;
		}
		
	</script>
	
	<style>
		label { vertical-align:super; }
		
		#dialog-novo label { width:370px; margin-bottom:10px; float:left; font-weight:bold; line-height:26px; }
		
		.taxa, .faturamento, .diaMes, .semanal { display:none; }
		
		#dialog-novo select, #dialog-novo input { }
	</style>

</head>

<body>

	<div id="dialog-excluir" title="Excluir Servi&ccedil;o de Entrega">
	
		<jsp:include page="../messagesDialog.jsp" />
		
		<p>Confirma a exclus&atilde;o deste Servi&ccedil;o de Entrega?</p>
		
	</div>


	<!--modal -->
	<div id="dialog-novo" title="Incluir Novo Servi&ccedil;o de Entrega">
		
		<input id="idServico" type="hidden" />
		<jsp:include page="../messagesDialog.jsp">
			<jsp:param value="dialogMensagemNovo" name="messageDialog"/>
		</jsp:include> 

		<table width="580" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td>C&oacute;digo:</td>
				<td colspan="3"><input id="codigoCadastro" type="text" style="width:270px;" disabled="disabled" /></td>
			</tr>
			<tr>
				<td>Descri&ccedil;&atilde;o:</td>
				<td colspan="3"><input id="descricaoCadastro" type="text" style="width:270px;" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="83">
					<strong>Cobran&ccedil;a:</strong>
				</td>
				<td width="20">
					<input name="radioCobranca" id="radioCobrancaTF" type="radio" value="TF" onchange="mostra_taxa();" />
				</td>
				<td width="177">
					Taxa Fixa R$
				</td>
				<td width="295">
					<input id="taxaFixaCadastro" type="text" style="width:70px; text-align:right;" class="taxa" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					<input name="radioCobranca" id="radioCobrancaPF" type="radio" value="PF" onclick="mostra_faturamento();" />
				</td>
				<td>
					Percentual do Faturamento
				</td>
				<td>
					<div class="faturamento">
						<input id="percentualFaturamento" type="text" style="width:70px; text-align:right;" />&nbsp;%
					</div>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><div class="faturamento">Base de C&aacute;lculo</div></td>
				<td>
					<div class="faturamento">
						<select id="baseCalculoCadastro" style="width:150px;">
							<option value="" selected="selected"></option>
							<option value="B" >Faturamento Bruto</option>
							<option value="L" >Faturamento L&iacute;quido</option>
						</select>
					</div>
				</td>
			</tr>
		</table>
		
		<table width="580" border="0" cellspacing="2" cellpadding="0">
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td width="86">Periodicidade:</td>
				<td width="20"><input id="periodicidadeCadastroDiario" name="periodicidadeCadastro" type="radio" value="D" onclick="mostra_diario();" /></td>
				<td width="87">Di&aacute;rio</td>
				<td width="20"><input id="periodicidadeCadastroSemanal" name="periodicidadeCadastro" type="radio" value="S" onclick="mostra_semanal();" /></td>
				<td width="148">Semanal</td>
				<td width="20"><input id="periodicidadeCadastroMensal" name="periodicidadeCadastro" type="radio" value="M" onclick="mostra_mensal();" /></td>
				<td width="191">Mensal</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>
					<div class="semanal">
						<select id="diaSemana" style="width:120px;">
							<option value="" ></option>
							<option value="2" >Segunda-feira</option>
							<option value="3" >Ter&ccedil;a-feira</option>
							<option value="4" >Quarta-feira</option>
							<option value="5" >Quinta-feira</option>
							<option value="6" >Sexta-feira</option>
						</select>
					</div>
				</td>
				<td>&nbsp;</td>
				<td>
					<div  class="diaMes">Dia do M&ecirc;s: 
						<input name="diaMes" id="diaMes" type="text" value="" style="width:70px; text-align:right;" />
					</div>
				</td>
			</tr>
		</table>
		
		<br clear="all" />
		
		<span class="bt_add">
			<a href="javascript:;" onclick="incluirENovoServico();">Incluir Novo</a>
		</span>
	</div>
	
	<!-- pesquisa -->	
    <fieldset class="classFieldset">
    	<legend> Pesquisar Servi&ccedil;os de Entrega</legend>
   		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="46">C&oacute;digo:</td>
				<td width="118" ><input type="text" maxlength="20" name="codigo" id="codigo" style="width:100px;"/></td>
				
				<td width="63">Descri&ccedil;&atilde;o:</td>
				<td width="250"><input type="text" name="descricao" id="descricao" style="width:222px;" maxlength="256" /></td>
				
				<td width="82">Periodicidade:</td>
				<td width="251">
					<select name="periodicidade" id="periodicidade" style="width:120px;">
						<option value="" selected="selected"></option>
						<option value="D" >Di&aacute;rio</option>
						<option value="S" >Semanal</option>
						<option value="M" >Mensal</option>
					</select>
				</td>
				<td width="104">
					<span class="bt_pesquisar" title="Pesquisar Servi&ccedil;o">
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>

	<!-- GRID PAGINACAO -->
	<div class="linha_separa_fields">&nbsp;</div>
		<fieldset class="classFieldset">
	 		<legend>Servi&ccedil;os de Entrega Cadastrados</legend>
			
			<div class="grids" style="display:none;">
				<table class="serviceGrid"></table>
			</div>
	
			<span class="bt_novos" title="Novo">
				<a href="javascript:;" onclick="incluirNovo();" >
					<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>
					Novo
				</a>
			</span>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
	   
	</div>

</body>