package com.example.produtos.controller;

import com.example.produtos.model.Produto;
import com.example.produtos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    /**
     * Aceita preço com vírgula ou ponto: "19,90", "19.90", "1.234,56".
     * Também garante que o valor seja renderizado como "19.90" no formulário.
     */
    @InitBinder
    public void configurarPreco(WebDataBinder binder) {
        binder.registerCustomEditor(BigDecimal.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isBlank()) {
                    setValue(null);
                    return;
                }
                try {
                    String valor = text.trim();
                    if (valor.contains(",")) {
                        valor = valor.replace(".", "").replace(",", ".");
                    }
                    setValue(new BigDecimal(valor));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Preço inválido");
                }
            }

            @Override
            public String getAsText() {
                Object valor = getValue();
                return valor == null ? "" : ((BigDecimal) valor).toPlainString();
            }
        });
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", service.listarTodos());
        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("modoEdicao", false);
        model.addAttribute("urlAcao", "/produtos");
        return "produtos/formulario";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("produto") Produto produto,
                        BindingResult resultado,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            model.addAttribute("urlAcao", "/produtos");
            return "produtos/formulario";
        }

        service.salvar(produto);
        redirectAttributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
        return "redirect:/produtos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Produto produto = service.buscarPorId(id);
        model.addAttribute("produto", produto);
        model.addAttribute("modoEdicao", true);
        model.addAttribute("urlAcao", "/produtos/" + produto.getId());
        return "produtos/formulario";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("produto") Produto produto,
                            BindingResult resultado,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            produto.setId(id);
            model.addAttribute("modoEdicao", true);
            model.addAttribute("urlAcao", "/produtos/" + id);
            return "produtos/formulario";
        }

        service.atualizar(id, produto);
        redirectAttributes.addFlashAttribute("mensagem", "Produto atualizado com sucesso!");
        return "redirect:/produtos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
        return "redirect:/produtos";
    }
}
