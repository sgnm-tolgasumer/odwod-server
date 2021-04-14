package org.acme;

import org.hibernate.jdbc.Work;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/worktype")
public class WorkTypeResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkType> getAll() {
        return WorkType.listAll();
    }

    @GET
    @Path("{id}")
    public WorkType getSingle(@PathParam("id") Long id) {
        WorkType entity = WorkType.findById(id);
        if (entity == null) {
            throw new WebApplicationException("WorkType with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(WorkType workType) {
        workType.persist();
        return Response.status(Response.Status.CREATED).entity(workType).build();
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public WorkType update(@PathParam("id") Long id, WorkType workType) {
        WorkType entity = workType.findById(id);
        entity.workType = workType.workType;
        entity.persist();
        return entity;
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        WorkType entity = WorkType.findById(id);
        entity.delete();
        if (entity == null) {
            throw new WebApplicationException("Work type with id of " + id + " does not exist.", 404);
        }
        return Response.status(Response.Status.OK).build();
    }
}
