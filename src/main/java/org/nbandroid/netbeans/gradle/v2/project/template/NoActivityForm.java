/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nbandroid.netbeans.gradle.v2.project.template;

import android.studio.imports.templates.Template;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 *
 * @author arsi
 */
public class NoActivityForm extends javax.swing.JPanel implements MouseListener, FocusListener, AndroidActivityTemplateProvider {

    /**
     * Creates new form ActivityForm
     */
    Border borderOn = BorderFactory.createLineBorder(Color.blue, 1);
    Border borderOff = BorderFactory.createLineBorder(Color.lightGray, 1);
    private final AndroidActivityTemplateHandler androidSettings;

    public NoActivityForm(AndroidActivityTemplateHandler androidSettings) {
        initComponents();
        this.androidSettings = androidSettings;
        addMouseListener(this);
        setBorder(null);
        setFocusable(true);
        setRequestFocusEnabled(true);
        addFocusListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(280, 290));

        title.setBackground(new java.awt.Color(255, 255, 255));
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(title, org.openide.util.NbBundle.getMessage(NoActivityForm.class, "NoActivityForm.title.text")); // NOI18N
        title.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(title)
                .addContainerGap(135, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent me) {
        requestFocusInWindow(true);
        setBorder(borderOn);
        androidSettings.setCurrentTemplate(null);
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void focusGained(FocusEvent fe) {
       
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(280, 290); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(280, 290); //To change body of generated methods, choose Tools | Templates.
    }





    @Override
    public void focusLost(FocusEvent fe) {
        setBorder(null);
    }

    @Override
    public void select(Template template) {
        if(Objects.equals(template, null)){
            mouseClicked(null);
        }
    }
}
