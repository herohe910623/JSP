package me.herohe.commonweb.post;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void crud() {
        Post post = new Post();
        post.setTitle("jpa");
        Post savedPost = postRepository.save(post);   //persist

        //처음 insert 인 persist()는 영속화 되는 post와 savedPost가 같다.
        assertThat(entityManager.contains(post)).isTrue();
        assertThat(entityManager.contains(savedPost)).isTrue();
        assertThat(post == savedPost).isTrue();

        Post postUpdate = new Post();
        postUpdate.setId(post.getId());
        postUpdate.setTitle("hibernate");
        Post updatedPost = postRepository.save(postUpdate); //merge

//하지만 postUpdate 인스턴스는 merge()가 발생하고 merge()는 그 복사본 updatedPost를 영속화 시킨다.
//실패   assertThat(entityManager.contains(postUpdate)).isTrue();
        assertThat(entityManager.contains(updatedPost)).isTrue();
        assertThat(postUpdate == updatedPost).isFalse();

        //결론 : 무조건 리턴 받은 인스턴스를 사용하여 실수를 줄이자.

        List<Post> findAll = postRepository.findAll();
        assertThat(findAll.size()).isEqualTo(1);
    }

    @Test
    public void findByTitleStartsWith() {
        savePost();

        List<Post> titleStartsWith = postRepository.findByTitleStartsWith("Spring");
        assertThat(titleStartsWith.size()).isEqualTo(1);
    }
// new Post 로 save 시켜주는 메소드
    private Post savePost() {
        Post post = new Post();
        post.setTitle("Spring");
        return postRepository.save(post);
    }

    @Test
    public void findByTitle() {
        savePost();
        List<Post> all = postRepository.findByTitle("Spring",
//                Sort.by("title")) 엘리언스나 프로퍼티 레퍼런스만 사용가능
                JpaSort.unsafe("LENGTH(title)"))    // 함수호출 결과도 가능
                ;
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void updateTitle() {
        Post spring = savePost();

        int update = postRepository.updateTitle("hibernate",spring.getId());
        assertThat(update).isEqualTo(1);

        // hibernate 로 바뀌었는지 확인했는데, 안되있다. 여전히 Spring 이다
        //select를 안해준다. 캐쉬가 비어지지 않았기 때문에 persist Context 가 비워지지 않았기 때문에 update 되지 않았다.
        //비록 DB 상에는 변경되었지만, = 복잡하다. 그래서 @Modifying(clearAutomatically = true)
        // 업데이트 쿼리를 실행한 다음에 persist Context 를 Clear 해준다.
        Optional<Post> hibernate = postRepository.findById(spring.getId());
        Post post = hibernate.get();
        assertThat(post.getTitle()).isEqualTo("hibernate");
    }

}