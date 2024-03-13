/* $This file is distributed under the terms of the license in LICENSE$ */

package edu.cornell.mannlib.vitro.webapp.dynapi.components;

public interface Removable {

    /*
     * Recursive remove all descendant nodes references
     */
    public void dereference();

}
