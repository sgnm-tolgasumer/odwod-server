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
    public Worker getSingle(@PathParam("id") Long id) {
        Worker entity = Worker.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Worker with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    /**
     It gets the number of records in Worker table on MySQL database.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCount(@QueryParam("workerCount") String workerCount) {
        String count = "{\"count\": \"" + Worker.count() + "\" }";
        return count;
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
    public Worker update(@PathParam("id") Long id, Worker worker) {
        Worker entity = worker.findById(id);
        entity.name = worker.name;
        entity.surname = worker.surname;
        entity.telephone = worker.telephone;
        entity.mail = worker.mail;
        entity.birthDate = worker.birthDate;
        entity.addressCity = worker.addressCity;
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
