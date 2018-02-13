package com.acuo.persist.utils

import com.fasterxml.jackson.databind.ObjectMapper

import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class JacksonConfig implements ContextResolver<ObjectMapper> {

    def mapper = new ObjectMapper()

    ObjectMapper getContext(Class<?> type) {
        return mapper
    }
}