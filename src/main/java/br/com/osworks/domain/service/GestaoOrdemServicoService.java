package br.com.osworks.domain.service;

import br.com.osworks.domain.exception.EntidadeNaoEncontradaException;
import br.com.osworks.domain.exception.NegocioException;
import br.com.osworks.domain.model.Cliente;
import br.com.osworks.domain.model.Comentario;
import br.com.osworks.domain.model.OrdemServico;
import br.com.osworks.domain.model.StatusOrdemServico;
import br.com.osworks.domain.repository.ClienteRepository;
import br.com.osworks.domain.repository.ComentarioRepository;
import br.com.osworks.domain.repository.OrdemServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
public class GestaoOrdemServicoService {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    public OrdemServico criar(OrdemServico ordemServico){
        Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
                          .orElseThrow(() -> new NegocioException("Cliente não encontrado"));

        ordemServico.setCliente(cliente);
        ordemServico.setStatus(StatusOrdemServico.ABERTA);
        ordemServico.setDataAbertura(OffsetDateTime.now());

        return ordemServicoRepository.save(ordemServico);
    }

    public Comentario adicionarComentario(Long ordemServicoId, String descricao){
        OrdemServico ordemServico = buscar(ordemServicoId);

        Comentario comentario = new Comentario();
        comentario.setDataEnvio(OffsetDateTime.now());
        comentario.setDescricao(descricao);
        comentario.setOrdemServico(ordemServico);

        return comentarioRepository.save(comentario);

    }

    public void finalizar(Long ordemSericoId){
        OrdemServico ordemServico = buscar(ordemSericoId);
        ordemServico.finalizar();
        ordemServicoRepository.save(ordemServico);
    }

    private OrdemServico buscar(Long ordemServicoId) {
        return ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
    }



}
