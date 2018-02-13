package com.acuo.persist.web.resources

import com.acuo.common.util.WithResteasyFixtures
import com.acuo.persist.core.DataImporter
import com.acuo.persist.utils.JacksonConfig
import org.jboss.resteasy.core.Dispatcher
import org.jboss.resteasy.mock.MockHttpRequest
import org.jboss.resteasy.mock.MockHttpResponse
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.MediaType

class ImportResourceSpock extends Specification implements WithResteasyFixtures {

    Dispatcher dispatcher
    DataImporter importService = Mock()
    @Subject
    ImportResource resource

    void setup() {
        resource = new ImportResource(importService)
        dispatcher = createDispatcher(JacksonConfig)
        dispatcher.getRegistry().addSingletonResource(resource)
    }

    void "reload clients with default branch"() {
        given:
        MockHttpRequest post = MockHttpRequest.post("/import/reload/clients")
        post.contentType(MediaType.APPLICATION_JSON)
        post.content("[\"ACUO\",\"Reuters\",\"Palo\"]".bytes)

        when:
        MockHttpResponse response = new MockHttpResponse()
        dispatcher.invoke(post, response)

        then:
        1 * importService.reload(['ACUO','Reuters','Palo'])
        with(response){
            status == HttpServletResponse.SC_OK
        }
    }

    void "reload clients with the branch specified"() {
        given:
        MockHttpRequest post = MockHttpRequest.post("/import/reload/clients?branch=develop")
        post.contentType(MediaType.APPLICATION_JSON)
        post.content("[\"ACUO\",\"Reuters\",\"Palo\"]".bytes)

        when:
        MockHttpResponse response = new MockHttpResponse()
        dispatcher.invoke(post, response)

        then:
        1 * importService.withBranch("develop") >> importService
        1 * importService.reload(['ACUO','Reuters','Palo'])
        with(response){
            status == HttpServletResponse.SC_OK
        }
    }

}
