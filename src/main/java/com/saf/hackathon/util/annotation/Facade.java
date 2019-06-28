package com.saf.hackathon.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

/**
 * This annotation will be used to create a Facade Component, it is
 * like @Service and @Component
 *
 * @author alagat
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface Facade {

	/**
	 * The value may indicate a suggestion for a logical component name, to be
	 * turned into a Spring bean in case of an autodetected component.
	 *
	 * @return the suggested component name, if any
	 */
	String value() default "";

}
