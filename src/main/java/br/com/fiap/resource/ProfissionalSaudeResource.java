package br.com.fiap.resource;

import br.com.fiap.bo.ProfissionalSaudeBO;
import br.com.fiap.exception.ProfissionalSaudeException;
import br.com.fiap.to.ErrorResponse;
import br.com.fiap.to.ProfissionalSaudeTO;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/profissionalsaude")
public class ProfissionalSaudeResource {
    private ProfissionalSaudeBO profissionalSaudeBO = new ProfissionalSaudeBO();


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid ProfissionalSaudeTO profissionalSaude) {
        try{
            ProfissionalSaudeTO resultado = profissionalSaudeBO.save(profissionalSaude);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.CREATED).entity(resultado).build();

        } catch (ProfissionalSaudeException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@Valid ProfissionalSaudeTO profissionalSaude, @PathParam("id") Long id) {
        try {
            profissionalSaude.setIdProfissionalSaude(id);
            ProfissionalSaudeTO resultado = profissionalSaudeBO.update(profissionalSaude);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (ProfissionalSaudeException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try{
            if (profissionalSaudeBO.delete(id)) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        catch (ProfissionalSaudeException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        ArrayList<ProfissionalSaudeTO> resultado = profissionalSaudeBO.findAll();
        return Response.status(Response.Status.OK).entity(resultado).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        ProfissionalSaudeTO resultado = profissionalSaudeBO.findById(id);
        if (resultado == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } return Response.status(Response.Status.OK).entity(resultado).build();
    }

}
