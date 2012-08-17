<div id="tabOperacao">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td valign="top">   
                    <fieldset style="width:440px!important; margin-bottom:5px; float:left;">
                    <legend>Dias de Operação:</legend>
                    <table width="441" border="0" cellpadding="0" cellspacing="1">
                      <tr class="header_table">
                        <td>Fornecedor</td>
                        <td align="center">Lançamento</td>
                        <td align="center">Recolhimento</td>
                      </tr>
                      <tr class="class_linha_1">
                        <td width="141">
                          <select name="selectFornecedoresLancamento[]" size="5" multiple="multiple" id="selectFornecedoresLancamento" style="width:130px; height:100px">
                              <c:forEach items="${fornecedores}" var="fornecedor">
                                <option value="${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</option>
                              </c:forEach>
                          </select>
                        </td>
                        <td width="157" align="center">
                          <select name="selectDiasLancamento[]" size="5" multiple="multiple" id="selectDiasLancamento" style="width:130px; height:100px">
                            <option value="1">Domingo</option>
                            <option value="2">Segunda-feira</option>
                            <option value="3">Terça-feira</option>
                            <option value="4">Quarta-feira</option>
                            <option value="5">Quinta-feira</option>
                            <option value="6">Sexta-feira</option>
                            <option value="7">Sábado</option>
                          </select>
                        </td>
                        <td width="139" align="center">
                          <select name="selectDiasRecolhimento[]" size="5" multiple="multiple" id="selectDiasRecolhimento" style="width:130px; height:100px">
                            <option value="1">Domingo</option>
                            <option value="2">Segunda-feira</option>
                            <option value="3">Terça-feira</option>
                            <option value="4">Quarta-feira</option>
                            <option value="5">Quinta-feira</option>
                            <option value="6">Sexta-feira</option>
                            <option value="7">Sábado</option>
                          </select>
                        </td>
                      </tr>
                      <tr>
                        <td>&nbsp;</td>
                        <td align="center">&nbsp;</td>
                        <td width="139" align="center"><span class="bt_add"><a href="javascript:;" onclick="parametrosDistribuidorController.gravarDiasDistribuidorFornecedor()" >Incluir Novo</a></span></td>
                      </tr>
                    </table>
                    <br />
                    <span id="spanDiasDistribuidorFornecedor">
                      <table width="441" border="0" cellpadding="0" cellspacing="1">
                        <tr class="header_table">
                          <td>Fornecedor</td>
                          <td align="center">Lançamento</td>
                          <td align="center">Recolhimento</td>
                          <td align="center">&nbsp;</td>
                        </tr>
                        <c:forEach items="${listaDiaOperacaoFornecedor}" var="registroDiaOperacaoFornecedor">
                          <tr class="class_linha_1">
                            <td width="139">${registroDiaOperacaoFornecedor.fornecedor.juridica.nomeFantasia}</td>
                            <td width="144" align="center">${registroDiaOperacaoFornecedor.diasLancamento}</td>
                            <td width="125" align="center">${registroDiaOperacaoFornecedor.diasRecolhimento}</td>
                            <td width="28" align="center"><a href="javascript:;" onclick="parametrosDistribuidorController.excluirDiasDistribuidorFornecedor(${registroDiaOperacaoFornecedor.fornecedor.id})" ><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" width="15" height="15" alt="Excluir" /></a></td>
                          </tr>
                          </c:forEach>
                      </table>
                    </span>
                  </fieldset> 
                  <fieldset style="width:440px!important; margin-bottom:5px;">
                    <legend>Parciais / Matriz de Lançamento</legend>
                    <label>Relançamento de Parciais em D+: </label>
                    <select name="parametrosDistribuidor.relancamentoParciaisEmDias" size="1" id="relancamentoParciaisEmDias" style="width:50px; height:19px;">
                      <option value="2" selected="selected">2</option>
                      <option value="3">3</option>
                      <option value="4">4</option>
                      <option value="5">5</option>
                    </select>
                  </fieldset>
                   <fieldset style="width:440px!important; margin-bottom:5px;">
                  <legend>Reutilização de Código de Cota</legend>
                  <table width="390" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                      <td width="222" align="left">Reutilização de Código de Cota Inativa:</td>
                      <td width="40" align="center"><input name="parametrosDistribuidor.reutilizacaoCodigoCotaInativa" value="${parametrosDistribuidor.reutilizacaoCodigoCotaInativa}" type="text" id="reutilizacaoCodigoCotaInativa" style="width:40px; text-align:center;" value="06" /></td>
                      <td width="124" align="left"> &nbsp;( meses )</td>
                    </tr>
                  </table>
                </fieldset>
                  </td>
                  <td style="width:10px;">&nbsp;</td>
                  <td valign="top"> 
                <fieldset style="width:420px!important; margin-bottom:5px;">
                      <legend>Recolhimento</legend>
                  <table width="398" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="197">&nbsp;</td>
                      <td colspan="11">&nbsp;</td>
                    </tr>
                   
                         <tr>
                            <td>Na CE, deseja utilizar:</td>
                            <td>
                             <c:if test="${parametrosDistribuidor.tipoContabilizacaoCE eq 'VALOR'}">
                <input type="radio" name="parametrosDistribuidor.tipoContabilizacaoCE" 
                     id="radioTipoContabilizacaoCEValor" 
                     checked="checked"
                                       value="VALOR" />
              </c:if>   
              <c:if test="${empty parametrosDistribuidor.tipoContabilizacaoCE or 
                (not (parametrosDistribuidor.tipoContabilizacaoCE eq 'VALOR'))}">
                <input type="radio" name="parametrosDistribuidor.tipoContabilizacaoCE" 
                     id="radioTipoContabilizacaoCEValor" 
                                       value="VALOR" />
              </c:if>                 
                            </td>
                            <td colspan="3">Valor</td>
                            <td>
                <c:if test="${parametrosDistribuidor.tipoContabilizacaoCE eq 'EXEMPLARES'}">
                <input type="radio" name="parametrosDistribuidor.tipoContabilizacaoCE" 
                                     id="radioTipoContabilizacaoCEExemplares" 
                   checked="checked"  
                                     value="EXEMPLARES" />
                </c:if>
                <c:if test="${empty parametrosDistribuidor.tipoContabilizacaoCE or 
                  (not (parametrosDistribuidor.tipoContabilizacaoCE eq 'EXEMPLARES'))}">
                  <input type="radio" name="parametrosDistribuidor.tipoContabilizacaoCE" 
                                     id="radioTipoContabilizacaoCEExamplares" 
                               value="EXEMPLARES" />
                </c:if>
               </td>
                            <td colspan="6">Exemplares</td>
                          </tr>
                    
                         <tr>
                      <td>Aceita Encalhe Juramentada:</td>
                      <td width="22">
                          <c:if test="${parametrosDistribuidor.aceitaEncalheJuramentada}">
                            <input name="parametrosDistribuidor.aceitaEncalheJuramentada" 
                                type="checkbox" 
                                id="aceitaEncalheJuramentada" checked="checked" />
                          </c:if>   
                            <c:if test="${not parametrosDistribuidor.aceitaEncalheJuramentada}">
                            <input name="parametrosDistribuidor.aceitaEncalheJuramentada" 
                                type="checkbox" 
                                id="aceitaEncalheJuramentada"/>
                          </c:if>        
                      </td>
                      <td width="15">&nbsp;</td>
                      <td width="21">&nbsp;</td>
                      <td width="16">&nbsp;</td>
                      <td width="20">&nbsp;</td>
                      <td width="16">&nbsp;</td>
                      <td width="21">&nbsp;</td>
                      <td width="13">&nbsp;</td>
                      <td width="20">&nbsp;</td>
                      <td width="15">&nbsp;</td>
                      <td width="22">&nbsp;</td>
                    </tr>
                    <tr>
                      <td>Dias de Recolhimento:</td>
                      <td>
                          <c:if test="${parametrosDistribuidor.diaRecolhimentoPrimeiro}">
                              <input name="parametrosDistribuidor.diaRecolhimentoPrimeiro" 
                                 type="checkbox" 
                                 id="diaRecolhimentoPrimeiro" checked="checked" />
                          </c:if>
                          <c:if test="${not parametrosDistribuidor.diaRecolhimentoPrimeiro}">
                              <input name="parametrosDistribuidor.diaRecolhimentoPrimeiro" 
                                 type="checkbox" 
                                 id="diaRecolhimentoPrimeiro"/>
                          </c:if>           
                          
                      </td>
                      <td>1º</td>
                      <td>
                         <c:if test="${parametrosDistribuidor.diaRecolhimentoSegundo}">   
                           <input name="parametrosDistribuidor.diaRecolhimentoSegundo" 
                                  type="checkbox" id="diaRecolhimentoSegundo" checked="checked" />
                         </c:if>
                         <c:if test="${not parametrosDistribuidor.diaRecolhimentoSegundo}">   
                           <input name="parametrosDistribuidor.diaRecolhimentoSegundo" 
                                  type="checkbox" id="diaRecolhimentoSegundo"/>
                         </c:if>  
                      </td>
                      <td>2º</td>
                      <td>
                         <c:if test="${parametrosDistribuidor.diaRecolhimentoTerceiro}">   
                          <input name="parametrosDistribuidor.diaRecolhimentoTerceiro" 
                                  type="checkbox" id="diaRecolhimentoTerceiro" checked="checked" />
                         </c:if>
                         <c:if test="${not parametrosDistribuidor.diaRecolhimentoTerceiro}">   
                          <input name="parametrosDistribuidor.diaRecolhimentoTerceiro" 
                                  type="checkbox" id="diaRecolhimentoTerceiro" />
                         </c:if>                
                      </td>
                      <td>3º</td>
                      <td>
                          <c:if test="${parametrosDistribuidor.diaRecolhimentoQuarto}">      
                            <input name="parametrosDistribuidor.diaRecolhimentoQuarto" 
                                    type="checkbox" id="diaRecolhimentoQuarto" checked="checked" />
                           </c:if>
                           <c:if test="${not parametrosDistribuidor.diaRecolhimentoQuarto}">      
                            <input name="parametrosDistribuidor.diaRecolhimentoQuarto" 
                                    type="checkbox" id="diaRecolhimentoQuarto"/>
                           </c:if>           
                      </td>
                      <td>4º</td>
                      <td>
                        <c:if test="${parametrosDistribuidor.diaRecolhimentoQuinto}"> 
                          <input name="parametrosDistribuidor.diaRecolhimentoQuinto" 
                                  type="checkbox" id="diaRecolhimentoQuinto" checked="checked"/>
                        </c:if>
                        <c:if test="${not parametrosDistribuidor.diaRecolhimentoQuinto}"> 
                          <input name="parametrosDistribuidor.diaRecolhimentoQuinto" 
                                  type="checkbox" id="diaRecolhimentoQuinto"/>
                        </c:if>               
                      </td>
                      <td>5º</td>
                      <td>Dias</td>
                    </tr>
                    <tr>
                      <td>Aceita devolução antecipada cota:</td>
                      <td>
                        <c:if test="${parametrosDistribuidor.limiteCEProximaSemana}">
                            <input name="parametrosDistribuidor.limiteCEProximaSemana" 
                               type="checkbox" id="limiteCEProximaSemana" checked="checked" />
                        </c:if>
                        <c:if test="${not parametrosDistribuidor.limiteCEProximaSemana}">
                            <input name="parametrosDistribuidor.limiteCEProximaSemana" 
                               type="checkbox" id="limiteCEProximaSemana"/>
                        </c:if>           
                      </td>
                      <td colspan="10">Limite CE Próxima Semana</td>
                    </tr>
                          <tr>
                            <td colspan="11">Em casos de Venda Negativa, solicita a senha de aprovação do Supervisor?</td>
                            <td>
                              <c:if test="${parametrosDistribuidor.supervisionaVendaNegativa}">
                                <input name="parametrosDistribuidor.supervisionaVendaNegativa" id="supervisionaVendaNegativa" type="checkbox" checked="checked" />
                              </c:if>
                              <c:if test="${empty parametrosDistribuidor.supervisionaVendaNegativa or (not parametrosDistribuidor.supervisionaVendaNegativa)}">
                                <input name="parametrosDistribuidor.supervisionaVendaNegativa" id="supervisionaVendaNegativa" type="checkbox"/>
                              </c:if>
                            </td>
                          </tr>
                    <tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                  </table>
                  <table width="390" border="0" cellspacing="1" cellpadding="0">
                    <tr class="header_table">
                      <td align="center">&nbsp;</td>
                      <td align="center">Recebimento</td>
                      <td align="center">Encalhe</td>
                    </tr>
                    <tr>
                      <td width="123" align="center" class="class_linha_1">Conferência Cega</td>
                      <td width="115" align="center">
                            <c:if test="${parametrosDistribuidor.conferenciaCegaRecebimento}">
                                <input name="parametrosDistribuidor.conferenciaCegaRecebimento" 
                                  type="checkbox" id="conferenciaCegaRecebimento" checked="checked" />
                            </c:if>
                            <c:if test="${not parametrosDistribuidor.conferenciaCegaRecebimento}">
                                <input name="parametrosDistribuidor.conferenciaCegaRecebimento" 
                                  type="checkbox" id="conferenciaCegaRecebimento"/>
                            </c:if>         
                      </td>
                      <td width="148" align="center">
                            <c:if test="${parametrosDistribuidor.conferenciaCegaEncalhe}">
                                <input name="parametrosDistribuidor.conferenciaCegaEncalhe" 
                                   type="checkbox" id="conferenciaCegaEncalhe" checked="checked" />
                            </c:if>
                            <c:if test="${not parametrosDistribuidor.conferenciaCegaEncalhe}">
                                <input name="parametrosDistribuidor.conferenciaCegaEncalhe" 
                                   type="checkbox" id="conferenciaCegaEncalhe" />
                            </c:if>           
                      </td>
                    </tr>
                  </table>
                </fieldset>
                <fieldset style="width:420px!important; margin-bottom:5px;">
                          <legend>Chamadão</legend>
                          <table width="387" border="0" cellspacing="1" cellpadding="0">
                            <tr>
                              <td width="221" align="right">Avisar quando a Cota permanecer por &nbsp;</td>
                              <td width="70"><input name="parametrosDistribuidor.chamadaoDiasSuspensao" 
                                type="text" id="chamadaoDiasSuspensao" style="width:20px; text-align:center;" value="${parametrosDistribuidor.chamadaoDiasSuspensao}" /></td>
                              <td width="92" align="left">dias suspensos</td>
                            </tr>
                            <tr>
                              <td align="right"> Ou atingir R$&nbsp; </td>
                              <td><input name="parametrosDistribuidor.chamadaoValorConsignado" 
                                type="text" id="chamadaoValorConsignado" style="width:50px; text-align:right;" value="${parametrosDistribuidor.chamadaoValorConsignado}" /></td>
                              <td align="left">de consignado</td>
                            </tr>
                         </table>
                      </fieldset>
                      <fieldset style="width:420px!important; margin-bottom:5px;">
                    <legend>Capacidade de Manuseio </legend>
                    <table width="390" border="0" cellspacing="1" cellpadding="0">
                      <tr class="header_table">
                        <td align="center">&nbsp;</td>
                        <td align="center">Lançamento</td>
                        <td align="center">Recolhimento</td>
                      </tr>
                      <tr>
                        <td width="123" align="center" class="class_linha_1">Exes. Homem/ Hora</td>
                        <td width="115" align="center"><input type="text" name="parametrosDistribuidor.capacidadeManuseioHomemHoraLancamento" id="capacidadeManuseioHomemHoraLancamento" value="${parametrosDistribuidor.capacidadeManuseioHomemHoraLancamento}" style="width:40px; text-align:center;" /></td>
                        <td width="148" align="center"><input type="text" name="parametrosDistribuidor.capacidadeManuseioHomemHoraRecolhimento" id="capacidadeManuseioHomemHoraRecolhimento" value="${parametrosDistribuidor.capacidadeManuseioHomemHoraRecolhimento}" style="width:40px; text-align:center;" /></td>
                      </tr>
                    </table>
                  </fieldset>     
                  </td>
                  </tr>
                 </table> 
      </div>