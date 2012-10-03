<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/geracaoNFe.js"></script>
<style type="text/css">
  fieldset label {width: auto; margin-bottom: 0px!important;
}
#dialogCotasSuspensas fieldset{width:570px!important;}
  </style>

<script type="text/javascript">
var geracaoNFeController;
$(function(){
	geracaoNFeController = $.extend(true, new GeracaoNFeController(),  BaseController);
	
});
</script>
<form>
<fieldset class="classFieldset">
	<legend> Geração de Arquivos aos Jornaleiros</legend>
	<table width="950" border="0" cellpadding="2" cellspacing="1"
		class="filtro">
		<tr>
			<td width="99">Tipo de Arquivo:</td>
			<td width="206"><select name="select" id="select"
				style="width: 150px;" onchange="opcaoGerarArq(this.value);">
					<option value="">Selecione...</option>
					<option value="1">Reparte</option>
					<option value="2" selected="selected">Encalhe</option>
			</select></td>
			<td width="105">
				<div id="dtLancto" style="display: none;">Data Lançamento:</div>
				<div id="dtRecolhimento">Data Recolhimento:</div>
			</td>
			<td width="386"><div id="dtEscolha">
					<input type="text" name="datepickerDe" id="datepickerDe"
						style="width: 70px;" />
				</div></td>
			<td width="128"><span class="bt_confirmar_novo"
				title="Confirmar"><a href="javascript:;" onclick="mostrar();"><img
						border="0" hspace="5" src="../images/ico_check.gif">Gerar
						Arquivos</a></span></td>
		</tr>
	</table>

</fieldset>
</form>

<div class="linha_separa_fields">&nbsp;</div>

<fieldset class="grids classFieldset"
	style="text-align: center !important;">
	<legend>Arquivos Gerados</legend>
	<h2>Quantidade de Arquivos Gerados: 99999</h2>

</fieldset>
