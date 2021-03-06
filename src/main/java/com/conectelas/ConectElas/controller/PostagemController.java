package com.conectelas.ConectElas.controller;

import java.util.List;
import javax.validation.Valid;

import com.conectelas.ConectElas.model.PostagemModel;
import com.conectelas.ConectElas.repository.PostagemRepository;
import com.conectelas.ConectElas.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

  @Autowired
  private PostagemRepository postagemRepository;

  @Autowired
  private TemaRepository temaRepository;

  @GetMapping
  public ResponseEntity<List<PostagemModel>> getAll (){
    return ResponseEntity.ok(postagemRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostagemModel> getById(@PathVariable Long id) {
    return postagemRepository.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/titulo/{titulo}")
  public ResponseEntity<List<PostagemModel>> getByTitulo(@PathVariable String titulo){
    return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
  }
  
  @GetMapping("/procurar/{tema}/{titulo}")
  public ResponseEntity<List<PostagemModel>> getPostagemByTema(@PathVariable String titulo, @PathVariable Long tema){
	  return ResponseEntity.ok(postagemRepository.findPostagemByTema(titulo, tema));
  }

  @PostMapping
  public ResponseEntity<PostagemModel> postPostagem (@Valid @RequestBody PostagemModel postagem){

    if (temaRepository.existsById(postagem.getTema().getId()))
      return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

  }

  @PutMapping
  public ResponseEntity<PostagemModel> putPostagem (@Valid @RequestBody PostagemModel postagem){

    if (postagemRepository.existsById(postagem.getId())){

      if (temaRepository.existsById(postagem.getTema().getId()))
        return ResponseEntity.status(HttpStatus.OK)
          .body(postagemRepository.save(postagem));

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePostagem(@PathVariable Long id) {

    return postagemRepository.findById(id)
      .map(resposta -> {
        postagemRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
      })
      .orElse(ResponseEntity.notFound().build());
  }

}
