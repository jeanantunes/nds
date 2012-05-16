
<script>

$(".sociosPjGrid").flexigrid({
	dataType : 'json',
	preProcess:SOCIO_COTA.processarResultadoConsultaSocios,
	colModel : [{
		display : 'Nome',
		name : 'nome',
		width : 380,
		sortable : false,
		align : 'left'
	},{
		display : 'Cargo',
		name : 'cargo',
		width : 190,
		sortable : false,
		align : 'left'
	}, {
		display : 'Principal',
		name : 'principalFlag',
		width : 70,
		sortable : false,
		align : 'center'
	}, {
		display : 'Ação',
		name : 'acao',
		width : 60,
		sortable : false,
		align : 'center'
	}],
	width : 770,
	height : 180
});

</script>

<div id="dialog-excluir-socio" title="Socios" style="display: none;">
	<p>Confirma esta Exclusão?</p>
</div>

<table width="300" cellspacing="2" cellpadding="2" border="0">
    <tbody><tr>
      <td>Nome:</td>
      <td><input type="text" id="idNomeSocio"></td>
    </tr>
    <tr>
      <td>Cargo:</td>
      <td><input type="text" id="idCargoSocio"></td>
    </tr>
    <tr>
      <td><label for="ePrincipal">Principal:</label></td>
      <td><input type="checkbox" id="idSocioPrincipal" value="" name="idSocioPrincipal"></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      
		<td>
			<span class="bt_add"><a href="javascript:SOCIO_COTA.incluirSocio();" id="btnAddSocio">Incluir Novo</a></span>
	
			<span class="bt_novos"><a href="javascript:SOCIO_COTA.incluirSocio();" id="btnEditarSocio" style="display:none;">
				<img src="/nds-client/images/ico_salvar.gif" hspace="5" border="0"> Salvar</a>
 			</span>
	
		</td>
     
      <td></td>
    </tr>
  </tbody>
</table>
<br>
<label><strong>Sócios Cadastrados</strong></label>
<br>
<table class="sociosPjGrid"></table>



