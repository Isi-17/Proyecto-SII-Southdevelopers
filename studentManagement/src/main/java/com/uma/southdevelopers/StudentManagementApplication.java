package com.uma.southdevelopers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class StudentManagementApplication {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentManagement");
        EntityManager em = emf.createEntityManager();

        em.close();
        emf.close();
    }
}