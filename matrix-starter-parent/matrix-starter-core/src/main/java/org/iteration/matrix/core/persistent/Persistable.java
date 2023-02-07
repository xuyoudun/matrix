package org.iteration.matrix.core.persistent;

import java.io.Serializable;

public interface Persistable<ID> extends Serializable {
    public ID getId();
}
