<head>

<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/scriptCadastroCalendario.js"></script>

<style type="text/css">

	label {
		vertical-align: super;
	}
	
	.ui-datepicker-inline {
		width: 950px !important;
	}
	
	.ui-datepicker-group {
		margin-left: 4px;
	}
	
	.ui-state-default {
		font-size: 13px !important;
	}
	
	.ui-datepicker-group {
		margin: 0px !important;
		padding: 5px !important;
	}

    td.highlight { 
    		border: none !important;
    		padding: 1px 0 1px 1px !important;
    		background: none !important;
    		overflow:hidden;
    }
    
	td.highlight a {
			background: #99dd73 url(${pageContext.request.contextPath}/images/highlightDate.png) 50% 50% repeat-x !important;  
			border: 1px #88a276 solid !important;
	}



</style>

</head>

<body>

	<input type="hidden" id="alternate" />

	<div id="dialog-excluir" title="Excluir Feriado">
		<p>Confirma a exclusão deste Feriado?</p>
	</div>


	<div id="dialog-editar" title="Editar Feriado">
		<fieldset style="width: 380px;">
			<legend>Dados do Feriado</legend>

			<table width="365" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="114">Data:</td>
					<td width="240"><input type="text" name="dtFeriado1"
						id="dtFeriado1" style="width: 110px;" />
					</td>
				</tr>
				<tr>
					<td>Tipo:</td>
					<td><select name="select" id="select" style="width: 150px;">
							<option selected="selected">Selecione...</option>
							<option>Estadual</option>
							<option>Municipal</option>
							<option>Nacional</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>Cidade:</td>
					<td><select name="select2" id="select2" style="width: 150px;">
							<option selected="selected">Selecione...</option>
							<option>São Paulo</option>
							<option>Rio de Janeiro</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>Descrição:</td>
					<td><input type="text" name="textfield33" id="textfield34"
						style="width: 230px;" />
					</td>
				</tr>
				<tr>
					<td>Opera?</td>
					<td><input name="opera" type="checkbox" value="" id="opera" />
					</td>
				</tr>
				<tr>
					<td>Efetua Cobrança?</td>
					<td><input name="opera2" type="checkbox" value="" id="opera2" />
					</td>
				</tr>
				<tr>
					<td>Repete Anualmente?</td>
					<td><input name="opera2" type="checkbox" value="" id="opera2" />
					</td>
				</tr>
			</table>
		</fieldset>
	</div>




	<div id="dialog-novo" title="Incluir Novo Feriado">

		<fieldset style="width: 650px;">
			<legend>Dados do Feriado</legend>

			<table width="365" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="114">Data:</td>
					<td width="240"><input type="text" name="dtFeriado"
						id="dtFeriado" style="width: 110px;" />
					</td>
				</tr>
				<tr>
					<td>Tipo:</td>
					<td><select name="tipoFeriado" id="tipoFeriado"
						style="width: 180px;">
							<option selected="selected"></option>
							<option value="FEDERAL">Federal</option>
							<option value="ESTADUAL">Estadual</option>
							<option value="MUNICIPAL">Municipal</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>Cidade:</td>
					<td><select name="select2" id="select2" style="width: 150px;">
							<option selected="selected">Selecione...</option>
							<option>São Paulo</option>
							<option>Rio de Janeiro</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>Descrição:</td>
					<td><input type="text" name="textfield33" id="descricao"
						style="width: 230px;" />
					</td>
				</tr>
				<tr>
					<td>Opera?</td>
					<td><input name="indOpera" type="checkbox" value=""
						id="indOpera" />
					</td>
				</tr>
				<tr>
					<td>Efetua Cobrança?</td>
					<td><input name="indEfetuaCobranca" type="checkbox" value=""
						id="indEfetuaCobranca" />
					</td>
				</tr>
				<tr>
					<td>Repete Anualmente?</td>
					<td><input name="indRepeteAnualmente" type="checkbox"
						value="" id="indRepeteAnualmente" />
					</td>
				</tr>
			</table>
		</fieldset>
		<fieldset style="width: 650px; margin-top: 10px;">
			<legend>Dados dos Feriados</legend>

			<table class="diaFeriadoGrid"></table>
		</fieldset>

	</div>

	<div class="corpo">
		<br clear="all" /> <br />

		<div class="container">

			<div id="effect" style="padding: 0 .7em; z-index: 1;"
				class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"></span> <b>Feriado < evento > com
						< status >.</b>
				</p>
			</div>

			<fieldset class="classFieldset">
				<legend> Calendário de Feriados</legend>

				<span class="bt_novos" title="Novo"><a href="javascript:;"
					onclick="CadastroCalendario.popup_bt();"><img src="../images/ico_salvar.gif"
						hspace="5" border="0" />Novo</a>
				</span> <span class="bt_novos" title="Imprimir Feriados"><a
					href="imprimir_feriados.htm" target="_blank"><img
						src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir
						Feriados</a>
				</span> <br clear="all" /> <br />


				<div id="feriados"></div>



				<div class="linha_separa_fields">&nbsp;</div>
				<div class="linha_separa_fields">&nbsp;</div>


				<span class="bt_novos" title="Novo"><a href="javascript:;"
					onclick="CadastroCalendario.popup_bt();"><img src="../images/ico_salvar.gif"
						hspace="5" border="0" />Novo</a>
				</span> <span class="bt_novos" title="Imprimir Feriados"><a
					href="imprimir_feriados.htm" target="_blank"><img
						src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir
						Feriados</a>
				</span>
				<div class="linha_separa_fields">&nbsp;</div>


			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="linha_separa_fields">&nbsp;</div>

		</div>


	</div>

</body>
