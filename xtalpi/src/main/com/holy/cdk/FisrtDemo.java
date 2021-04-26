package com.holy.cdk;

import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

public class FisrtDemo {
    public static void main(String[] args) {
        try {
            SmilesParser   sp  = new SmilesParser(SilentChemObjectBuilder.getInstance());
            IAtomContainer m   = sp.parseSmiles("c1ccccc1");
            System.out.println(m.getTitle());
        } catch (InvalidSmilesException e) {
            System.err.println(e.getMessage());
        }
    }
}
