package DAO;

import Model.Staff;
import Model.Poste;
import Model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAOImpl implements StaffDAOI {

    @Override
    public void add(Staff staff) {
        String sql = "INSERT INTO Staff (nom, prenom, email, phone, salaire, role, poste) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staff.getNom());
            stmt.setString(2, staff.getPrenom());
            stmt.setString(3, staff.getEmail());
            stmt.setString(4, staff.getPhone());
            stmt.setDouble(5, staff.getSalaire());
            stmt.setString(6, staff.getRole().name());
            stmt.setString(7, staff.getPoste().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Staff WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
public List<Staff> listAll() {
    List<Staff> staffs = new ArrayList<>();
    String sql = "SELECT * FROM Staff";
    try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String roleStr = rs.getString("role").toUpperCase();
            String posteStr = rs.getString("poste").toUpperCase();

            Role role = null;
            Poste poste = null;
            try {
                role = Role.valueOf(roleStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Role non valide : " + roleStr);
                role = Role.EMPLOYE; // Valeur par défaut
            }

            try {
                poste = Poste.valueOf(posteStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Poste non valide : " + posteStr);
                poste = Poste.INGENIEURE_ETUDE_ET_DEVELOPPEMENT; // Valeur par défaut
            }

            Staff staff = new Staff(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getDouble("salaire"),
                    role,
                    poste
            );
            staff.setId(rs.getInt("id"));
            staffs.add(staff);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return staffs;
}

    



    @Override
    public Staff findById(int id) {
        String sql = "SELECT * FROM Staff WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Staff(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDouble("salaire"),
                        Role.valueOf(rs.getString("role")),
                        Poste.valueOf(rs.getString("poste"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Staff staff, int id) {
        String sql = "UPDATE Staff SET nom = ?, prenom = ?, email = ?, phone = ?, salaire = ?, role = ?, poste = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staff.getNom());
            stmt.setString(2, staff.getPrenom());
            stmt.setString(3, staff.getEmail());
            stmt.setString(4, staff.getPhone());
            stmt.setDouble(5, staff.getSalaire());
            stmt.setString(6, staff.getRole().name()); // Envoi du rôle en tant que chaîne (avec la méthode .name())
            stmt.setString(7, staff.getPoste().name()); // Idem pour le poste
            stmt.setInt(8, id); // L'ID de l'employé à mettre à jour
            
            int rowsUpdated = stmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("L'employé a été mis à jour avec succès.");
            } else {
                System.out.println("Aucun employé trouvé avec cet ID.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}