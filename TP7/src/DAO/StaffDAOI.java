package DAO;

import Model.Staff;
import java.util.List;

public interface StaffDAOI {
    void add(Staff staff); // Ajouter un employé
    void delete(int id); // Supprimer un employé
    List<Staff> listAll(); // Lister tous les employés
    Staff findById(int id); // Trouver un employé par ID
    void update(Staff staff, int id); // Mettre à jour un employé
}