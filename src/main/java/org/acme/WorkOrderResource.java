package org.acme;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Path("/workorder")
public class WorkOrderResource {

    @Inject
    KafkaService kafkaService;

    /**
     * This function created to generate random 16 character IDs. May not be unique it MUST be changed with UUID in
     * future.
     * @return id as String.
     */
    public static String idCreator() {
        final int SHORT_ID_LENGTH = 16;
        // Using all possible alpha numeric characters inside the ID.
        String shortId = RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH);
        System.out.println(shortId);
        return shortId;
    }

    /**
     * It returns all work orders according to given Kafka topic with checking other topics for to be sure this work
     * order only exist in given topicId.
     * @param topicId String.
     * @return Work orders as JSON.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkOrder> getAll(@QueryParam("topicId") String topicId) {

        List<WorkOrder> temp = new ArrayList<>();
        List<WorkOrder> workOrdersPending = kafkaService.getAll("pending");
        List<WorkOrder> workOrdersProgress = kafkaService.getAll("in_progress");
        List<WorkOrder> workOrdersDone = kafkaService.getAll("done");
        List<WorkOrder> workOrdersExpired = kafkaService.getAll("expired");

        if(topicId.equals("pending")) {
            for (WorkOrder woPending:workOrdersPending)
            {
                for (WorkOrder woInProgress: workOrdersProgress)
                {
                    if (woPending.workOrderId.equals(woInProgress.workOrderId))
                    {
                        temp.add(woPending);
                    }
                }
                for (WorkOrder woDone: workOrdersDone)
                {
                    if (woPending.workOrderId.equals(woDone.workOrderId))
                    {
                        temp.add(woPending);
                    }
                }
                for (WorkOrder woExpired: workOrdersExpired)
                {
                    if (woPending.workOrderId.equals(woExpired.workOrderId))
                    {
                        temp.add(woPending);
                    }
                }
            }
            workOrdersPending.removeAll(temp);

            return workOrdersPending;
        }
        else if(topicId.equals("in_progress")) {

            for (WorkOrder woProgress:workOrdersProgress)
            {
                for (WorkOrder woDone: workOrdersDone)
                {
                    if (woProgress.workOrderId.equals(woDone.workOrderId))
                    {
                        temp.add(woProgress);
                    }
                }
                for (WorkOrder woExpired: workOrdersExpired)
                {
                    if (woProgress.workOrderId.equals(woExpired.workOrderId))
                    {
                        temp.add(woProgress);
                    }
                }
            }
                workOrdersProgress.removeAll(temp);
                return workOrdersProgress;
        }

        return kafkaService.getAll(topicId);
    }

    /**
     * It returns work orders according to worker's preferred job types and workable districts.
     * @param userId String.
     * @return Work orders as JSON.
     */
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkOrder> getByWorkerPreferences(@PathParam("userId") String userId) {
        List<WorkOrder> workOrdersPending = new ArrayList<>();
        List<WorkOrder> workOrdersByPref = new ArrayList<>();
        workOrdersPending = getAll("pending");
        System.out.println(workOrdersPending);
        Worker worker = Worker.find("userId", userId).firstResult();
        //Worker worker = Worker.find("userId = ?1", userId).firstResult();

        System.out.println(userId);

        System.out.println(worker.toString());
        List<String> jobTypes = Arrays.asList(worker.jobTypes.split(","));
        List<String> workableDistricts = Arrays.asList(worker.workableDistricts.split(","));
        for (WorkOrder workOrder: workOrdersPending) {
           String type = workOrder.type;
           String district = workOrder.addressDistrict;
           if(jobTypes.contains(type) && workableDistricts.contains(district))
           {
               workOrdersByPref.add(workOrder);
           }
        }
        return workOrdersByPref;
    }

    @GET
    @Path("single/{workOrderId}/{topic}")
    @Produces(MediaType.APPLICATION_JSON)
    public WorkOrder getSingle(@PathParam("workOrderId") String workOrderId, @PathParam("topic") String topic) {
        System.out.println(workOrderId + topic);
       return kafkaService.getSingle(workOrderId, topic);
    }

    @GET
    @Path("activeCount/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountActiveWorkOrders() {
        return "{\"count\": \"" + (getAll("in_progress").size() + getAll("pending").size()) + "\" }";
    }

    /**
     * It returns the current status of given work order.
     * @param workOrderId String.
     * @return status as String.
     */
    @GET
    @Path("status/{workOrderId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus(@PathParam("workOrderId") String workOrderId) {

        if (kafkaService.getSingle(workOrderId, "done") != null)
            return "Done";
        else if (kafkaService.getSingle(workOrderId, "expired") != null)
            return "Expired";
        else if (kafkaService.getSingle(workOrderId, "in_progress") != null)
            return "In Progress";
        else
            return "Pending";
    }

    /**
     * It creates a work order and publishes to pending topic in Kafka.
     * @param workOrder String.
     * @return Response.
     */
    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(WorkOrder workOrder) {
        workOrder.workOrderId = idCreator();
        kafkaService.publish(workOrder);
        System.out.println(workOrder.description);
        System.out.println(workOrder.addressDistrict);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * it returns all work orders according to workers' unique id.
     * @param workerId String.
     * @return Work orders as JSON.
     */
    @GET
    @Path("worker/{workerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkOrder> getWorkOrderByWorker(@PathParam("workerId") String workerId) {
        return kafkaService.getAllByWorkerId(workerId);
    }

    /**
     * It returns all work orders according to customer's unique id.
     * @param userId String.
     * @return Work orders as JSON.
     */
    @GET
    @Path("customer/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<WorkOrder> getWorkOrderByCustomer(@PathParam("userId") String userId) {
        return kafkaService.getAllByCustomerId(userId);
    }

    /**
     * It transfers specific work order from one Kafka topic to another.
     * @param workOrderId String.
     * @param sourceTopic String.
     * @param targetTopic String.
     * @param workerId String.
     * @return Response
     */
    @Transactional
    @POST
    @Path("transfer/{workOrderId}/{sourceTopic}/{targetTopic}/{workerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(@PathParam("workOrderId") String workOrderId, @PathParam("sourceTopic") String sourceTopic, @PathParam("targetTopic") String targetTopic, @PathParam("workerId") String workerId) {
        kafkaService.transferWorder(workOrderId, sourceTopic, targetTopic, workerId);
        return Response.status(Response.Status.OK).build();
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