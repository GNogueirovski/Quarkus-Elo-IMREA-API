package br.com.fiap.resource;

import br.com.fiap.bo.PacienteBO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.to.PacienteTO;
import br.com.fiap.to.ErrorResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/paciente")
public class PacienteResource {
    private PacienteBO pacienteBO = new PacienteBO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid PacienteTO paciente) {
        try{
            PacienteTO resultado = pacienteBO.save(paciente);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.CREATED).entity(resultado).build();

        } catch (PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@Valid PacienteTO paciente, @PathParam("id") Long id) {
        try {
            paciente.setIdPaciente(id);
            PacienteTO resultado = pacienteBO.update(paciente);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @DELETE
    @Path("/{codigo}")
    public Response delete(@PathParam("codigo") Long codigo) {
        if (pacienteBO.delete(codigo)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        ArrayList<PacienteTO> resultado = pacienteBO.findAll();
        return Response.status(Response.Status.OK).entity(resultado).build();
    }

    @GET
    @Path("/acompanhante/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByAcompanhante(@PathParam("id") Long id) {
        try {
            PacienteTO resultado = pacienteBO.findByAcompanhante(id);
            if (resultado == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (AcompanhanteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        PacienteTO resultado = pacienteBO.findById(id);
        if (resultado == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } return Response.status(Response.Status.OK).entity(resultado).build();
    }

}
