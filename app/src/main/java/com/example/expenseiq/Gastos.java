package com.example.expenseiq;

public class Gastos {
    private int id;
    private String descripcion;
    private double cantidad;
    private String categoria;
    private String fecha;

    public Gastos(int id, String descripcion, double cantidad, String categoria, String fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    public Gastos(String descripcion, double cantidad, String categoria, String fecha) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    // Getters y setters...
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
        }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;

    }
}