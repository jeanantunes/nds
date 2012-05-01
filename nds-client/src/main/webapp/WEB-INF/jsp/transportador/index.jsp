<head>
	<script language="javascript" type="text/javascript">
		
		function popup_novo_transportador() {
	
			$("#dialog-novo").dialog({
				resizable : false,
				height : 590,
				width : 900,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$(this).dialog("close");
						$("#effect").show("highlight", {}, 1000, callback);
						$(".grids").show();
	
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
	
		};
	
		function popup_alterar() {
	
			$("#dialog-novo").dialog({
				resizable : false,
				height : 600,
				width : 840,
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
	
			$("#dialog-excluir").dialog({
				resizable : false,
				height : 170,
				width : 380,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$(this).dialog("close");
						$("#effect").show("highlight", {}, 1000, callback);
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
		};
	
		function popup_incluir_veiculo() {
	
			$("#dialog-incluir-veiculo").dialog({
				resizable : false,
				height : 'auto',
				width : 420,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$(this).dialog("close");
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
		};
	
		function popup_incluir_motorista() {
	
			$("#dialog-incluir-motorista").dialog({
				resizable : false,
				height : 'auto',
				width : 420,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$(this).dialog("close");
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
		};
		function popup_excluir_associacao() {
	
			$("#dialog-excluir-associacao").dialog({
				resizable : false,
				height : 'auto',
				width : 380,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						$(this).dialog("close");
					},
					"Cancelar" : function() {
						$(this).dialog("close");
					}
				}
			});
		};
	
		$(function() {
			$("#tabs").tabs();
			
			$(".transportadoraGrid").flexigrid({
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigo',
					width : 60,
					sortable : true,
					align : 'left'
				}, {
					display : 'Razão Social',
					name : 'razaoSocial',
					width : 180,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNPJ',
					name : 'cnpj',
					width : 120,
					sortable : true,
					align : 'left'
				}, {
					display : 'Responsável',
					name : 'responsavel',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Telefone',
					name : 'telefone',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'E-Mail',
					name : 'email',
					width : 220,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 60,
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
			$(".transportadoraGrid").flexOptions({url: "<c:url value='/cadastro/transportador/pesquisarTransportadores'/>"});
		
			$(".associacaoGrid").flexigrid({
				dataType : 'json',
				colModel : [ {
					display : 'Veículo',
					name : 'veiculo',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Placa',
					name : 'placa',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'Motorista',
					name : 'motorista',
					width : 190,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNH',
					name : 'cnh',
					width : 95,
					sortable : true,
					align : 'left'
				}, {
					display : 'Rota',
					name : 'rota',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Roteiro',
					name : 'roteiro',
					width : 110,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 30,
					sortable : true,
					align : 'center'
				} ],
				width : 820,
				height : 100
			});
		
			$(".veiculosGrid").flexigrid({
				dataType : 'json',
				colModel : [ {
					display : ' ',
					name : 'sel',
					width : 20,
					sortable : true,
					align : 'left'
				}, {
					display : 'Tipo',
					name : 'tipoVeiculo',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Placa',
					name : 'placa',
					width : 64,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 37,
					sortable : true,
					align : 'center'
				} ],
				width : 260,
				height : 150
			});
		
			$(".motoristasGrid").flexigrid({
				dataType : 'json',
				colModel : [ {
					display : ' ',
					name : 'sel',
					width : 20,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nome',
					width : 80,
					sortable : true,
					align : 'left'
				}, {
					display : 'CNH',
					name : 'CNH',
					width : 55,
					sortable : true,
					align : 'left'
				}, {
					display : 'Ação',
					name : 'acao',
					width : 37,
					sortable : true,
					align : 'center'
				} ],
				width : 260,
				height : 150
			});
		
			$(".boxRotaGrid").flexigrid({
				dataType : 'json',
				colModel : [ {
					display : ' ',
					name : 'checks',
					width : 20,
					sortable : true,
					align : 'left'
				}, {
					display : 'Rota',
					name : 'rota',
					width : 91,
					sortable : true,
					align : 'left'
				}, {
					display : 'Roteiro',
					name : 'roteiro',
					width : 91,
					sortable : true,
					align : 'left'
				} ],
				width : 260,
				height : 150
			});
		});
		
		function pesquisarTransportadores(){
			
			var data = "filtro.razaoSocial=" + $("#razaoSocialPesquisa").val() + 
			           "&filtro.nomeFantasia=" + $("#nomeFantasiaPesquisa").val() +
			           "&filtro.cnpj=" + $("#cnpjPesquisa").val();
			
			$.postJSON("<c:url value='/cadastro/transportador/pesquisarTransportadores' />", data, 
				function(result) {
					
					if (result[0].tipoMensagem){
						exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
					}
					
					if (result[1] != ""){
						$(".transportadoraGrid").flexAddData({
							page: result[1].page, total: result[1].total, rows: result[1].rows
						});
						
						$(".transportadoraGrid").show();
					} else {
						$(".transportadoraGrid").hide();
					}
				}
			);
		}
	</script>
	<style>
		.diasFunc label,.finceiro label {
			vertical-align: super;
		}
		
		#tabs-4 .especialidades fieldset {
			width: 220px !important;
			margin-left: -16px;
			width: 258px !important;
		}
		
		#tabs-4 .bt_novos,#tabs-4 .bt_confirmar_novo {
			margin-left: -14px !important;
		}
		
		#dialog-incluir-veiculo fieldset,#dialog-incluir-motorista fieldset {
			width: 370px !important;
		}
		
		#dialog-incluir-veiculo,#dialog-incluir-motorista,#dialog-excluir-associacao{
			display: none;
		}
		
		.associacao {
			width: 818px !important;
			margin-left: -11px !important;
		}
	</style>
</head>

<body>

	<div id="dialog-excluir-associacao" title="Veículos / Motoristas / Rota / Roteiro">
		<p>Confirma a exclusão desta associação de Veículos / Motoristas / Rota / Roteiro?</p>
	</div>

	<div id="dialog-excluir" title="Excluir Transportador">
		<p>Confirma a exclusão deste Transportador</p>
	</div>

	<div id="dialog-incluir-motorista" title="Motoristas">
		<fieldset>
			<legend>Cadastrar Veículos</legend>
			<table width="350" cellpadding="2" cellspacing="2" style="text-align: left;">
				<tr>
					<td width="54">Nome:</td>
					<td width="280"><input type="text" style="width: 230px" /></td>
				</tr>
				<tr>
					<td>CNH:</td>
					<td><input type="text" style="width: 110px" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><span class="bt_add"><a href="javascript:;">Incluir Novo</a></span></td>
				</tr>
			</table>
		</fieldset>
	</div>

	<div id="dialog-incluir-veiculo" title="Veículos">
		<fieldset>
			<legend>Cadastrar Veículos</legend>
			<table width="350" cellpadding="2" cellspacing="2" style="text-align: left;">
				<tr>
					<td width="95">Tipo de Veículo:</td>
					<td width="239"><input type="text" style="width: 230px" /></td>
				</tr>
				<tr>
					<td>Placa:</td>
					<td><input type="text" style="width: 110px" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><span class="bt_add"><a href="javascript:;">Incluir Novo</a></span></td>
				</tr>
			</table>
		</fieldset>
	</div>


	<div id="dialog-novo" title="Novo Transportador">

		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Dados Cadastrais</a></li>
				<li><a href="#tabs-2">Endereços</a></li>
				<li><a href="#tabs-3">Telefones</a></li>
				<li><a href="#tabs-4">Veículos / Motoristas</a></li>
			</ul>
			<div id="tabs-1">
				<table width="730" cellpadding="2" cellspacing="2" style="text-align: left;">
					<tr>
						<td width="98">Razão Social:</td>
						<td width="248"><input type="text" style="width: 230px" /></td>
						<td width="113">Nome Fantasia:</td>
						<td width="243"><input type="text" style="width: 230px" /></td>
					</tr>
					<tr>
						<td>E-mail:</td>
						<td><input type="text" style="width: 230px" /></td>
						<td>Responsável:</td>
						<td><input type="text" style="width: 230px" /></td>
					</tr>
					<tr>
						<td>CNPJ:</td>
						<td><input type="text" style="width: 150px" /></td>
						<td>Insc. Estadual:</td>
						<td><input type="text" style="width: 150px" /></td>
					</tr>
				</table>
			</div>
			<div id="tabs-2">
				<jsp:include page="../endereco/index.jsp"></jsp:include>
			</div>
			<div id="tabs-3">
				<jsp:include page="../telefone/index.jsp"></jsp:include>
			</div>

			<div id="tabs-4">
				<table border="0" cellpadding="2" cellspacing="2">
					<tr class="especialidades">
						<td valign="top">
							<fieldset>
								<legend>Veículos</legend>
								<table class="veiculosGrid"></table>
							</fieldset>
						</td>
						<td valign="top">
							<fieldset>
								<legend>Motoristas</legend>
								<table class="motoristasGrid"></table>
							</fieldset>
						</td>
						<td valign="top">
							<fieldset>
								<legend>Rota / Roteiro</legend>
								<table class="boxRotaGrid"></table>
							</fieldset>
						</td>
					</tr>
					<tr class="especialidades">
						<td valign="top">
							<span class="bt_novos" title="Novo">
								<a href="javascript:;" onclick="popup_incluir_veiculo();">
									<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo Veículo
								</a>
							</span>
						</td>
						<td valign="top">
							<span class="bt_novos" title="Novo">
								<a href="javascript:;" onclick="popup_incluir_motorista();">
									<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo Motorista
								</a>
							</span>
						</td>
						<td valign="top">
							<span class="bt_confirmar_novo"	title="Confirmar">
								<a href="javascript:;">
									<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar Associação
								</a>
							</span>
						</td>
					</tr>
				</table>
				<br clear="all" />

				<fieldset class="associacao">
					<legend>Veículos / Motoristas / Rota / Roteiro</legend>
					<table class="associacaoGrid"></table>
				</fieldset>
				<br clear="all" />
			</div>
		</div>
	</div>
	
	<div class="corpo">
		<div class="container">

			<div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"></span> <b>Transportador < evento > com < status >.</b>
				</p>
			</div>

			<fieldset class="classFieldset">
				<legend> Pesquisar Transportador </legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
					<tr>
						<td width="85">Razão Social:</td>
						<td colspan="3"><input type="text" id="razaoSocialPesquisa" style="width: 190px;" maxlength="255" /></td>
						<td width="104">Nome Fantasia:</td>
						<td width="192"><input type="text" id="nomeFantasiaPesquisa" style="width: 190px;" maxlength="255" /></td>
						<td width="42">CNPJ:</td>
						<td width="179"><input type="text" id="cnpjPesquisa" style="width: 110px;" maxlength="255" /></td>
						<td width="109"><span class="bt_pesquisar">
							<a href="javascript:;" onclick="pesquisarTransportadores();">Pesquisar</a></span>
						</td>
					</tr>
				</table>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
			<fieldset class="classFieldset">
				<legend>Transportadores Cadastrados</legend>
				<div class="grids" style="display: none;">
					<table class="transportadoraGrid"></table>
				</div>

				<span class="bt_novos" title="Novo">
					<a href="javascript:;" onclick="popup_novo_transportador();">
						<img src="${pageContext.request.contextPath}/images/ico_salvar.gif"	hspace="5" border="0" />Novo
					</a>
				</span>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
		</div>
	</div>
</body>