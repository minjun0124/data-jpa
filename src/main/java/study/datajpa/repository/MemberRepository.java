package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //  @Query(name = "Member.findByUsername") 생략 가능하다.
    //  하지만 실무에서는 NamedQuery 는 잘 사용되지 않는다.
    //  장점은 어플리케이션 로딩 시점에 파싱, 컴파일하기 때문에 이때, 에러를 잡을 수 있다.
    List<Member> findByUsername(@Param("username") String username);

    //  이름없는 NamedQuery라고 이해해도 좋다. 장점도 같다.
    //  실무에서 자주 쓰는 방식이며 조건이 한두개라 길지 않을 때는 메소드 네임으로
    //  조금 길어지거나 복잡한 쿼리는 @Query로 사용하면 좋다.
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

}
