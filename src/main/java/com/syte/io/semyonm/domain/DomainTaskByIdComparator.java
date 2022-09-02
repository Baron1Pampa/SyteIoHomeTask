package com.syte.io.semyonm.domain;

import java.util.Comparator;

public class DomainTaskByIdComparator implements Comparator<DomainTask> {

    @Override
    public int compare(DomainTask o1, DomainTask o2) {
        return o1.getId().compareTo(o2.getId());
    }
}