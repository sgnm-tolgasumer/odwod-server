package org.acme;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/worker")
public class WorkerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Worker> getAll() {
        return Worker.listAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Worker getSingle(@PathParam("id") String id) {
        Worker entity = Worker.find("userId", id).firstResult();
        if (entity == null) {
            throw new WebApplicationException("Worker with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    /**
     It gets the number of records in Worker table on MySQL database.
     */
    @GET
    @Path("workerCount/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCount() {
        return "{\"count\": \"" + Worker.count() + "\" }";
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Worker worker) {
        worker.persist();
        return Response.status(Response.Status.CREATED).entity(worker).build();
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Worker update(@PathParam("id") String id, Worker worker) {
        Worker entity = Worker.find("userId", id).firstResult();
        entity.name = worker.name;
        entity.surname = worker.surname;
        entity.telephone = worker.telephone;
        entity.workableDistricts = worker.workableDistricts;
        entity.jobTypes = worker.jobTypes;
        entity.persist();

        return entity;
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        Worker entity = Worker.findById(id);
        entity.delete();
        if (entity == null) {
            throw new WebApplicationException("Worker with id of " + id + " does not exist.", 404);
        }
        return Response.status(Response.Status.OK).build();
    }
}
