package br.com.fiap.resource;

import br.com.fiap.bo.LembreteBO;
import br.com.fiap.to.LembreteTO;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/Lembrete")
public class LembreteResource {
    private LembreteBO lembreteBO = new LembreteBO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid LembreteTO Lembrete) {
        LembreteTO resultado = lembreteBO.save(Lembrete);
        Response.ResponseBuilder response = null;
        if (resultado != null){
            response = Response.created(null);  // 201 - CREATED
        } else {
            response = Response.status(400);  // 401 - BAD REQUEST
        }
        response.entity(resultado);
        return response.build();
    }

    @DELETE
    @Path("/{codigo}")
    public Response delete(@PathParam("codigo") Long codigo) {
        Response.ResponseBuilder response = null;
        if (lembreteBO.delete(codigo)){
            response = Response.status(204);  // 204 - NO CONTENT
        } else {
            response = Response.status(404);  // 404 - NOT FOUND
        }
        return response.build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@Valid LembreteTO Lembrete, @PathParam("id") Long id) {
        Lembrete.setIdLembrete(id);
        LembreteTO resultado = lembreteBO.update(Lembrete);
        Response.ResponseBuilder response = null;
        if (resultado != null){
            response = Response.created(null);  // 201 - CREATED
        } else {
            response = Response.status(400);  // 400 - BAD REQUEST
        }
        response.entity(resultado);
        return response.build();
    }


    @GET
    @Path("/paciente/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllByPaciente(@PathParam("id") Long id) {
        ArrayList<LembreteTO> resultado = lembreteBO.findAllByPaciente(id);
        Response.ResponseBuilder response = null;
        if (resultado != null) {
            response = Response.ok();  // 200 (OK)
        } else {
            response = Response.status(404);  // 404 (NOT FOUND)
        }
        response.entity(resultado);
        return response.build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByCodigo(@PathParam("id") Long id) {
        LembreteTO resultado = lembreteBO.findById(id);
        Response.ResponseBuilder response = null;
        if (resultado != null) {
            response = Response.ok();  // 200 (OK)
        } else {
            response = Response.status(404);  // 404 (NOT FOUND)
        }
        response.entity(resultado);
        return response.build();
    }

}
