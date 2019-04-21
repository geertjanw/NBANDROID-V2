/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.android.avd.manager.ui;

import com.android.repository.api.RemotePackage;
import com.android.repository.api.RepoManager;
import com.android.repository.impl.meta.RepositoryPackages;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.SdkVersionInfo;
import com.android.sdklib.repository.IdDisplay;
import com.android.sdklib.repository.targets.SystemImage;
import com.android.sdklib.repository.targets.SystemImageManager;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.nbandroid.netbeans.gradle.v2.sdk.AndroidSdk;
import org.nbandroid.netbeans.gradle.v2.sdk.manager.SdkManagerToolsChangeListener;
import org.nbandroid.netbeans.gradle.v2.sdk.manager.SdkManagerToolsRootNode;
import org.nbandroid.netbeans.gradle.v2.ui.IconProvider;
import org.netbeans.modules.android.avd.manager.AvdManager;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.WeakListeners;
import org.openide.windows.WindowManager;

public final class CreateAvdVisualPanel2 extends JPanel implements ListSelectionListener, MouseListener, SdkManagerToolsChangeListener {

    private final WizardDescriptor wiz;
    private final SystemImageManager systemImageManager;
    private final RepoManager repoManager;
    private ImageTableModel model;
    private SystemImageDescription selected;
    private final CreateAvdWizardPanel2 panel;
    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JMenuItem downloadMenu = new JMenuItem("Download");
    private final JMenuItem uninstallMenu = new JMenuItem("Remove");
    private final AndroidSdk androidSdk;

    /**
     * Creates new form CreateAvdVisualPanel2
     */
    public CreateAvdVisualPanel2(WizardDescriptor wiz, CreateAvdWizardPanel2 panel) {
        initComponents();
        this.panel = panel;
        this.wiz = wiz;
        systemImageManager = (SystemImageManager) wiz.getProperty(CreateAvdWizardIterator.IMAGE_MANAGER);
        repoManager = (RepoManager) wiz.getProperty(CreateAvdWizardIterator.REPO_MANAGER);
        androidSdk = (AndroidSdk) wiz.getProperty(CreateAvdWizardIterator.ANDROID_SDK);
        refreshPackages();
        table.getSelectionModel().addListSelectionListener(this);
        androidSdk.addSdkToolsChangeListener(WeakListeners.create(SdkManagerToolsChangeListener.class, (SdkManagerToolsChangeListener) this, androidSdk));
        initMenu();
    }

    @Override
    public String getName() {
        return "Select a system image";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshPackages() {
        List<SystemImageDescription> imagesDescriptions = new ArrayList<>();
        try {
            Field field = SystemImageManager.class.getDeclaredField("mPackageToImage");
            field.setAccessible(true);
            field.set(systemImageManager, null);
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
        Collection<SystemImage> images = systemImageManager.getImages();
        images.forEach((t) -> {
            imagesDescriptions.add(new SystemImageDescription(t));
        });
        RepositoryPackages packages = repoManager.getPackages();
        Set<RemotePackage> newPkgs = packages.getNewPkgs();
        newPkgs.stream().forEach((t) -> {
            if (SystemImageDescription.hasSystemImage(t)) {
                imagesDescriptions.add(new SystemImageDescription(t));
            }
        });
        Runnable runnable = () -> {
            selected = null;
            model = new ImageTableModel(imagesDescriptions);
            table.setModel(model);
        };
        WindowManager.getDefault().invokeWhenUIReady(runnable);

    }

    boolean valid() {
        return selected != null && !selected.isRemote();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = -1;
            if (table.getSelectedRow() > -1) {
                selectedRow = table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());
                selected = model.getImagesDescriptions().get(selectedRow);
            } else {
                selected = null;
            }
        }
        panel.refreshValid();
        downloadMenu.setEnabled(selected != null && selected.isRemote() && !selected.isDownloadInProgress());
        uninstallMenu.setEnabled(selected != null && !selected.isRemote());
    }

    private void initMenu() {
        table.addMouseListener(this);
        popupMenu.add(downloadMenu);
        downloadMenu.setAction(new AbstractAction("Download") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected != null) {
                    selected.setDownloadInProgress(true);
                    downloadMenu.setEnabled(false);
                    RemotePackage remotePackage = selected.getRemotePackage();
                    androidSdk.installPackage(remotePackage);
                }
            }
        });
        popupMenu.add(uninstallMenu);
        uninstallMenu.setAction(new AbstractAction("Remove") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected != null && !selected.isRemote()) {
                    if (systemImageManager.getImageMap().containsValue(selected.getSystemImage())) {
                        systemImageManager.getImageMap().forEach((t, u) -> {
                            if (selected.getSystemImage().equals(u)) {
                                androidSdk.uninstallPackage(t);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void packageListChanged(SdkManagerToolsRootNode sdkToolsRootNode) {
        AvdManager.RP.execute(() -> {
            refreshPackages();
        });
    }

    void storeSettings() {
        wiz.putProperty(CreateAvdWizardIterator.SYSTEM_IMAGE, selected);
    }

    void readSettings() {
        SystemImageDescription tmp = (SystemImageDescription) wiz.getProperty(CreateAvdWizardIterator.SYSTEM_IMAGE);
        table.clearSelection();
        if (tmp != null) {
            model.getImagesDescriptions().forEach((t) -> {
                if (tmp.equals(t)) {
                    table.getSelectionModel().setSelectionInterval(model.getImagesDescriptions().indexOf(t), model.getImagesDescriptions().indexOf(t));
                }
            });
        }
    }

    private static class ImageTableModel extends AbstractTableModel {

        private final List<SystemImageDescription> imagesDescriptions;

        public List<SystemImageDescription> getImagesDescriptions() {
            return imagesDescriptions;
        }

        public ImageTableModel(List<SystemImageDescription> imagesDescriptions) {
            this.imagesDescriptions = new ArrayList<>(imagesDescriptions);
        }

        @Override
        public int getRowCount() {
            return imagesDescriptions.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Release Name";
                case 1:
                    return "API Level";
                case 2:
                    return "ABI";
                case 3:
                    return "Target";
                case 4:
                    return "Installed";
                default:
                    return "Err";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 4:
                    return Icon.class;
                default:
                    return String.class;
            }
        }

        private static final Icon ICON_LOCAL = new ImageIcon(IconProvider.IMG_LOCAL);
        private static final Icon ICON_REMOTE = new ImageIcon(IconProvider.IMG_REMOTE);

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            SystemImageDescription description = imagesDescriptions.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return releaseDisplayName(description);
                case 1:
                    return "" + description.getVersion().getApiLevel();
                case 2:
                    return description.getAbiType();
                case 3:
                    IdDisplay tag = description.getTag();
                    String name = description.getName();
                    return String.format("%1$s%2$s", name, tag.equals(SystemImage.DEFAULT_TAG) ? ""
                            : String.format(" (%s)", tag.getDisplay()));
                case 4:
                    if (description.isRemote()) {
                        return ICON_REMOTE;
                    } else {
                        return ICON_LOCAL;
                    }
                default:
                    return "err";
            }
        }

    }

    public static String releaseDisplayName(SystemImageDescription systemImage) {
        AndroidVersion version = systemImage.getVersion();
        String codeName = version.isPreview() ? version.getCodename()
                : SdkVersionInfo.getCodeName(version.getApiLevel());
        if (codeName == null) {
            codeName = "API " + version.getApiLevel();
        }
        String maybeDeprecated = systemImage.obsolete()
                || version.getApiLevel() < SdkVersionInfo.LOWEST_ACTIVE_API ? " (Deprecated)" : "";
        return codeName + maybeDeprecated;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            Component comp = e.getComponent();
            if (comp instanceof JTable) {
                JTable table = (JTable) comp;
                int rowIndex = table.rowAtPoint(e.getPoint());
                table.setRowSelectionInterval(rowIndex, rowIndex);
            }
            popupMenu.show(comp, e.getX(), e.getY());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
