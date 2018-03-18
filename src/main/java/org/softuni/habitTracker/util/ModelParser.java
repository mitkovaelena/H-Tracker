package org.softuni.habitTracker.util;

import org.modelmapper.ModelMapper;

public class ModelParser {

    private static ModelMapper MODEL_MAPPER = null;

    private ModelParser() {
    }

    public static ModelMapper getInstance() {
        if (MODEL_MAPPER == null) {
            MODEL_MAPPER = new ModelMapper();
        }
        return MODEL_MAPPER;
    }
}
