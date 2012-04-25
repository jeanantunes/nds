

<fieldset>
<legend>Características</legend>
<table width="777" border="0" cellspacing="1" cellpadding="1">
  <tr>

    <td colspan="2" class="complementar" colspan="2">
    	  <input name="pontoPrincipal" type="checkbox" value="" id="ptoPrincipal" />
	      <label for="ptoPrincipal">Ponto Principal</label>
	      <br clear="all" />
	      <input name="balcaoCentral" type="checkbox" value="" id="balcaoCentral" />
	      <label for="balcaoCentral">Balcão Central</label>
	      <br clear="all" />
	      <input name="temComputador" type="checkbox" value="" id="temComputador" />
	      <label for="temComputador">Tem Computador?</label>
	      <br clear="all" />
	      <input name="luminoso" type="checkbox" value="" id="luminoso" onclick="PDV.opcaoTextoLuminoso('#luminoso')" />
	      <label for="luminoso">Luminoso</label>
	      <textarea name="textoLuminoso" cols="" rows="2" style="width:610px;" id="textoLuminoso" disabled="disabled"></textarea>
      </td>
  </tr>
</table>
</fieldset>

<fieldset>
 <legend>Segmentação</legend>
    <table width="522" border="0" cellspacing="1" cellpadding="1">
  <tr>
    <td width="123">Tipo de Ponto:</td>
    <td width="392">
    <select name="selectdTipoPonto" id="selectdTipoPonto" style="width:232px;">
      <option selected="selected">Selecione...</option>
      <option value="1">Convencional</option>
      <option value="2">Alternativo</option>
    </select></td>
    </tr>
    
    <tr>
	    <td>Características:</td>
	    <td>
		    <select style="width:232px;" id="selectCaracteristica" name="selectCaracteristica">
		      <option selected="selected">Selecione...</option>
		      <option value="1">Convencional</option>
		      <option value="2">Alternativo</option>
		    </select>
	    </td>
  </tr>
    
  <tr>
    <td>Área de Influência:</td>
    <td>
	    <select name="selectAreainfluencia" id="selectAreainfluencia" style="width:232px;">
	      <option selected="selected">Selecione....</option>
	      <option value="1">Residencial</option>
	      <option value="2">Escritórios / Indústrias</option>
	      <option value="3">Comercial</option>
	      <option value="4">Estradas</option>
	    </select></td>
    </tr>
  <tr>
    <td>Cluster:</td>
    <td>
	    <select name="selectCluster" id="selectCluster" style="width:232px;">
	      <option selected="selected">Selecione...</option>
	      <option value="1">Popular</option>
	    </select></td>
    </tr>
    </table>
 </fieldset>

