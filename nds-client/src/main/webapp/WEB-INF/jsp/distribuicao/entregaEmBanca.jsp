
 <div id="entregaBancaPj" style="display:none;">
  		<table width="399" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td width="153">Termo Adesão:</td>
    
    <!--  -->
    <td width="20"><input type="checkbox" name="checkbox15" id="checkbox15" onclick="mostraTermoPf();" /></td>
    <td width="216"><span class="bt_imprimir" style="display:block;"><a href="../termo_adesao.html" target="_blank">Termo</a></span></td>
  </tr>
  <tr>
    <td>Termo Adesão Recebido?</td>
    <td colspan="2"><input type="checkbox" name="checkbox13" id="checkbox13" /></td>
  </tr>
  <tr>
    <td>Arquivo:</td>
    <td colspan="2">
    
    
    
<form action="<c:url value='/cadastro/cota/uploadTermoAdesao' />" id="formUploadTermoAdesao"
		  method="post" enctype="multipart/form-data" >		
				
		<input type="hidden" id="numCotaUpload" name="numCota" value="1" />
		<input type="hidden" name="formUploadAjax" value="true" />
		<input name="uploadedFile" type="file" id="uploadedFile" size="40" />
</form>
    
    
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td colspan="2"><a href="javascript:;">nome_do_arquivo</a> <a href="javascript:;"><img src="../images/ico_excluir.gif" alt="Excluir arquivo" width="15" height="15" border="0" /></a></td>
  </tr>
  <tr>
    <td>Percentual Faturamento:</td>
    <td colspan="2"><input type="text" style="width:70px; text-align:right;" /></td>
  </tr>
  <tr>
    <td>Taxa Fixa R$</td>
    <td colspan="2"><input type="text" style="width:70px; text-align:right;" /></td>
  </tr>
  <tr>
    <td>Base de Cálculo:</td>
    <td colspan="2"><select name="select" id="select3" style="width:107px;">
    </select></td>
  </tr>
  <tr>
    <td>Período Carência:</td>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="27%"><input name="carenciaDe2" type="text" id="carenciaDe2" style="width:70px" /></td>
        <td width="6%">Até</td>
        <td width="34%"><input name="carenciaAte2" type="text" id="carenciaAte2" style="width:70px" /></td>
      </tr>
    </table></td>
  </tr>
</table>

</div>