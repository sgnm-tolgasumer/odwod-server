package org.acme;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/workorder")
public class WorkOrderResource {

    @Inject
    KafkaService kafkaService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll(@QueryParam("topicId") String topicId) {

        return kafkaService.getAll(topicId);
    }

    @GET
    @Path("{id}")
    public String getSingle(@PathParam("id") Long id) {

       return kafkaService.getSingle(id);
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(WorkOrder workOrder) {
        kafkaService.publish(workOrder);
        System.out.println(workOrder.description);
        return Response.status(Response.Status.CREATED).build();
    }

/*
    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public WorkOrder update(@PathParam("id") Long id, WorkOrder workOrder) {
        WorkOrder entity = WorkOrder.findById(id);
        entity.title = workOrder.title;
        entity.description = workOrder.description;
        entity.telephone = workOrder.telephone;
        entity.type = workOrder.type;
        entity.addressCity = workOrder.addressCity;
        entity.addressDistrict = workOrder.addressDistrict;
        entity.openAddress = workOrder.openAddress;
        entity.workerId = workOrder.workerId;

        kafkaService.publishAsJson(entity, 0);
        return entity;
    }
*/
    /*
    @Transactional
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        /*WorkOrder entity = WorkOrder.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Work Order with id of " + id + " does not exist.", 404);
        }
        System.out.println();
        return Response.status(Response.Status.OK).build();
    }
*/
    /*
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getById(@PathParam("id") String id){
        return employeeService.getById(id);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Employee create(Employee employeeToCreate){
        return employeeService.create(employeeToCreate);
    }
     */
}