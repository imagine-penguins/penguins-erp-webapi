package com.knackitsolutions.crm.imaginepenguins.dbservice.common.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GroupBySorter implements Comparator<Sortable> {
    private List<Comparator<Sortable>> comparators;

    public GroupBySorter(Comparator<Sortable>... comparators) {
        this.comparators = Arrays.asList(comparators);
    }

    public GroupBySorter(List<Comparator<Sortable>> comparators) {
        this.comparators = comparators;
    }

    @Override
    public int compare(Sortable o1, Sortable o2) {
        for (Comparator<Sortable> comparator : comparators) {
            int result = comparator.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
