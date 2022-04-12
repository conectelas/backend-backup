package com.conectelas.ConectElas.repository;

import com.conectelas.ConectElas.model.PostagemModel;
import com.conectelas.ConectElas.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<PostagemModel, Long> {
  List<PostagemModel> findAllByTituloContainingIgnoreCase(String titulo);
  
  @Query(value="SELECT * FROM postagem WHERE tema_id= ?2 AND titulo LIKE %?1%", nativeQuery=true)
  public List<PostagemModel> findPostagemByTema(String titulo, Long id);
}
