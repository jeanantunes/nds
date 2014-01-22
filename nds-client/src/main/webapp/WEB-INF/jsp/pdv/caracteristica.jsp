
<style>
	
.complementar label {
   margin-left: 5px;
   margin-right: 5px;
   vertical-align: super;
   float: left;
}

.complementar input {
	float: left;
}

</style>


<fieldset style="margin-left: 20px">

<legend>Caracter&iacute;sticas</legend>

<table border="0" cellspacing="1" cellpadding="1">
  <tr>
     <td colspan="2" class="complementar" colspan="2">
    	  <input name="balcaoCentral" type="checkbox" value="" id="balcaoCentral" />
	      <label for="balcaoCentral" >Balc&atilde;o Central</label>
	      <br clear="all" />
	      <input name="temComputador" type="checkbox" value="" id="temComputador" />
	      <label for="temComputador" >Tem Computador?</label>
	      <br clear="all" />
	      <input name="possuiCartao" type="checkbox" value="" id="possuiCartao" />
	      <label for="possuiCartao" >Possui cart&atilde;o de Cr&eacute;dito/D&eacute;bito?</label>
	      <br clear="all" />
	      <input name="luminoso" type="checkbox" value="" id="luminoso" onclick="PDV.opcaoTextoLuminoso('#luminoso')" />
	      <label for="luminoso" >Luminoso</label>
	      <textarea name="textoLuminoso" cols="" rows="2" style="width:610px;" id="textoLuminoso" disabled="disabled"></textarea>
      </td>
  </tr>
</table>

</fieldset>

<fieldset style="margin-left: 20px">

    <legend>Segmenta&ccedil;&atilde;o</legend>
 
    <table width="522" border="0" cellspacing="1" cellpadding="1">
    
	    <tr>
		    <td>Caracter&iacute;ticas:</td>
		    <td>
		     <select name="selectCaracteristica" id="selectCaracteristica" style="width:232px;">
			      <option selected="selected">Selecione...</option>     
			      <c:forEach items="${listaTipoPontoPDV}" var="item">
				      <option value="${item.key}">${item.value}</option>	          
				  </c:forEach>
			    </select>
			    
		    </td>
	    </tr>
    
	    <tr>
		    <td>&Aacute;rea de Influ&ecirc;ncia:</td>
		    <td>
			    <select name="selectAreainfluencia" id="selectAreainfluencia" style="width:232px;">
			      <option selected="selected">Selecione....</option>
			       <c:forEach items="${listaAreaInfluenciaPDV}" var="item">
			    	  <option value="${item.key}">${item.value}</option>	          
			  	   </c:forEach>
			    </select>
			</td>
	    </tr>
 
    </table>
 </fieldset>
