package com.uma.southdevelopers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class UserManagementApplication {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("userManagement");
        EntityManager em = emf.createEntityManager();

        em.close();
        emf.close();
    }
}