package com.pbl.animals.models.inner;

import com.pbl.animals.models.base.ModelBase;

import java.util.List;

public class SpeciesLookup extends ModelBase {
    public String name;

    public List<BreedLookup> breeds;
}
