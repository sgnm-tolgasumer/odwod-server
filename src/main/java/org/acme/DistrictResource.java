package org.acme;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/district")
public class DistrictResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<District> getAll() {
        return District.listAll();
    }

    @GET
    @Path("{id}")
    public District getSingle(@PathParam("id") Long id) {
        District entity = District.findById(id);
        if (entity == null) {
            throw new WebApplicationException("District with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(District district) {
        district.persist();
        return Response.status(Response.Status.CREATED).entity(district).build();
    }

    @Transactional
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public District update(@PathParam("id") Long id, District district) {
        District entity = district.findById(id);
        entity.districtName = district.districtName;
        entity.persist();
        return entity;
    }

    @Transactional
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        District entity = District.findById(id);
        entity.delete();
        if (entity == null) {
            throw new WebApplicationException("District with id of " + id + " does not exist.", 404);
        }
        return Response.status(Response.Status.OK).build();
    }

}
