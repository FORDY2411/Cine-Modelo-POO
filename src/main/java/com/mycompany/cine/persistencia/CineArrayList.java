
package com.mycompany.cine.persistencia;

import com.mycompany.cine.modelo.Administrador;
import java.util.ArrayList;

public class CineArrayList implements Listable<Administrador> {

    private final ArrayList<Administrador> lista;

    public CineArrayList() {
        this.lista = new ArrayList<>();
    }

    @Override
    public boolean agregar(Administrador elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("El elemento no puede ser nulo.");
        }
        if (traer(String.valueOf(elemento.getId())) != null) {
            return false; // id duplicado
        }
        return lista.add(elemento);
    }

    @Override
    public boolean actualizar(Administrador elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException("El elemento no puede ser nulo.");
        }
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == elemento.getId()) {
                lista.set(i, elemento);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(String dato) {
        if (dato == null || dato.trim().isEmpty()) {
            throw new IllegalArgumentException("El dato a eliminar no puede estar vacío.");
        }
        int id;
        try {
            id = Integer.parseInt(dato.trim());
        } catch (NumberFormatException e) {
            return false;
        }
        return lista.removeIf(a -> a.getId() == id);
    }

    @Override
    public ArrayList<Administrador> listar() {
        return new ArrayList<>(lista);
    }

    @Override
    public ArrayList<Administrador> consultar(String dato) {
        if (dato == null || dato.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String filtro = dato.trim().toLowerCase();
        ArrayList<Administrador> resultado = new ArrayList<>();
        for (Administrador a : lista) {
            if (String.valueOf(a.getId()).equals(dato.trim())
                    || a.getNombre().toLowerCase().contains(filtro)
                    || a.getApellido().toLowerCase().contains(filtro)
                    || a.getEmail().toLowerCase().contains(filtro)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    @Override
    public Administrador traer(String dato) {
        if (dato == null || dato.trim().isEmpty()) {
            return null;
        }
        try {
            int id = Integer.parseInt(dato.trim());
            for (Administrador a : lista) {
                if (a.getId() == id) {
                    return a;
                }
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }
}
