package com.acuo.persist.web.resources;

import com.acuo.persist.core.DataImporter;
import com.codahale.metrics.annotation.Timed;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static com.acuo.common.util.ArgChecker.notEmpty;
import static com.acuo.common.util.ArgChecker.notNull;

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

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Path("reload/clients")
    @Timed
    public Response reload(List<String> clients, @QueryParam("branch") String branch) {
        String[] array = notNull(clients, "clients").toArray(new String[0]);
        array = notEmpty(array, "clients array");
        if (branch != null) {
            service.withBranch(branch).reload(array);
        } else {
            service.reload(array);
        }
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