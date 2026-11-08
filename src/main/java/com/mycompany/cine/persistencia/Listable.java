/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/License/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cine.persistencia;

import java.util.ArrayList;

/**
 * Contrato de persistencia en memoria para entidades del sistema de cine.
 *
 * @param <T> tipo de entidad que gestiona la lista
 * @author Fordiis
 */
public interface Listable<T> {

    boolean agregar(T elemento);
    boolean actualizar(T elemento);
    boolean eliminar(String dato);
    ArrayList<T> listar();
    ArrayList<T> consultar(String dato);
    T traer(String dato);

}
