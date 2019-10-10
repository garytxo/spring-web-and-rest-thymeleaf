package com.murray.communications.security.jwtaudit;


import java.lang.annotation.*;

/**
 * This annotation can be used in the FS Controllers. It prefixes a method parameter indicating
 * that it's  preceding object contains user credentials which are parsed from the request attribute
 * by a Spring Mthod Resolver. <br/>
 * A sample of how this is declared:
 * <pre>
 * <code>
 *   public SmsDto save(
 *     @RequestBody SmsDto dto ,
 *     @Credentials AuditingCredentials auditingCredentials)
 * </code>
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Credentials {

    boolean required() default true;
}