package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

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

    /**
     * 반환 타입
     */
//    List<Member> findByUsername(String name); //컬렉션
//    Member findByUsername(String name); //단건
//    Optional<Member> findByUsername(String name); //단건 Optional

    /**
     * paging / sorting
     */
//    Page<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용
//    Slice<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
//    List<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함
//    List<Member> findByUsername(String name, Sort sort);

//    paging 에서 join 쿼리를 사용한다고 해도
//    count query 는 join 이 필요없을 수 있다.
//    이 경우 효율이 떨어지기 떄문에 필요한 경우 아래와 같이 count 쿼리를 분리해줄 수 있다.
//    @Query(value = "select m from Member m",
//            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);

//    @Modifying(clearAutomatically = true) : 영속성 컨텍스트 초기화
    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // Fetch join
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    /**
     * EntityGraph
     * - left outer join 사용
     * - 아주 간단한 Fetch join 대신 사용
     */
    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    // Entity Named Query
    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findNamedEntityGraph();


}
