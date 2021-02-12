package com.tst.shop.subscription.constant;

import java.util.stream.Stream;

public enum ProductAccessType {

    UNDEFINED(0),
    TRAIL(1),
    PREMIUM(2)
    ;

    Integer type;

    ProductAccessType(Integer type) {
        this.type = type;
    }

    public static String resolveAccessType(Integer typeToResolve){
        return Stream.of(values())
                .filter(productAccessType -> productAccessType.type.equals(typeToResolve))
                .map(Enum::name)
                .findFirst().orElse(UNDEFINED.name());
    }
}
