package com.acuo.persist.web.resources;

import com.acuo.persist.core.ImportService;
import com.codahale.metrics.annotation.Timed;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/import")
public class ImportResource {

    private final ImportService service;

    @Inject
    public ImportResource(ImportService service) {
        this.service = service;
    }

    @GET
    @Path("reload/{branch}")
    @Timed
    public Response reload(@PathParam("branch") String value) {
        service.branch(value).reload();
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("load/{branch}/{fileName}")
    @Timed
    public Response load(@PathParam("branch") String branch, @PathParam("fileName") String fileName) {
        service.branch(branch).load(fileName);
        return Response.status(Status.OK).build();
    }
}