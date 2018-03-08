package com.note.gestion.table;

import java.io.Serializable;

/**
 * Created by Arnaud Moncel on 19/02/2018.
 */

public class Table implements Serializable {
    private String m_id;

    public Table( String num ) {
        m_id = num;
    }

    public String getId() { return m_id; }
}
