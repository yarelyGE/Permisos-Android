package com.orbita.innovacion.permisos;

public class PojoContactos {
    private String Nombre;
    private String Numero;

    public PojoContactos(String nombre, String numero) {
        Nombre = nombre;
        Numero = numero;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    @Override
    public String toString() {
        return "Nombre: " + Nombre + "\n" + "Numero: " + Numero;
    }

}
