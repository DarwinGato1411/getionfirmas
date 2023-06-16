/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

/**
 *
 * @author gato
 */
public class MenuPublic extends SelectorComposer<Component> {


    public MenuPublic() {
       

    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
      
    }

    @Listen("onClick = #login")
    public void buttonConsultar() {
        Executions.sendRedirect("/inicio.zul");
    }

 

   
   
}
