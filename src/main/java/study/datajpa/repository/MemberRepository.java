package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
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

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO 직접 조회
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 파라미터 바인딩은 위치 기반보다는 가급적 이름 기반으로 한다.
    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    // Collection type in 절 지원
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);
}
