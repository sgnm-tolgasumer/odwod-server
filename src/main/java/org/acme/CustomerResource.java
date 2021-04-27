package org.acme;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * /customer endpoint to do CRUD operations of customers via HTTP REST API.
 * These operations for Panache Entity type it directly communicates with MySQL database.
 **/

@Path("/customer")
public class CustomerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getAll() {
        return Customer.listAll();
    }

    /**
    It gets the number of records in Customer table on MySQL database.
    */
    @GET
    @Path("customerCount/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCount() {
        return "{\"count\": \"" + Customer.count() + "\" }";
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getSingle(@PathParam("id") Long id) {
        Customer entity = Customer.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Customer with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Customer customer) {
        customer.persist();
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer update(@PathParam("id") Long id, Customer customer) {
        Customer entity = customer.findById(id);
        entity.name = customer.name;
        entity.surname = customer.surname;
        entity.telephone = customer.telephone;
        entity.birthDate = customer.birthDate;
        entity.persist();
        return entity;
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        Customer entity = Customer.findById(id);
        entity.delete();
        if (entity == null) {
            throw new WebApplicationException("Customer with id of " + id + " does not exist.", 404);
        }
        return Response.status(Response.Status.OK).build();
    }

}