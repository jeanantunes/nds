
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


<fieldset style="left: 10px">

<legend>Características</legend>

<table border="0" cellspacing="1" cellpadding="1">
  <tr>
     <td colspan="2" class="complementar" colspan="2">
    	  <input name="balcaoCentral" type="checkbox" value="" id="balcaoCentral" />
	      <label for="balcaoCentral" >Balcão Central</label>
	      <br clear="all" />
	      <input name="temComputador" type="checkbox" value="" id="temComputador" />
	      <label for="temComputador" >Tem Computador?</label>
	      <br clear="all" />
	      <input name="possuiCartao" type="checkbox" value="" id="possuiCartao" />
	      <label for="possuiCartao" >Possui cartão de Crédito/Débito?</label>
	      <br clear="all" />
	      <input name="luminoso" type="checkbox" value="" id="luminoso" onclick="PDV.opcaoTextoLuminoso('#luminoso')" />
	      <label for="luminoso" >Luminoso</label>
	      <textarea name="textoLuminoso" cols="" rows="2" style="width:610px;" id="textoLuminoso" disabled="disabled"></textarea>
      </td>
  </tr>
</table>

</fieldset>

<fieldset style="left: 10px">

    <legend>Segmentação</legend>
 
    <table width="522" border="0" cellspacing="1" cellpadding="1">
    
	    <tr>
		    <td width="123">Tipo de Ponto:</td>
		    <td width="392">
			    <select name="selectdTipoPonto" id="selectdTipoPonto" style="width:232px;">
			      <option selected="selected">Selecione...</option>     
			      <c:forEach items="${listaTipoPontoPDV}" var="item">
				      <option value="${item.key}">${item.value}</option>	          
				  </c:forEach>
			    </select>
		    </td>
	    </tr>
    
	    <tr>
		    <td>Características:</td>
		    <td>
			    <select style="width:232px;" id="selectCaracteristica" name="selectCaracteristica">
			      <option selected="selected">Selecione...</option>
			       <c:forEach items="${listaCaracteristicaPDV}" var="item">
				      <option value="${item.key}">${item.value}</option>	          
				  </c:forEach>
			    </select>
		    </td>
	    </tr>
    
	    <tr>
		    <td>Área de Influência:</td>
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
 
