package Controller;

import DAO.StaffDAOI;
import DAO.StaffDAOImpl;
import Model.Staff;
import Model.Poste;
import Model.Role;
import View.StaffView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffController {
    private final StaffView view;
    private final StaffDAOI dao;

    public StaffController(StaffView view) {
        this.view = view;
        this.dao = new StaffDAOImpl();

        // Écouteur pour le bouton Ajouter
        view.addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStaff();
            }
        });

        // Écouteur pour le bouton Lister
        view.listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listStaffs();
            }
        });

        // Écouteur pour le bouton Supprimer
        view.deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStaff();
            }
        });

        // Écouteur pour le bouton Modifier
        view.modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyStaff();
            }
        });
    }

    // Méthode pour ajouter un employé
    private void addStaff() {
        try {
            String nom = view.nameField.getText();
            String prenom = view.surnameField.getText();
            String email = view.emailField.getText();
            String phone = view.phoneField.getText();
            double salaire = Double.parseDouble(view.salaryField.getText());
            Role role = Role.valueOf(view.roleCombo.getSelectedItem().toString().toUpperCase());
            Poste poste = Poste.valueOf(view.posteCombo.getSelectedItem().toString().toUpperCase());

            Staff staff = new Staff(nom, prenom, email, phone, salaire, role, poste);
            dao.add(staff);
            JOptionPane.showMessageDialog(view, "Employé ajouté avec succès.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }

    // Méthode pour afficher la liste des employés
    private void listStaffs() {
        List<Staff> Staffs = dao.listAll();
        if (Staffs == null || Staffs.isEmpty()) {
            System.out.println("Aucune donnée trouvée.");}
        String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Salaire", "Rôle", "Poste"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Staff stf : Staffs) {
            Object[] row = {stf.getId(), stf.getNom(), stf.getPrenom(), stf.getEmail(), stf.getPhone(), stf.getSalaire(), stf.getRole(), stf.getPoste()};
            model.addRow(row);
        }
        view.staffTable.setModel(model);
    }

    // Méthode pour supprimer un employé
    private void deleteStaff() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(view, "Entrez l'ID de l'employé à supprimer :"));
            dao.delete(id);
            JOptionPane.showMessageDialog(view, "Employé supprimé avec succès.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
        }
    }

    // Méthode pour modifier un employé
private void modifyStaff() {
    try {
        // Vérifier si une ligne est sélectionnée dans le tableau
        int selectedRow = view.staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un employé dans le tableau.");
            return;
        }

        // Récupérer l'ID de l'employé sélectionné
        int id = (int) view.staffTable.getValueAt(selectedRow, 0);

        // Rechercher l'employé depuis la base de données
        Staff existingStaff = dao.findById(id);
        if (existingStaff == null) {
            JOptionPane.showMessageDialog(view, "Aucun employé trouvé avec cet ID.");
            return;
        }

        // Charger les informations dans les champs
        view.nameField.setText(existingStaff.getNom());
        view.surnameField.setText(existingStaff.getPrenom());
        view.emailField.setText(existingStaff.getEmail());
        view.phoneField.setText(existingStaff.getPhone());
        view.salaryField.setText(String.valueOf(existingStaff.getSalaire()));

        // Utiliser une méthode sécurisée pour l'énumération
        setComboSelection(view.roleCombo, existingStaff.getRole().name());
        setComboSelection(view.posteCombo, existingStaff.getPoste().name());

        // Demander confirmation de la mise à jour
        int confirmation = JOptionPane.showConfirmDialog(view, "Confirmez-vous la modification ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            // Mettre à jour les informations
            existingStaff.setNom(view.nameField.getText());
            existingStaff.setPrenom(view.surnameField.getText());
            existingStaff.setEmail(view.emailField.getText());
            existingStaff.setPhone(view.phoneField.getText());
            existingStaff.setSalaire(Double.parseDouble(view.salaryField.getText()));
            existingStaff.setRole(getRoleFromCombo(view.roleCombo));
            existingStaff.setPoste(getPosteFromCombo(view.posteCombo));

            // Sauvegarder dans la base de données
            dao.update(existingStaff, id);

            // Rafraîchir la table
            JOptionPane.showMessageDialog(view, "Employé mis à jour avec succès.");
            listStaffs();
        } else {
            JOptionPane.showMessageDialog(view, "Modification annulée.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(view, "Erreur: " + ex.getMessage());
    }
}

// Mapping pour Role
private Role getRoleFromCombo(JComboBox<String> comboBox) {
    Map<String, Role> roleMap = new HashMap<>();
    roleMap.put("Admin", Role.ADMIN);
    roleMap.put("Employé", Role.EMPLOYE);

    String selectedRole = (String) comboBox.getSelectedItem();
    return roleMap.getOrDefault(selectedRole, null);
}

// Mapping pour Poste
private Poste getPosteFromCombo(JComboBox<String> comboBox) {
    Map<String, Poste> posteMap = new HashMap<>();
    posteMap.put("Ingénieur Etude et Développement", Poste.INGENIEURE_ETUDE_ET_DEVELOPPEMENT);
    posteMap.put("Team Leader", Poste.TEAM_LEADER);
    posteMap.put("Pilote", Poste.PILOTE);

    String selectedPoste = (String) comboBox.getSelectedItem();
    return posteMap.getOrDefault(selectedPoste, null);
}

// Méthode pour sélectionner l'élément approprié dans le combo
private void setComboSelection(JComboBox<String> comboBox, String enumValue) {
    for (int i = 0; i < comboBox.getItemCount(); i++) {
        if (comboBox.getItemAt(i).equalsIgnoreCase(enumValue.replace("_", " "))) {
            comboBox.setSelectedIndex(i);
            return;
        }
    }
}

    
    
}
