package br.com.fiap.resource;

import br.com.fiap.bo.AtendimentoBO;
import br.com.fiap.bo.LembreteBO;
import br.com.fiap.exception.*;
import br.com.fiap.to.AtendimentoTO;
import br.com.fiap.to.AtendimentoTO;
import br.com.fiap.to.ErrorResponse;
import br.com.fiap.to.LembreteTO;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/atendimento")
public class AtendimentoResource {
    private AtendimentoBO atendimentoBO = new AtendimentoBO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@Valid AtendimentoTO atendimento) {
        try {
            AtendimentoTO resultado = atendimentoBO.save(atendimento);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.CREATED).entity(resultado).build();
        } catch (PacienteException | ProfissionalSaudeException | AtendimentoException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@Valid AtendimentoTO atendimento, @PathParam("id") Long id) {
        try {
            atendimento.setIdAtendimento(id);
            AtendimentoTO resultado = atendimentoBO.update(atendimento);
            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (PacienteException | ProfissionalSaudeException | AtendimentoException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (atendimentoBO.delete(id)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/concluir")
    public Response concluirAtendimento(@PathParam("id") Long id) {
        try {
            AtendimentoTO resultado = atendimentoBO.concluir(id);
            if(resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (AtendimentoException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @PUT
    @Path("/{id}/cancelar")
    public Response cancelarAtendimento(@PathParam("id") Long id) {
        try {
            AtendimentoTO resultado = atendimentoBO.cancelar(id);
            if(resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (AtendimentoException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        ArrayList<AtendimentoTO> resultado = atendimentoBO.findAll();
        return Response.status(Response.Status.OK).entity(resultado).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        AtendimentoTO resultado = atendimentoBO.findById(id);
        if (resultado == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } return Response.status(Response.Status.OK).entity(resultado).build();
    }

    @GET
    @Path("/paciente/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllByPaciente(@PathParam("id") Long id) {
        try {
            ArrayList<AtendimentoTO> resultado = atendimentoBO.findAllByPaciente(id);
            return Response.status(Response.Status.OK).entity(resultado).build();
        } catch (PacienteException e) {
            ErrorResponse errorResponse = new ErrorResponse(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

}
