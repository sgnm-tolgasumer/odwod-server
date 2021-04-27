package org.acme;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/doneWorkOrder")
public class DoneWorkOrderResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<DoneWorkOrderEntity> getAll() {
        return DoneWorkOrderEntity.listAll();
    }

    /**
     It gets the number of records in workorder_done table on MySQL database.
     */
    @GET
    @Path("doneCount/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCount() {
        String count = "{\"count\": \"" + DoneWorkOrderEntity.count() + "\" }";
        return count;
    }

    @GET
    @Path("byCustomer/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DoneWorkOrderEntity> getAllDoneByCustomer(@PathParam("customerId") String customerId) {

        return DoneWorkOrderEntity.find("userId", customerId).list();
    }

    @GET
    @Path("byWorker/{workerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DoneWorkOrderEntity> getAllDoneByWorker(@PathParam("workerId") String workerId) {

        return DoneWorkOrderEntity.find("workerId", workerId).list();
    }
}
