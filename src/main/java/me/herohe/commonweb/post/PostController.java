package me.herohe.commonweb.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable("id") Post post){
        //DomainClassConverter 스프링이 제공해주는 Converter 아무타입이나 변환 가능 ToIdConverter 등록이된다. ToEntityConverter = findById를 해준다.
//        Optional<Post> byId = postRepository.findById(id);
//        Post post = byId.get();
        return post.getTitle();
    }

    @GetMapping("/posts")
    public PagedModel<EntityModel<Post>> getPosts(Pageable pageable, PagedResourcesAssembler<Post> assembler) {
        return assembler.toModel(postRepository.findAll(pageable));
    }

}
