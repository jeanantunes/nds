<head>
	<script type="text/javascript" src="scripts/transportador.js"></script>
	<script language="javascript" type="text/javascript">
		$(function() {
			transportadorController.init();
		} );
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

	<form id="form-excluir-associacao">
	<div id="dialog-excluir-associacao" title="Veículos / Motoristas / Rota / Roteiro">
		<p>Confirma a exclusão desta associação de Veículos / Motoristas / Rota / Roteiro?</p>
	</div>
	</form>

	<form id="form-excluir-transportador">
	<div id="dialog-excluir" title="Excluir Transportador">
		<p>Confirma a exclusão deste Transportador</p>
	</div>
	</form>
	
	<form id="form-excluir-veiculo">
	<div id="dialog-excluir-veiculo" title="Excluir Veículo" style="display: none;">
		<p>Confirma a exclusão deste Veículo</p>
	</div>
	</form>
	
	<form id="form-excluir-motorista">
	<div id="dialog-excluir-motorista" title="Excluir Motorista" style="display: none;">
		<p>Confirma a exclusão deste Motorista</p>
	</div>
	</form>
	
	<form id="form-cancelar-cadastro-transportador">
	<div id="dialog-cancelar-cadastro-transportador" title="Transportadores" style="display: none;">
		<p>Dados não salvos serão perdidos. Confirma o cancelamento?</p>
	</div>
	</form>

	<form id="form-incluir-motorista">
	<div id="dialog-incluir-motorista" title="Motoristas">
		<fieldset>
			
			<jsp:include page="../messagesDialog.jsp">
		
				<jsp:param value="idModalCadastroMotorista" name="messageDialog"/>
	
			</jsp:include>
			
			<legend>Cadastrar Motorista</legend>
			<table width="350" cellpadding="2" cellspacing="2" style="text-align: left;">
				<tr>
					<td width="54">Nome:</td>
					<td width="280"><input type="text" style="width: 230px" id="nomeMotorista" maxlength="255" /></td>
				</tr>
				<tr>
					<td>CNH:</td>
					<td><input type="text" style="width: 110px" id="cnhMotorista" maxlength="255" /></td>
				</tr>
				<tr id="btnAddMotorista">
					<td>&nbsp;</td>
					<td><span class="bt_add"><a href="javascript:transportadorController.adicionarMotorista();">Incluir Novo</a></span></td>
				</tr>
			</table>
		</fieldset>
	</div>
	</form>

	<form id="form-incluir-veiculo">
	<div id="dialog-incluir-veiculo" title="Veículos">
		<fieldset>
			
			<jsp:include page="../messagesDialog.jsp">
		
				<jsp:param value="idModalCadastroVeiculo" name="messageDialog"/>
	
			</jsp:include>
			
			<legend>Cadastrar Veículos</legend>
			<table width="350" cellpadding="2" cellspacing="2" style="text-align: left;">
				<tr>
					<td width="95">Tipo de Veículo:</td>
					<td width="239"><input type="text" style="width: 230px" id="tipoVeiculo" maxlength="255" /></td>
				</tr>
				<tr>
					<td>Placa:</td>
					<td><input type="text" style="width: 110px" id="placa" maxlength="255" /></td>
				</tr>
				<tr id="btnAddVeiculo">
					<td>&nbsp;</td>
					<td><span class="bt_add"><a href="javascript:transportadorController.adicionarVeiculo();">Incluir Novo</a></span></td>
				</tr>
			</table>
		</fieldset>
	</div>
	</form>

	<form id="form-novo-transportador">
	<div id="dialog-novo" title="Novo Transportador">
	
		<jsp:include page="../messagesDialog.jsp">
		
			<jsp:param value="idModalCadastroTransportador" name="messageDialog"/>

		</jsp:include>
		
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Dados Cadastrais</a></li>
				<li><a href="#tabs-2" onclick="ENDERECO_TRANSPORTADOR.popularGridEnderecos();">Endereços</a></li>
				<li><a href="#tabs-3" onclick="TRANSPORTADOR.carregarTelefones();">Telefones</a></li>
				<li><a href="#tabs-4" onclick="transportadorController.carregarGrids();">Veículos / Motoristas</a></li>
			</ul>
			<div id="tabs-1">
				<table width="730" cellpadding="2" cellspacing="2" style="text-align: left;">
					<tr>
						<td width="98">Razão Social:</td>
						<td width="248"><input type="text" style="width: 230px" id="razaoSocial" maxlength="255" /></td>
						<td width="113">Nome Fantasia:</td>
						<td width="243"><input type="text" style="width: 230px" id="nomeFantasia" maxlength="255" /></td>
					</tr>
					<tr>
						<td>E-mail:</td>
						<td><input type="text" style="width: 230px" id="email" maxlength="255" /></td>
						<td>Responsável:</td>
						<td><input type="text" style="width: 230px" id="responsavel" maxlength="255" /></td>
					</tr>
					<tr>
						<td>CNPJ:</td>
						<td><input type="text" style="width: 150px" id="cnpj" onblur="transportadorController.buscarPessoaCNPJ(this.value);" /></td>
						<td>Insc. Estadual:</td>
						<td><input type="text" style="width: 150px" id="inscEstadual" maxlength="255" /></td>
					</tr>
				</table>
			</div>
			<div id="tabs-2">
				<jsp:include page="../endereco/index.jsp">
					<jsp:param value="ENDERECO_TRANSPORTADOR" name="telaEndereco"/>
				</jsp:include>
			</div>
			<div id="tabs-3">
				<jsp:include page="../telefone/index.jsp">
					<jsp:param value="TRANSPORTADOR" name="tela"/>
				</jsp:include>
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
								<a href="javascript:;" onclick="transportadorController.popup_incluir_veiculo();">
									<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo Veículo
								</a>
							</span>
						</td>
						<td valign="top">
							<span class="bt_novos" title="Novo">
								<a href="javascript:;" onclick="transportadorController.popup_incluir_motorista();">
									<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Novo Motorista
								</a>
							</span>
						</td>
						<td valign="top">
							<span class="bt_confirmar_novo"	title="Confirmar">
								<a href="javascript:;" onclick="transportadorController.confirmarAssociacao();">
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
	</form>
	
	<div class="corpo">
		<div class="container">

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
							<a href="javascript:;" onclick="transportadorController.pesquisarTransportadores();">Pesquisar</a></span>
						</td>
					</tr>
				</table>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
			<fieldset class="classFieldset">
				<legend>Transportadores Cadastrados</legend>
				<div class="divTransportadoraGrid" style="display: none;">
					<table class="transportadoraGrid"></table>
				</div>

				<span class="bt_novos" title="Novo">
					<a href="javascript:;" onclick="transportadorController.limparCamposCadastroTransportador();transportadorController.popup_novo_transportador();">
						<img src="${pageContext.request.contextPath}/images/ico_salvar.gif"	hspace="5" border="0" />Novo
					</a>
				</span>

			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
		</div>
	</div>
</body>