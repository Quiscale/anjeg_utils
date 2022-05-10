package net.anjeg.utils.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Application {

	/* ************************************************************************
	 * Methods
	 * ***********************************************************************/

	public String name();

}
