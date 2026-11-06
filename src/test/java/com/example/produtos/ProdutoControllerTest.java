package com.example.produtos;

import com.example.produtos.model.Produto;
import com.example.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository repository;

    @Test
    void listaProdutos() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/lista"))
                .andExpect(model().attributeExists("produtos"));
    }

    @Test
    void criaProdutoAceitandoPrecoComVirgula() throws Exception {
        mockMvc.perform(post("/produtos")
                        .param("nome", "Produto de teste")
                        .param("descricao", "Criado no teste")
                        .param("preco", "1.234,56")
                        .param("quantidade", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"))
                .andExpect(flash().attribute("mensagem", "Produto cadastrado com sucesso!"));

        Produto salvo = repository.listarTodos().stream()
                .filter(produto -> "Produto de teste".equals(produto.getNome()))
                .findFirst()
                .orElseThrow();

        assertEquals(new BigDecimal("1234.56"), salvo.getPreco());
        assertEquals(3, salvo.getQuantidade());
    }

    @Test
    void rejeitaProdutoInvalido() throws Exception {
        mockMvc.perform(post("/produtos")
                        .param("nome", "")
                        .param("preco", "abc")
                        .param("quantidade", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/formulario"))
                .andExpect(model().attributeHasFieldErrors("produto", "nome", "preco", "quantidade"));
    }

    @Test
    void atualizaEExcluiProduto() throws Exception {
        Produto produto = repository.salvar(
                new Produto("Temporário", "Produto do teste", new BigDecimal("10.00"), 1));

        mockMvc.perform(post("/produtos/{id}", produto.getId())
                        .param("id", produto.getId().toString())
                        .param("nome", "Temporário editado")
                        .param("descricao", "Produto do teste")
                        .param("preco", "20,50")
                        .param("quantidade", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        Produto atualizado = repository.buscarPorId(produto.getId()).orElseThrow();
        assertEquals("Temporário editado", atualizado.getNome());
        assertEquals(new BigDecimal("20.50"), atualizado.getPreco());

        mockMvc.perform(post("/produtos/{id}/excluir", produto.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        assertTrue(repository.buscarPorId(produto.getId()).isEmpty());
    }
}
