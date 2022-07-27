package me.herohe.commonweb.comment;

import me.herohe.commonweb.post.Post;
import me.herohe.commonweb.post.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Optional;

import static me.herohe.commonweb.comment.CommentSpecs.isBest;
import static me.herohe.commonweb.comment.CommentSpecs.isGood;

@RunWith(SpringRunner.class)
//@DataJpaTest
@SpringBootTest
public class CommentRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void getComment() {

        commentRepository.findCommentById(1l);
        System.out.println("=========EntityGraph 위 EAGER 아래 LAZY=======================");
        commentRepository.findById(1l);

        Post post = new Post();
        post.setTitle("jpa");
        Post savedPost = postRepository.save(post);

        Comment comment = new Comment();
        comment.setComment("Spring Data Jpa Projection");
        comment.setPost(savedPost);
        comment.setUp(10);
        comment.setDown(1);
        commentRepository.save(comment);

        System.out.println("======Spring Data Jpa: Projection ===== 원하는 곳만 Select=====");
        System.out.println("====closed projection 으로 CommentSummary 에 최적화된다.======");
        commentRepository.findByPost_Id(savedPost.getId(), CommentSummary.class).forEach( c -> {
            System.out.println("==========");
            System.out.println(c.getVotes());
        });
    }

    @Test
    public void specs() {
        commentRepository.findAll(isBest().or(CommentSpecs.isGood()));
        commentRepository.findAll(isBest().or(isGood()), PageRequest.of(0,10));
    }

    @Test
    public void qbe() {
        Comment prove = new Comment();
        prove.setBest(true);

        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                        .withIgnorePaths("up","down");
        Example<Comment> example = Example.of(prove, exampleMatcher);

        commentRepository.findAll(example);
    }

}