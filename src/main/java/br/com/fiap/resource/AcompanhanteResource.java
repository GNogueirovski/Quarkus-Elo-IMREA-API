package br.com.fiap.resource;

import br.com.fiap.bo.AcompanhanteBO;
import br.com.fiap.exception.AcompanhanteException;
import br.com.fiap.exception.PacienteException;
import br.com.fiap.to.AcompanhanteTO;
import br.com.fiap.to.ErrorResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.ArrayList;

@Path("/acompanhante")
public class AcompanhanteResource {
    private AcompanhanteBO acompanhanteBO = new AcompanhanteBO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid AcompanhanteTO acompanhante) {
        try {
            AcompanhanteTO resultado = acompanhanteBO.save(acompanhante);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.CREATED).entity(resultado).build();

        } catch (AcompanhanteException | PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@Valid AcompanhanteTO acompanhante, @PathParam("id") Long id) {
        try {
            acompanhante.setIdAcompanhante(id);
            AcompanhanteTO resultado = acompanhanteBO.update(acompanhante);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (AcompanhanteException | PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (acompanhanteBO.delete(id)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        ArrayList<AcompanhanteTO> resultado = acompanhanteBO.findAll();
        return Response.status(Response.Status.OK).entity(resultado).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        AcompanhanteTO resultado = acompanhanteBO.findById(id);
        if (resultado == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } return Response.status(Response.Status.OK).entity(resultado).build();
    }

    @GET
    @Path("/paciente/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllByPaciente(@PathParam("id") Long id) {
        try {
            ArrayList<AcompanhanteTO> resultado = acompanhanteBO.findAllByPaciente(id);
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }


}
