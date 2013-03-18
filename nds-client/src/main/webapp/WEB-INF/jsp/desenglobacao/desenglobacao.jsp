<head>

<script type="text/javascript" src="scripts/desenglobacao.js"></script>
<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
<script type="text/javascript">

	var	pesquisaCota = new PesquisaCota();

	$(function() {
		desenglobacaoController.init();
	});
	

</script>
<style type="text/css">
.ui-tabs .ui-tabs-panel {
	padding: 0.45em 0.4em !important;
}

#tbInsertCotaEnglobada th {
	text-align: left;
}

#tableCotasEnglobadas input[type="text"], input[type="password"], textarea {
	border: 1px solid #BBBBBB;
	box-shadow: 2px 2px 1px #EEEEEE inset;
	padding: 2px 4px;
}

#btnInserirCotaEnglobada {
	float: right;
	margin-right: 15px;
	margin-top: 10px;
}



</style>
</head>

<body>
	<div class="corpo">
		<br clear="all" /> <br />
		<div class="container">
			<fieldset class="classFieldset">
				<legend> Pesquisar Cota</legend>
				<form id="filtroPrincipal">
					<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
						<tr>
							<td width="32">
								Cota:
							</td>
							<td width="111">
								<input type="text" name="filtro.cotaDto.numeroCota" id="filtroPrincipalNumeroCota" style="width: 100px;" />
							</td>
							<td width="44">
								Nome:
							</td>
							<td width="270">
								<input type="text" name="filtro.cotaDto.nomePessoa" id="filtroPrincipalNomePessoa" style="width: 250px;" />
							</td>
							<td width="104">
								<span class="bt_pesquisar">
									<a href="javascript:;" id="pesquisaPorCota">Pesquisar</a>
								</span>
							</td>
						</tr>
					</table>
				</form>
			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="grids" style="display: none;">
				<fieldset class="classFieldset">
					<legend>Cotas Desenglobadas</legend>

					<table class="englobadosGrid"></table>

					<span class="bt_novos" title="Gerar Arquivo">
						<a href="${pageContext.request.contextPath}/distribuicao/desenglobacao/exportar?fileType=XLS">
							<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
								Arquivo
						</a>
					</span>
					<span class="bt_novos" title="Imprimir">
						<a href="${pageContext.request.contextPath}/distribuicao/desenglobacao/exportar?fileType=PDF">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
								Imprimir
						</a>
					</span>
					<span class="bt_novos" title="Novo">
						<a href="javascript:;" onclick="desenglobacaoController.popup();">
							<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />
								Novo
						</a>
					</span>
				</fieldset>
			</div>
			<div class="linha_separa_fields">&nbsp;</div>
		</div>
	</div>

	<div id="dialog-novo" title="Englobar">
		<form id="formInserirEnglobada">
			<fieldset style="float: left; width: 600px !important; margin-bottom: 10px;">
				<legend>Desenglobar de:</legend>
				<table width="100%" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td>
							Cota:
						</td>
						<td>
							<input type="text" name="desenglobaDTO[0].desenglobaNumeroCota" id="filtroDesenglobaNumeroCota" style="width: 60px;" />
						</td>
						<td>
							Nome:
						</td>
						<td>
							<input type="text" name="desenglobaDTO[0].desenglobaNomePessoa" id="filtroDesenglobaNomePessoa" style="width: 200px;" />
						</td>
						<td>
							<span class="classPesquisar">
								<a href="javascript:;">&nbsp;</a>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>
			
			<fieldset style="float: left; width: 600px !important; margin-top: 10px;">
				<legend>Cotas englobadas:</legend>
				<div>
					<table id="tbInsertCotaEnglobada" style="width: 100%;">
						<thead>
							<tr>
								<th>Cota:</th>
								<th>Nome:</th>
								<th>% Cota</th>
							</tr>	
						</thead>
						<tbody>
							<tr>
								<td><input type="text" style="width: 30px;" id="inserirEnglobadaNumeroCota"></td>
								<td><input type="text" style="width: 400px;" id="inserirEnglobadaNomePessoa"></td>
								<td><input type="text" style="width: 30px;" id="inserirEnglobadaPorcentagemCota"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</fieldset>
			<div>
				<button id="btnInserirCotaEnglobada">Inserir</button>
			</div>
			
			<fieldset style="float: left; width: 600px !important; margin-top: 10px;">
				<legend>Cotas englobadas:</legend>
				<div>
					<table id="tbInsertCotaEnglobada" style="width: 100%;">
						<thead>
							<tr>
								<th>Cota:</th>
								<th>Nome:</th>
								<th>% Cota</th>
							</tr>	
						</thead>
						<tbody id="tableCotasEnglobadas"></tbody>
					</table>
				</div>
			</fieldset>
		</form>
	</div>
</body>
