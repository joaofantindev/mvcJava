package com.example.produtos;

import com.example.produtos.model.Produto;
import com.example.produtos.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class ProdutosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProdutosApplication.class, args);
    }

    /**
     * Dados de exemplo para facilitar o teste do CRUD.
     * Como o repositório é em memória, eles são recriados a cada inicialização.
     */
    @Bean
    CommandLineRunner carregarDadosIniciais(ProdutoRepository repository) {
        return args -> {
            repository.salvar(new Produto(
                    "Notebook Dell Inspiron",
                    "16 GB RAM, SSD 512 GB, tela 15,6\"",
                    new BigDecimal("3499.90"), 12));

            repository.salvar(new Produto(
                    "Mouse sem fio Logitech",
                    "Conexão USB, 1000 DPI",
                    new BigDecimal("89.90"), 35));

            repository.salvar(new Produto(
                    "Teclado mecânico",
                    "Switch azul, ABNT2, iluminação RGB",
                    new BigDecimal("249.00"), 8));
        };
    }
}
