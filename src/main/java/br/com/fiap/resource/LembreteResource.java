package br.com.fiap.resource;

import br.com.fiap.bo.LembreteBO;
import br.com.fiap.exception.*;
import br.com.fiap.to.ErrorResponse;
import br.com.fiap.to.LembreteTO;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/lembrete")
public class LembreteResource {
    private LembreteBO lembreteBO = new LembreteBO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid LembreteTO lembrete) {
        try {
            LembreteTO resultado = lembreteBO.save(lembrete);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.CREATED).entity(resultado).build();
        } catch (AtendimentoException | ColaboradorException | PacienteException | ProfissionalSaudeException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (lembreteBO.delete(id)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/reenviar/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reenviarLembrete(@PathParam("id") Long id) {
        try {
            LembreteTO resultado = lembreteBO.reenviar(id);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (LembreteException | AtendimentoException | ProfissionalSaudeException | PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }


    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        LembreteTO resultado = lembreteBO.findById(id);
        if (resultado == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(resultado).build();
    }

    @GET
    @Path("/paciente/{id}")
    public Response findAllByPaciente(@PathParam("id") Long idPaciente) {
        try {
            ArrayList<LembreteTO> resultado = lembreteBO.findAllByPaciente(idPaciente);
            return Response.ok(resultado).build();
        } catch (PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @GET
    @Path("/paciente/{id}/ultimo")
    public Response findUltimoByPaciente(@PathParam("id") Long idPaciente) {
        try {
            LembreteTO resultado = lembreteBO.findUltimoByPaciente(idPaciente);
            if (resultado == null) {
                return Response.status(Response.Status.OK).entity(null).build();
            }
            return Response.ok(resultado).build();
        } catch (PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

}
