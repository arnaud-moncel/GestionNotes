package com.note.gestion.carte;

import com.note.gestion.vat.Vat;

import java.util.List;

/**
 * Created by Arnaud Moncel on 28/02/2018.
 */

public class GroupList {
    private int m_groupParentId;

    private List<Group> m_groups;

    public GroupList( int groupParentId ) { m_groupParentId = groupParentId; }
    public GroupList( int groupParentId, List<Group> groups ) {
        m_groupParentId = groupParentId;
        m_groups = groups;
    }

    public void createGroup( String des, Vat vat ) { m_groups.add( new Group( des, vat, m_groupParentId ) ); }

    public List<Group> getGroups() { return m_groups; }
    public Group getGroup( int position ) { return m_groups.get( position ); }
}
