package com.note.gestion.vat;

import java.io.Serializable;

/**
 * Created by Arnaud Moncel on 28/02/2018.
 */

public class Vat implements Serializable {
    private String m_designation;
    private Double m_percent;

    public Vat( String designation, Double percent ) {
        m_designation = designation;
        m_percent = percent;
    }

    public String getDesignation() { return m_designation; }
    public Double getPercent() { return m_percent; }

    public void editVat( String des, Double percent ) {  m_designation = des; m_percent = percent; }
}
