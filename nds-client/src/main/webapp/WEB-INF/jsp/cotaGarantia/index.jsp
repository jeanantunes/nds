<script language="text/javascript" type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/cotaGarantia.js"></script>
	
<script type="text/javascript"
	src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
	
<script language="text/javascript" type="text/javascript" 
	src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	
<script type="text/javascript">
	var tipoCotaGarantia;
	$(function() {
		tipoCotaGarantia = new TipoCotaGarantia();
	});
</script>
<table width="755" cellpadding="2" cellspacing="2"
	style="text-align: left;">
	<tr>
		<td width="112">Tipo de Garantia:</td>
		<td width="631"><select id="tipoGarantiaSelect"
			onchange="tipoCotaGarantia.changeController($(this).val());"
			style="width: 250px;">
				<option value="" selected="selected">Selecione...</option>
		</select></td>
	</tr>
</table>

<div id="cotaGarantiaNotaPromissoriaPanel" style="display: none;">
	<fieldset>
		<legend>Nota Promiss&oacute;ria</legend>

		<table width="755" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tr>
				<td width="128">N&#176; Nota Promiss&oacute;ria:</td>
				<td width="219"><span id="cotaGarantiaNotaPromissoriaId"></span></td>
				<td width="78">Vencimento:</td>
				<td width="302"><input type="text"
					name="cotaGarantiaNotaPromissoriaVencimento"
					id="cotaGarantiaNotaPromissoriaVencimento" style="width: 120px;" /></td>

			</tr>
			<tr>
				<td>Valor R&#36;</td>

				<td><input type="text" name="cotaGarantiaNotaPromissoriaValor"
					id="cotaGarantiaNotaPromissoriaValor"
					style="width: 120px; text-align: right;" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>por extenso:</td>
				<td colspan="3"><input type="text"
					name="cotaGarantiaNotaPromissoriavalorExtenso"
					id="cotaGarantiaNotaPromissoriavalorExtenso" style="width: 425px;" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="3"><span class="bt_novos" title="Imprimir"><a
						href="javascript:void(0);"
						id="cotaGarantiaNotaPromissoriaImprimir" target="_blank"><img
							src="${pageContext.request.contextPath}/images/ico_impressora.gif"
							alt="Imprimir" hspace="5" border="0" />Imprimir</a></span></td>
			</tr>
		</table>
	</fieldset>
</div>

<div id="cotaGarantiaFiadorPanel" style="display: none;">

	<fieldset>

		<legend>Fiador</legend>

		<table width="755" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tr>
				<td width="150"><strong>Nome / Raz&atilde;o Social:</strong></td>

				<td width="212"><input type="text" id="cotaGarantiaFiadorSearchName" /></td>

				<td width="92"><strong>CPF/CNPJ:</strong></td>

				<td width="273"><input type="text" id="cotaGarantiaFiadorSearchDoc" style="width: 200px;" /></td>
			</tr>

		</table>

	</fieldset>

	<br clear="all" /> <br />

	<fieldset id="cotaGarantiaFiadorDadosPanel" style="display: none;">
		<legend>Dados do Fiador</legend>
		<table width="755" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tr>
				<td width="154"><strong>Nome / Raz&atilde;o Social:</strong></td>

				<td width="272"><span id="cotaGarantiaFiadorNome"></span></td>

				<td width="87"><strong>CPF/CNPJ:</strong></td>

				<td width="214"><span id="cotaGarantiaFiadorDoc"></span</td>
			</tr>
			<tr>
				<td><strong>Endere&ccedil;o:</strong></td>
				<td colspan="3"><span id="cotaGarantiaFiadorEndereco"></span</td>
			</tr>
			<tr>
				<td><strong>Telefone:</strong></td>

				<td><span id="cotaGarantiaFiadorTelefone"></span</td>

				<td>&nbsp;</td>

				<td>&nbsp;</td>

			</tr>

		</table>

	</fieldset>
</div>

<div id="cotaGarantiaChequeCaucaoPanel" style="display: none;">
	<fieldset>
		<legend>Dados Banc&aacute;rios</legend>
		<table width="601" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td width="138">Num. Banco:</td>
				<td width="122"><input type="text"
					name="cotaGarantiaChequeCaucaoNumeroBanco"
					id="cotaGarantiaChequeCaucaoNumeroBanco" style="width: 60px;" /></td>

				<td width="82">Nome:</td>
				<td width="233"><input type="text"
					name="cotaGarantiaChequeCaucaoNomeBanco"
					id="cotaGarantiaChequeCaucaoNomeBanco" style="width: 200px;" /></td>
			</tr>

			<tr>
				<td>Ag&ecirc;ncia:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoAgencia"
					id="cotaGarantiaChequeCaucaoAgencia" style="width: 60px;" />- <input
					type="text" name="cotaGarantiaChequeCaucaoDvAgencia"
					id="cotaGarantiaChequeCaucaoDvAgencia" style="width: 30px;" /></td>

				<td>Conta:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoConta"
					id="cotaGarantiaChequeCaucaoConta" style="width: 60px;" />- <input
					type="text" name="cotaGarantiaChequeCaucaoDvConta"
					id="cotaGarantiaChequeCaucaoDvConta" style="width: 30px;" /></td>
			</tr>

			<tr>
				<td>N&ordm; Cheque:</td>
				<td><input type="text"
					name="cotaGarantiaChequeCaucaoNumeroCheque"
					id="cotaGarantiaChequeCaucaoNumeroCheque" style="width: 107px;" />
				</td>
				<td>Valor R$:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoValor"
					id="cotaGarantiaChequeCaucaoValor"
					style="width: 108px; text-align: right;" /></td>
			</tr>
			<tr>
				<td>Data do Cheque:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoEmissao"
					id="cotaGarantiaChequeCaucaoEmissao" style="width: 107px;" /></td>
				<td>Validade:</td>
				<td><input type="text" name="cotaGarantiaChequeCaucaoValidade"
					id="cotaGarantiaChequeCaucaoValidade"
					style="width: 108px; text-align: right;" /></td>
			</tr>
			<tr>
				<td>Nome Correntista:</td>
				<td colspan="3"><input type="text"
					name="cotaGarantiaChequeCaucaoCorrentista"
					id="cotaGarantiaChequeCaucaoCorrentista" style="width: 345px;" />
				</td>
			</tr>

			<tr>
				<td>Imagem Cheque:</td>
				<td colspan="3">
					<input name="fileField" type="file"
					id="fileField" size="58" />
				</td>
			</tr>
		</table>
	</fieldset>
	<br clear="all" /> <br />
	<fieldset>
		<legend>Foto Cheque</legend>
		<br />
		<div align="center">
			<img src="" />
		</div>
	</fieldset>
</div>

<div id="cotaGarantiaImovelPanel" style="display: none;">
	<fieldset>
		<legend>Im&oacute;vel</legend>
		<table width="755" border="0" cellpadding="2" cellspacing="2"
			style="text-align: left;">
			<tbody>
				<tr>
					<td width="106">Propriet&aacute;rio:</td>
					<td width="635"><input type="text"
						name="cotaGarantiaImovelProprietario"
						id="cotaGarantiaImovelProprietario" style="width: 250px;"></td>
				</tr>
				<tr>
					<td>Endere&ccedil;o:</td>
					<td><input type="text" name="cotaGarantiaImovelEndereco"
						id="cotaGarantiaImovelEndereco" style="width: 450px;"></td>
				</tr>
				<tr>
					<td>N&uacute;mero Registro:</td>
					<td><input type="text" name="cotaGarantiaImovelNumeroRegistro"
						id="cotaGarantiaImovelNumeroRegistro" style="width: 250px;"></td>
				</tr>
				<tr>
					<td>Valor R$:</td>
					<td><input type="text" name="cotaGarantiaImovelValor"
						id="cotaGarantiaImovelValor"
						style="width: 100px; text-align: right;"></td>
				</tr>
				<tr>
					<td>Observa&ccedil;&atilde;o:</td>
					<td><textarea name="cotaGarantiaImovelObservacao" rows="3"
							id="cotaGarantiaImovelObservacao" style="width: 450px;"></textarea></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><span class="bt_add"> <a href="javascript:;"
							id="cotaGarantiaImovelIncluirNovo">Incluir
								Novo</a>
					</span></td>

				</tr>
			</tbody>
		</table>
	</fieldset>
	<br clear="all"> <br>

	<fieldset>
		<legend>Im&oacute;veis Cadastradas</legend>
		<div class="flexigrid" style="width: 740px;">
			<table class="cotaGarantiaImovelGrid"></table>
		</div>
	</fieldset>
</div>

<br clear="all" />