package com.miketheshadow.complexmmostats.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CMMOCommand {

    String command();

}
