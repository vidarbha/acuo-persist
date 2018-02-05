package com.acuo.persist.web.resources;

import com.acuo.persist.core.DataImporter;
import com.codahale.metrics.annotation.Timed;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/import")
public class ImportResource {

    private final DataImporter service;

    @Inject
    public ImportResource(DataImporter service) {
        this.service = service;
    }

    @GET
    @Path("delete/all")
    @Timed
    public Response deleteAll() {
        service.deleteAll();
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("reload/client/{client}")
    @Timed
    public Response reload(@PathParam("client") String client,
                           @QueryParam("branch") String branch) {
        service.withBranch(branch).reload();
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("reload/clients/{clients}")
    @Timed
    public Response reload(@PathParam("clients") List<String> clients,
                           @QueryParam("branch") String branch) {
        service.withBranch(branch).reload(clients.toArray(new String[0]));
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("load/client/{client}")
    @Timed
    public Response load(@PathParam("client") String client,
                         @QueryParam("branch") String branch,
                         @QueryParam("file") String fileName) {
        service.withBranch(branch).load(client, fileName);
        return Response.status(Status.OK).build();
    }
}