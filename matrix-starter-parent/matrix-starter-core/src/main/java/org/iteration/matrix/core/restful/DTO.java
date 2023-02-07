package org.iteration.matrix.core.restful;

import lombok.Data;
import org.iteration.matrix.core.convertor.Convertor;

import java.io.Serializable;

@Data
public class DTO implements Convertor, Serializable {
    private Long version;
}
