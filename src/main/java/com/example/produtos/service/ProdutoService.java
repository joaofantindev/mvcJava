package com.example.produtos.service;

import com.example.produtos.model.Produto;
import com.example.produtos.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listarTodos() {
        return repository.listarTodos();
    }

    public Produto buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado: " + id));
    }

    public Produto salvar(Produto produto) {
        return repository.salvar(produto);
    }

    public Produto atualizar(Long id, Produto dadosAtualizados) {
        Produto produto = buscarPorId(id);
        produto.setNome(dadosAtualizados.getNome());
        produto.setDescricao(dadosAtualizados.getDescricao());
        produto.setPreco(dadosAtualizados.getPreco());
        produto.setQuantidade(dadosAtualizados.getQuantidade());
        return repository.salvar(produto);
    }

    public void excluir(Long id) {
        if (!repository.excluir(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Produto não encontrado: " + id);
        }
    }
}
