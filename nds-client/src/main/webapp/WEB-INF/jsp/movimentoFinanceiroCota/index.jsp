<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/movimentoFinanceiroCota.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/data.holder.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

	<script type="text/javascript">
	
		$(function(){
		
			movimentoFinanceiroCotaController.init();
		});
		
	</script>
	
	<style>

		#dialog-confirma-processamento{display:none;}
        
	</style>
	
    </head>

<body>

	<div class="areaBts">
	
		<div class="area" style="display:none">
		
            <div id="botoesDividasPagas">
                <span class="bt_novos">
	             	<a onclick="movimentoFinanceiroCotaController.confirmarBaixa();"
	             	   href="javascript:;" rel="tipsy" title="Confirmar">
	             	   	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">
	             	</a>
                </span>
             	<span class="bt_novos">
	             	<a onclick="movimentoFinanceiroCotaController.cancelarBaixa();"
	             	    href="javascript:;" rel="tipsy" title="Cancelar">
	             	   	<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_bloquear.gif">
	             	</a>
            	</span>
            </div>

		</div>
	</div>

	<div class="linha_separa_fields">&nbsp;</div>

	<fieldset class="fieldFiltro">
		
		<legend>Processamento Financeiro</legend>
		
		<!-- BAIXA MANUAL -->
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro" id="tableBaixaManual">
            <tr>
				<td width="20">Cota:</td>
                
                <td>
	                <input name="filtroNumCota" 
	             	       id="filtroNumCota" 
	             		   type="text"
	             		   maxlength="11"
	             		   style="width:60px; 
	             		   float:left; 
	             		   margin-right:5px;"
	             		   onchange="pesquisaCotaBaixaFinanceira.pesquisarPorNumeroCota('#filtroNumCota', '#descricaoCota');" />
				</td>
				
				<td width="30">Nome:</td>
             	
             	<td width="150">
		        	<input name="descricaoCota" 
		      		 	   id="descricaoCota" 
		      		 	   type="text"
		      		 	   class="nome_jornaleiro" 
		      		 	   maxlength="255"
		      		 	   style="width:130px;"
		      		 	   onkeyup="pesquisaCotaBaixaFinanceira.autoCompletarPorNome('#descricaoCota');" 
		      		 	   onblur="pesquisaCotaBaixaFinanceira.pesquisarPorNomeCota('#filtroNumCota', '#descricaoCota');" />
		        </td>
				
				<td width="30">
				    <span class="bt_pesquisar">
				        <a href="javascript:;" onclick="movimentoFinanceiroCotaController.buscaCota();"></a>
				    </span>
				</td>
				
			</tr>
        </table>
        
        <!--  -->
        
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<form name="formularioListaCotas" id="formularioListaCotas">


	    <input type="hidden" id="saldoDividaHidden" />
	 
	
		<fieldset class="classFieldset" id="extratoBaixaManual" >
		
	      	<legend>Cotas</legend>
	      	
	        <br />
	        
	        <div id="dialog-confirma-processamento" title="Processamento Financeiro">
			    <p>Deseja confirmar processamento financeiro para a Cota ?</p>
		    </div>
			
	      	<div  id="cotasAVista">
	      	
		       <table class="liberaDividaGrid" id="tabelaDividas"></table>
		    
		       <table width="100%" border="0" cellspacing="2" cellpadding="2">
		            <tr>
		            
		                <td width="20%">

		                </td>
						
						<td width="30%">   

		                </td>
		                
		                <td width="14%">
		                    <strong>Total Selecionado R$:</strong>
		                </td>
		                <td width="7%" id="totalDividasSelecionadas"></td>
		                
		                <td width="7%">
		                    <strong>Total R$:</strong>
		                </td>
		                <td width="7%" id="totalDividas"></td>
		                
		                <td width="20%">
		                
		                    <span class="checar">
		                        
		                        <label for="textoSelTodos" id="textoSelTodos">
		                            Marcar Todos
		                        </label>
		                        
		                        <input isEdicao="true" title="Selecionar todas as Dívidas" type="checkbox" id="selTodos" name="selTodos" onclick="baixaFinanceiraController.selecionarTodos(this.checked);" style="float:left;"/>
		                    </span>

		                </td>
		            </tr>
		        </table>
	        </div>

	    </fieldset>
    
    </form>
    
    <!--  -->
    
</body>