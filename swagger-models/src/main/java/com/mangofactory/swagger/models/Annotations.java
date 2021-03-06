package com.mangofactory.swagger.models;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class Annotations {
  /**
   * Finds first annotation of the given type on the given bean property and returns it.
   * Search precedence is getter, setter, field.
   *
   * @param beanPropertyDefinition introspected jackson proprty defintion
   * @param annotationClass        class object representing desired annotation
   * @param <A>                    type that extends Annotation
   * @return first annotation found for property
   */
  public static <A extends Annotation> A findPropertyAnnotation(BeanPropertyDefinition beanPropertyDefinition,
      Class<A> annotationClass) {

      return tryGetGetterAnnotation(beanPropertyDefinition, annotationClass)
              .or(tryGetSetterAnnotation(beanPropertyDefinition, annotationClass))
              .or(tryGetFieldAnnotation(beanPropertyDefinition, annotationClass))
              .orNull();
  }
  @SuppressWarnings("PMD")
  private static <A extends Annotation> Optional<A> tryGetGetterAnnotation(
          BeanPropertyDefinition  beanPropertyDefinition,
          Class<A> annotationClass) {

    try {
      if (beanPropertyDefinition.hasGetter()) {
        return Optional.fromNullable(beanPropertyDefinition.getGetter().getAnnotation(annotationClass));
      }
    } catch (IllegalArgumentException ignored) {
    }
    return Optional.absent();
  }
  @SuppressWarnings("PMD")
  private static <A extends Annotation> Optional<A> tryGetSetterAnnotation(
          BeanPropertyDefinition beanPropertyDefinition, Class<A> annotationClass) {
    try {
      if (beanPropertyDefinition.hasSetter()) {
        return Optional.fromNullable(beanPropertyDefinition.getSetter().getAnnotation(annotationClass));
      }
    } catch (IllegalArgumentException ignored) {
    }
    return Optional.absent();
  }
  @SuppressWarnings("PMD")
  private static <A extends Annotation> Optional<A> tryGetFieldAnnotation(
          BeanPropertyDefinition beanPropertyDefinition, Class<A> annotationClass) {
    try {
        if (beanPropertyDefinition.hasField()){
          return Optional.fromNullable(beanPropertyDefinition.getField().getAnnotation(annotationClass));
        }
    } catch (IllegalArgumentException ignored) {
    }
    return Optional.absent();
  }

  public static boolean memberIsUnwrapped(AnnotatedMember member) {
    return Optional.fromNullable(member.getAnnotation(JsonUnwrapped.class)).isPresent();
  }

  public static Optional<ApiModelProperty> findApiModePropertyAnnotation(AnnotatedElement annotated) {
    return Optional.fromNullable(AnnotationUtils.getAnnotation(annotated, ApiModelProperty.class));
  }

  public static Optional<ApiParam> findApiParamAnnotation(AnnotatedElement annotated) {
    return Optional.fromNullable(AnnotationUtils.getAnnotation(annotated, ApiParam.class));
  }

  public static Optional<ApiResponses> findApiResponsesAnnotations(AnnotatedElement annotated) {
    return Optional.fromNullable(AnnotationUtils.getAnnotation(annotated, ApiResponses.class));
  }
}
