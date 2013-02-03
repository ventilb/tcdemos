/*
 * Copyright 2012-2013 Manuel Schulze <manuel_schulze@i-entwicklung.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iew.web.isc.spring;

import de.iew.web.isc.DSRequest;
import de.iew.web.isc.annotations.IscRequest;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Implements a {@link HandlerMethodArgumentResolver} to resolve Smartclient
 * request meta data into a {@link DSRequest} object.
 * <code>
 * public Model fetchAction(@IscRequest DSRequest dsRequest) throws Exception;
 * </code>
 *
 * @author Manuel Schulze <manuel_schulze@i-entwicklung.de>
 * @since 02.02.13 - 00:26
 */
public class IscRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter methodParameter) {
        IscRequest iscRequestAnnotation = methodParameter.getParameterAnnotation(IscRequest.class);
        return (iscRequestAnnotation != null);
    }

    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        DSRequest dsRequest = new DSRequest();
        WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, dsRequest, "dsRequest");
        binder.setFieldMarkerPrefix("__");

        // Man kann noch eigene Property Editoren konfigurieren
        //binder.registerCustomEditor();

        if (binder instanceof ServletRequestDataBinder) {
            /*
            @TODO

            Wir machen es uns etwas leicht. Unser DSRequest Objekt muss
            eigentlich nur die isc_-Eigenschaften kennen. Dort ist auch isc_metaDataPreifx
            konfiguriert. Anhand dieses Prefix müssen wir die anderen Eiegenschaften auflösen.
             */
            ServletRequestDataBinder servletRequestDataBinder = (ServletRequestDataBinder) binder;
            HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

            servletRequestDataBinder.bind(request);

            BindingResult bindingResult = binder.getBindingResult();
            modelAndViewContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + "dsRequest", bindingResult);

            return dsRequest;
        } else {
            throw new UnsupportedOperationException("Using " + binder.getClass() + " WebDataBinder is not supported.");
        }

    }
}
