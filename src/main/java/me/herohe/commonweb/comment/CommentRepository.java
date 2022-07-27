package me.herohe.commonweb.comment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> , JpaSpecificationExecutor<Comment> , QueryByExampleExecutor<Comment> {

    @EntityGraph(attributePaths = {"post"}) //.. attributePath로 대체 가능
    Optional<Comment> findCommentById(Long id);

//    List<Comment> findByPost_Id(Long id);
//    List<CommentSummary> findByPost_Id(Long id);
//    List<CommentOnly> findByPost_Id(Long id); 이렇게 두개는 사용못하기 때문에 아래와 같이 제네릭을 사용하여 테스트

    @Transactional(readOnly = true) //수정하지 않을때 사용.
    <T> List<T> findByPost_Id(Long id, Class<T> type);
}
