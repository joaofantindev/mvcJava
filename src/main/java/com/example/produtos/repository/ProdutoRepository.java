package com.example.produtos.repository;

import com.example.produtos.model.Produto;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória. Os dados são perdidos quando a aplicação é encerrada.
 */
@Repository
public class ProdutoRepository {

    private final Map<Long, Produto> produtos = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong();

    public List<Produto> listarTodos() {
        return produtos.values().stream()
                .sorted(Comparator.comparing(Produto::getId))
                .toList();
    }

    public Optional<Produto> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(produtos.get(id));
    }

    public Produto salvar(Produto produto) {
        if (produto.getId() == null) {
            produto.setId(sequencia.incrementAndGet());
        }
        produtos.put(produto.getId(), produto);
        return produto;
    }

    public boolean excluir(Long id) {
        return id != null && produtos.remove(id) != null;
    }
}
