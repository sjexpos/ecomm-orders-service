/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package org.springframework.data.web;

import io.oigres.ecomm.service.orders.model.PageableRequest;
import io.oigres.ecomm.service.orders.model.PageableRequestImpl;
import io.oigres.ecomm.service.orders.model.SortRequest;
import java.lang.reflect.Method;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String INVALID_DEFAULT_PAGE_SIZE =
      "Invalid default page size configured for method %s; Must not be less than one";

  private static final String DEFAULT_PAGE_PARAMETER = "page";
  private static final String DEFAULT_SIZE_PARAMETER = "size";
  private static final String DEFAULT_PREFIX = "";
  private static final String DEFAULT_QUALIFIER_DELIMITER = "_";
  private static final int DEFAULT_MAX_PAGE_SIZE = 2000;
  static final PageableRequest DEFAULT_PAGE_REQUEST = PageableRequestImpl.of(0, 20);

  private PageableRequest fallbackPageable = DEFAULT_PAGE_REQUEST;
  private String pageParameterName = DEFAULT_PAGE_PARAMETER;
  private String sizeParameterName = DEFAULT_SIZE_PARAMETER;
  private String prefix = DEFAULT_PREFIX;
  private String qualifierDelimiter = DEFAULT_QUALIFIER_DELIMITER;
  private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;
  private boolean oneIndexedParameters = false;

  private static final SortRequestHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER =
      new SortRequestHandlerMethodArgumentResolver();
  private SortRequestHandlerMethodArgumentResolver sortResolver;

  /**
   * Constructs an instance of this resolved with a default {@link SortHandlerMethodArgumentResolver}.
   */
  public PageableRequestHandlerMethodArgumentResolver() {
    this(null);
  }

  /**
   * Constructs an instance of this resolver with the specified {@link SortArgumentResolver}.
   *
   * @param sortResolver the sort resolver to use
   * @since 1.13
   */
  public PageableRequestHandlerMethodArgumentResolver(
      @Nullable SortRequestHandlerMethodArgumentResolver sortResolver) {
    this.sortResolver = sortResolver == null ? DEFAULT_SORT_RESOLVER : sortResolver;
  }

  /**
   * Configures the {@link PageableRequest} to be used as fallback in case no {@link PageableDefault} can be found at the
   * method parameter to be resolved.
   * <p>
   * If you set this to {@literal PageableRequest#unpaged()}, be aware that your controller methods will get an {@code unpaged}
   * instance handed into them in case no {@link PageableRequest} data can be found in the request.
   *
   * @param fallbackPageable the {@link Pageable} to be used as general fallback.
   */
  public void setFallbackPageable(PageableRequest fallbackPageable) {

    Assert.notNull(fallbackPageable, "Fallback Pageable must not be null");

    this.fallbackPageable = fallbackPageable;
  }

  /**
   * Returns whether the given {@link PageableRequest} is the fallback one.
   *
   * @param pageable can be {@literal null}.
   * @return
   */
  public boolean isFallbackPageable(PageableRequest pageable) {
    return fallbackPageable.equals(pageable);
  }

  /**
   * Configures the maximum page size to be accepted. This allows to put an upper boundary of the page size to prevent
   * potential attacks trying to issue an {@link OutOfMemoryError}. Defaults to {@link #DEFAULT_MAX_PAGE_SIZE}.
   *
   * @param maxPageSize the maxPageSize to set
   */
  public void setMaxPageSize(int maxPageSize) {
    this.maxPageSize = maxPageSize;
  }

  /**
   * Retrieves the maximum page size to be accepted. This allows to put an upper boundary of the page size to prevent
   * potential attacks trying to issue an {@link OutOfMemoryError}. Defaults to {@link #DEFAULT_MAX_PAGE_SIZE}.
   *
   * @return the maximum page size allowed.
   */
  protected int getMaxPageSize() {
    return this.maxPageSize;
  }

  /**
   * Configures the parameter name to be used to find the page number in the request. Defaults to {@code page}.
   *
   * @param pageParameterName the parameter name to be used, must not be {@literal null} or empty.
   */
  public void setPageParameterName(String pageParameterName) {

    Assert.hasText(pageParameterName, "Page parameter name must not be null or empty");
    this.pageParameterName = pageParameterName;
  }

  /**
   * Retrieves the parameter name to be used to find the page number in the request. Defaults to {@code page}.
   *
   * @return the parameter name to be used, never {@literal null} or empty.
   */
  protected String getPageParameterName() {
    return this.pageParameterName;
  }

  /**
   * Configures the parameter name to be used to find the page size in the request. Defaults to {@code size}.
   *
   * @param sizeParameterName the parameter name to be used, must not be {@literal null} or empty.
   */
  public void setSizeParameterName(String sizeParameterName) {

    Assert.hasText(sizeParameterName, "Size parameter name must not be null or empty");
    this.sizeParameterName = sizeParameterName;
  }

  /**
   * Retrieves the parameter name to be used to find the page size in the request. Defaults to {@code size}.
   *
   * @return the parameter name to be used, never {@literal null} or empty.
   */
  protected String getSizeParameterName() {
    return this.sizeParameterName;
  }

  /**
   * Configures a general prefix to be prepended to the page number and page size parameters. Useful to namespace the
   * property names used in case they are clashing with ones used by your application. By default, no prefix is used.
   *
   * @param prefix the prefix to be used or {@literal null} to reset to the default.
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix == null ? DEFAULT_PREFIX : prefix;
  }

  /**
   * The delimiter to be used between the qualifier and the actual page number and size properties. Defaults to
   * {@code _}. So a qualifier of {@code foo} will result in a page number parameter of {@code foo_page}.
   *
   * @param qualifierDelimiter the delimiter to be used or {@literal null} to reset to the default.
   */
  public void setQualifierDelimiter(String qualifierDelimiter) {
    this.qualifierDelimiter =
        qualifierDelimiter == null ? DEFAULT_QUALIFIER_DELIMITER : qualifierDelimiter;
  }

  /**
   * Configures whether to expose and assume 1-based page number indexes in the request parameters. Defaults to
   * {@literal false}, meaning a page number of 0 in the request equals the first page. If this is set to
   * {@literal true}, a page number of 1 in the request will be considered the first page.
   *
   * @param oneIndexedParameters the oneIndexedParameters to set
   */
  public void setOneIndexedParameters(boolean oneIndexedParameters) {
    this.oneIndexedParameters = oneIndexedParameters;
  }

  /**
   * Indicates whether to expose and assume 1-based page number indexes in the request parameters. Defaults to
   * {@literal false}, meaning a page number of 0 in the request equals the first page. If this is set to
   * {@literal true}, a page number of 1 in the request will be considered the first page.
   *
   * @return whether to assume 1-based page number indexes in the request parameters.
   */
  protected boolean isOneIndexedParameters() {
    return this.oneIndexedParameters;
  }

  protected PageableRequest getPageable(
      MethodParameter methodParameter,
      @Nullable String pageString,
      @Nullable String pageSizeString) {
    SpringDataAnnotationUtils.assertPageableUniqueness(methodParameter);

    Optional<PageableRequest> defaultOrFallback =
        getDefaultFromAnnotationOrFallback(methodParameter).toOptional();

    Optional<Integer> page = parseAndApplyBoundaries(pageString, Integer.MAX_VALUE, true);
    Optional<Integer> pageSize = parseAndApplyBoundaries(pageSizeString, maxPageSize, false);

    if (!(page.isPresent() && pageSize.isPresent()) && !defaultOrFallback.isPresent()) {
      return PageableRequest.unpaged();
    }

    int p =
        page.orElseGet(
            () ->
                defaultOrFallback
                    .map(PageableRequest::getPageNumber)
                    .orElseThrow(IllegalStateException::new));
    int ps =
        pageSize.orElseGet(
            () ->
                defaultOrFallback
                    .map(PageableRequest::getPageSize)
                    .orElseThrow(IllegalStateException::new));

    // Limit lower bound
    ps =
        ps < 1
            ? defaultOrFallback
                .map(PageableRequest::getPageSize)
                .orElseThrow(IllegalStateException::new)
            : ps;
    // Limit upper bound
    ps = ps > maxPageSize ? maxPageSize : ps;

    return PageableRequestImpl.of(
        p, ps, defaultOrFallback.map(PageableRequest::getSort).orElseGet(SortRequest::unsorted));
  }

  /**
   * Returns the name of the request parameter to find the {@link Pageable} information in. Inspects the given
   * {@link MethodParameter} for {@link Qualifier} present and prefixes the given source parameter name with it.
   *
   * @param source the basic parameter name.
   * @param parameter the {@link MethodParameter} potentially qualified.
   * @return the name of the request parameter.
   */
  protected String getParameterNameToUse(String source, @Nullable MethodParameter parameter) {

    StringBuilder builder = new StringBuilder(prefix);

    String value = SpringDataAnnotationUtils.getQualifier(parameter);

    if (StringUtils.hasLength(value)) {
      builder.append(value);
      builder.append(qualifierDelimiter);
    }

    return builder.append(source).toString();
  }

  private PageableRequest getDefaultFromAnnotationOrFallback(MethodParameter methodParameter) {

    MergedAnnotation<PageableDefault> defaults =
        MergedAnnotations.from(methodParameter.getParameterAnnotations())
            .get(PageableDefault.class);

    if (defaults.isPresent()) {
      return getDefaultPageRequestFrom(methodParameter, defaults);
    }

    return fallbackPageable;
  }

  private static PageableRequest getDefaultPageRequestFrom(
      MethodParameter parameter, MergedAnnotation<PageableDefault> defaults) {

    int defaultPageNumber = defaults.getInt("page");
    int defaultPageSize = defaults.getInt("size");

    if (defaultPageSize < 1) {
      Method annotatedMethod = parameter.getMethod();
      throw new IllegalStateException(String.format(INVALID_DEFAULT_PAGE_SIZE, annotatedMethod));
    }

    String[] sort = defaults.getStringArray("sort");
    if (sort.length == 0) {
      return PageableRequestImpl.of(defaultPageNumber, defaultPageSize);
    }

    Sort.Direction direction = defaults.getEnum("direction", Sort.Direction.class);

    SortRequest.Direction myDirection = null;
    if (direction != null) {
      if (Sort.Direction.ASC.equals(direction)) {
        myDirection = SortRequest.Direction.ASC;
      } else {
        myDirection = SortRequest.Direction.DESC;
      }
    }

    return PageableRequestImpl.of(defaultPageNumber, defaultPageSize, myDirection, sort);
  }

  /**
   * Tries to parse the given {@link String} into an integer and applies the given boundaries. Will return 0 if the
   * {@link String} cannot be parsed.
   *
   * @param parameter the parameter value.
   * @param upper the upper bound to be applied.
   * @param shiftIndex whether to shift the index if {@link #oneIndexedParameters} is set to true.
   * @return
   */
  private Optional<Integer> parseAndApplyBoundaries(
      @Nullable String parameter, int upper, boolean shiftIndex) {

    if (!StringUtils.hasText(parameter)) {
      return Optional.empty();
    }

    try {
      int parsed = Integer.parseInt(parameter) - (oneIndexedParameters && shiftIndex ? 1 : 0);
      return Optional.of(parsed < 0 ? 0 : parsed > upper ? upper : parsed);
    } catch (NumberFormatException e) {
      return Optional.of(0);
    }
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return PageableRequest.class.equals(parameter.getParameterType());
  }

  @Override
  @Nullable public PageableRequest resolveArgument(
      MethodParameter methodParameter,
      @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory)
      throws Exception {

    String page =
        webRequest.getParameter(getParameterNameToUse(getPageParameterName(), methodParameter));
    String pageSize =
        webRequest.getParameter(getParameterNameToUse(getSizeParameterName(), methodParameter));

    SortRequest sort =
        sortResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    PageableRequest pageable = getPageable(methodParameter, page, pageSize);

    if (sort == null || !sort.isSorted()) {
      return pageable;
    }

    return pageable.isPaged()
        ? PageableRequestImpl.of(pageable.getPageNumber(), pageable.getPageSize(), sort)
        : PageableRequest.unpaged();
  }
}
