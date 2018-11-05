/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author DM3-2-22
 */
public class Pelikula
{
    private String izena;
    private String direktorea;
    private int urtea;
    private String genero;
    private String egoera;

    public Pelikula(String izena, String direktorea, String urtea, String genero, String egoera)
    {
        this.izena = izena;
        this.direktorea = direktorea;
        setUrte(urtea);
        this.genero = genero;
        this.egoera = egoera;
    }

    public String getIzena()
    {
        return izena;
    }
    public void setIzena(String izena)
    {
        this.izena = izena;
    }

    public String getDirek()
    {
        return direktorea;
    }
    public void setDirek(String direktorea)
    {
        this.direktorea = direktorea;
    }
    
    public String getUrte()
    {
        return String.valueOf(urtea);
    }
    public void setUrte(String urtea)
    {
        this.urtea = Integer.parseInt(urtea);
    }

    public String getGenero()
    {
        return genero;
    }
    public void setGenero(String genero)
    {
        this.genero = genero;
    }
    
    public String getEgoera()
    {
        return egoera;
    }
    public void setEgoera(String egoera)
    {
        this.egoera = egoera;
    }

}
